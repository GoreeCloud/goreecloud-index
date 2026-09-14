# GoreeCloud Index — Conformance

## Lifecycle State

**Release lifecycle: Development.** Production acceptance and Stable qualification remain false. Accepted `main` is `cc3cc21d6e11dad026253c3371c3b67663d3b726`; the `0.3.0-dev` provider/authority work remains branch source until normal merge acceptance completes.

The latest verified implementation checkpoint is `36056e4640e9c083fadbcabc0b0fa05d40615070`. Platform Contract #36 and Android Index foundation validation #163 passed on that exact revision.

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
- [x] Explicit `DEGRADED` source state is a bounded cross-provider tie-breaker only after normalized relevance ties; stronger degraded matches still outrank weaker healthy matches.
- [x] When normalized relevance and health are equal, processing location is a final bounded tie-breaker: `LOCAL`, then `MIXED`, then `REMOTE`.
- [x] Stronger remote relevance still outranks weaker local relevance; processing location cannot make an ineligible provider eligible or bypass authority gates.
- [x] Equal cross-provider relevance, health, and processing location fall back to deterministic stable identity rather than raw provider score.
- [x] Providers are independently constrained by the requested `IndexQuery.maxResults` result count before cross-provider federation.
- [x] Over-limit provider responses are surfaced as `INVALID_RESULT` rather than silently accepted.
- [x] Provider-local score/source order is applied before the engine bounds an over-limit response, so the strongest distinct provider-local results survive rather than arbitrary response-order entries.
- [x] Provider-scoped duplicate IDs are collapsed before the per-provider result bound is consumed.
- [x] `INVALID_RESULT` issue precedence remains distinct from ordinary degradation and covers provenance, required fields/source ordering, and result-bound violations.
- [x] Final result-count bounds remain at 100 or the smaller caller-requested count.
- [x] Branch source: provider authority requirements and evidence.
- [x] Branch source: `AUTHORIZATION_REQUIRED` state.
- [x] Branch source: non-browsing providers excluded on blank query.
- [x] Incremental Flow emits an initial snapshot containing static compatibility/authorization state before eligible provider completion.
- [x] Incremental Flow emits a newly recomposed snapshot after each eligible provider completes rather than waiting for all providers.
- [x] Incremental snapshots use the same normalized relevance, health, processing-location, deterministic-identity, validation, fan-out, and deduplication logic as the final result.
- [x] A late stronger result can deterministically re-rank ahead of earlier weaker results without giving completion order ranking authority.
- [x] One-shot `search()` consumes the last incremental snapshot instead of maintaining a second composition path.
- [x] Cancelling incremental collection cancels outstanding provider work and does not manufacture provider failures.
- [x] Repository guard now requires the incremental Flow/launch/composition/UI wiring and the dedicated incremental regression suite.
- [ ] Intent-aware/result-type/source-confidence/richer-health/privacy-cost blending beyond the current textual + degraded-state + local-first baseline.
- [ ] Representative-device incremental-rendering/perceived-latency acceptance.
- [ ] Bounded update coalescing if measured provider counts/completion cadence produce excessive UI churn.
- [ ] Richer provider health/capability negotiation.
- [ ] Bounded top-K provider-local selection that avoids full local sort if future measured result volumes justify the added complexity.

## Incremental Delivery Evidence

Exact implementation checkpoint `36056e4640e9c083fadbcabc0b0fa05d40615070` passed Android Index foundation validation #163 and Platform Contract #36.

Dedicated source regressions verify:

- initial snapshot emission before provider completion;
- an early local result becoming visible while a slower remote provider is still in flight;
- deterministic re-ranking when a later remote result has stronger Index-owned relevance;
- fail-closed `AUTHORIZATION_REQUIRED` emission without dispatching the protected provider;
- collector cancellation propagating to outstanding provider work;
- exact equality between one-shot `search()` output and the final `searchIncrementally()` snapshot.

These are source/build checks. They do not establish representative-device UI smoothness, performance targets, accessibility acceptance, production Search transport, or Stable qualification.

## Authority Model

- [x] Android runtime permission is distinct from Privacy Shield and Identity authority.
- [x] Privacy Shield/Identity evidence requires referenced unconstrained `ALLOW` for the provider paths that use that evidence model.
- [x] `DENY`, `REQUIRE_USER_DECISION`, and `UNAVAILABLE` fail closed.
- [x] Missing authority prevents provider dispatch.
- [x] Static compatibility/authorization issues are computed before incremental provider launch and can be exposed in the initial snapshot.
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
- [x] Honors the requested result limit before returning results.
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
- [x] Bounded result collection respects the caller-requested result count and provider hard ceiling.
- [x] Current runtime keeps unavailable authority fail-closed, so protected Contacts dispatch is not fabricated.
- [ ] Accepted Contacts runtime enablement.
- [ ] Explicit user opt-in and Android permission grant flow.
- [ ] Representative-device cancellation/timeout/action acceptance.

## Glaze UI V1.4 / 1.4.0 — Optical Intelligence

- [x] Branch source contract targets the current Official Stable Glaze UI V1.4 / `1.4.0` baseline.
- [x] Historical `1.1.0` and `2.1.0` values are treated as superseded implementation history, not current release authority.
- [x] Search-first interaction and visible source/authority state remain explicit.
- [x] Authorization-required state remains distinct from operational failure/timeout.
- [x] Compose source now consumes incremental `IndexSearchSnapshot` Flow updates while preserving the same issue/result surface.
- [x] Safe-drawing insets, bounded targets, semantic headings, and non-animated progress remain represented in the Development source line.
- [ ] Repository-local rendered/native V1.4 visual acceptance.
- [ ] Reduced Transparency / Increased Contrast / Reduced Motion / large-text acceptance.
- [ ] Localization/RTL acceptance.
- [ ] Representative phone/tablet/form-factor and performance acceptance, including incremental result-update behavior.
- [ ] Formal application-specific V1.4 conformance and production acceptance.

## Privacy Shield

- [x] No silent remote fallback.
- [x] Local providers declare local processing where applicable.
- [x] No intentional persistent search history or query analytics.
- [x] Incremental snapshots retain only current in-memory query results/issues and do not create a persistent history mechanism.
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

## Wardveil Security, Everkeep, and Mesh

- [x] No Wardveil trust/protection claim inferred from provider success.
- [x] Current query state remains transient.
- [ ] Wardveil provider/action security evidence integration.
- [ ] Everkeep continuity for applicable durable settings/configuration.
- [ ] Mesh provider discovery/coordination integration.

## GoreeCloud Sync

GoreeCloud Sync is a separate application/service capability, not one of the seven Integral Platform Systems.

- [x] Transient query text, incremental snapshots, and search history are not designated as Sync datasets.
- [x] The Platform Contract manifest does not misclassify Sync as a `platform_systems` member.
- [ ] Define explicit GoreeCloud Sync dataset contracts only for future approved durable Index state where synchronization is genuinely applicable.
- [ ] Complete runtime registration, authorization, reconciliation, conflict/deletion behavior, privacy review, and cross-device acceptance before claiming synchronized Index state.

## Platform Contract 0.3

- [x] Branch manifest declares the seven authoritative Integral Platform Systems: Manager, Privacy Shield, Wardveil Security, Everkeep, Glaze UI, GoreeCloud Mesh, and GoreeCloud Identity.
- [x] Compatibility requires `goreecloud-platform-contract==0.3` and `glaze-ui==1.4.0`.
- [x] Development validation is pinned to corrected GoreeCloud/GoreeCloud PR #30 candidate `96701cc5f20c8e0deaad512d2a9f83e0411f3f18` while that central PR remains draft/unmerged.
- [x] Sync-specific future obligations remain tracked separately without converting Sync into an Integral Platform System.
- [ ] Replace the temporary draft-candidate pin with an accepted central Platform Contract revision after normal governance completes.
- [ ] Achieve passing application-specific results for every applicable Integral Platform System before Stable qualification.

## Accepted Main Automated Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Exact-main workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

This section records historical accepted-main Development evidence. Branch checks and newer source work are not marked accepted-main or Stable until normal merge/release governance and runtime acceptance complete.
