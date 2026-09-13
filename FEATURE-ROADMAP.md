# GoreeCloud Index — Feature Roadmap

**Status:** Active roadmap control  
**As of:** 2026-09-13  
**Authoritative project record:** Project Specification — Index  
**Canonical repository:** GoreeCloud/goreecloud-index  
**Drive control:** `GoreeCloud/Feature Roadmap/GoreeCloud Index/FEATURE-ROADMAP.docx`

## Purpose

This file is the repository-side feature roadmap control for GoreeCloud Index. It records current planned and recommended feature work without replacing the authoritative project record, implementation evidence, release gates, or GoreeCloud Tasks Management.

## Roadmap

| ID | Feature / obligation | Priority | Current state |
| --- | --- | --- | --- |
| FR-001 | Reconcile and maintain every current planned or recommended GoreeCloud Index feature from the authoritative project record and verified repository evidence in this roadmap. | High | Ongoing control |
| FR-002 | Move actionable feature obligations into GoreeCloud Tasks Management when required, preserving priority, dependency, and lifecycle disposition. | High | Ongoing control |
| FR-003 | Do not mark features implemented, complete, cancelled, or superseded without authoritative evidence and synchronized repository/Drive roadmap updates. | High | Ongoing control |
| FR-004 | Complete a current GLAZE UI V1.3 / 1.3.0 native application migration and fresh rendered/native, interaction, accessibility, adaptive/form-factor, representative-device/OEM, performance, Human Visual Excellence, rollback, release, and production acceptance. | High | Development migration remains open; historical stacked V1.3 candidate evidence is not current-main acceptance. |
| FR-005 | Keep local query normalization, matching, and result ordering deterministic without retaining private query data or broadening provider authority. | High | Current `main` includes NFKC query normalization and whitespace canonicalization; this candidate adds a stable result-ID final tie-breaker while preserving existing score weights. Fresh PR validation required. |
| FR-006 | Expand first-party providers only through explicit provider contracts and accepted source-authority boundaries, including Files, Calendar, media, additional GoreeCloud application content, and connected-device resources where approved. | High | Applications and bounded Settings are implemented; Contacts remains fail-closed. This candidate adds provider-result provenance validation before ranking/deduplication. |
| FR-007 | Integrate GoreeCloud Search as the clearly identified Internet/Web provider without silently sending unrelated local query context or provider data remotely. | High | Development source candidate on current `main`: transport-neutral Search provider/client contract, Privacy Shield pre-dispatch gating, URL validation, bounded result limits, and safe web actions. No live transport or runtime registration. |
| FR-008 | Integrate and accept Privacy Shield, Wardveil Security, GoreeCloud Identity, GoreeCloud Mesh, Everkeep, and GoreeCloud Manager runtime authorities while preserving provider isolation and fail-closed unknown/stale evidence behavior. | High | Blocked pending accepted live platform-runtime evidence. |
| FR-009 | Add provider enable/disable, permission review, local-only mode, Internet-provider preference, third-party connection/revocation, history controls if history is implemented, and index/cache clearing with understandable local-versus-remote processing disclosure. | Medium | Planned; current shipped execution remains local-only. |
| FR-010 | Add local indexing only where it materially improves latency or result quality, keeping indexes reconstructible, profile/device scoped, removable, rebuildable, and non-authoritative. | Medium | Planned; no production local-content index accepted. |
| FR-011 | Validate provider cancellation/timeouts, partial-result behavior, resource use, incremental delivery, accessibility, localization/RTL, text scaling/reflow, reduced-motion/transparency, contrast, and representative performance before release advancement. | High | Source-level cancellation/timeouts and partial-result isolation exist; representative acceptance and incremental-delivery qualification remain open. |
| FR-012 | Complete controlled packaging/signing/provenance, upgrade/rollback, Release Candidate qualification, production approval, and Stable qualification after all applicable provider/platform/accessibility/performance gates pass. | High | Planned / release gates open. |

## Current sequencing recommendation

1. Validate the current-main deterministic normalization plus provider-provenance/Search-provider candidate as one exact revision.
2. Keep Search remote execution disabled until a reviewed client transport, explicit user control, and accepted Privacy Shield/Wardveil runtime evidence exist.
3. Complete the current Glaze UI V1.3 application migration and representative accessibility/performance acceptance.
4. Add Files, Calendar, media, and other first-party providers one bounded authority contract at a time.
5. Complete platform-system runtime acceptance, signing/provenance, rollback, Release Candidate, production, and Stable gates without collapsing independent authorities.

## Maintenance and synchronization

This roadmap and the corresponding Drive `FEATURE-ROADMAP.docx` must remain materially synchronized with one another and with the authoritative project or service record. Update both copies whenever feature scope, priority, dependency, implementation status, cancellation, supersession, recommendation, or verification state materially changes.

No feature may be represented as complete or Stable solely because it appears in this roadmap. Completion and lifecycle claims require the applicable authoritative implementation, validation, review, release, and production evidence.

## Reconciliation rule

At each material feature change, reconcile this roadmap against the current authoritative project record, repository implementation state, applicable platform-system requirements, and GoreeCloud Tasks Management. Missing obligations, stale status, duplicated work, roadmap drift, or undocumented disposition changes are defects to correct.
