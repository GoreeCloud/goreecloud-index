package com.goreecloud.index.provider.search

import com.goreecloud.index.core.GoreeCloudIndexContract
import com.goreecloud.index.core.IndexAction
import com.goreecloud.index.core.IndexAuthorityRequirement
import com.goreecloud.index.core.IndexProcessingLocation
import com.goreecloud.index.core.IndexProvider
import com.goreecloud.index.core.IndexQuery
import com.goreecloud.index.core.IndexResult
import com.goreecloud.index.core.IndexResultType
import com.goreecloud.index.core.IndexTextMatcher
import java.net.URI
import java.util.Locale

internal const val GOREECLOUD_SEARCH_API_VERSION = "1"
private const val GOREECLOUD_SEARCH_GENERAL_CATEGORY = "general"
private const val GOREECLOUD_SEARCH_MAX_RESULTS = 100

/**
 * The only data Index needs to send to GoreeCloud Search for the initial
 * Internet-provider contract. Keeping this transport-neutral request narrow is
 * intentional: local provider state, local results, files, contacts, calendar
 * data, application inventory, identity identifiers, and authorization evidence
 * are not part of this boundary.
 */
data class GoreeCloudSearchRequest(
    val query: String,
    val category: String = GOREECLOUD_SEARCH_GENERAL_CATEGORY,
    val limit: Int,
)

data class GoreeCloudSearchResult(
    val title: String,
    val url: String,
    val snippet: String? = null,
    val searchScore: Int = 0,
)

data class GoreeCloudSearchResponse(
    val apiVersion: String,
    val query: String,
    val category: String,
    val results: List<GoreeCloudSearchResult>,
    val degraded: Boolean = false,
)

fun interface GoreeCloudSearchClient {
    suspend fun search(request: GoreeCloudSearchRequest): GoreeCloudSearchResponse
}

class GoreeCloudSearchProvider(
    private val client: GoreeCloudSearchClient,
) : IndexProvider {
    override val providerId: String = GoreeCloudIndexContract.PROVIDER_SEARCH
    override val displayName: String = "GoreeCloud Search"
    override val processingLocation: IndexProcessingLocation = IndexProcessingLocation.REMOTE
    override val timeoutMillis: Long = 5_000L
    override val contractVersion: Int = GoreeCloudIndexContract.PROVIDER_CONTRACT_VERSION
    override val authorityRequirements: Set<IndexAuthorityRequirement> =
        setOf(IndexAuthorityRequirement.PRIVACY_SHIELD)
    override val supportsEmptyQuery: Boolean = false

    override suspend fun search(query: IndexQuery): List<IndexResult> {
        val normalizedQuery = query.text.trim()
        require(normalizedQuery.isNotEmpty()) { "GoreeCloud Search requires a non-empty query" }
        val limit = query.maxResults.coerceIn(1, GOREECLOUD_SEARCH_MAX_RESULTS)
        val request = GoreeCloudSearchRequest(
            query = normalizedQuery,
            category = GOREECLOUD_SEARCH_GENERAL_CATEGORY,
            limit = limit,
        )
        val response = client.search(request)

        check(response.apiVersion == GOREECLOUD_SEARCH_API_VERSION) {
            "GoreeCloud Search response API version is not supported"
        }
        check(response.query == request.query) {
            "GoreeCloud Search response query does not match the delegated query"
        }
        check(response.category == request.category) {
            "GoreeCloud Search response category does not match the delegated category"
        }

        return response.results
            .asSequence()
            .take(limit)
            .map { result -> result.toIndexResult(normalizedQuery) }
            .toList()
    }

    private fun GoreeCloudSearchResult.toIndexResult(query: String): IndexResult {
        val normalizedURL = normalizeWebURL(url)
        val normalizedTitle = title.trim()
        val normalizedSnippet = snippet?.trim()?.takeIf(String::isNotEmpty)
        val localScore = IndexTextMatcher.score(
            query = query,
            title = normalizedTitle,
            secondary = normalizedSnippet.orEmpty(),
        ) ?: 0

        return IndexResult(
            id = normalizedURL.orEmpty(),
            providerId = providerId,
            type = IndexResultType.WEB,
            title = normalizedTitle,
            subtitle = normalizedSnippet,
            score = localScore,
            action = normalizedURL?.let(IndexAction::OpenWeb),
        )
    }

    private fun normalizeWebURL(raw: String): String? {
        val uri = runCatching { URI(raw.trim()) }.getOrNull() ?: return null
        val scheme = uri.scheme?.lowercase(Locale.ROOT) ?: return null
        if (scheme != "https" && scheme != "http") return null
        if (uri.host.isNullOrBlank()) return null
        if (uri.userInfo != null) return null
        return uri.normalize().toASCIIString()
    }
}
