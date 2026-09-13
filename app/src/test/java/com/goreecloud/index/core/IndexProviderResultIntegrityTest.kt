package com.goreecloud.index.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IndexProviderResultIntegrityTest {
    @Test
    fun providerCannotAttributeResultToAnotherProvider() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val compromised = provider("trusted-provider", "Trusted provider") {
            listOf(
                result(
                    id = "spoofed",
                    providerId = "other-provider",
                    title = "Spoofed result",
                ),
            )
        }

        val snapshot = IndexQueryEngine(listOf(compromised), dispatcher).search(
            rawQuery = "spoofed",
            executionContext = contextFor("trusted-provider"),
        )

        assertTrue(snapshot.results.isEmpty())
        assertEquals(1, snapshot.providerIssues.size)
        assertEquals("trusted-provider", snapshot.providerIssues.single().providerId)
        assertEquals(IndexProviderIssueKind.INVALID_RESULT, snapshot.providerIssues.single().kind)
    }

    @Test
    fun malformedResultDoesNotSuppressValidSiblingResult() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val mixed = provider("apps", "Applications") {
            listOf(
                result(id = "valid", providerId = "apps", title = "Valid app"),
                result(id = "", providerId = "apps", title = "Missing identity"),
                result(id = "untitled", providerId = "apps", title = "   "),
            )
        }

        val snapshot = IndexQueryEngine(listOf(mixed), dispatcher).search(
            rawQuery = "app",
            executionContext = contextFor("apps"),
        )

        assertEquals(listOf("Valid app"), snapshot.results.map { it.title })
        assertEquals(IndexProviderIssueKind.INVALID_RESULT, snapshot.providerIssues.single().kind)
    }

    @Test
    fun duplicateProviderResultIdsAreRejectedWithoutSuppressingUniqueSibling() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val ambiguous = provider("apps", "Applications") {
            listOf(
                result(id = "duplicate", providerId = "apps", title = "First identity claim"),
                result(id = "duplicate", providerId = "apps", title = "Second identity claim"),
                result(id = "unique", providerId = "apps", title = "Unique app"),
            )
        }

        val snapshot = IndexQueryEngine(listOf(ambiguous), dispatcher).search(
            rawQuery = "app",
            executionContext = contextFor("apps"),
        )

        assertEquals(listOf("Unique app"), snapshot.results.map { it.title })
        assertEquals(IndexProviderIssueKind.INVALID_RESULT, snapshot.providerIssues.single().kind)
    }

    @Test
    fun validProviderResultsDoNotCreateIntegrityIssue() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val healthy = provider("settings", "Settings") {
            listOf(result(id = "wifi", providerId = "settings", title = "Wi-Fi"))
        }

        val snapshot = IndexQueryEngine(listOf(healthy), dispatcher).search(
            rawQuery = "wifi",
            executionContext = contextFor("settings"),
        )

        assertEquals(listOf("Wi-Fi"), snapshot.results.map { it.title })
        assertTrue(snapshot.providerIssues.isEmpty())
    }

    private fun contextFor(vararg providerIds: String) = IndexExecutionContext(
        allowedProviderIds = providerIds.toSet(),
        localOnly = true,
    )

    private fun result(
        id: String,
        providerId: String,
        title: String,
    ) = IndexResult(
        id = id,
        providerId = providerId,
        type = IndexResultType.APP,
        title = title,
        score = 700,
    )

    private fun provider(
        id: String,
        name: String,
        block: suspend (IndexQuery) -> List<IndexResult>,
    ) = object : IndexProvider {
        override val providerId: String = id
        override val displayName: String = name
        override val processingLocation = IndexProcessingLocation.LOCAL
        override val timeoutMillis = 1_000L
        override suspend fun search(query: IndexQuery): List<IndexResult> = block(query)
    }
}
