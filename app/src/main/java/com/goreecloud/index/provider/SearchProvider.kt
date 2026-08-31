package com.goreecloud.index.provider

import com.goreecloud.index.model.SearchProviderDescriptor
import com.goreecloud.index.model.SearchQuery
import com.goreecloud.index.model.SearchResult

interface SearchProvider {
    val descriptor: SearchProviderDescriptor

    fun search(query: SearchQuery): List<SearchResult>
}
