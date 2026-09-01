package com.goreecloud.index.core

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlatformAuthorityAdaptersTest {
    private val now = Instant.parse("2026-09-01T16:00:00Z")

    @Test
    fun privacyShieldUnconstrainedAllowProjectsAllow() {
        val request = contactsRequest("contacts-read-1")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(request),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW, evidence.outcome)
        assertTrue(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldObligationsRemainConstrained() {
        val request = contactsRequest("contacts-read-2")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(
                request = request,
                obligations = listOf("do-not-retain"),
            ),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW_WITH_CONSTRAINTS, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldStaleEvidenceFailsClosed() {
        val request = contactsRequest("contacts-read-3")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(
                request = request,
                expiresAt = now.minusSeconds(1),
            ),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldOperationMismatchFailsClosed() {
        val request = contactsRequest("contacts-read-operation")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(
                request = request,
                permittedOperations = setOf("search", "export"),
            ),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldDestinationMismatchFailsClosed() {
        val request = contactsRequest("contacts-read-destination")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(
                request = request,
                permittedDestinations = setOf("goreecloud-index-ui", "external-service"),
            ),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldRetentionMismatchFailsClosed() {
        val request = contactsRequest("contacts-read-retention")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(
                request = request,
                retentionMode = "session",
            ),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldConsentRequirementDoesNotBecomeAllow() {
        val request = contactsRequest("contacts-read-consent")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(
                request = request,
                consentRequired = true,
            ),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.REQUIRE_USER_DECISION, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldEffectiveScopeConstraintDoesNotBecomeAllow() {
        val request = contactsRequest("contacts-read-scope")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(
                request = request,
                effectiveScopeConstrained = true,
            ),
            expectedRequest = request,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW_WITH_CONSTRAINTS, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun contactsPrivacyRequestMatchesReviewedManifestScope() {
        val request = ContactsPrivacyShieldAuthorization.request(
            requestId = "contacts-manifest-1",
            resourceClassification = "private-contact-record",
        )

        assertNotNull(request)
        requireNotNull(request)
        assertTrue(request.isExactIndexContactsSearchScope())
        assertEquals("com.goreecloud.index", request.requesterId)
        assertEquals("application", request.requesterType)
        assertEquals("android.contacts", request.resourceId)
        assertEquals("search", request.operation)
        assertEquals("universal-search", request.purpose)
        assertEquals("local", request.processingZone)
        assertEquals("goreecloud-index-ui", request.destination)
        assertEquals("none", request.retentionMode)
        assertFalse(request.externalDisclosure)
        assertEquals(
            "goreecloud/privacy-shield.application-manifest.json",
            request.manifestReference,
        )
    }

    @Test
    fun contactsPrivacyRequestRequiresAuthoritativeClassification() {
        val request = ContactsPrivacyShieldAuthorization.request(
            requestId = "contacts-manifest-2",
            resourceClassification = "",
        )

        assertEquals(null, request)
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

    @Test
    fun unavailableGatewayKeepsContactsBlocked() {
        val authority = ContactsAuthorityProjection.project(
            androidPermissionGranted = true,
            snapshot = UnavailableIndexPlatformAuthorityGateway.contactsSnapshot(),
            now = now,
        )

        assertTrue(authority.androidPermissionGranted)
        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, authority.privacyShield.outcome)
        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, authority.identity.outcome)
        assertFalse(authority.satisfiesAll(setOf(
            IndexAuthorityRequirement.ANDROID_RUNTIME_PERMISSION,
            IndexAuthorityRequirement.PRIVACY_SHIELD,
            IndexAuthorityRequirement.GOREECLOUD_IDENTITY,
        )))
    }

    @Test
    fun acceptedSnapshotCanSatisfyContactsAuthorities() {
        val request = contactsRequest("contacts-read-4")
        val snapshot = IndexPlatformAuthoritySnapshot(
            privacyShieldDecision = validPrivacyDecision(request),
            expectedPrivacyShieldRequest = request,
            identityEvidence = validIdentityEvidence(outcome = "authorized"),
            expectedIdentitySubjectId = "goreecloud-index",
            identityOutcomeInterpreter = IdentityAuthorizationOutcomeInterpreter { outcome ->
                if (outcome == "authorized") IndexAuthorityOutcome.ALLOW else null
            },
        )
        val authority = ContactsAuthorityProjection.project(
            androidPermissionGranted = true,
            snapshot = snapshot,
            now = now,
        )

        assertTrue(authority.satisfiesAll(setOf(
            IndexAuthorityRequirement.ANDROID_RUNTIME_PERMISSION,
            IndexAuthorityRequirement.PRIVACY_SHIELD,
            IndexAuthorityRequirement.GOREECLOUD_IDENTITY,
        )))
    }

    private fun contactsRequest(requestId: String): PrivacyShieldAuthorizationRequest =
        requireNotNull(
            ContactsPrivacyShieldAuthorization.request(
                requestId = requestId,
                resourceClassification = "private-contact-record",
            ),
        )

    private fun validPrivacyDecision(
        request: PrivacyShieldAuthorizationRequest,
        permittedOperations: Set<String> = setOf(request.operation),
        permittedDestinations: Set<String> = setOf(request.destination),
        retentionMode: String = request.retentionMode,
        effectiveScopeConstrained: Boolean = false,
        consentRequired: Boolean = false,
        obligations: List<String> = emptyList(),
        expiresAt: Instant? = now.plusSeconds(300),
    ): PrivacyShieldDecisionEvidence = PrivacyShieldDecisionEvidence(
        decisionId = "decision-${request.requestId}",
        requestId = request.requestId,
        outcome = PrivacyShieldDecisionOutcome.ALLOW,
        reasonCode = "authorized",
        permittedOperations = permittedOperations,
        processingZone = request.processingZone,
        permittedDestinations = permittedDestinations,
        retentionMode = retentionMode,
        effectiveScopeConstrained = effectiveScopeConstrained,
        consentRequired = consentRequired,
        obligations = obligations,
        evidenceReference = "privacy-shield:evidence:${request.requestId}",
        expiresAt = expiresAt,
    )

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
