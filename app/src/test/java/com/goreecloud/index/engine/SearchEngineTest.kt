package com.goreecloud.index.engine

import com.goreecloud.index.model.ProcessingMode
import com.goreecloud.index.model.SearchProviderDescriptor
import com.goreecloud.index.model.SearchQuery
import com.goreecloud.index.model.SearchResourceType
import com.goreecloud.index.model.SearchResult
import com.goreecloud.index.provider.SearchProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchEngineTest {
    @Test
    fun blankQueryDoesNotDispatchProviders() {
        var calls = 0
        val provider = fakeProvider("apps") {
            calls += 1
            emptyList()
        }

        val snapshot = SearchEngine(listOf(provider)).search(SearchQuery("   "))

        assertEquals(0, calls)
        assertTrue(snapshot.results.isEmpty())
        assertTrue(snapshot.providerIssues.isEmpty())
    }

    @Test
    fun providerFailureDoesNotSuppressHealthyResults() {
        val failing = fakeProvider("failing") {
            error("provider failure")
        }
        val healthy = fakeProvider("healthy") {
            listOf(
                SearchResult(
                    id = "com.example.notes",
                    providerId = "healthy",
                    resourceType = SearchResourceType.APPLICATION,
                    title = "Notes",
                    subtitle = "com.example.notes",
                    score = 0.9,
                ),
            )
        }

        val snapshot = SearchEngine(listOf(failing, healthy)).search(SearchQuery("notes"))

        assertEquals(listOf("Notes"), snapshot.results.map { it.title })
        assertEquals(1, snapshot.providerIssues.size)
        assertEquals("failing", snapshot.providerIssues.single().providerId)
    }

    @Test
    fun resultsAreRankedAndDeduplicatedPerProviderIdentity() {
        val provider = fakeProvider("apps") {
            listOf(
                result(id = "a", title = "Alpha", score = 0.7),
                result(id = "b", title = "Beta", score = 0.95),
                result(id = "b", title = "Beta duplicate", score = 0.4),
            )
        }

        val snapshot = SearchEngine(listOf(provider)).search(SearchQuery("a"))

        assertEquals(listOf("Beta", "Alpha"), snapshot.results.map { it.title })
    }

    private fun result(id: String, title: String, score: Double) = SearchResult(
        id = id,
        providerId = "apps",
        resourceType = SearchResourceType.APPLICATION,
        title = title,
        subtitle = id,
        score = score,
    )

    private fun fakeProvider(
        id: String,
        search: (SearchQuery) -> List<SearchResult>,
    ): SearchProvider = object : SearchProvider {
        override val descriptor = SearchProviderDescriptor(
            id = id,
            displayName = id,
            resourceTypes = setOf(SearchResourceType.APPLICATION),
            processingMode = ProcessingMode.LOCAL,
        )

        override fun search(query: SearchQuery): List<SearchResult> = search(query)
    }
}
