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
        val decision = validPrivacyDecision(request)
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW, evidence.outcome)
        assertTrue(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldMissingEnvelopeFailsClosed() {
        val request = contactsRequest("contacts-read-no-envelope")
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = validPrivacyDecision(request),
            expectedRequest = request,
            envelope = null,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldWrongProducerRepositoryFailsClosed() {
        val request = contactsRequest("contacts-read-wrong-producer")
        val decision = validPrivacyDecision(request)
        val envelope = privacyEnvelope(request, decision).copy(
            producer = privacyEnvelope(request, decision).producer.copy(
                repository = "GoreeCloud/not-privacy-shield",
            ),
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = envelope,
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldSensitiveEnvelopeFailsClosed() {
        val request = contactsRequest("contacts-read-sensitive-envelope")
        val decision = validPrivacyDecision(request)
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision).copy(containsUserContent = true),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldObligationsRemainConstrained() {
        val request = contactsRequest("contacts-read-2")
        val decision = validPrivacyDecision(
            request = request,
            obligations = listOf("do-not-retain"),
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW_WITH_CONSTRAINTS, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldStaleEvidenceFailsClosed() {
        val request = contactsRequest("contacts-read-3")
        val decision = validPrivacyDecision(
            request = request,
            expiresAt = now.minusSeconds(1),
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldOperationMismatchFailsClosed() {
        val request = contactsRequest("contacts-read-operation")
        val decision = validPrivacyDecision(
            request = request,
            permittedOperations = setOf("search", "export"),
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldDestinationMismatchFailsClosed() {
        val request = contactsRequest("contacts-read-destination")
        val decision = validPrivacyDecision(
            request = request,
            permittedDestinations = setOf("goreecloud-index-ui", "external-service"),
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldRetentionMismatchFailsClosed() {
        val request = contactsRequest("contacts-read-retention")
        val decision = validPrivacyDecision(
            request = request,
            retentionMode = "session",
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun privacyShieldConsentRequirementDoesNotBecomeAllow() {
        val request = contactsRequest("contacts-read-consent")
        val decision = validPrivacyDecision(
            request = request,
            consentRequired = true,
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.REQUIRE_USER_DECISION, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun privacyShieldEffectiveScopeConstraintDoesNotBecomeAllow() {
        val request = contactsRequest("contacts-read-scope")
        val decision = validPrivacyDecision(
            request = request,
            effectiveScopeConstrained = true,
        )
        val evidence = PrivacyShieldAuthorityAdapter.evaluate(
            decision = decision,
            expectedRequest = request,
            envelope = privacyEnvelope(request, decision),
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
        val identity = validIdentityEvidence(outcome = "authorized")
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = identity,
            expectedSubjectKind = "application",
            expectedSubjectId = "goreecloud-index",
            envelope = identityEnvelope(identity),
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter { outcome ->
                if (outcome == "authorized") IndexAuthorityOutcome.ALLOW else null
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.ALLOW, evidence.outcome)
        assertTrue(evidence.isUnconstrainedAllow())
    }

    @Test
    fun identityMissingEnvelopeFailsClosed() {
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = validIdentityEvidence(outcome = "authorized"),
            expectedSubjectKind = "application",
            expectedSubjectId = "goreecloud-index",
            envelope = null,
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter {
                IndexAuthorityOutcome.ALLOW
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun identityWrongProducerRepositoryFailsClosed() {
        val identity = validIdentityEvidence(outcome = "authorized")
        val envelope = identityEnvelope(identity).copy(
            producer = identityEnvelope(identity).producer.copy(
                repository = "GoreeCloud/not-identity",
            ),
        )
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = identity,
            expectedSubjectKind = "application",
            expectedSubjectId = "goreecloud-index",
            envelope = envelope,
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter {
                IndexAuthorityOutcome.ALLOW
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun identityEnvelopeOutcomeMismatchFailsClosed() {
        val identity = validIdentityEvidence(outcome = "authorized")
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = identity,
            expectedSubjectKind = "application",
            expectedSubjectId = "goreecloud-index",
            envelope = identityEnvelope(identity).copy(outcome = "different-outcome"),
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter {
                IndexAuthorityOutcome.ALLOW
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun identityUnknownOutcomeFailsClosed() {
        val identity = validIdentityEvidence(outcome = "identity-specific-future-state")
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = identity,
            expectedSubjectKind = "application",
            expectedSubjectId = "goreecloud-index",
            envelope = identityEnvelope(identity),
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter { null },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun identityMinimizationViolationFailsClosed() {
        val identity = validIdentityEvidence(
            outcome = "authorized",
            containsReusableCredentials = true,
        )
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = identity,
            expectedSubjectKind = "application",
            expectedSubjectId = "goreecloud-index",
            envelope = identityEnvelope(identity),
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter {
                IndexAuthorityOutcome.ALLOW
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
        assertFalse(evidence.isUnconstrainedAllow())
    }

    @Test
    fun identityExpiredEnvelopeFailsClosed() {
        val identity = validIdentityEvidence(
            outcome = "authorized",
            validUntil = now.minusSeconds(1),
        )
        val evidence = IdentityAuthorityAdapter.evaluate(
            evidence = identity,
            expectedSubjectKind = "application",
            expectedSubjectId = "goreecloud-index",
            envelope = identityEnvelope(identity),
            outcomeInterpreter = IdentityAuthorizationOutcomeInterpreter {
                IndexAuthorityOutcome.ALLOW
            },
            now = now,
        )

        assertEquals(IndexAuthorityOutcome.UNAVAILABLE, evidence.outcome)
    }

    @Test
    fun meshEnvelopeRejectsMalformedProducerRevision() {
        val identity = validIdentityEvidence(outcome = "authorized")
        val envelope = identityEnvelope(identity).copy(
            producer = identityEnvelope(identity).producer.copy(revision = "not-a-git-sha"),
        )
        assertFalse(
            MeshEvidenceEnvelopeValidator.validate(
                envelope = envelope,
                expectation = identityExpectation(identity),
                now = now,
            ),
        )
    }

    @Test
    fun meshEnvelopeRejectsMalformedPayloadDigest() {
        val identity = validIdentityEvidence(outcome = "authorized")
        val envelope = identityEnvelope(identity).copy(payloadDigest = "sha256:not-a-digest")
        assertFalse(
            MeshEvidenceEnvelopeValidator.validate(
                envelope = envelope,
                expectation = identityExpectation(identity),
                now = now,
            ),
        )
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
        val privacyDecision = validPrivacyDecision(request)
        val identity = validIdentityEvidence(outcome = "authorized")
        val snapshot = IndexPlatformAuthoritySnapshot(
            privacyShieldDecision = privacyDecision,
            expectedPrivacyShieldRequest = request,
            privacyShieldEnvelope = privacyEnvelope(request, privacyDecision),
            identityEvidence = identity,
            expectedIdentitySubjectKind = "application",
            expectedIdentitySubjectId = "goreecloud-index",
            identityEnvelope = identityEnvelope(identity),
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
        evidenceReference = "privacy-shield://evidence/${request.requestId}",
        expiresAt = expiresAt,
    )

    private fun privacyEnvelope(
        request: PrivacyShieldAuthorizationRequest,
        decision: PrivacyShieldDecisionEvidence,
    ): MeshEvidenceEnvelope = MeshEvidenceEnvelope(
        version = "goreecloud.evidence-envelope.v1",
        id = "privacy-shield-${decision.decisionId}",
        producer = MeshEvidenceProducer(
            system = "privacy-shield",
            repository = "GoreeCloud/goreecloud-privacy-shield",
            revision = "a".repeat(40),
            contract = "contracts/privacy-shield.decision.schema.json",
        ),
        authorityDomain = "privacy",
        subject = MeshEvidenceSubject(
            kind = "resource",
            id = request.resourceId,
        ),
        assertion = "privacy-decision",
        outcome = decision.outcome.name,
        source = requireNotNull(decision.evidenceReference),
        observedAt = now.minusSeconds(30),
        validUntil = decision.expiresAt ?: now.plusSeconds(300),
        dataClass = "derived",
        payloadDigest = "sha256:" + "b".repeat(64),
        containsUserContent = false,
        containsSecretMaterial = false,
    )

    private fun validIdentityEvidence(
        outcome: String,
        containsReusableCredentials: Boolean = false,
        validUntil: Instant = now.plusSeconds(300),
    ): IdentityAuthorizationEvidence = IdentityAuthorizationEvidence(
        contract = "goreecloud.identity-evidence.v1",
        authorityDomain = "authorization",
        assertion = "authorization-decision",
        outcome = outcome,
        subjectKind = "application",
        subjectId = "goreecloud-index",
        subjectScope = "contacts-search",
        source = "identity:evidence:index-contacts",
        observedAt = now.minusSeconds(30),
        validUntil = validUntil,
        dataClass = "derived",
        payloadDigest = "sha256:" + "c".repeat(64),
        containsUserContent = false,
        containsSecretMaterial = false,
        containsReusableCredentials = containsReusableCredentials,
        containsRawProfileAttributes = false,
    )

    private fun identityEnvelope(
        evidence: IdentityAuthorizationEvidence,
    ): MeshEvidenceEnvelope = MeshEvidenceEnvelope(
        version = "goreecloud.evidence-envelope.v1",
        id = "identity-index-contacts-authorization",
        producer = MeshEvidenceProducer(
            system = "goreecloud-identity",
            repository = "GoreeCloud/goreecloud-identity",
            revision = "d".repeat(40),
            contract = "contracts/identity.evidence.schema.json",
        ),
        authorityDomain = evidence.authorityDomain,
        subject = MeshEvidenceSubject(
            kind = evidence.subjectKind,
            id = evidence.subjectId,
            scope = evidence.subjectScope,
        ),
        assertion = evidence.assertion,
        outcome = evidence.outcome,
        source = evidence.source,
        observedAt = evidence.observedAt,
        validUntil = evidence.validUntil,
        dataClass = evidence.dataClass,
        payloadDigest = evidence.payloadDigest,
        containsUserContent = false,
        containsSecretMaterial = false,
    )

    private fun identityExpectation(
        evidence: IdentityAuthorizationEvidence,
    ): MeshEvidenceExpectation = MeshEvidenceExpectation(
        producerSystem = "goreecloud-identity",
        repository = "GoreeCloud/goreecloud-identity",
        contract = "contracts/identity.evidence.schema.json",
        authorityDomain = evidence.authorityDomain,
        subjectKind = evidence.subjectKind,
        subjectId = evidence.subjectId,
        subjectScope = evidence.subjectScope,
        assertion = evidence.assertion,
        outcome = evidence.outcome,
        source = evidence.source,
        observedAt = evidence.observedAt,
        validUntil = evidence.validUntil,
    )
}
