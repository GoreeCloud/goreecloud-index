package com.goreecloud.index.core

object GoreeCloudIndexContract {
    const val ACTION_SEARCH = "com.goreecloud.index.action.SEARCH"
    const val EXTRA_QUERY = "com.goreecloud.index.extra.QUERY"
    const val PROVIDER_APPS = "goreecloud.index.provider.apps"
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

sealed interface IndexAction {
    data class LaunchActivity(
        val packageName: String,
        val className: String,
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

data class IndexProviderIssue(
    val providerId: String,
    val providerName: String,
)

data class IndexSearchSnapshot(
    val results: List<IndexResult> = emptyList(),
    val providerIssues: List<IndexProviderIssue> = emptyList(),
)

interface IndexProvider {
    val providerId: String
    val displayName: String
    fun search(query: IndexQuery): List<IndexResult>
}

class IndexQueryEngine(
    private val providers: List<IndexProvider>,
) {
    fun search(rawQuery: String, maxResults: Int = 50): IndexSearchSnapshot {
        val query = IndexQuery(
            text = rawQuery.trim(),
            maxResults = maxResults.coerceIn(1, 100),
        )
        val issues = mutableListOf<IndexProviderIssue>()

        val results = providers
            .asSequence()
            .flatMap { provider ->
                try {
                    provider.search(query).asSequence()
                } catch (_: Exception) {
                    issues += IndexProviderIssue(
                        providerId = provider.providerId,
                        providerName = provider.displayName,
                    )
                    emptySequence()
                }
            }
            .distinctBy { result -> "${result.providerId}:${result.id}" }
            .sortedWith(
                compareByDescending<IndexResult> { it.score }
                    .thenBy(String.CASE_INSENSITIVE_ORDER) { it.title }
                    .thenBy { it.providerId }
            )
            .take(query.maxResults)
            .toList()

        return IndexSearchSnapshot(
            results = results,
            providerIssues = issues.distinctBy { it.providerId },
        )
    }
}

object IndexTextMatcher {
    fun score(query: String, title: String, secondary: String = ""): Int? {
        val needle = query.trim().lowercase()
        if (needle.isEmpty()) return 100

        val normalizedTitle = title.trim().lowercase()
        val normalizedSecondary = secondary.trim().lowercase()

        return when {
            normalizedTitle == needle -> 1_000
            normalizedTitle.startsWith(needle) -> 850
            normalizedTitle.split(Regex("\\s+")).any { it.startsWith(needle) } -> 760
            normalizedTitle.contains(needle) -> 650
            normalizedSecondary == needle -> 540
            normalizedSecondary.startsWith(needle) -> 500
            normalizedSecondary.contains(needle) -> 420
            else -> null
        }
    }
}
