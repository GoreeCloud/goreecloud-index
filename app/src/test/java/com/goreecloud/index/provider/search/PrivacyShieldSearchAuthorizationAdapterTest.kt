package com.goreecloud.index.provider.search

import java.time.Instant
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PrivacyShieldSearchAuthorizationAdapterTest {
    private val now = Instant.parse("2026-09-14T12:00:00Z")

    @Test
    fun exactAllowDecisionReturnsCapabilityTokenReference() = runTest {
        var observedRequest: GoreeCloudSearchPrivacyAuthorizationRequest? = null
        val adapter = PrivacyShieldSearchAuthorizationAdapter(
            decisionClient = PrivacyShieldDecisionClient { request ->
                observedRequest = request
                allowedDecision()
            },
            clock = { now },
        )

        val authorization = adapter.authorize(GoreeCloudSearchPrivacyAuthorizationRequest())

        assertEquals("search.query", observedRequest?.operation)
        assertEquals("private_goreecloud", observedRequest?.processingZone)
        assertEquals("https://search.goreecloud.com", observedRequest?.destination)
        assertEquals("none", observedRequest?.retentionMode)
        assertEquals("privacy-shield:capability:123", authorization.capabilityTokenReference)
    }

    @Test
    fun constrainedDecisionFailsClosed() = runTest {
        val adapter = adapter(
            allowedDecision().copy(
                outcome = "ALLOW_WITH_CONSTRAINTS",
                obligations = setOf("redact-sensitive-terms"),
            ),
        )

        val failure = runCatching {
            adapter.authorize(GoreeCloudSearchPrivacyAuthorizationRequest())
        }.exceptionOrNull()

        assertTrue(failure is IllegalStateException)
        assertEquals(
            "Privacy Shield Search decision is not an unconstrained allow",
            failure?.message,
        )
    }

    @Test
    fun wrongDestinationFailsClosed() = runTest {
        val adapter = adapter(
            allowedDecision().copy(permittedDestinations = setOf("https://example.com")),
        )

        val failure = runCatching {
            adapter.authorize(GoreeCloudSearchPrivacyAuthorizationRequest())
        }.exceptionOrNull()

        assertTrue(failure is IllegalStateException)
        assertEquals(
            "Privacy Shield Search decision does not permit the GoreeCloud Search destination",
            failure?.message,
        )
    }

    @Test
    fun expiredDecisionFailsClosed() = runTest {
        val adapter = adapter(
            allowedDecision().copy(expiresAt = "2026-09-14T11:59:59Z"),
        )

        val failure = runCatching {
            adapter.authorize(GoreeCloudSearchPrivacyAuthorizationRequest())
        }.exceptionOrNull()

        assertTrue(failure is IllegalStateException)
        assertEquals("Privacy Shield Search decision is expired", failure?.message)
    }

    @Test
    fun missingCapabilityTokenReferenceFailsClosed() = runTest {
        val adapter = adapter(allowedDecision().copy(capabilityTokenReference = null))

        val failure = runCatching {
            adapter.authorize(GoreeCloudSearchPrivacyAuthorizationRequest())
        }.exceptionOrNull()

        assertTrue(failure is IllegalStateException)
        assertEquals(
            "Privacy Shield Search decision is missing a capability-token reference",
            failure?.message,
        )
    }

    private fun adapter(decision: PrivacyShieldSearchDecision) =
        PrivacyShieldSearchAuthorizationAdapter(
            decisionClient = PrivacyShieldDecisionClient { decision },
            clock = { now },
        )

    private fun allowedDecision() = PrivacyShieldSearchDecision(
        decisionId = "privacy-shield:decision:123",
        outcome = "ALLOW",
        permittedOperations = setOf("search.query"),
        processingZone = "private_goreecloud",
        permittedDestinations = setOf("https://search.goreecloud.com"),
        retentionMode = "none",
        obligations = emptySet(),
        expiresAt = "2026-09-14T12:05:00Z",
        capabilityTokenReference = "privacy-shield:capability:123",
    )
}
