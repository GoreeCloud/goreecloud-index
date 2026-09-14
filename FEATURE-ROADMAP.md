# GoreeCloud Index — Feature Roadmap

**Status:** Active roadmap control  
**As of:** 2026-09-14  
**Authoritative project record:** Project Specification — Index  
**Canonical repository:** GoreeCloud/goreecloud-index  
**Drive control:** `GoreeCloud/Feature Roadmap/GoreeCloud Index/FEATURE-ROADMAP.docx`

## Purpose

This file is the repository-side feature roadmap control for GoreeCloud Index. It records current planned and recommended feature work without replacing the authoritative project record, implementation evidence, release gates, or GoreeCloud Tasks Management.

## Current Development checkpoint

Accepted `main` remains the previously merged Development line. Active PR #35 is a newer Development candidate and is not Stable, production accepted, or merged merely because its CI passes.

The latest verified implementation checkpoint on PR #35 is `36056e4640e9c083fadbcabc0b0fa05d40615070`. Platform Contract #36 and Android Index foundation validation #163 both passed on that exact implementation revision. The Android workflow passed exact-revision recording, repository/provider/branding gates, the full unit-test suite, lint, Development APK assembly, APK identity verification, and Development APK evidence upload.

The PR #35 candidate now includes:

- current Glaze UI V1.4 / `1.4.0` targeting while retaining migration-required status pending Index-local rendered/native, accessibility, localization/RTL, representative-device, performance, and production acceptance;
- corrected Platform Contract 0.3 Development declarations for the seven Integral Platform Systems: GoreeCloud Manager, Privacy Shield, Wardveil Security, Everkeep, Glaze UI, GoreeCloud Mesh, and GoreeCloud Identity;
- GoreeCloud Sync tracked separately as an application/service capability rather than an Integral Platform System;
- a hardened GoreeCloud Search consumer boundary with explicit Development/Production capability acceptance, operation-scoped Privacy Shield request correlation, canonical opaque `psc_*` reference validation, production requirements for POST + JSON-body transport, authenticated requester evidence, and fail-closed Search capability checks;
- Index-owned cross-provider normalized textual relevance rather than comparing provider-private raw score magnitudes;
- bounded source-health composition where an explicitly degraded source loses only an equal-relevance tie, while a stronger degraded match still outranks a weaker healthy match;
- a privacy-preserving final processing-location tie-breaker of `LOCAL → MIXED → REMOTE` only after relevance and health are equal, without broadening provider eligibility or bypassing authority gates;
- independent engine enforcement of the requested `IndexQuery.maxResults` provider-response bound, with over-limit responses surfaced as `INVALID_RESULT` evidence;
- preservation of the strongest distinct provider-local results up to the requested bound rather than trusting provider response order;
- duplicate provider result IDs prevented from consuming the bounded provider budget;
- deterministic incremental result delivery through one `Flow<IndexSearchSnapshot>` composition path: static authority issues can emit immediately, eligible providers execute concurrently, each provider completion triggers a fully recomposed snapshot, and the one-shot API returns the final Flow snapshot;
- collector/query cancellation propagates to outstanding provider work without manufacturing `FAILED`/`TIMED_OUT` evidence;
- existing invalid-result evidence precedence, provider timeout isolation, local-only gating, and provider-scoped deduplication preserved.

Remote Search execution remains blocked on accepted runtime Privacy Shield decision acquisition plus approved GoreeCloud Identity requester/service authentication. The current candidate does not enable live remote Search traffic or claim production Search acceptance.

## Roadmap

| ID | Feature / obligation | Priority | Current state |
| --- | --- | --- | --- |
| FR-001 | Reconcile and maintain every current planned or recommended GoreeCloud Index feature from the authoritative project record and verified repository evidence in this roadmap. | High | Ongoing control; synchronized to the verified September 14 PR #35 implementation checkpoint. |
| FR-002 | Move actionable feature obligations into GoreeCloud Tasks Management when required, preserving priority, dependency, and lifecycle disposition. | High | Ongoing control; GOR-26 tracks the current modernization work and exact verified checkpoints. |
| FR-003 | Do not mark features implemented, complete, cancelled, or superseded without authoritative evidence and synchronized repository/Drive roadmap updates. | High | Ongoing control; PR/Linear checkpoints distinguish verified Development source from Stable or production acceptance. |
| FR-004 | Complete current Glaze UI V1.4 / `1.4.0` native application adoption plus fresh rendered/native, interaction, accessibility, adaptive/form-factor, representative-device/OEM, performance, Human Visual Excellence, rollback, release, and production acceptance. | High | V1.4 source targeting and semantic foundations exist. Full Index-local rendered/native consumer conformance, accessibility/device evidence, and production acceptance remain open. |
| FR-005 | Keep query normalization, matching, cross-provider composition, and result ordering deterministic without retaining private query data or broadening provider authority. | High | Active PR #35 provides Index-owned textual normalization, same-provider score/source ordering, health-aware equal-relevance composition, `LOCAL → MIXED → REMOTE` full-tie ordering, deterministic identity fallback, bounded provider fan-out, and deterministic incremental recomposition. Exact implementation checkpoint `36056e46…` passed Platform Contract #36 and Android #163. |
| FR-006 | Expand first-party providers only through explicit provider contracts and accepted source-authority boundaries, including Files, Calendar, media, additional GoreeCloud application content, and connected-device resources where approved. | High | Applications and bounded Settings are implemented; Contacts remains fail-closed pending accepted authority/runtime enablement. Provider provenance, contract-version validation, result validation, provider response bounds, and cancellation semantics are enforced. Files/Calendar/media remain planned. |
| FR-007 | Integrate GoreeCloud Search as the clearly identified Internet/Web provider without silently sending unrelated local query context or provider data remotely. | High | PR #35 contains the hardened consumer contract: explicit Development/Production acceptance, bounded POST/JSON requirements, capability discovery checks, request correlation, canonical Privacy Shield reference handling, safe result actions, and privacy-safe diagnostics. Incremental federation is transport-neutral and does not enable live Search. Runtime transport remains disabled pending accepted Identity-backed requester/service authentication and real Privacy Shield decision acquisition. |
| FR-008 | Integrate and accept Privacy Shield, Wardveil Security, GoreeCloud Identity, GoreeCloud Mesh, Everkeep, and GoreeCloud Manager runtime authorities while preserving provider isolation and fail-closed unknown/stale evidence behavior. | High | Blocked pending accepted live Platform-System runtime evidence. Platform Contract declarations remain nonconformant/migration-required where evidence is incomplete. |
| FR-009 | Add provider enable/disable, permission review, local-only mode, Internet-provider preference, third-party connection/revocation, history controls if history is implemented, and index/cache clearing with understandable local-versus-remote processing disclosure. | Medium | Planned. Current shipped execution remains local-only; the PR candidate adds internal local-vs-remote composition semantics but not user-facing provider controls. |
| FR-010 | Add local indexing only where it materially improves latency or result quality, keeping indexes reconstructible, profile/device scoped, removable, rebuildable, and non-authoritative. | Medium | Planned; no production local-content index accepted. |
| FR-011 | Validate provider cancellation/timeouts, partial-result behavior, degraded-provider reporting, resource use, incremental delivery, accessibility, localization/RTL, text scaling/reflow, reduced-motion/transparency, contrast, and representative performance before release advancement. | High | Source-level cancellation/timeouts, partial-result isolation, degraded-state propagation, deterministic composition, local-first full-tie behavior, provider-result validation, bounded fan-out, and incremental Flow delivery are verified at `36056e46…`. Remaining work is representative-device/perceived-latency behavior, potential bounded update coalescing if measured churn warrants it, rendered accessibility, localization/RTL, text scaling, reduced-effects, contrast, and device qualification. |
| FR-012 | Complete controlled packaging/signing/provenance, upgrade/rollback, Release Candidate qualification, production approval, and Stable qualification after all applicable provider/platform/accessibility/performance gates pass. | High | Development APK identity/evidence exists for PR validation only. Production signing/distribution, rollback, RC, production approval, and Stable gates remain open. |

## Current sequencing recommendation

1. Preserve the verified fail-closed provider, Search-capability, authority, ranking, cancellation, fan-out, and incremental-composition boundaries while continuing development from PR #35.
2. Measure incremental rendering/provider completion on representative Android devices and add bounded update coalescing only if evidence shows excessive UI churn; never create a second ranking/composition path.
3. Establish an approved GoreeCloud Identity requester/service-authentication path and real Privacy Shield decision-acquisition adapter before enabling live GoreeCloud Search transport.
4. Add user-facing provider controls and local-versus-remote disclosure before remote Search becomes user-operable.
5. Complete Glaze UI V1.4 rendered/native accessibility, localization/RTL, text-scaling, reduced-effects, representative-device, and performance acceptance.
6. Add Files, Calendar, media, and other first-party providers one bounded authority contract at a time.
7. Complete Platform-System runtime acceptance, production signing/provenance, recovery/rollback, Release Candidate, production, and Stable gates without collapsing independent authorities.

## Maintenance and synchronization

This roadmap and the corresponding Drive `FEATURE-ROADMAP.docx` must remain materially synchronized with one another and with the authoritative project or service record. Update both copies whenever feature scope, priority, dependency, implementation status, cancellation, supersession, recommendation, or verification state materially changes.

No feature may be represented as complete or Stable solely because it appears in this roadmap. Completion and lifecycle claims require the applicable authoritative implementation, validation, review, release, and production evidence.

## Reconciliation rule

At each material feature change, reconcile this roadmap against the current authoritative project record, repository implementation state, applicable Platform-System requirements, and GoreeCloud Tasks Management. Missing obligations, stale status, duplicated work, roadmap drift, or undocumented disposition changes are defects to correct.
