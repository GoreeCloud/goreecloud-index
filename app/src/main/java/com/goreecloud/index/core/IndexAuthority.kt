package com.goreecloud.index.core

enum class IndexAuthorityRequirement {
    ANDROID_RUNTIME_PERMISSION,
    PRIVACY_SHIELD,
    GOREECLOUD_IDENTITY,
}

enum class IndexAuthorityOutcome {
    ALLOW,
    ALLOW_WITH_CONSTRAINTS,
    DENY,
    REQUIRE_USER_DECISION,
    UNAVAILABLE,
}

data class IndexAuthorityEvidence(
    val outcome: IndexAuthorityOutcome,
    val reference: String? = null,
) {
    fun isUnconstrainedAllow(): Boolean =
        outcome == IndexAuthorityOutcome.ALLOW && !reference.isNullOrBlank()

    companion object {
        fun unavailable(): IndexAuthorityEvidence = IndexAuthorityEvidence(
            outcome = IndexAuthorityOutcome.UNAVAILABLE,
        )
    }
}

data class IndexProviderAuthority(
    val androidPermissionGranted: Boolean = false,
    val privacyShield: IndexAuthorityEvidence = IndexAuthorityEvidence.unavailable(),
    val identity: IndexAuthorityEvidence = IndexAuthorityEvidence.unavailable(),
) {
    fun satisfies(requirement: IndexAuthorityRequirement): Boolean = when (requirement) {
        IndexAuthorityRequirement.ANDROID_RUNTIME_PERMISSION -> androidPermissionGranted
        IndexAuthorityRequirement.PRIVACY_SHIELD -> privacyShield.isUnconstrainedAllow()
        IndexAuthorityRequirement.GOREECLOUD_IDENTITY -> identity.isUnconstrainedAllow()
    }

    fun satisfiesAll(requirements: Set<IndexAuthorityRequirement>): Boolean =
        requirements.all(::satisfies)
}
