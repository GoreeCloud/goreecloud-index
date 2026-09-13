package com.goreecloud.index.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IndexQueryNormalizerTest {
    @Test
    fun normalizeCollapsesWhitespaceAndCompatibilityCharacters() {
        assertEquals(
            "GoreeCloud Search",
            IndexQueryNormalizer.normalize("  ＧｏｒｅｅＣｌｏｕｄ\t  Search  "),
        )
    }

    @Test
    fun matcherUsesCompatibilityNormalization() {
        val exact = IndexTextMatcher.score("ＧｏｒｅｅＣｌｏｕｄ", "GoreeCloud")
        val prefix = IndexTextMatcher.score("ｓｅａｒ", "Search")

        assertEquals(1_000, exact)
        assertEquals(850, prefix)
    }

    @Test
    fun enginePassesCanonicalWhitespaceToProviders() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var observedQuery: IndexQuery? = null
        val provider = object : IndexProvider {
            override val providerId = "test"
            override val displayName = "Test"
            override val processingLocation = IndexProcessingLocation.LOCAL
            override val timeoutMillis = 1_000L

            override suspend fun search(query: IndexQuery): List<IndexResult> {
                observedQuery = query
                return emptyList()
            }
        }

        val snapshot = IndexQueryEngine(listOf(provider), dispatcher).search(
            rawQuery = "  calendar\t   event  ",
            executionContext = IndexExecutionContext(
                allowedProviderIds = setOf("test"),
            ),
        )

        assertEquals("calendar event", observedQuery?.text)
        assertTrue(snapshot.providerIssues.isEmpty())
    }
}
