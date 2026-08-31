package com.goreecloud.index.model

enum class SearchResourceType {
    APPLICATION,
}

data class SearchQuery(
    val text: String,
) {
    val normalizedText: String = text.trim()
}

data class SearchProviderDescriptor(
    val id: String,
    val displayName: String,
    val resourceTypes: Set<SearchResourceType>,
    val processingMode: ProcessingMode,
)

enum class ProcessingMode {
    LOCAL,
    REMOTE,
    MIXED,
}

sealed interface SearchAction {
    data class LaunchApplication(val packageName: String) : SearchAction
}

data class SearchResult(
    val id: String,
    val providerId: String,
    val resourceType: SearchResourceType,
    val title: String,
    val subtitle: String,
    val score: Double,
    val action: SearchAction? = null,
)

data class ProviderIssue(
    val providerId: String,
    val message: String,
)

data class SearchSnapshot(
    val query: SearchQuery,
    val results: List<SearchResult>,
    val providerIssues: List<ProviderIssue>,
)
