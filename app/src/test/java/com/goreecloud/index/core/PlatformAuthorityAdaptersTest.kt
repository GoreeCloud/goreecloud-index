package com.goreecloud.index.core

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlatformAuthorityAdaptersTest {
    private val now = Instant.parse("2026-09-01T16:00:00Z")

    @Test
    fun privacyShieldUnconstrainedAllowProjectsAllow() {
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = PrivacyShieldDecisionEvidence(
                requestId = "contacts-read-1",
                outcome = PrivacyShieldDecisionOutcome.ALLOW,
                evidenceReference = "privacy-shield:evidence:contacts-read-1",
                expiresAt = now.plusSeconds(300),
            ),
            expectedRequestId = "contacts-read-1",
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW, evidence.outcome)
        assertTrue(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldObligationsRemainConstrained() {
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = PrivacyShieldDecisionEvidence(
                requestId = "contacts-read-2",
                outcome = PrivacyShieldDecisionOutcome.ALLOW,
                obligations = listOf("do-not-retain"),
                evidenceReference = "privacy-shield:evidence:contacts-read-2",
            ),
            expectedRequestId = "contacts-read-2",
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW_WITH_CONSTRAINTS, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldStaleEvidenceFailsClosed() {
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = PrivacyShieldDecisionEvidence(
                requestId = "contacts-read-3",
                outcome = PrivacyShieldDecisionOutcome.ALLOW,
                evidenceReference = "privacy-shield:evidence:contacts-read-3",
                expiresAt = now.minusSeconds(1),
            ),
            expectedRequestId = "contacts-read-3",
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun identityAuthorizationUsesIdentityOwnedInterpreter() {
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = validIdentityEvidence(outcome = "authorized"),
            expectedSubjectId = "goreecloud-index",
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter { outcome ->
                if (outcome == "authorized") IndexAuthorityOutcome.ALLOW else null
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW, evidence.outcome)
        assertTrue(evidence.isUnconstrainedAllow())
    }

    @Test
    fun identityUnknownOutcomeFailsClosed() {
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = validIdentityEvidence(outcome = "identity-specific-future-state"),
            expectedSubjectId = "goreecloud-index",
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter { null },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun identityMinimizationViolationFailsClosed() {
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = validIdentityEvidence(
                outcome = "authorized",
                containsReusableCredentials = true,
            ),
            expectedSubjectId = "goreecloud-index",
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter {
                IndexAuthorityOutcome.ALLOW
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    private fun validIdentityEvidence(
        outcome: String,
        containsReusableCredentials: Boolean = false,
    ): IdentityAuthorizationEvidence = IdentityAuthorizationEvidence(
        contract = "goreecloud.identity-evidence.v1",
        authorityDomain = "authorization",
        assertion = "authorization-decision",
        outcome = outcome,
        subjectId = "goreecloud-index",
        source = "identity:evidence:index-contacts",
        observedAt = now.minusSeconds(30),
        validUntil = now.plusSeconds(300),
        containsUserContent = false,
        containsSecretMaterial = false,
        containsReusableCredentials = containsReusableCredentials,
        containsRawProfileAttributes = false,
    )
}
