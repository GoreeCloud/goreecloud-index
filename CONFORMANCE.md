# GoreeCloud Index — Conformance

## Lifecycle State

**Release lifecycle: Development.** Production acceptance and Stable qualification remain false. Accepted `main` is `cc3cc21d6e11dad026253c3371c3b67663d3b726`; the `0.3.0-dev` Contacts/authority slice remains branch source until exact-head CI and merge acceptance complete.

## Native Implementation

- [x] Original GoreeCloud-owned Android Kotlin/Compose source.
- [x] Production package `com.goreecloud.index`; Development package `com.goreecloud.index.dev`.
- [x] API 26 minimum, compile API 37, target API 36.
- [x] Accepted-main `0.2.0-dev` / code 2.
- [x] Branch source `0.3.0-dev` / code 3.
- [ ] Representative Android device acceptance.
- [ ] Controlled production signing/release packaging.

## Search Runtime

- [x] Provider-neutral query/result/action model.
- [x] Suspendable provider contract with processing location and timeout.
- [x] Exact allowlisting and local-only gating.
- [x] Structured concurrent dispatch and cancellation propagation.
- [x] Bounded provider timeouts.
- [x] Sanitized `FAILED` and `TIMED_OUT` issues.
- [x] Healthy sibling-result preservation.
- [x] Ranking before provider-scoped deduplication.
- [x] Result-count bounds.
- [x] Branch source: provider authority requirements and evidence.
- [x] Branch source: `AUTHORIZATION_REQUIRED` state.
- [x] Branch source: non-browsing providers excluded on blank query.
- [ ] Incremental/streaming results.
- [ ] Provider health/capability negotiation.

## Authority Model

- [x] Android runtime permission is distinct from Privacy Shield and Identity authority.
- [x] Privacy Shield/Identity evidence requires referenced unconstrained `ALLOW`.
- [x] `DENY`, `REQUIRE_USER_DECISION`, and `UNAVAILABLE` fail closed.
- [x] `ALLOW_WITH_CONSTRAINTS` fails closed until obligations can be evaluated.
- [x] Missing authority prevents provider dispatch.
- [x] Internal execution context is not described as platform authorization.
- [ ] Accepted Privacy Shield runtime adapter.
- [ ] Accepted GoreeCloud Identity runtime adapter.
- [ ] User decision/permission workflow bound to accepted platform decisions.
- [ ] Runtime decision expiry/revocation/obligation evaluation.

## Applications Provider

- [x] Launcher-visible provider using `ACTION_MAIN` / `CATEGORY_LAUNCHER`.
- [x] No unrestricted `QUERY_ALL_PACKAGES`.
- [x] No Android Internet permission.
- [x] Local processing and 500 ms provisional timeout.
- [x] Exact launcher-component action.
- [ ] Representative-device performance acceptance.

## Contacts Provider — Branch Source

- [x] Android `READ_CONTACTS` declared.
- [x] Android ContactsProvider/`ContactsContract` is source authority.
- [x] `LOCAL` processing.
- [x] 750 ms provisional timeout.
- [x] No blank-query enumeration.
- [x] Filtered Contacts URI query path.
- [x] Projection limited to ID, lookup key, and display name.
- [x] No phone/email field reads in this provider slice.
- [x] Typed contact-view result action.
- [x] Contact action URI scheme/authority/path validation before handoff.
- [x] Android + Privacy Shield + Identity requirements declared.
- [x] Current runtime keeps Privacy Shield/Identity unavailable, so Contacts is not dispatched.
- [ ] Accepted Contacts runtime enablement.
- [ ] Explicit user opt-in and Android permission grant flow.
- [ ] Representative-device cancellation/timeout/action acceptance.

## Glaze UI 2.1.0

- [x] Source targets Glaze UI 2.1.0.
- [x] Search-first interaction and visible source state.
- [x] Authorization-required state distinct from operational failure/timeout.
- [x] Safe-drawing insets, bounded targets, semantic headings, non-animated progress.
- [ ] Formal application-specific conformance.
- [ ] Reduced transparency / increased contrast / reduced motion / large-text acceptance.
- [ ] Representative phone/tablet visual/accessibility acceptance.

## Privacy Shield

- [x] No silent remote fallback.
- [x] Current providers declare local processing.
- [x] No intentional persistent search history or query analytics.
- [x] Branch source consumes decision outcome/reference separately from Android permission.
- [ ] Real Privacy Shield request/response adapter and runtime evidence.
- [ ] Provider controls and retained-decision lifecycle.

## GoreeCloud Identity

- [x] Authentication is not treated as blanket authorization.
- [x] Branch source requires independent Identity authorization evidence for Contacts.
- [ ] Actual Identity authorization adapter/API acceptance.
- [ ] User/profile/caller isolation acceptance.

## Wardveil Security, Everkeep, and Mesh

- [x] No Wardveil trust/protection claim inferred from provider success.
- [x] Current query state remains transient.
- [ ] Wardveil provider/action security evidence integration.
- [ ] Everkeep continuity for applicable durable settings/configuration.
- [ ] Mesh provider discovery/coordination integration.

## Accepted Main Automated Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Exact-main workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

Branch checks are not marked accepted until their conclusions are observed.
