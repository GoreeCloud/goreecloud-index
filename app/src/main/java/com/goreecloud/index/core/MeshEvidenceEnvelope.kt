package com.goreecloud.index.core

import java.time.Instant

private const val MESH_EVIDENCE_ENVELOPE_VERSION = "goreecloud.evidence-envelope.v1"
private val MESH_REVISION_PATTERN = Regex("^[0-9a-f]{40}$")
private val MESH_DIGEST_PATTERN = Regex("^sha256:[0-9a-f]{64}$")

data class MeshEvidenceProducer(
    val system: String,
    val repository: String,
    val revision: String,
    val contract: String,
)

data class MeshEvidenceSubject(
    val kind: String,
    val id: String,
    val scope: String = "",
)

data class MeshEvidenceEnvelope(
    val version: String,
    val id: String,
    val producer: MeshEvidenceProducer,
    val authorityDomain: String,
    val subject: MeshEvidenceSubject,
    val assertion: String,
    val outcome: String,
    val source: String,
    val observedAt: Instant,
    val validUntil: Instant,
    val dataClass: String,
    val payloadDigest: String? = null,
    val containsUserContent: Boolean,
    val containsSecretMaterial: Boolean,
)

data class MeshEvidenceExpectation(
    val producerSystem: String,
    val repository: String,
    val contract: String,
    val authorityDomain: String,
    val subjectKind: String,
    val subjectId: String,
    val subjectScope: String? = null,
    val assertion: String,
    val outcome: String,
    val source: String,
    val observedAt: Instant? = null,
    val validUntil: Instant? = null,
)

object MeshEvidenceEnvelopeValidator {
    fun validate(
        envelope: MeshEvidenceEnvelope?,
        expectation: MeshEvidenceExpectation,
        now: Instant = Instant.now(),
    ): Boolean {
        if (envelope == null) return false
        if (
            envelope.version != MESH_EVIDENCE_ENVELOPE_VERSION ||
            !validText(envelope.id, 128) ||
            envelope.producer.system != expectation.producerSystem ||
            envelope.producer.repository != expectation.repository ||
            !MESH_REVISION_PATTERN.matches(envelope.producer.revision) ||
            envelope.producer.contract != expectation.contract ||
            envelope.authorityDomain != expectation.authorityDomain ||
            envelope.subject.kind != expectation.subjectKind ||
            envelope.subject.id != expectation.subjectId ||
            expectation.subjectScope?.let { envelope.subject.scope == it } == false ||
            envelope.assertion != expectation.assertion ||
            envelope.outcome != expectation.outcome ||
            envelope.source != expectation.source ||
            !validDataClass(envelope.dataClass) ||
            envelope.containsUserContent ||
            envelope.containsSecretMaterial
        ) {
            return false
        }
        if (
            !validText(envelope.subject.kind, 64) ||
            !validText(envelope.subject.id, 256) ||
            envelope.subject.scope.length > 256 ||
            !validText(envelope.assertion, 128) ||
            !validText(envelope.outcome, 128) ||
            !validText(envelope.source, 512)
        ) {
            return false
        }
        if (envelope.payloadDigest?.let { !MESH_DIGEST_PATTERN.matches(it) } == true) {
            return false
        }
        if (
            envelope.observedAt.isAfter(now) ||
            !envelope.validUntil.isAfter(envelope.observedAt)
        ) {
            return false
        }
        if (expectation.observedAt?.let { envelope.observedAt != it } == true) return false
        if (expectation.validUntil?.let { envelope.validUntil != it } == true) return false
        return true
    }

    fun isCurrent(envelope: MeshEvidenceEnvelope?, now: Instant = Instant.now()): Boolean =
        envelope != null && envelope.validUntil.isAfter(now)

    private fun validDataClass(value: String): Boolean =
        value == "public" || value == "operational" || value == "derived"

    private fun validText(value: String, max: Int): Boolean =
        value.isNotBlank() &&
            value.length <= max &&
            value.none { character -> character.code < 0x20 || character.code == 0x7f }
}
