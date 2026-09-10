# GoreeCloud Index — Feature Roadmap

**Lifecycle:** Development  
**Canonical Drive control:** `GoreeCloud/Feature Roadmap/GoreeCloud Index/FEATURE-ROADMAP.docx`  
**Authoritative project record:** `Project Specification — Index`  
**Canonical repository:** `GoreeCloud/goreecloud-index`

## Control rules

This repository roadmap and the canonical Drive roadmap must remain materially synchronized with the authoritative Index project specification, verified repository implementation state, applicable GoreeCloud platform-system requirements, and GoreeCloud Tasks Management.

A roadmap entry does not establish implementation, acceptance, Release Candidate, production, or Stable status. Status changes require applicable source, exact-revision validation, representative runtime/device evidence, review, release, and production evidence.

| ID | Feature / obligation | Priority | Current state |
| --- | --- | --- | --- |
| FR-001 | Reconcile and maintain every current planned or recommended GoreeCloud Index feature from the authoritative project record and verified repository evidence in this roadmap. | High | Ongoing control |
| FR-002 | Move actionable feature obligations into GoreeCloud Tasks Management when required, preserving priority, dependency, and lifecycle disposition. | High | Ongoing control |
| FR-003 | Do not mark features implemented, complete, cancelled, or superseded without authoritative evidence and synchronized repository/Drive roadmap updates. | High | Ongoing control |
| FR-004 | Maintain the current GLAZE UI V1.3 / 1.3.0 native source mapping and complete fresh Index-specific rendered/native, interaction, accessibility, adaptive/form-factor, representative-device/OEM, performance, Human Visual Excellence, rollback, release, and production acceptance. | High | Development — Draft PR #20 exact head `e7e4a28a26e84d5d7848426150ea29d1df90a04a` passed Android Index foundation `34298089902` and Platform Contract `34298090646`; whole-application acceptance remains incomplete. |
| FR-005 | Improve local ranking and matching determinism without broadening provider authority or retaining query data. | High | Development — PR #21 exact head `82d57c97e0e9997740689808c0cd6734f67a8100` passed Android Index foundation `34417745730`; PR #22 exact head `430abb02da20e1fa09a277a18525e9228336b277` passed `34525053727` and adds a stable result-ID final tie-breaker. |
| FR-006 | Expand first-party providers only through explicit provider contracts and accepted source-authority boundaries, including Files, Calendar, media, additional GoreeCloud application content, and connected-device resources where approved. | High | Planned — Applications and bounded Settings are implemented; Contacts source exists but remains fail-closed pending accepted authority evidence. |
| FR-007 | Integrate GoreeCloud Search as the clearly identified Internet/Web provider without silently sending unrelated local query context or provider data remotely. | High | Planned / not implemented or accepted in the current Index Development stack. |
| FR-008 | Integrate and accept Privacy Shield, Wardveil Security, GoreeCloud Identity, GoreeCloud Mesh, Everkeep, and GoreeCloud Manager runtime authorities while preserving provider isolation and fail-closed unknown/stale evidence behavior. | High | Blocked — current Platform Contract remains nonconformant and the shipped Development gateway does not provide accepted live platform evidence. |
| FR-009 | Add provider enable/disable, permission review, local-only mode, Internet-provider preference, third-party connection/revocation, history controls if history is implemented, and index/cache clearing with understandable local-versus-remote processing disclosure. | Medium | Planned; do not infer implementation from provider source alone. |
| FR-010 | Add local indexing only where it materially improves latency or result quality, keeping indexes reconstructible, profile/device scoped, removable, rebuildable, and non-authoritative. | Medium | Planned / no production local-content index accepted. |
| FR-011 | Validate provider cancellation/timeouts, partial-result behavior, resource use, incremental delivery, accessibility, localization/RTL, text scaling/reflow, reduced-motion/transparency, contrast, and representative performance before release advancement. | High | Partially implemented at source/runtime level; representative acceptance and exact performance thresholds remain open. |
| FR-012 | Complete controlled packaging/signing/provenance, upgrade/rollback, Release Candidate qualification, production approval, and Stable qualification after all applicable provider/platform/accessibility/performance gates pass. | High | Planned / release gates open. |

## Current sequencing recommendation

1. Keep the V1.3 source contract and deterministic local matching line exact-revision validated while removing roadmap/documentation drift.
2. Complete Index-specific rendered/accessibility/representative-device and performance acceptance for the currently implemented Applications, Contacts-gated, and Settings surfaces.
3. Establish accepted live platform-authority adapters before enabling Contacts or any similarly sensitive provider.
4. Add GoreeCloud Search and additional first-party providers one bounded authority contract at a time; keep remote and optional third-party providers explicitly user-controlled.
5. Complete Everkeep continuity decisions, signing/provenance, rollback, Release Candidate, production, and Stable gates.

This sequencing preserves Index as an authorization-aware universal search coordinator rather than a second authoritative data store or a silent remote-query aggregator.
