package com.goreecloud.index.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IndexQueryEngineTest {
    @Test
    fun providerFailureDoesNotSuppressHealthyProviderResults() {
        val healthy = object : IndexProvider {
            override val providerId = "healthy"
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
            override fun search(query: IndexQuery): List<IndexResult> = error("provider unavailable")
        }

        val results = IndexQueryEngine(listOf(failing, healthy)).search("cal")

        assertEquals(1, results.size)
        assertEquals("Calendar", results.single().title)
    }

    @Test
    fun resultsAreRankedAndProviderScopedDuplicatesAreCollapsed() {
        val provider = object : IndexProvider {
            override val providerId = "apps"
            override fun search(query: IndexQuery) = listOf(
                IndexResult("same", providerId, IndexResultType.APP, "Beta", score = 400),
                IndexResult("same", providerId, IndexResultType.APP, "Beta duplicate", score = 900),
                IndexResult("alpha", providerId, IndexResultType.APP, "Alpha", score = 800),
            )
        }

        val results = IndexQueryEngine(listOf(provider)).search("a")

        assertEquals(listOf("Alpha", "Beta"), results.map { it.title })
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
}
