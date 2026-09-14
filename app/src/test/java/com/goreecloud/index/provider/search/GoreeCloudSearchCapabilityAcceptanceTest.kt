package com.goreecloud.index.provider.search

import com.goreecloud.index.core.IndexQuery
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GoreeCloudSearchCapabilityAcceptanceTest {
    @Test
    fun developmentModeAcceptsLegacyDevelopmentGetCapability() = runTest {
        var searchCalls = 0
        val provider = provider(
            mode = GoreeCloudSearchAcceptanceMode.DEVELOPMENT,
            capability = capability(
                productionAccepted = false,
                methods = setOf("GET"),
                preferredMethod = "GET",
            ),
        ) {
            searchCalls++
        }

        val response = provider.searchWithStatus(IndexQuery(text = "goreecloud", maxResults = 1))

        assertEquals(1, searchCalls)
        assertTrue(response.results.isEmpty())
    }

    @Test
    fun productionModeRejectsDevelopmentCapabilityBeforeQuery() = runTest {
        var searchCalls = 0
        val provider = provider(
            mode = GoreeCloudSearchAcceptanceMode.PRODUCTION,
            capability = capability(productionAccepted = false),
        ) {
            searchCalls++
        }

        val failure = runCatching {
            provider.searchWithStatus(IndexQuery(text = "goreecloud", maxResults = 1))
        }.exceptionOrNull()

        assertTrue(failure is IllegalStateException)
        assertEquals("GoreeCloud Search query capability is not production accepted", failure?.message)
        assertEquals(0, searchCalls)
    }

    @Test
    fun productionModeRejectsGetOnlyCapabilityBeforeQuery() = runTest {
        var searchCalls = 0
        val provider = provider(
            mode = GoreeCloudSearchAcceptanceMode.PRODUCTION,
            capability = capability(
                productionAccepted = true,
                methods = setOf("GET"),
                preferredMethod = "GET",
            ),
        ) {
            searchCalls++
        }

        val failure = runCatching {
            provider.searchWithStatus(IndexQuery(text = "goreecloud", maxResults = 1))
        }.exceptionOrNull()

        assertTrue(failure is IllegalStateException)
        assertEquals(
            "GoreeCloud Search query capability does not provide the required production POST transport",
            failure?.message,
        )
        assertEquals(0, searchCalls)
    }

    @Test
    fun productionModeAcceptsProductionPostCapability() = runTest {
        var searchCalls = 0
        val provider = provider(
            mode = GoreeCloudSearchAcceptanceMode.PRODUCTION,
            capability = capability(productionAccepted = true),
        ) {
            searchCalls++
        }

        provider.searchWithStatus(IndexQuery(text = "goreecloud", maxResults = 1))

        assertEquals(1, searchCalls)
    }

    private fun provider(
        mode: GoreeCloudSearchAcceptanceMode,
        capability: GoreeCloudSearchCapability,
        onSearch: () -> Unit,
    ): GoreeCloudSearchProvider = GoreeCloudSearchProvider(
        client = GoreeCloudSearchClient { request ->
            onSearch()
            GoreeCloudSearchResponse(
                apiVersion = GOREECLOUD_SEARCH_API_VERSION,
                query = request.query,
                category = request.category,
                results = emptyList(),
            )
        },
        capabilityClient = GoreeCloudSearchCapabilityClient { capability },
        acceptanceMode = mode,
    )

    private fun capability(
        productionAccepted: Boolean,
        methods: Set<String> = setOf("POST", "GET"),
        preferredMethod: String = GOREECLOUD_SEARCH_PREFERRED_METHOD,
    ): GoreeCloudSearchCapability = GoreeCloudSearchCapability(
        id = GOREECLOUD_SEARCH_QUERY_CAPABILITY_ID,
        contractVersion = GOREECLOUD_SEARCH_API_VERSION,
        authoritative = true,
        current = true,
        endpoint = GOREECLOUD_SEARCH_QUERY_ENDPOINT,
        maxResults = GOREECLOUD_SEARCH_MAX_RESULTS,
        productionAccepted = productionAccepted,
        methods = methods,
        preferredMethod = preferredMethod,
    )
}
