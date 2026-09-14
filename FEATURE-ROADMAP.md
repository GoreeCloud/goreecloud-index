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

The latest verified implementation checkpoint on PR #35 is `774e727d5ec4e1c74c9ec85e3d023c9b508bd18c`. Platform Contract #61 and Android Index foundation validation #188 both passed on that exact implementation revision. Android #188 passed exact-revision recording, repository/provider/branding guards, the full unit-test suite, lint, Development APK assembly, APK identity verification, and Development APK evidence upload.

The preceding privacy-safe authority-explanation checkpoint `703dcf554981b4c00c10365b07de21ee937904d0` passed Platform Contract #56 and Android #183. The preceding source-control implementation checkpoint `b201e182d0698bee55f19e37e46f9d42f419c638` passed Platform Contract #47 and Android #174.

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
- deterministic incremental result delivery through one `Flow<IndexSearchSnapshot>` composition path, with cancellation propagation and one-shot/final-Flow equivalence;
- session-scoped source controls for the currently integrated local Applications, Settings, and Contacts providers, with a centralized `IndexDevelopmentSourcePolicy`, enforced `localOnly=true`, rejection of unknown/remote providers, and no durable preference claim;
- explicit UI disclosure that source selection does not grant permission or platform authority and cannot silently enable GoreeCloud Search or another remote fallback;
- privacy-safe Contacts authority explanation exposing only coarse missing prerequisite domains—Android runtime permission, Privacy Shield, and GoreeCloud Identity—without raw references, IDs, subjects, reason codes, timestamps, or authority payloads;
- an explicit user-initiated Android Contacts permission review action that is available only when Android `READ_CONTACTS` is actually missing, uses Android's own runtime permission contract, refreshes authority-derived state afterward, and explicitly states that Privacy Shield and GoreeCloud Identity remain independent requirements;
- existing invalid-result evidence precedence, provider timeout isolation, local-only gating, provider-scoped deduplication, and fail-closed protected-provider dispatch preserved.

Remote Search execution remains blocked on accepted runtime Privacy Shield decision acquisition plus approved GoreeCloud Identity requester/service authentication. The current candidate does not enable live remote Search traffic or claim production Search acceptance. Contacts also remains non-dispatchable in the shipped Development application unless Android permission **and** accepted Privacy Shield **and** accepted GoreeCloud Identity evidence are all independently satisfied. Source selections are deliberately not persisted yet; durable provider preferences require an approved profile/device persistence and applicable Everkeep continuity/recovery design before they can become authoritative durable state.

## Roadmap

| ID | Feature / obligation | Priority | Current state |
| --- | --- | --- | --- |
| FR-001 | Reconcile and maintain every current planned or recommended GoreeCloud Index feature from the authoritative project record and verified repository evidence in this roadmap. | High | Ongoing control; synchronized to the verified September 14 PR #35 Android permission-review checkpoint. |
| FR-002 | Move actionable feature obligations into GoreeCloud Tasks Management when required, preserving priority, dependency, and lifecycle disposition. | High | Ongoing control; GOR-26 tracks the current modernization work and exact verified checkpoints. |
| FR-003 | Do not mark features implemented, complete, cancelled, or superseded without authoritative evidence and synchronized repository/Drive roadmap updates. | High | Ongoing control; PR/Linear checkpoints distinguish verified Development source from Stable or production acceptance. |
| FR-004 | Complete current Glaze UI V1.4 / `1.4.0` native application adoption plus fresh rendered/native, interaction, accessibility, adaptive/form-factor, representative-device/OEM, performance, Human Visual Excellence, rollback, release, and production acceptance. | High | V1.4 source targeting and semantic foundations exist. Full Index-local rendered/native consumer conformance, accessibility/device evidence, and production acceptance remain open. |
| FR-005 | Keep query normalization, matching, cross-provider composition, and result ordering deterministic without retaining private query data or broadening provider authority. | High | Active PR #35 provides Index-owned textual normalization, same-provider score/source ordering, health-aware equal-relevance composition, `LOCAL → MIXED → REMOTE` full-tie ordering, deterministic identity fallback, bounded provider fan-out, deterministic incremental recomposition, and cancellation-aware source/authority refreshes. Verified permission-review implementation checkpoint `774e727…` passed Platform Contract #61 and Android #188. |
| FR-006 | Expand first-party providers only through explicit provider contracts and accepted source-authority boundaries, including Files, Calendar, media, additional GoreeCloud application content, and connected-device resources where approved. | High | Applications and bounded Settings are implemented. Contacts source, privacy-safe prerequisite explanation, and Android-owned runtime permission review are implemented in Development, but Contacts remains fail-closed pending accepted Privacy Shield and GoreeCloud Identity runtime evidence. Files/Calendar/media remain planned. |
| FR-007 | Integrate GoreeCloud Search as the clearly identified Internet/Web provider without silently sending unrelated local query context or provider data remotely. | High | PR #35 contains the hardened consumer contract: explicit Development/Production acceptance, bounded POST/JSON requirements, capability discovery checks, request correlation, canonical Privacy Shield reference handling, safe result actions, and privacy-safe diagnostics. The source-control policy explicitly excludes Search from the user-selectable Development provider set. Runtime transport remains disabled pending accepted Identity-backed requester/service authentication and real Privacy Shield decision acquisition. |
| FR-008 | Integrate and accept Privacy Shield, Wardveil Security, GoreeCloud Identity, GoreeCloud Mesh, Everkeep, and GoreeCloud Manager runtime authorities while preserving provider isolation and fail-closed unknown/stale evidence behavior. | High | Blocked pending accepted live Platform-System runtime evidence. Coarse UI prerequisite explanation does not constitute runtime acceptance. Platform Contract declarations remain nonconformant/migration-required where evidence is incomplete. |
| FR-009 | Add provider enable/disable, permission review, local-only mode, Internet-provider preference, third-party connection/revocation, history controls if history is implemented, and index/cache clearing with understandable local-versus-remote processing disclosure. | Medium | **Partial Development implementation verified.** Session-scoped enable/disable controls for Applications/Settings/Contacts, enforced local-only mode, remote-processing disclosure, privacy-safe Contacts prerequisite explanation, and explicit Android-owned `READ_CONTACTS` permission review are implemented and exact-head validated. Android permission review does not satisfy Privacy Shield or Identity. Durable provider preferences, representative-device permission-flow acceptance, accepted Privacy Shield/Identity user-decision flows, Internet-provider preference, third-party connection/revocation, history controls if applicable, cache/index clearing, Everkeep continuity for durable settings, and remote-provider user enablement remain open. |
| FR-010 | Add local indexing only where it materially improves latency or result quality, keeping indexes reconstructible, profile/device scoped, removable, rebuildable, and non-authoritative. | Medium | Planned; no production local-content index accepted. |
| FR-011 | Validate provider cancellation/timeouts, partial-result behavior, degraded-provider reporting, resource use, incremental delivery, accessibility, localization/RTL, text scaling/reflow, reduced-motion/transparency, contrast, and representative performance before release advancement. | High | Source-level cancellation/timeouts, partial-result isolation, degraded-state propagation, deterministic composition, local-first full-tie behavior, provider-result validation, bounded fan-out, incremental Flow delivery, source-selection cancellation, privacy-safe authority refresh, and Android permission-result refresh are verified. Remaining work includes representative-device permission grant/deny/re-prompt behavior, perceived latency, potential bounded update coalescing if measured churn warrants it, rendered accessibility, localization/RTL, text scaling, reduced-effects, contrast, and device qualification. |
| FR-012 | Complete controlled packaging/signing/provenance, upgrade/rollback, Release Candidate qualification, production approval, and Stable qualification after all applicable provider/platform/accessibility/performance gates pass. | High | Development APK identity/evidence exists for PR validation only. Production signing/distribution, rollback, RC, production approval, and Stable gates remain open. |

## Current sequencing recommendation

1. Preserve the verified fail-closed provider, Search-capability, authority, ranking, cancellation, fan-out, incremental-composition, session source-control, privacy-safe authority-explanation, and Android permission-review boundaries while continuing from PR #35.
2. Validate the Android Contacts permission review on representative Android devices, including grant, deny, return/resume refresh, and any Android-authoritative re-prompt/settings behavior; do not invent a custom permission state machine.
3. Establish accepted producer-owned Privacy Shield and GoreeCloud Identity runtime authorization paths for Contacts before claiming Contacts runtime enablement; never treat Android permission as satisfying those authorities.
4. Define any future durable provider-preference storage only after profile/device scoping and applicable Everkeep continuity/recovery requirements are explicit; keep current source selections session-scoped until then.
5. Establish an approved GoreeCloud Identity requester/service-authentication path and real Privacy Shield decision-acquisition adapter before enabling live GoreeCloud Search transport or an Internet-provider preference.
6. Measure incremental rendering/provider completion, source-toggle, and authority-refresh behavior on representative devices; add bounded update coalescing only if evidence shows excessive UI churn, while preserving one composition path.
7. Complete Glaze UI V1.4 rendered/native accessibility, localization/RTL, text-scaling, reduced-effects, representative-device, and performance acceptance; then add Files, Calendar, media, and other first-party providers one bounded authority contract at a time and complete remaining Platform-System/release gates.

## Maintenance and synchronization

This roadmap and the corresponding Drive `FEATURE-ROADMAP.docx` must remain materially synchronized with one another and with the authoritative project or service record. Update both copies whenever feature scope, priority, dependency, implementation status, cancellation, supersession, recommendation, or verification state materially changes.

No feature may be represented as complete or Stable solely because it appears in this roadmap. Completion and lifecycle claims require the applicable authoritative implementation, validation, review, release, and production evidence.

## Reconciliation rule

At each material feature change, reconcile this roadmap against the current authoritative project record, repository implementation state, applicable Platform-System requirements, and GoreeCloud Tasks Management. Missing obligations, stale status, duplicated work, roadmap drift, or undocumented disposition changes are defects to correct.