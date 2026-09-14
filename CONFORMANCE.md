# GoreeCloud Index — Conformance

## Lifecycle State

**Release lifecycle: Development.** Production acceptance and Stable qualification remain false. Accepted `main` is `cc3cc21d6e11dad026253c3371c3b67663d3b726`; the `0.3.0-dev` provider/authority work remains branch source until exact-head CI and merge acceptance complete.

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
- [x] Cross-provider ranking uses Index-owned normalized textual relevance rather than comparing raw provider score magnitudes across sources.
- [x] Provider-local score and source order remain same-provider ordering inputs only.
- [x] Equal cross-provider normalized relevance falls back to deterministic stable identity rather than raw provider score.
- [x] Explicit `DEGRADED` source state is a bounded cross-provider tie-breaker only after normalized relevance ties; stronger degraded matches still outrank weaker healthy matches.
- [x] `INVALID_RESULT` issue precedence remains distinct from ordinary degradation and is not silently converted into a ranking-health signal.
- [x] Result-count bounds.
- [x] Branch source: provider authority requirements and evidence.
- [x] Branch source: `AUTHORIZATION_REQUIRED` state.
- [x] Branch source: non-browsing providers excluded on blank query.
- [ ] Intent-aware/result-type/source-confidence/local-vs-remote/privacy-cost blending beyond the current textual + degraded-state baseline.
- [ ] Incremental/streaming results.
- [ ] Richer provider health/capability negotiation.

## Authority Model

- [x] Android runtime permission is distinct from Privacy Shield and Identity authority.
- [x] Privacy Shield/Identity evidence requires referenced unconstrained `ALLOW` for the provider paths that use that evidence model.
- [x] `DENY`, `REQUIRE_USER_DECISION`, and `UNAVAILABLE` fail closed.
- [x] Missing authority prevents provider dispatch.
- [x] Internal execution context is not described as platform authorization.
- [x] Branch source: remote GoreeCloud Search uses a separate operation-scoped Privacy Shield authorization adapter and canonical `psc_*` capability-reference boundary.
- [ ] Accepted Privacy Shield runtime adapter/decision-acquisition transport.
- [ ] Accepted GoreeCloud Identity runtime adapter and authenticated service/requester identity path.
- [ ] User decision/permission workflow bound to accepted platform decisions where applicable.
- [ ] Runtime decision expiry/revocation/obligation evaluation beyond currently modeled provider contracts.

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
- [x] Current runtime keeps unavailable authority fail-closed, so protected Contacts dispatch is not fabricated.
- [ ] Accepted Contacts runtime enablement.
- [ ] Explicit user opt-in and Android permission grant flow.
- [ ] Representative-device cancellation/timeout/action acceptance.

## Glaze UI V1.4 / 1.4.0 — Optical Intelligence

- [x] Branch source contract targets the current Official Stable Glaze UI V1.4 / `1.4.0` baseline.
- [x] Historical `1.1.0` and `2.1.0` values are treated as superseded implementation history, not current release authority.
- [x] Search-first interaction and visible source/authority state remain explicit.
- [x] Authorization-required state remains distinct from operational failure/timeout.
- [x] Safe-drawing insets, bounded targets, semantic headings, and non-animated progress remain represented in the Development source line.
- [ ] Repository-local rendered/native V1.4 visual acceptance.
- [ ] Reduced Transparency / Increased Contrast / Reduced Motion / large-text acceptance.
- [ ] Localization/RTL acceptance.
- [ ] Representative phone/tablet/form-factor and performance acceptance.
- [ ] Formal application-specific V1.4 conformance and production acceptance.

## Privacy Shield

- [x] No silent remote fallback.
- [x] Local providers declare local processing where applicable.
- [x] No intentional persistent search history or query analytics.
- [x] Branch source consumes decision outcome/reference separately from Android permission.
- [x] Branch source uses a bounded operation-scoped capability-reference contract for production Search delegation preparation.
- [ ] Real Privacy Shield request/response decision-acquisition adapter and accepted runtime evidence.
- [ ] Provider controls and retained-decision lifecycle where applicable.

## GoreeCloud Identity

- [x] Authentication is not treated as blanket authorization.
- [x] Branch source requires independent Identity authorization evidence for protected local providers where declared.
- [ ] Actual Identity authorization adapter/API acceptance.
- [ ] Authenticated requester/service identity for protected remote-provider transport.
- [ ] User/profile/caller isolation acceptance.

## Wardveil Security, Everkeep, Mesh, and Sync

- [x] No Wardveil trust/protection claim inferred from provider success.
- [x] Current query state remains transient.
- [x] Platform Contract `0.3` branch manifest declares all eight Integral Platform Systems, including GoreeCloud Sync, without converting blocked systems into conformance.
- [x] Transient query text and search history are not designated as Sync datasets.
- [ ] Wardveil provider/action security evidence integration.
- [ ] Everkeep continuity for applicable durable settings/configuration.
- [ ] Mesh provider discovery/coordination integration.
- [ ] GoreeCloud Sync dataset contracts and runtime registration/reconciliation for any future approved durable Index state.

## Platform Contract 0.3

- [x] Branch manifest declares Manager, Privacy Shield, Wardveil Security, Everkeep, Glaze UI, GoreeCloud Mesh, GoreeCloud Identity, and GoreeCloud Sync.
- [x] Compatibility requires `goreecloud-platform-contract==0.3` and `glaze-ui==1.4.0`.
- [x] Development validation is pinned to exact GoreeCloud/GoreeCloud PR #30 head `5e10d17f10a4c12cc0a98b595646e4fc5992c1f2` while that central PR remains draft/unmerged.
- [ ] Replace the temporary draft-candidate pin with an accepted central Platform Contract revision after normal governance completes.
- [ ] Achieve passing application-specific results for every applicable Integral Platform System before Stable qualification.

## Accepted Main Automated Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Exact-main workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

This section records historical accepted-main Development evidence. Branch checks and newer source work are not marked accepted-main or Stable until normal merge/release governance and runtime acceptance complete.
