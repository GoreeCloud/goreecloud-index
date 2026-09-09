# GoreeCloud Index — Conformance

## Lifecycle State

**Release lifecycle: Development.** Production acceptance and Stable qualification remain false.

Current repository `main` is `9f3aaa9543a8a8351fa2d4713481bccb8199069d`. The older accepted APK/build evidence remains bound to exact source `cc3cc21d6e11dad026253c3371c3b67663d3b726`, workflow `33431294298`, APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, and artifact `9772740479`. Later source does not inherit that artifact acceptance automatically.

This V1.3 reconciliation branch is newer Development source and requires its own exact-head validation and review.

## Native Implementation

- [x] Original GoreeCloud-owned Android Kotlin/Compose source.
- [x] Production package `com.goreecloud.index`; Development package `com.goreecloud.index.dev`.
- [x] API 26 minimum, compile API 37, target API 36.
- [x] Current source version `0.3.0-dev` / code 3.
- [x] Current `main` contains the Applications, Contacts authority, and bounded Settings-navigation source slices.
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
- [x] Provider authority requirements and evidence model.
- [x] `AUTHORIZATION_REQUIRED` state.
- [x] Non-browsing providers excluded on blank query.
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

## Contacts Provider — Current Source, Authority-Gated

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
- [x] Current unavailable platform-authority gateway keeps Contacts non-dispatchable.
- [ ] Accepted Contacts runtime enablement.
- [ ] Explicit user opt-in and Android permission grant flow.
- [ ] Representative-device cancellation/timeout/action acceptance.

## Settings Navigation — Current Source

- [x] Bounded static Android Settings destination catalog exists in current source.
- [x] Local-only search of repository-defined navigation metadata.
- [x] No setting values, accounts, permission state, history, or device configuration values are indexed by this slice.
- [x] No new Internet permission, telemetry, cache, or persistent query history.
- [x] Typed Settings handoff is constrained to the reviewed static action allowlist.
- [ ] Representative-device/OEM action availability and navigation acceptance.
- [ ] Accessibility and performance acceptance for the Settings path.

## GLAZE UI V1.3 / 1.3.0 — Current Candidate

- [x] Live Glaze lifecycle authority identifies `1.3.0` as current Official Stable and consumer-eligible.
- [x] Repository-local native contract records exact V1.3 source integration anchor `fc7cc91d2eace8da2371371c2855c24cbcb326a1`.
- [x] Repository-local contract records Stable web/runtime identifiers and `1.2.0` rollback baseline without claiming that a native consumer imports web assets.
- [x] Compose theme uses explicit deterministic Light, Dark, and Deep Dark schemes rather than generic Android dynamic-color sampling.
- [x] 16/24/32 dp optical geometry and 48/56 dp target floors are encoded in the candidate source.
- [x] Machine-readable Platform declaration uses source/required `1.3.0` and remains `applicable-migration-required` / overall `nonconformant`.
- [ ] Exact-head Android and Platform Contract CI for this branch.
- [ ] Formal rendered/native application-specific Glaze conformance.
- [ ] TalkBack/accessibility, 200% text/reflow, RTL/localization, Reduced Motion, Reduced Transparency, contrast/high-contrast, representative phone/tablet, and performance acceptance.
- [ ] User-facing appearance preference acceptance for Deep Dark if exposed as product behavior.

## Privacy Shield

- [x] No silent remote fallback.
- [x] Current providers declare local processing.
- [x] No intentional persistent search history or query analytics.
- [x] Source consumes decision outcome/reference separately from Android permission.
- [ ] Real Privacy Shield request/response adapter and runtime evidence.
- [ ] Provider controls and retained-decision lifecycle.

## GoreeCloud Identity

- [x] Authentication is not treated as blanket authorization.
- [x] Source requires independent Identity authorization evidence for Contacts.
- [ ] Actual Identity authorization adapter/API acceptance.
- [ ] User/profile/caller isolation acceptance.

## Wardveil Security, Everkeep, Mesh, and Manager

- [x] No Wardveil trust/protection claim inferred from provider success.
- [x] Current query state remains transient.
- [ ] Wardveil provider/action security evidence integration.
- [ ] Everkeep continuity decision for applicable durable settings/configuration.
- [ ] Mesh provider discovery/coordination integration.
- [ ] Manager health/readiness and operational visibility integration.

## Accepted Historical Automated Evidence

The last accepted APK/build evidence remains:

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Exact-main workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

Current `main` and this V1.3 branch are newer source states. Their validation must be recorded independently and must not be represented by the historical APK evidence above.
