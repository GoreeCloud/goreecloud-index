package com.goreecloud.index.core

import java.text.Normalizer
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout

object GoreeCloudIndexContract {
    const val ACTION_SEARCH = "com.goreecloud.index.action.SEARCH"
    const val EXTRA_QUERY = "com.goreecloud.index.extra.QUERY"
    const val PROVIDER_APPS = "goreecloud.index.provider.apps"
    const val PROVIDER_CONTACTS = "goreecloud.index.provider.contacts"
    const val PROVIDER_SETTINGS = "goreecloud.index.provider.settings"
    const val PROVIDER_SEARCH = "goreecloud.index.provider.search"
}

enum class IndexResultType {
    APP,
    ACTION,
    CONTACT,
    FILE,
    CALENDAR,
    MEDIA,
    SETTING,
    GOREECLOUD,
    DEVICE,
    WEB,
}

enum class IndexProcessingLocation {
    LOCAL,
    REMOTE,
    MIXED,
}

enum class IndexProviderIssueKind {
    FAILED,
    TIMED_OUT,
    AUTHORIZATION_REQUIRED,
    INVALID_RESULT,
}

sealed interface IndexAction {
    data class LaunchActivity(
        val packageName: String,
        val className: String,
    ) : IndexAction

    data class ViewContact(
        val uri: String,
    ) : IndexAction

    data class OpenSystemSetting(
        val action: String,
    ) : IndexAction

    data class OpenWeb(
        val uri: String,
    ) : IndexAction
}

data class IndexResult(
    val id: String,
    val providerId: String,
    val type: IndexResultType,
    val title: String,
    val subtitle: String? = null,
    val score: Int,
    val action: IndexAction? = null,
)

data class IndexQuery(
    val text: String,
    val maxResults: Int = 50,
)

data class IndexExecutionContext(
    val allowedProviderIds: Set<String>,
    val localOnly: Boolean = true,
    val providerAuthorities: Map<String, IndexProviderAuthority> = emptyMap(),
) {
    fun allows(provider: IndexProvider): Boolean =
        provider.providerId in allowedProviderIds &&
            (!localOnly || provider.processingLocation == IndexProcessingLocation.LOCAL) &&
            providerAuthorities
                .getOrDefault(provider.providerId, IndexProviderAuthority())
                .satisfiesAll(provider.authorityRequirements)

    fun authorizationIssue(provider: IndexProvider): IndexProviderIssue? {
        if (provider.providerId !in allowedProviderIds) return null
        if (localOnly && provider.processingLocation != IndexProcessingLocation.LOCAL) return null
        if (provider.authorityRequirements.isEmpty()) return null

        val authority = providerAuthorities.getOrDefault(provider.providerId, IndexProviderAuthority())
        if (authority.satisfiesAll(provider.authorityRequirements)) return null

        return IndexProviderIssue(
            providerId = provider.providerId,
            providerName = provider.displayName,
            kind = IndexProviderIssueKind.AUTHORIZATION_REQUIRED,
        )
    }
}

data class IndexProviderIssue(
    val providerId: String,
    val providerName: String,
    val kind: IndexProviderIssueKind,
)

data class IndexSearchSnapshot(
    val results: List<IndexResult> = emptyList(),
    val providerIssues: List<IndexProviderIssue> = emptyList(),
)

interface IndexProvider {
    val providerId: String
    val displayName: String
    val processingLocation: IndexProcessingLocation
    val timeoutMillis: Long
    val authorityRequirements: Set<IndexAuthorityRequirement>
        get() = emptySet()
    val supportsEmptyQuery: Boolean
        get() = true
    suspend fun search(query: IndexQuery): List<IndexResult>
}

private data class IndexProviderOutcome(
    val results: List<IndexResult> = emptyList(),
    val issue: IndexProviderIssue? = null,
)

private data class RankedIndexResult(
    val result: IndexResult,
    val normalizedTitle: String,
)

class IndexQueryEngine(
    private val providers: List<IndexProvider>,
    private val providerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend fun search(
        rawQuery: String,
        executionContext: IndexExecutionContext,
        maxResults: Int = 50,
    ): IndexSearchSnapshot = supervisorScope {
        val query = IndexQuery(
            text = IndexQueryNormalizer.normalize(rawQuery),
            maxResults = maxResults.coerceIn(1, MAX_RESULTS),
        )

        val applicableProviders = providers.filter { provider ->
            query.text.isNotEmpty() || provider.supportsEmptyQuery
        }
        val authorizationIssues = applicableProviders
            .mapNotNull(executionContext::authorizationIssue)

        val outcomes = applicableProviders
            .asSequence()
            .filter(executionContext::allows)
            .map { provider ->
                async(providerDispatcher) {
                    queryProvider(provider, query)
                }
            }
            .toList()
            .awaitAll()

        val ranking = compareByDescending<RankedIndexResult> { it.result.score }
            .thenBy { it.normalizedTitle }
            .thenBy { it.result.providerId }
            .thenBy { it.result.id }

        val results = outcomes
            .asSequence()
            .flatMap { it.results.asSequence() }
            .map { result ->
                RankedIndexResult(
                    result = result,
                    normalizedTitle = IndexQueryNormalizer.normalizeForMatching(result.title),
                )
            }
            .sortedWith(ranking)
            .map { it.result }
            .distinctBy { result -> "${result.providerId}:${result.id}" }
            .take(query.maxResults)
            .toList()

        IndexSearchSnapshot(
            results = results,
            providerIssues = (authorizationIssues + outcomes.mapNotNull { it.issue })
                .distinctBy { it.providerId },
        )
    }

    private suspend fun queryProvider(
        provider: IndexProvider,
        query: IndexQuery,
    ): IndexProviderOutcome = try {
        val timeoutMillis = provider.timeoutMillis.coerceIn(1L, MAX_PROVIDER_TIMEOUT_MILLIS)
        val providerResults = withTimeout(timeoutMillis) {
            provider.search(query)
        }
        val validResults = providerResults.filter { result ->
            result.providerId == provider.providerId &&
                result.id.isNotBlank() &&
                result.title.isNotBlank()
        }

        IndexProviderOutcome(
            results = validResults,
            issue = if (validResults.size == providerResults.size) {
                null
            } else {
                IndexProviderIssue(
                    providerId = provider.providerId,
                    providerName = provider.displayName,
                    kind = IndexProviderIssueKind.INVALID_RESULT,
                )
            },
        )
    } catch (_: TimeoutCancellationException) {
        IndexProviderOutcome(
            issue = IndexProviderIssue(
                providerId = provider.providerId,
                providerName = provider.displayName,
                kind = IndexProviderIssueKind.TIMED_OUT,
            ),
        )
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (_: Exception) {
        IndexProviderOutcome(
            issue = IndexProviderIssue(
                providerId = provider.providerId,
                providerName = provider.displayName,
                kind = IndexProviderIssueKind.FAILED,
            ),
        )
    }

    private companion object {
        const val MAX_RESULTS = 100
        const val MAX_PROVIDER_TIMEOUT_MILLIS = 5_000L
    }
}

object IndexQueryNormalizer {
    private val whitespace = Regex("\\s+")

    fun normalize(value: String): String =
        whitespace.replace(
            Normalizer.normalize(value, Normalizer.Form.NFKC).trim(),
            " ",
        )

    fun normalizeForMatching(value: String): String =
        normalize(value).lowercase(Locale.ROOT)
}

object IndexTextMatcher {
    fun score(query: String, title: String, secondary: String = ""): Int? {
        val needle = IndexQueryNormalizer.normalizeForMatching(query)
        if (needle.isEmpty()) return 100

        val normalizedTitle = IndexQueryNormalizer.normalizeForMatching(title)
        val normalizedSecondary = IndexQueryNormalizer.normalizeForMatching(secondary)

        return when {
            normalizedTitle == needle -> 1_000
            normalizedTitle.startsWith(needle) -> 850
            normalizedTitle.split(' ').any { it.startsWith(needle) } -> 760
            normalizedTitle.contains(needle) -> 650
            normalizedSecondary == needle -> 540
            normalizedSecondary.startsWith(needle) -> 500
            normalizedSecondary.contains(needle) -> 420
            else -> null
        }
    }
}
