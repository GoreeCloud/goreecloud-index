package com.goreecloud.index.core

import java.time.Instant

enum class PrivacyShieldDecisionOutcome {
    ALLOW,
    DENY,
    ALLOW_WITH_CONSTRAINTS,
    REQUIRE_USER_DECISION,
}

data class PrivacyShieldAuthorizationRequest(
    val requestId: String,
    val requesterId: String,
    val requesterType: String,
    val resourceId: String,
    val resourceClassification: String,
    val operation: String,
    val purpose: String,
    val processingZone: String,
    val destination: String,
    val retentionMode: String,
    val externalDisclosure: Boolean,
    val manifestReference: String,
) {
    fun isExactIndexContactsSearchScope(): Boolean =
        validContractText(requestId) &&
            requesterId == "com.goreecloud.index" &&
            requesterType == "application" &&
            resourceId == "android.contacts" &&
            validContractText(resourceClassification) &&
            operation == "search" &&
            purpose == "universal-search" &&
            processingZone == "local" &&
            destination == "goreecloud-index-ui" &&
            retentionMode == "none" &&
            !externalDisclosure &&
            manifestReference == "goreecloud/privacy-shield.application-manifest.json"
}

object ContactsPrivacyShieldAuthorization {
    fun request(
        requestId: String,
        resourceClassification: String,
    ): PrivacyShieldAuthorizationRequest? {
        if (!validContractText(requestId) || !validContractText(resourceClassification)) {
            return null
        }

        return PrivacyShieldAuthorizationRequest(
            requestId = requestId,
            requesterId = "com.goreecloud.index",
            requesterType = "application",
            resourceId = "android.contacts",
            resourceClassification = resourceClassification,
            operation = "search",
            purpose = "universal-search",
            processingZone = "local",
            destination = "goreecloud-index-ui",
            retentionMode = "none",
            externalDisclosure = false,
            manifestReference = "goreecloud/privacy-shield.application-manifest.json",
        )
    }
}

data class PrivacyShieldDecisionEvidence(
    val decisionId: String = "",
    val requestId: String,
    val outcome: PrivacyShieldDecisionOutcome,
    val reasonCode: String = "",
    val permittedOperations: Set<String> = emptySet(),
    val processingZone: String = "",
    val permittedDestinations: Set<String> = emptySet(),
    val retentionMode: String = "",
    val effectiveScopeConstrained: Boolean = false,
    val consentRequired: Boolean = false,
    val obligations: List<String> = emptyList(),
    val evidenceReference: String? = null,
    val expiresAt: Instant? = null,
)

object PrivacyShieldAuthorityAdapter {
    fun evaluate(
        decision: PrivacyShieldDecisionEvidence?,
        expectedRequest: PrivacyShieldAuthorizationRequest?,
        now: Instant = Instant.now(),
    ): IndexAuthorityEvidence {
        val expectedRequestId = expectedRequest?.requestId.orEmpty()
        if (decision == null || expectedRequest == null || !expectedRequest.isExactIndexContactsSearchScope()) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (decision.requestId != expectedRequestId || decision.evidenceReference.isNullOrBlank()) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (
            !validContractText(decision.decisionId) ||
            !validContractText(decision.reasonCode) ||
            decision.processingZone.isBlank() ||
            decision.retentionMode.isBlank()
        ) {
            return IndexAuthorityEvidence.unavailable()
        }
        if (decision.expiresAt?.isAfter(now) == false) {
            return IndexAuthorityEvidence.unavailable()
        }

        val exactScope =
            decision.permittedOperations == setOf(expectedRequest.operation) &&
                decision.processingZone == expectedRequest.processingZone &&
                decision.permittedDestinations == setOf(expectedRequest.destination) &&
                decision.retentionMode == expectedRequest.retentionMode
        if (!exactScope) {
            return IndexAuthorityEvidence.unavailable()
        }

        val projectedOutcome = when (decision.outcome) {
            PrivacyShieldDecisionOutcome.ALLOW -> when {
                decision.consentRequired -> IndexAuthorityOutcome.REQUIRE_USER_DECISION
                decision.effectiveScopeConstrained || decision.obligations.isNotEmpty() ->
                    IndexAuthorityOutcome.ALLOW_WITH_CONSTRAINTS
                else -> IndexAuthorityOutcome.ALLOW
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
    val expectedPrivacyShieldRequest: PrivacyShieldAuthorizationRequest? = null,
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
            expectedRequest = snapshot.expectedPrivacyShieldRequest,
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

private fun validContractText(value: String): Boolean =
    value.isNotBlank() &&
        value.length <= 256 &&
        value.none { character -> character.code < 0x20 || character.code == 0x7f }
