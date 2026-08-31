# GoreeCloud Index — Specifications

## 1. Product Status

- **Product:** GoreeCloud Index
- **Repository:** `GoreeCloud/goreecloud-index`
- **Release lifecycle:** Development
- **Current source milestone:** Native Android application-search foundation with asynchronous provider runtime
- **Development model:** Original GoreeCloud-owned native software built from the ground up
- **License:** GNU Affero General Public License v3.0
- **Production acceptance:** No
- **Stable qualification:** No
- **Current Stable Glaze UI target:** 2.1.0

This specification defines approved target scope and current source boundaries. Only sections explicitly describing current implementation may be treated as source-capability claims. Source capability does not automatically establish exact-candidate acceptance, representative-device behavior, platform integration, production acceptance, or Stable qualification.

The accepted main baseline before the current asynchronous branch is `19737c11c59a30a94ee8b6dad8855b449c011eca`. Exact-main run `33420873144` passed for that baseline. The `0.2.0-dev` asynchronous runtime requires its own exact-candidate validation and merge acceptance.

## 2. Purpose and Responsibilities

GoreeCloud Index is the universal search and indexing authority for GoreeCloud. It accepts a query, determines which eligible and authorized providers may participate, retrieves provider results, normalizes and ranks them, preserves provenance, and presents provider-authorized actions through a consistent search experience.

Index must:

- Search authorized local resources.
- Coordinate first-party GoreeCloud search providers.
- Support approved extension and optional third-party providers.
- Normalize heterogeneous provider results without erasing source identity.
- Rank, group, filter, and deduplicate within authorization boundaries.
- Expose only provider-authorized actions.
- Delegate Internet/web/current-information search to GoreeCloud Search.
- Preserve independent Privacy Shield, Wardveil Security, Everkeep, Identity, Mesh, and source-provider authority.

## 3. Product Boundary

- **GoreeCloud Index** coordinates universal discovery across applications, files, contacts, calendar, media, settings, GoreeCloud services, connected devices, extensions, third-party services, and optional Internet results.
- **GoreeCloud Search** remains authoritative for Internet/web/current-information search.
- **GoreeCloud Launcher** is a primary Index invocation/presentation surface and may provide Launcher-specific context. Launcher must not become a competing universal index.
- Source applications and services remain authoritative for their own data. Searchability never transfers data ownership to Index.

## 4. Native Android Application Model

The production implementation must remain original GoreeCloud-owned software. Complete third-party universal-search products may not become the permanent implementation foundation.

Current Android identity on this branch:

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Development version: `0.2.0-dev`
- Version code: `2`
- Minimum API: 26
- Compile API: 37
- Target API: 36

Narrow operating-system APIs, frameworks, coroutine libraries, indexing/database primitives, protocol libraries, and other justified foundations may be used when they improve compatibility, accessibility, safety, or maintainability without replacing GoreeCloud product authority.

## 5. Target Platforms and Surfaces

Long-term targets include phone, tablet, laptop, desktop, and other approved GoreeCloud device classes. Platform support must be claimed only after platform-specific implementation and validation.

Potential surfaces include GoreeCloud Launcher universal search, a dedicated Index application, embedded first-party application search, keyboard-accessible desktop invocation, system-search entry points, and authorized application/service APIs.

## 6. Provider-Oriented Architecture

The target architecture is authority-preserving:

```text
User query
  → query intake
  → caller/user identity
  → privacy/permission evaluation
  → provider discovery
  → execution eligibility
  → bounded concurrent provider dispatch
  → provider retrieval
  → result normalization
  → applicable trust/security evaluation
  → ranking/grouping/deduplication
  → source-aware Glaze UI presentation
  → authorized result action
```

The current Android source implements provider-neutral models, explicit processing location, execution eligibility, structured concurrent dispatch, provider timeouts, cancellation propagation, deterministic ranking/deduplication, one local applications provider, Launcher handoff, and a Compose search surface.

## 7. Provider Contract

Every source must participate through an explicit provider contract rather than unrestricted internal database access.

The current `IndexProvider` contract requires:

- stable provider ID;
- human-readable display name;
- processing location: `LOCAL`, `REMOTE`, or `MIXED`;
- provider timeout declaration;
- suspendable search operation.

A mature external/provider ecosystem additionally requires provider ownership/type, resource types, query capabilities, required scopes/permissions, network requirements, result/action capabilities, health state, indexing/change-notification support, retention characteristics, contract versioning, and capability negotiation.

## 8. Execution Eligibility and Authorization Boundary

`IndexExecutionContext` currently carries:

- exact `allowedProviderIds`;
- `localOnly`.

The engine fails closed: providers outside the allowlist are not dispatched, and `REMOTE`/`MIXED` providers are not dispatched when `localOnly=true`.

This is an internal execution-eligibility guard. It does **not** constitute accepted Privacy Shield consent/purpose authorization or GoreeCloud Identity authorization. Future platform authorities must feed the provider-runtime boundary through approved contracts rather than being inferred from local application state.

## 9. Asynchronous Query Runtime

`IndexQueryEngine.search` is suspendable and uses Kotlin structured concurrency.

Current source requirements and behavior:

- Trim query text.
- Clamp result count to 1–100.
- Filter providers through execution eligibility before dispatch.
- Dispatch eligible providers concurrently under `supervisorScope`.
- Use an injectable provider dispatcher.
- Apply each provider timeout through `withTimeout`.
- Clamp timeout declarations to a global five-second safety ceiling.
- Convert timeout to sanitized `TIMED_OUT` provider issue.
- Convert ordinary provider exception to sanitized `FAILED` provider issue.
- Explicitly rethrow external `CancellationException`.
- Preserve healthy-provider results when another provider fails or times out.
- Rank before deduplication by score, case-insensitive title, then provider ID.
- Deduplicate by `providerId:resultId`, retaining the best-ranked representation.
- Apply the final result cap.

These timeout values are Development safety bounds, not performance SLAs.

## 10. Superseded-Query Cancellation

`IndexRoot` executes the suspendable search operation from `LaunchedEffect(query)`. A query change cancels the previous effect and therefore cancels its structured provider coroutine tree.

The engine must not catch external cancellation as an ordinary provider failure. Tests must verify cancellation propagation explicitly.

Incremental/streaming result delivery is not yet implemented; a search snapshot is returned after all eligible providers have either completed, failed, timed out, or the parent query has been cancelled.

## 11. Provider Failure Model

`IndexSearchSnapshot` contains successful normalized results and sanitized `IndexProviderIssue` entries.

Issue kinds:

- `FAILED` — ordinary provider exception.
- `TIMED_OUT` — provider exceeded its bounded execution time.

Provider exception contents must not be surfaced directly to the user. A provider issue must not suppress healthy sibling-provider results.

## 12. Current Android Applications Provider

The implemented provider uses:

- `Intent.ACTION_MAIN`;
- `Intent.CATEGORY_LAUNCHER`;
- narrow manifest visibility for the same launcher intent;
- no unrestricted `QUERY_ALL_PACKAGES` permission;
- no `INTERNET` permission in the current local-only slice;
- API 33+ `PackageManager.ResolveInfoFlags` with older-API compatibility.

It declares:

- provider ID `goreecloud.index.provider.apps`;
- display name `Applications`;
- processing location `LOCAL`;
- provisional timeout `500 ms`.

It discovers launcher-visible activities, excludes Index itself, preserves exact `ComponentName` identity, refreshes its immutable snapshot when Index resumes, searches labels/package names, and produces typed `LaunchActivity` actions.

The 500 ms value is not an accepted representative-device latency target.

## 13. Query Matching and Ranking

Current application matching supports:

- exact title;
- title prefix;
- word prefix;
- title containment;
- exact package/secondary match;
- package/secondary prefix;
- package/secondary containment;
- low browse score for blank query.

Across provider results, higher scores rank first. Provider-scoped duplicate identities are collapsed only after ranking so the highest-ranked representation is retained.

Future ranking may incorporate authorized recency, provider signals, resource type, context, locality, availability, and explicit user preferences. Personalization must remain Privacy Shield-governed and must not become hidden behavioral profiling.

## 14. Launcher Search Handoff

`GoreeCloudIndexContract` defines:

- Search action: `com.goreecloud.index.action.SEARCH`
- Initial-query extra: `com.goreecloud.index.extra.QUERY`

This provides a Launcher→Index invocation boundary without transferring universal-search authority to Launcher. Integrated representative-device acceptance remains pending.

## 15. Current Search UI

The Compose surface currently provides:

- immediate query focus;
- **Applications · On-device** source/processing disclosure;
- non-animated searching text state;
- blank/browse state;
- no-match state;
- provider ordinary-failure state;
- provider timeout state;
- valid result rows and source labels;
- launch-failure feedback;
- safe-drawing insets;
- 56 px minimum search-field height;
- 72 px minimum result-row height;
- 48 px result identity surface;
- semantic headings.

The UI targets Glaze UI 2.1.0. Formal application-specific Glaze consumer conformance, reduced-transparency, contrast, reduced-motion, large-text/reflow, and representative-device visual/accessibility acceptance remain pending.

## 16. Planned Provider Categories

Planned categories include files/folders, contacts, calendar, media, settings/platform actions, first-party GoreeCloud application/service resources, connected-device resources, extensions, optional third-party services, and GoreeCloud Search Internet/web results.

A provider category must not be enabled merely because the asynchronous runtime can dispatch it. Each provider requires applicable source authority, permissions, privacy, security, identity, retention, failure, recovery, and acceptance evidence.

## 17. Local Indexing and Storage

Local indexing should exist only when it materially improves search latency or quality.

Requirements include derived searchable metadata over unnecessary full-content duplication, user/profile/device scoping, provider removal cleanup, rebuild/corruption recovery, incremental updates where supported, protection of sensitive index state, and strict distinction between reconstructible index/cache state and authoritative source data.

No local content index is implemented yet.

## 18. Internet, Extension, and Third-Party Providers

Internet/web/current-information search must be delegated to GoreeCloud Search as a clearly identified remote provider.

Extensions and third-party providers must be optional, explicit, narrowly scoped, transparent about processing location, independently revocable, isolated from unrelated provider data, and subject to applicable Wardveil, Privacy Shield, Identity, compatibility, and lifecycle requirements.

No such provider is currently implemented or enabled.

## 19. Privacy Shield Requirements

Privacy Shield remains authoritative for applicable consent, purpose limitation, minimization, retention, sharing, and local-versus-remote data use.

The current source is local-only for the applications provider, requests no Internet permission, adds no intentional query analytics/remote telemetry/persistent search history, and uses `localOnly=true` execution eligibility. These properties do not establish accepted Privacy Shield runtime integration.

## 20. Wardveil Security Requirements

Wardveil Security remains authoritative for applicable provider trust, threat, protection, and security evidence.

Index must preserve least privilege, treat provider output as untrusted input, validate sensitive provider actions where applicable, avoid positive security claims without evidence, fail safely when required trust decisions are unavailable, and avoid leaking private query/result content into diagnostics.

Current application actions are typed to exact Android launcher components. No Wardveil runtime acceptance is claimed.

## 21. GoreeCloud Identity and Mesh

GoreeCloud Identity remains authoritative for applicable user/profile/device/caller/provider identity and scoped authorization. Authentication must not become blanket search permission.

GoreeCloud Mesh may provide bounded first-party provider/capability discovery and availability coordination. It must not take over source ownership or independent platform authorities.

Neither integration is currently accepted.

## 22. Everkeep

Current query state and asynchronous execution state are transient. No durable Index database or provider-configuration store is established by this branch.

When durable state is introduced, reconstructible indexes/caches should normally be rebuilt from authoritative providers while durable preferences/configuration receive applicable Everkeep continuity coverage.

## 23. Accessibility and Glaze UI 2.1.0

All user-facing surfaces must complete the applicable Stable Glaze UI 2.1.0 consumer contract before Stable qualification. This includes accessible semantics, logical focus, adequate touch targets, scalable text/reflow, reduced motion, contrast/theme compatibility, reduced-transparency behavior where applicable, and understandable loading/offline/permission/degraded states.

Current source establishes only a subset and must not be represented as formally Glaze-conformant.

## 24. Reliability and Performance

Current source includes structured concurrency, supersession cancellation, provider timeouts, failure isolation, partial healthy results, deterministic ranking, and bounded result counts.

Future reliability includes provider health negotiation, incremental results, safe retry policies where justified, index rebuild/corruption recovery, offline provider behavior, and cross-device resilience.

Exact latency, battery, memory, and provider timeout targets require representative-device evidence.

## 25. Observability

Operational telemetry must be privacy-minimized and must not log raw private queries or result contents by default.

Future observability may include provider availability/latency, aggregate query timing, index status, provider errors, contract mismatch, cancellation rates, timeout rates, and resource impact. The current slice adds no intentional analytics or remote telemetry.

## 26. Testing Requirements

The asynchronous runtime must have automated tests for:

- concurrent provider execution;
- ordinary failure isolation;
- timeout isolation;
- external cancellation propagation;
- disallowed-provider non-dispatch;
- local-only remote/mixed provider non-dispatch;
- deterministic ranking;
- ranking-before-deduplication;
- result-count bounds;
- text matching.

The repository pipeline must additionally validate source contracts, lint, Development APK assembly, exact package/version/label identity, source revision, checksum evidence, and artifact publication.

## 27. Accepted Main Evidence

Accepted main commit `19737c11c59a30a94ee8b6dad8855b449c011eca` passed exact-main run `33420873144` for the prior `0.1.0-dev` state.

Accepted-main evidence:

- Package: `com.goreecloud.index.dev`
- Version: `0.1.0-dev`
- Version code: `1`
- APK SHA-256: `a867073c433941297da985a1c8ec3e3972e7ecd6db4883f18e935a8d6fd72f83`
- Artifact ID: `9768893227`

The `0.2.0-dev` branch must produce independent exact-candidate evidence before merge.

## 28. Current Limitations

Not implemented or accepted:

- files/folders, contacts, calendar, media, settings, connected-device, GoreeCloud Search, Mesh-discovered first-party, extension, or third-party providers;
- local metadata/content index;
- incremental/streaming result delivery;
- provider capability/health negotiation;
- Privacy Shield user/provider settings center;
- accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration;
- formal Glaze UI 2.1.0 application-specific consumer conformance;
- representative-device/Launcher/provider/accessibility/performance acceptance;
- controlled production signing, release, deployment, production acceptance, or Stable qualification.

## 29. Next Development Sequence

After this asynchronous runtime passes exact-candidate validation and merges:

1. Reconcile accepted-main source/build evidence and canonical project/changelog records.
2. Select one second provider with a clear authoritative source and permission model.
3. Define and integrate the approved Privacy Shield/Identity decision path required by that provider before dispatch.
4. Validate provider cancellation, timeout, provenance, result actions, and failure behavior end to end.
5. Add incremental result delivery only after snapshot semantics and UI accessibility remain coherent under multiple providers.
6. Continue provider expansion one authority boundary at a time.

## 30. Production and Stable Gates

GoreeCloud Index must not be represented as production-ready or Stable until the exact release revision has evidence for all applicable gates, including source/build validation, supported local-search behavior on representative devices, provider permission/isolation behavior, current Stable Glaze UI conformance, accepted Privacy Shield/Wardveil/Everkeep/Identity/Mesh integration where applicable, third-party review if included, accessibility, representative performance/resource use, controlled packaging/signing/deployment/rollback, and reconciled repository/project/changelog documentation.
