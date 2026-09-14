package com.goreecloud.index.core

/**
 * Privacy-safe presentation state for a provider's authorization prerequisites.
 *
 * This exposes only which authority domains are still required. It deliberately
 * excludes evidence references, decision IDs, subject identifiers, reason codes,
 * expiry timestamps, and any other authority-owned payload.
 */
data class IndexSourceAuthorityStatus(
    val missingRequirements: Set<IndexAuthorityRequirement>,
) {
    val available: Boolean
        get() = missingRequirements.isEmpty()
}

object IndexSourceAuthorityProjection {
    fun project(
        authority: IndexProviderAuthority,
        requirements: Set<IndexAuthorityRequirement>,
    ): IndexSourceAuthorityStatus = IndexSourceAuthorityStatus(
        missingRequirements = requirements
            .filterNot(authority::satisfies)
            .toCollection(linkedSetOf()),
    )

    fun contacts(authority: IndexProviderAuthority): IndexSourceAuthorityStatus = project(
        authority = authority,
        requirements = linkedSetOf(
            IndexAuthorityRequirement.ANDROID_RUNTIME_PERMISSION,
            IndexAuthorityRequirement.PRIVACY_SHIELD,
            IndexAuthorityRequirement.GOREECLOUD_IDENTITY,
        ),
    )
}

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
