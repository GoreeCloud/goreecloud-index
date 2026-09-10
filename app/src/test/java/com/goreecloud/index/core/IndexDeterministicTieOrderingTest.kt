package com.goreecloud.index.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IndexDeterministicTieOrderingTest {
    @Test
    fun equalScoreTitleAndProviderResultsFallThroughToStableIdOrdering() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val provider = object : IndexProvider {
            override val providerId = "apps"
            override val displayName = "Applications"
            override val processingLocation = IndexProcessingLocation.LOCAL
            override val timeoutMillis = 1_000L

            override suspend fun search(query: IndexQuery): List<IndexResult> = listOf(
                result("z-result"),
                result("a-result"),
                result("m-result"),
            )
        }

        val snapshot = IndexQueryEngine(listOf(provider), dispatcher).search(
            rawQuery = "same",
            executionContext = IndexExecutionContext(
                allowedProviderIds = setOf("apps"),
                localOnly = true,
            ),
        )

        assertEquals(
            listOf("a-result", "m-result", "z-result"),
            snapshot.results.map { it.id },
        )
    }

    private fun result(id: String) = IndexResult(
        id = id,
        providerId = "apps",
        type = IndexResultType.APP,
        title = "Same title",
        score = 700,
    )
}
