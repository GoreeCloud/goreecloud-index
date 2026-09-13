package com.goreecloud.index.core

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout
import java.text.Normalizer
import java.util.Locale

object GoreeCloudIndexContract {
    const val ACTION_SEARCH = "com.goreecloud.index.action.SEARCH"
    const val EXTRA_QUERY = "com.goreecloud.index.extra.QUERY"
    const val PROVIDER_APPS = "goreecloud.index.provider.apps"
    const val PROVIDER_CONTACTS = "goreecloud.index.provider.contacts"
    const val PROVIDER_SETTINGS = "goreecloud.index.provider.settings"
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
            text = rawQuery.trim(),
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

        val ranking = compareByDescending<IndexResult> { it.score }
            .thenBy { normalizedOrderingText(it.title) }
            .thenBy { it.providerId }
            .thenBy { it.id }

        val results = outcomes
            .asSequence()
            .flatMap { it.results.asSequence() }
            .sortedWith(ranking)
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
        val structurallyValidResults = providerResults.filter { result ->
            result.providerId == provider.providerId &&
                result.id.isNotBlank() &&
                result.title.isNotBlank()
        }
        val duplicateResultIds = structurallyValidResults
            .groupingBy { it.id }
            .eachCount()
            .filterValues { count -> count > 1 }
            .keys
        val validResults = structurallyValidResults.filterNot { result ->
            result.id in duplicateResultIds
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

object IndexTextMatcher {
    fun score(query: String, title: String, secondary: String = ""): Int? {
        val needle = normalizeForMatch(query)
        if (needle.isEmpty()) return 100

        val normalizedTitle = normalizeForMatch(title)
        val normalizedSecondary = normalizeForMatch(secondary)

        return when {
            normalizedTitle == needle -> 1_000
            normalizedTitle.startsWith(needle) -> 850
            normalizedTitle.split(INDEX_WORD_BOUNDARY).any { it.startsWith(needle) } -> 760
            normalizedTitle.contains(needle) -> 650
            normalizedSecondary == needle -> 540
            normalizedSecondary.startsWith(needle) -> 500
            normalizedSecondary.contains(needle) -> 420
            else -> null
        }
    }

    private fun normalizeForMatch(value: String): String = normalizedOrderingText(value.trim())

    // Include Unicode separator characters so provider titles copied from contacts/files/media keep
    // the same word-prefix behavior across NBSP, figure-space, and other separator code points.
    private val INDEX_WORD_BOUNDARY = Regex("[\\s\\p{Z}]+")
}

private fun normalizedOrderingText(value: String): String =
    Normalizer.normalize(value, Normalizer.Form.NFC).lowercase(Locale.ROOT)
