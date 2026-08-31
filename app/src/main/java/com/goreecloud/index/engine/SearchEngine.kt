package com.goreecloud.index.engine

import com.goreecloud.index.model.ProviderIssue
import com.goreecloud.index.model.SearchQuery
import com.goreecloud.index.model.SearchSnapshot
import com.goreecloud.index.provider.SearchProvider

class SearchEngine(
    private val providers: List<SearchProvider>,
) {
    fun search(query: SearchQuery): SearchSnapshot {
        if (query.normalizedText.isEmpty()) {
            return SearchSnapshot(query = query, results = emptyList(), providerIssues = emptyList())
        }

        val issues = mutableListOf<ProviderIssue>()
        val results = providers.flatMap { provider ->
            runCatching { provider.search(query) }
                .getOrElse {
                    issues += ProviderIssue(
                        providerId = provider.descriptor.id,
                        message = "${provider.descriptor.displayName} is temporarily unavailable.",
                    )
                    emptyList()
                }
        }

        return SearchSnapshot(
            query = query,
            results = results
                .distinctBy { "${it.providerId}:${it.id}" }
                .sortedWith(compareByDescending<com.goreecloud.index.model.SearchResult> { it.score }.thenBy { it.title.lowercase() }),
            providerIssues = issues,
        )
    }
}
