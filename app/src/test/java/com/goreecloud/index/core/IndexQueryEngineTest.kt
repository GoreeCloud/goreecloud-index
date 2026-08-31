package com.goreecloud.index.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IndexQueryEngineTest {
    @Test
    fun providerFailureIsReportedWithoutSuppressingHealthyResults() {
        val healthy = object : IndexProvider {
            override val providerId = "healthy"
            override val displayName = "Healthy"
            override fun search(query: IndexQuery) = listOf(
                IndexResult(
                    id = "one",
                    providerId = providerId,
                    type = IndexResultType.APP,
                    title = "Calendar",
                    score = 700,
                )
            )
        }
        val failing = object : IndexProvider {
            override val providerId = "failing"
            override val displayName = "Failing"
            override fun search(query: IndexQuery): List<IndexResult> = error("provider unavailable")
        }

        val snapshot = IndexQueryEngine(listOf(failing, healthy)).search("cal")

        assertEquals(1, snapshot.results.size)
        assertEquals("Calendar", snapshot.results.single().title)
        assertEquals(1, snapshot.providerIssues.size)
        assertEquals("failing", snapshot.providerIssues.single().providerId)
        assertEquals("Failing", snapshot.providerIssues.single().providerName)
    }

    @Test
    fun resultsAreRankedAndProviderScopedDuplicatesAreCollapsed() {
        val provider = object : IndexProvider {
            override val providerId = "apps"
            override val displayName = "Applications"
            override fun search(query: IndexQuery) = listOf(
                IndexResult("same", providerId, IndexResultType.APP, "Beta", score = 400),
                IndexResult("same", providerId, IndexResultType.APP, "Beta duplicate", score = 900),
                IndexResult("alpha", providerId, IndexResultType.APP, "Alpha", score = 800),
            )
        }

        val snapshot = IndexQueryEngine(listOf(provider)).search("a")

        assertEquals(listOf("Alpha", "Beta"), snapshot.results.map { it.title })
        assertTrue(snapshot.providerIssues.isEmpty())
    }

    @Test
    fun textMatcherPrioritizesExactAndPrefixMatches() {
        val exact = IndexTextMatcher.score("maps", "Maps", "com.example.maps")
        val prefix = IndexTextMatcher.score("map", "Maps", "com.example.maps")
        val secondary = IndexTextMatcher.score("example", "Maps", "com.example.maps")

        assertTrue(exact!! > prefix!!)
        assertTrue(prefix > secondary!!)
        assertEquals(null, IndexTextMatcher.score("calendar", "Maps", "com.example.maps"))
    }

    @Test
    fun emptyQueryCanBrowseProviderResultsWithinBoundedLimit() {
        val provider = object : IndexProvider {
            override val providerId = "apps"
            override val displayName = "Applications"
            override fun search(query: IndexQuery) = (1..5).map { index ->
                IndexResult(
                    id = index.toString(),
                    providerId = providerId,
                    type = IndexResultType.APP,
                    title = "App $index",
                    score = 100,
                )
            }
        }

        val snapshot = IndexQueryEngine(listOf(provider)).search("", maxResults = 3)

        assertEquals(3, snapshot.results.size)
        assertTrue(snapshot.providerIssues.isEmpty())
    }
}
