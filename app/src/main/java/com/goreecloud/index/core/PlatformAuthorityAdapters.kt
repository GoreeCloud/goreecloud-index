package com.goreecloud.index.core

import java.time.Instant

enum class PrivacyShieldDecisionOutcome {
    ALLOW,
    DENY,
    ALLOW_WITH_CONSTRAINTS,
    REQUIRE_USER_DECISION,
}

data class PrivacyShieldDecisionEvidence(
    val requestId: String,
    val outcome: PrivacyShieldDecisionOutcome,
    val obligations: List<String> = emptyList(),
    val evidenceReference: String? = null,
    val expiresAt: Instant? = null,
)

object PrivacyShieldAuthorityAdapter {
    fun evaluate(
        decision: PrivacyShieldDecisionEvidence?,
        expectedRequestId: String,
        now: Instant = Instant.now(),
    ): IndexAuthorityEvidence {
        if (decision == null || expectedRequestId.isBlank()) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (decision.requestId != expectedRequestId || decision.evidenceReference.isNullOrBlank()) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (decision.expiresAt?.isAfter(now) == false) {
            return IndexAuthorityEvidence.unavailable()
        }

        val projectedOutcome = when (decision.outcome) {
            PrivacyShieldDecisionOutcome.ALLOW -> {
                if (decision.obligations.isEmpty()) {
                    IndexAuthorityOutcome.ALLOW
                } else {
                    IndexAuthorityOutcome.ALLOW_WITH_CONSTRAINTS
                }
            }
            PrivacyShieldDecisionOutcome.DENY -> IndexAuthorityOutcome.DENY
            PrivacyShieldDecisionOutcome.ALLOW_WITH_CONSTRAINTS ->
                IndexAuthorityOutcome.ALLOW_WITH_CONSTRAINTS
            PrivacyShieldDecisionOutcome.REQUIRE_USER_DECISION ->
                IndexAuthorityOutcome.REQUIRE_USER_DECISION
        }

        return IndexAuthorityEvidence(
            outcome = projectedOutcome,
            reference = decision.evidenceReference,
        )
    }
}

data class IdentityAuthorizationEvidence(
    val contract: String,
    val authorityDomain: String,
    val assertion: String,
    val outcome: String,
    val subjectId: String,
    val source: String,
    val observedAt: Instant,
    val validUntil: Instant,
    val containsUserContent: Boolean,
    val containsSecretMaterial: Boolean,
    val containsReusableCredentials: Boolean,
    val containsRawProfileAttributes: Boolean,
)

fun interface IdentityAuthorizationOutcomeInterpreter {
    fun interpret(outcome: String): IndexAuthorityOutcome?
}

object IdentityAuthorityAdapter {
    private const val IDENTITY_EVIDENCE_CONTRACT = "goreecloud.identity-evidence.v1"
    private const val AUTHORIZATION_DOMAIN = "authorization"
    private const val AUTHORIZATION_ASSERTION = "authorization-decision"

    fun evaluate(
        evidence: IdentityAuthorizationEvidence?,
        expectedSubjectId: String,
        outcomeInterpreter: IdentityAuthorizationOutcomeInterpreter,
        now: Instant = Instant.now(),
    ): IndexAuthorityEvidence {
        if (evidence == null || expectedSubjectId.isBlank()) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (
            evidence.contract != IDENTITY_EVIDENCE_CONTRACT ||
            evidence.authorityDomain != AUTHORIZATION_DOMAIN ||
            evidence.assertion != AUTHORIZATION_ASSERTION ||
            evidence.subjectId != expectedSubjectId ||
            evidence.source.isBlank() ||
            evidence.outcome.isBlank()
        ) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (evidence.observedAt.isAfter(now) || !evidence.validUntil.isAfter(now)) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (
            evidence.containsUserContent ||
            evidence.containsSecretMaterial ||
            evidence.containsReusableCredentials ||
            evidence.containsRawProfileAttributes
        ) {
            return IndexAuthorityEvidence.unavailable()
        }

        val projectedOutcome = outcomeInterpreter.interpret(evidence.outcome)
            ?: return IndexAuthorityEvidence.unavailable()

        return IndexAuthorityEvidence(
            outcome = projectedOutcome,
            reference = evidence.source,
        )
    }
}

data class IndexPlatformAuthoritySnapshot(
    val privacyShieldDecision: PrivacyShieldDecisionEvidence? = null,
    val expectedPrivacyShieldRequestId: String = "",
    val identityEvidence: IdentityAuthorizationEvidence? = null,
    val expectedIdentitySubjectId: String = "",
    val identityOutcomeInterpreter: IdentityAuthorizationOutcomeInterpreter =
        IdentityAuthorizationOutcomeInterpreter { null },
)

fun interface IndexPlatformAuthorityGateway {
    fun contactsSnapshot(): IndexPlatformAuthoritySnapshot
}

object UnavailableIndexPlatformAuthorityGateway : IndexPlatformAuthorityGateway {
    override fun contactsSnapshot(): IndexPlatformAuthoritySnapshot = IndexPlatformAuthoritySnapshot()
}

object ContactsAuthorityProjection {
    fun project(
        androidPermissionGranted: Boolean,
        snapshot: IndexPlatformAuthoritySnapshot,
        now: Instant = Instant.now(),
    ): IndexProviderAuthority = IndexProviderAuthority(
        androidPermissionGranted = androidPermissionGranted,
        privacyShield = PrivacyShieldAuthorityAdapter.evaluate(
            decision = snapshot.privacyShieldDecision,
            expectedRequestId = snapshot.expectedPrivacyShieldRequestId,
            now = now,
        ),
        identity = IdentityAuthorityAdapter.evaluate(
            evidence = snapshot.identityEvidence,
            expectedSubjectId = snapshot.expectedIdentitySubjectId,
            outcomeInterpreter = snapshot.identityOutcomeInterpreter,
            now = now,
        ),
    )
}
