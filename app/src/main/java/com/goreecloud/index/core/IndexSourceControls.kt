package com.goreecloud.index.core

/**
 * Development-only user source selection policy.
 *
 * This policy intentionally exposes only the currently integrated local
 * providers. It does not persist user preferences, enable remote Search, or
 * grant provider authority. The execution engine still evaluates provider
 * scope and authority independently for every query.
 */
object IndexDevelopmentSourcePolicy {
    val selectableProviderIds: Set<String> = linkedSetOf(
        GoreeCloudIndexContract.PROVIDER_APPS,
        GoreeCloudIndexContract.PROVIDER_SETTINGS,
        GoreeCloudIndexContract.PROVIDER_CONTACTS,
    )

    fun sanitizeEnabledProviderIds(requestedProviderIds: Set<String>): Set<String> =
        requestedProviderIds.intersect(selectableProviderIds)

    fun executionContext(
        requestedProviderIds: Set<String>,
        providerAuthorities: Map<String, IndexProviderAuthority> = emptyMap(),
    ): IndexExecutionContext = IndexExecutionContext(
        allowedProviderIds = sanitizeEnabledProviderIds(requestedProviderIds),
        localOnly = true,
        providerAuthorities = providerAuthorities,
    )
}
