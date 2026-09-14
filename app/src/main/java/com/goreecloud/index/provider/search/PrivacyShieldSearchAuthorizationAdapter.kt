package com.goreecloud.index.provider.search

import java.time.Instant

/**
 * Transport-neutral adapter between Privacy Shield's canonical decision response
 * and Index's production GoreeCloud Search authorization client.
 *
 * Network or IPC acquisition of the decision remains a separate runtime concern.
 * This adapter only accepts an unconstrained, unexpired ALLOW for the exact
 * Search operation and returns the capability-token reference that is allowed to
 * travel with the delegated operation.
 */
class PrivacyShieldSearchAuthorizationAdapter(
    private val decisionClient: PrivacyShieldDecisionClient,
    private val clock: () -> Instant = Instant::now,
) : GoreeCloudSearchAuthorizationClient {
    override suspend fun authorize(
        request: GoreeCloudSearchPrivacyAuthorizationRequest,
    ): GoreeCloudSearchPrivacyAuthorization {
        require(request.requestId.isNotBlank()) {
            "Privacy Shield Search authorization request is missing a request identifier"
        }
        val decision = decisionClient.decide(request)
        validateDecision(request, decision, clock())
        return GoreeCloudSearchPrivacyAuthorization(
            capabilityTokenReference = decision.capabilityTokenReference!!.trim(),
        )
    }

    private fun validateDecision(
        request: GoreeCloudSearchPrivacyAuthorizationRequest,
        decision: PrivacyShieldSearchDecision,
        now: Instant,
    ) {
        check(decision.decisionId.isNotBlank()) {
            "Privacy Shield Search decision is missing a decision identifier"
        }
        check(decision.requestId == request.requestId) {
            "Privacy Shield Search decision does not match the authorization request"
        }
        check(decision.outcome == "ALLOW") {
            "Privacy Shield Search decision is not an unconstrained allow"
        }
        check(request.operation in decision.permittedOperations) {
            "Privacy Shield Search decision does not permit the Search operation"
        }
        check(decision.processingZone == request.processingZone) {
            "Privacy Shield Search decision does not permit the required processing zone"
        }
        check(request.destination in decision.permittedDestinations) {
            "Privacy Shield Search decision does not permit the GoreeCloud Search destination"
        }
        check(decision.retentionMode == request.retentionMode) {
            "Privacy Shield Search decision does not permit the required retention mode"
        }
        check(decision.obligations.isEmpty()) {
            "Privacy Shield Search decision contains obligations Index cannot enforce"
        }
        if (decision.expiresAt != null) {
            val expiresAt = runCatching { Instant.parse(decision.expiresAt) }.getOrNull()
                ?: error("Privacy Shield Search decision expiry is invalid")
            check(expiresAt.isAfter(now)) {
                "Privacy Shield Search decision is expired"
            }
        }
        check(!decision.capabilityTokenReference.isNullOrBlank()) {
            "Privacy Shield Search decision is missing a capability-token reference"
        }
    }
}

fun interface PrivacyShieldDecisionClient {
    suspend fun decide(
        request: GoreeCloudSearchPrivacyAuthorizationRequest,
    ): PrivacyShieldSearchDecision
}

data class PrivacyShieldSearchDecision(
    val decisionId: String,
    val requestId: String,
    val outcome: String,
    val permittedOperations: Set<String>,
    val processingZone: String,
    val permittedDestinations: Set<String>,
    val retentionMode: String,
    val obligations: Set<String> = emptySet(),
    val expiresAt: String? = null,
    val capabilityTokenReference: String? = null,
)
