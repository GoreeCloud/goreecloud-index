# GoreeCloud Index — Feature Roadmap

**Status:** Active roadmap control  
**As of:** 2026-09-13  
**Authoritative project record:** Project Specification — Index  
**Canonical repository:** GoreeCloud/goreecloud-index  
**Drive control:** `GoreeCloud/Feature Roadmap/GoreeCloud Index/FEATURE-ROADMAP.docx`

## Purpose

This file is the repository-side feature roadmap control for GoreeCloud Index. It records current planned and recommended feature work without replacing the authoritative project record, implementation evidence, release gates, or GoreeCloud Tasks Management.

## Current Development checkpoint

Current `main` includes the GoreeCloud Search provider foundation and provider-result provenance boundary from merged PR #26 (`714b4696eea6471bca8e5ce545d66b3a19042441`), the ranking-key optimization from PR #27 (`29aba00e0d2aca3891b633439fe13646399dc22f`), Search API-version binding from PR #29 (`13b93551bd807c4fe288e6efe74f0d11d49d6cfe`), explicit provider contract-version compatibility from PR #30 (`edeeb68ed59ad66ad300d7e2da52bee50569e918`), degraded-provider status propagation from PR #31 (`19288fd5e77c337a31d1627edb9e8bd170d81446`), fail-closed Search capability preflight from PR #32 (`bb5c45a0c02267ff7e7cc47ce59e6392dab4eac6`), and the current GLAZE UI V1.4 native design foundation from PR #33 (`cafb6812891282e7e7fa83193ce268f005c535e8`).

The Search boundary now validates API version, provider contract version, capability identity, capability freshness/authority, canonical endpoint, published result bounds, delegated query/category integrity, and safe result URLs before remote-provider results can be accepted. Partially degraded Search responses can preserve valid results while surfacing a distinct degraded state. The shipped runtime still does not register the Search provider, does not add Android Internet permission, and remains local-only.

PR #33 replaced generic device-owned dynamic-color ownership with a deterministic GoreeCloud semantic light/dark theme projection bound to current GLAZE UI V1.4 / `1.4.0` Stable authority. It preserves local-only optical authority, accessibility-first fallback behavior, and the V1.4 8% decorative memory-tint ceiling without introducing camera, wallpaper, telemetry, browsing-content, or remote-context collection. Exact-head Android validation passed before merge. These are Development source/build validation results only; full rendered/native Glaze conformance, manual accessibility and physical-device optical qualification, live remote Search transport, representative-device performance, platform-runtime acceptance, Release Candidate, production, and Stable gates remain open.

## Roadmap

| ID | Feature / obligation | Priority | Current state |
| --- | --- | --- | --- |
| FR-001 | Reconcile and maintain every current planned or recommended GoreeCloud Index feature from the authoritative project record and verified repository evidence in this roadmap. | High | Ongoing control |
| FR-002 | Move actionable feature obligations into GoreeCloud Tasks Management when required, preserving priority, dependency, and lifecycle disposition. | High | Ongoing control |
| FR-003 | Do not mark features implemented, complete, cancelled, or superseded without authoritative evidence and synchronized repository/Drive roadmap updates. | High | Ongoing control |
| FR-004 | Complete current GLAZE UI V1.4 / `1.4.0` native application adoption plus fresh rendered/native, interaction, accessibility, adaptive/form-factor, representative-device/OEM, performance, Human Visual Excellence, rollback, release, and production acceptance. | High | V1.4 native semantic foundation is merged on `main`; deterministic GoreeCloud light/dark schemes and V1.4 authority/accessibility bounds are source-validated. Full rendered/native consumer conformance and deferred manual/device optical qualification remain open. |
| FR-005 | Keep local query normalization, matching, and result ordering deterministic without retaining private query data or broadening provider authority. | High | Current `main` includes NFKC query normalization, whitespace canonicalization, a stable result-ID final tie-breaker, and one-time normalized-title ranking keys. Existing score weights and authority behavior remain unchanged; PR #27 exact-head validation passed before merge. |
| FR-006 | Expand first-party providers only through explicit provider contracts and accepted source-authority boundaries, including Files, Calendar, media, additional GoreeCloud application content, and connected-device resources where approved. | High | Applications and bounded Settings are implemented; Contacts remains fail-closed. Provider-result provenance validation rejects cross-attributed, blank-ID, or blank-title results while preserving valid siblings. Provider contract version `1` is now explicit; undeclared or incompatible in-scope providers fail closed before dispatch. |
| FR-007 | Integrate GoreeCloud Search as the clearly identified Internet/Web provider without silently sending unrelated local query context or provider data remotely. | High | Development source foundation is merged on `main`: Privacy Shield pre-dispatch gating; API-version and provider-contract compatibility; capability preflight for `search.query`; canonical `/api/v1/search` endpoint and published max-result validation; normalized-query/category/limit minimization; response/query/category validation; degraded-state propagation; URL validation; and safe web actions. The shipped runtime remains local-only with no live Search transport, runtime registration, or Android Internet permission. |
| FR-008 | Integrate and accept Privacy Shield, Wardveil Security, GoreeCloud Identity, GoreeCloud Mesh, Everkeep, and GoreeCloud Manager runtime authorities while preserving provider isolation and fail-closed unknown/stale evidence behavior. | High | Blocked pending accepted live platform-runtime evidence. |
| FR-009 | Add provider enable/disable, permission review, local-only mode, Internet-provider preference, third-party connection/revocation, history controls if history is implemented, and index/cache clearing with understandable local-versus-remote processing disclosure. | Medium | Planned; current shipped execution remains local-only. |
| FR-010 | Add local indexing only where it materially improves latency or result quality, keeping indexes reconstructible, profile/device scoped, removable, rebuildable, and non-authoritative. | Medium | Planned; no production local-content index accepted. |
| FR-011 | Validate provider cancellation/timeouts, partial-result behavior, degraded-provider reporting, resource use, incremental delivery, accessibility, localization/RTL, text scaling/reflow, reduced-motion/transparency, contrast, and representative performance before release advancement. | High | Source-level cancellation/timeouts, partial-result isolation, provider-result provenance validation, explicit degraded-provider propagation, contract/capability fail-closed checks, and bounded local ranking optimization are present. Representative acceptance and incremental-delivery qualification remain open. |
| FR-012 | Complete controlled packaging/signing/provenance, upgrade/rollback, Release Candidate qualification, production approval, and Stable qualification after all applicable provider/platform/accessibility/performance gates pass. | High | Planned / release gates open. |

## Current sequencing recommendation

1. Continue from the validated current `main` baseline; preserve API/provider-contract/capability fail-closed behavior and exact-revision tests as new providers or transport work are added.
2. Define and validate the live GoreeCloud Search transport/service-discovery boundary plus explicit user-facing Internet-provider controls before enabling remote Search execution; do not infer discovery, authentication, DNS, TLS, proxy, or production acceptance from the current transport-neutral contracts.
3. Complete GLAZE UI V1.4 repository-local adoption beyond the native semantic foundation, including rendered/native accessibility, form-factor, performance, rollback, and product acceptance; do not fabricate V1.4.1 deferred human/device optical evidence.
4. Add Files, Calendar, media, and other first-party providers one bounded authority contract at a time.
5. Complete platform-system runtime acceptance, signing/provenance, rollback, Release Candidate, production, and Stable gates without collapsing independent authorities.

## Maintenance and synchronization

This roadmap and the corresponding Drive `FEATURE-ROADMAP.docx` must remain materially synchronized with one another and with the authoritative project or service record. Update both copies whenever feature scope, priority, dependency, implementation status, cancellation, supersession, recommendation, or verification state materially changes.

No feature may be represented as complete or Stable solely because it appears in this roadmap. Completion and lifecycle claims require the applicable authoritative implementation, validation, review, release, and production evidence.

## Reconciliation rule

At each material feature change, reconcile this roadmap against the current authoritative project record, repository implementation state, applicable platform-system requirements, and GoreeCloud Tasks Management. Missing obligations, stale status, duplicated work, roadmap drift, or undocumented disposition changes are defects to correct.
