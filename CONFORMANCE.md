# GoreeCloud Index — Conformance

## Lifecycle State

**Release lifecycle: Development.** Active development continues on the native Android application-search foundation. Production acceptance and Stable qualification remain false.

A checked source item means the capability exists in repository source on this branch. It does not automatically establish exact-candidate acceptance, representative-device behavior, platform-runtime acceptance, production deployment, or Stable qualification.

The accepted main baseline before this branch is `19737c11c59a30a94ee8b6dad8855b449c011eca`. The `0.2.0-dev` asynchronous runtime requires its own exact-candidate CI and merge acceptance.

## Native Implementation

- [x] Original GoreeCloud-owned Android application source.
- [x] Kotlin and Jetpack Compose application foundation.
- [x] Production package `com.goreecloud.index` and development package `com.goreecloud.index.dev`.
- [x] API 26 minimum, compile API 37, target API 36 baseline.
- [x] Development version `0.2.0-dev`, version code `2` on this branch.
- [x] User-visible Launcher→Index search-entry contract.
- [ ] Representative Android device acceptance for this branch.
- [ ] Controlled production signing and release packaging.

## Search Architecture

- [x] Provider-neutral query/result/type/action model.
- [x] Provider-scoped result identity.
- [x] Human-readable provider identity for degraded-state reporting.
- [x] Provider processing-location declaration.
- [x] Suspendable provider query contract.
- [x] Explicit provider timeout declaration.
- [x] Fail-closed `IndexExecutionContext` with exact provider allowlisting.
- [x] Local-only execution gating for remote/mixed providers.
- [x] Structured concurrent provider execution under `supervisorScope`.
- [x] Provider-dispatcher injection for deterministic tests.
- [x] Superseded-query cancellation through the Compose query lifecycle.
- [x] External `CancellationException` propagation.
- [x] Per-provider timeout enforcement through `withTimeout`.
- [x] Global five-second timeout safety ceiling.
- [x] Explicit `FAILED` and `TIMED_OUT` provider issue kinds.
- [x] Provider issues returned independently of healthy results.
- [x] Deterministic result ranking.
- [x] Ranking before provider-scoped deduplication.
- [x] Result-count bounds.
- [ ] Incremental/streaming result delivery.
- [ ] Provider health/capability negotiation.
- [ ] Approved Privacy Shield / Identity authorization context integration.
- [ ] Local index storage/rebuild lifecycle.

`IndexExecutionContext` is a source-level execution eligibility guard only. It is not a formal Privacy Shield consent decision, GoreeCloud Identity authorization token, or production security boundary.

## Current Provider Coverage

- [x] Android launcher-visible applications provider in source.
- [x] Scoped `ACTION_MAIN` / `CATEGORY_LAUNCHER` visibility.
- [x] No unrestricted `QUERY_ALL_PACKAGES` permission.
- [x] No `INTERNET` permission in the current local-only slice.
- [x] Processing location declared `LOCAL`.
- [x] Provisional 500 ms development timeout declared.
- [x] Application-label and package-name search.
- [x] Exact Android launcher-component identity and typed launch action.
- [x] API 33+ `ResolveInfoFlags` path with older-API compatibility.
- [ ] Representative-device acceptance of the 500 ms timeout value.
- [ ] File/folder provider.
- [ ] Contacts provider.
- [ ] Calendar provider.
- [ ] GoreeCloud Search Internet provider.
- [ ] Mesh-discovered first-party provider runtime.
- [ ] Extension provider runtime.
- [ ] Third-party provider runtime.

## Launcher Integration

- [x] Manifest search action: `com.goreecloud.index.action.SEARCH`.
- [x] Shared query extra: `com.goreecloud.index.extra.QUERY`.
- [x] Initial query can be displayed when Index is launched through the contract.
- [ ] Representative Launcher→Index device integration acceptance for the `0.2.0-dev` runtime.

## Glaze UI 2.1.0

- [x] Source targets Glaze UI 2.1.0 as the current Stable design-language baseline.
- [x] Search is the primary interaction surface.
- [x] Current provider and processing location are visible.
- [x] Blank/browse, searching, no-match, provider-failed, provider-timed-out, and result states are distinct.
- [x] Provider failure/timeout is not represented as successful empty search.
- [x] Safe-drawing insets are applied under edge-to-edge rendering.
- [x] 56 px minimum search-field height and 72 px minimum result-row height.
- [x] 48 px result identity surface.
- [x] Semantic headings on major search sections.
- [x] Searching state does not require an animated indicator.
- [ ] Formal Glaze UI 2.1.0 application-specific consumer conformance validation.
- [ ] Reduced-transparency acceptance.
- [ ] Increased-contrast/forced-colors acceptance where applicable.
- [ ] Reduced-motion acceptance.
- [ ] Large-text/reflow acceptance.
- [ ] Representative phone/tablet visual acceptance.

No formal Glaze UI consumer-conformance claim is made yet.

## Privacy Shield

- [x] Current implemented provider is local-only.
- [x] Current manifest requests no Internet permission.
- [x] Current MainActivity uses `localOnly=true` execution eligibility.
- [x] No intentional query analytics or remote telemetry added by the current slice.
- [x] No intentional persistent search-history store added by the current slice.
- [ ] Approved Privacy Shield runtime contract integration.
- [ ] Provider enable/disable and permission review center.
- [ ] Authorization evidence for future private-content providers.
- [ ] Retention/history rules when durable query state exists.

## Wardveil Security

- [x] No positive trust/protection claim inferred from provider success.
- [x] Provider exceptions/timeouts become degraded state rather than clean/trusted state.
- [x] Current result action is typed to an exact Android launcher component.
- [ ] Approved Wardveil runtime provider-trust/security evidence integration.
- [ ] Security review for extension and third-party providers.
- [ ] Security-aware validation for future URI/deep-link or remote actions.

## Everkeep

- [x] Current query state is transient and not represented as irreplaceable user data.
- [ ] Approved Everkeep integration for durable provider preferences/configuration when introduced.
- [ ] Recovery/portability validation for material durable Index state.

## GoreeCloud Identity and Mesh

- [ ] GoreeCloud Identity runtime integration.
- [ ] User/profile isolation acceptance.
- [ ] Caller/provider scoped authorization.
- [ ] GoreeCloud Mesh first-party provider discovery/coordination.

## Automated Validation

Accepted-main commit `19737c11c59a30a94ee8b6dad8855b449c011eca` passed exact-main GitHub Actions run `33420873144`.

Accepted-main validated steps:

- [x] Exact source revision capture.
- [x] Repository-contract validation.
- [x] Android unit tests.
- [x] Android lint.
- [x] Development APK assembly.
- [x] Development package/version/label verification.
- [x] APK SHA-256 capture.
- [x] Artifact publication.

Accepted-main APK SHA-256: `a867073c433941297da985a1c8ec3e3972e7ecd6db4883f18e935a8d6fd72f83`.

Accepted-main artifact: `9768893227`, `goreecloud-index-development-apk-19737c11c59a30a94ee8b6dad8855b449c011eca`.

## Branch Validation Requirements

Before this asynchronous runtime may be merged, exact-candidate CI must pass:

- [ ] Repository validation requiring coroutine runtime/test dependencies and the new async/cancellation/timeout/context contracts.
- [ ] Unit tests including virtual-time concurrency, timeout, cancellation, execution-gating, ranking/deduplication, and result-bound behavior.
- [ ] Android lint.
- [ ] `0.2.0-dev` Development APK assembly.
- [ ] Package `com.goreecloud.index.dev`, version code `2`, version name `0.2.0-dev`, and label verification.
- [ ] Exact candidate SHA capture.
- [ ] APK SHA-256 capture.
- [ ] Artifact publication.

Passing these checks establishes source/build evidence only. It does not establish representative-device performance, platform-runtime integration acceptance, production deployment, or Stable qualification.
