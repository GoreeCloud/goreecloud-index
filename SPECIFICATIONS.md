# GoreeCloud Index — Specifications

## 1. Product Status

- **Product:** GoreeCloud Index
- **Repository:** `GoreeCloud/goreecloud-index`
- **Lifecycle state:** Active development — native Android application-search foundation
- **Development model:** Original GoreeCloud-owned native software built from the ground up
- **License:** GNU Affero General Public License v3.0
- **Production acceptance:** No
- **Stable qualification:** No
- **Current Stable Glaze UI target:** 2.1.0

This specification defines approved target scope and current source boundaries. Only sections explicitly describing current implementation may be treated as source-capability claims. Source capability does not automatically establish device, integration, production, or Stable acceptance.

## 2. Purpose and Responsibilities

GoreeCloud Index is the universal search and indexing authority for GoreeCloud. It accepts a query, determines which authorized providers may participate, retrieves provider results, normalizes and ranks them, preserves provenance, and presents provider-authorized actions through a consistent search experience.

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

GoreeCloud Index and GoreeCloud Search have distinct responsibilities.

- **Index** coordinates universal discovery across applications, files, contacts, calendar, media, settings, GoreeCloud services, connected devices, extensions, third-party services, and optional Internet results.
- **GoreeCloud Search** remains authoritative for Internet/web/current-information search.

GoreeCloud Launcher is a primary Index invocation/presentation surface and may provide Launcher-specific context. Launcher must not become a competing universal index.

Index must not take ownership of provider data merely because the resource is searchable.

## 4. Native Application Model

The production implementation must remain original GoreeCloud-owned software. Complete third-party universal-search products may not become the permanent implementation foundation.

Narrow operating-system APIs, frameworks, indexing/database primitives, protocol libraries, and other justified foundations may be used when they improve compatibility, accessibility, safety, or maintainability without replacing GoreeCloud authority or product direction.

The first native client is Android:

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Minimum API: 26
- Compile API: 37
- Target API: 36

## 5. Target Platforms and Surfaces

Long-term targets include phone, tablet, laptop, desktop, and other approved GoreeCloud device classes. Platform support must be claimed only after platform-specific implementation and validation.

Potential surfaces include:

- GoreeCloud Launcher universal search.
- Dedicated GoreeCloud Index application surfaces.
- Embedded search in compatible GoreeCloud applications.
- Keyboard-accessible desktop invocation.
- Mobile/tablet system-search entry points.
- Authorized application/service API calls.

A shared search core should preserve query semantics, provider identity, permission boundaries, ranking rules, and action contracts while each platform adapts its presentation natively.

## 6. Architecture

The target architecture is provider-oriented and authority-preserving:

```text
User query
  → Query intake
  → Identity / caller context
  → Privacy and permission evaluation
  → Provider discovery
  → Bounded provider dispatch
  → Provider retrieval
  → Result normalization
  → Security / trust evaluation where applicable
  → Ranking, grouping, and deduplication
  → Source-aware Glaze UI presentation
  → Authorized result action
```

Target components include Query Controller, Provider Registry, Authorization Gate, Local Index Manager, Provider Runtime, Normalizer, Ranker, Deduplicator/Grouper, Action Resolver, and Search UI Adapter.

The current Android slice implements a bounded subset: provider-neutral core models, `IndexQueryEngine`, one Android launcher-applications provider, deterministic ranking/deduplication, provider issue reporting, Launcher handoff, and a Compose search surface.

## 7. Search Provider Model

Every source must participate through an explicit provider contract rather than unrestricted internal database access.

A mature provider must be able to declare:

- Stable provider identity and human-readable name.
- Ownership/provider type.
- Searchable resource types.
- Query capabilities.
- Required scopes/permissions.
- Local, remote, or mixed processing.
- Network requirements.
- Result fields and authorized actions.
- Health/availability state.
- Optional indexing/change-notification support.
- Privacy/retention characteristics.
- Contract version and capability negotiation.

Providers must be independently cancellable, timeout-bounded, permission-aware, and failure-isolated before remote or expensive providers are enabled.

The current provider contract is synchronous because the first provider searches an in-memory snapshot of local launcher applications. This is not the final multi-provider runtime.

## 8. Current Applications Provider

The Android applications provider is implemented in source and uses:

- `Intent.ACTION_MAIN`.
- `Intent.CATEGORY_LAUNCHER`.
- Narrow manifest package visibility for the same launcher intent.
- No unrestricted `QUERY_ALL_PACKAGES` permission.
- No `INTERNET` permission in the current local-only slice.
- API 33+ `PackageManager.ResolveInfoFlags` with older-API compatibility.

Provider behavior:

- Discovers launcher-visible activities.
- Excludes the current Index application.
- Preserves exact `ComponentName` identity.
- Refreshes its immutable cached entry snapshot when Index resumes.
- Searches application labels and package names.
- Produces typed `LaunchActivity` actions.
- Applies the requested result bound.

Representative-device acceptance remains pending.

## 9. Planned Provider Categories

Planned categories include:

- Files and folders.
- Contacts.
- Calendar events.
- Media.
- Settings and platform actions where approved.
- First-party GoreeCloud application/service resources.
- Connected-device resources.
- GoreeCloud extensions.
- Optional third-party services.
- GoreeCloud Search Internet/web results.

Each provider requires its own permission, privacy, security, lifecycle, failure, and acceptance evidence. A category must not be made visible merely by adding a UI section.

## 10. Launcher Search Handoff

`GoreeCloudIndexContract` defines the first Android first-party handoff:

- Search action: `com.goreecloud.index.action.SEARCH`
- Initial-query extra: `com.goreecloud.index.extra.QUERY`

The Android activity accepts an optional initial query through this contract. This provides a Launcher→Index invocation boundary without making Launcher responsible for universal indexing.

Integrated device acceptance remains pending.

## 11. Query Behavior

Target query behavior includes incremental search, source/resource filtering, exact/prefix/fuzzy matching where appropriate, metadata-aware matching, optional structured operators, privacy-governed suggestions/history, local-only mode, cancellation, and provider timeouts.

Current application matching is deterministic:

- Exact title.
- Title prefix.
- Word prefix.
- Title containment.
- Exact package/secondary match.
- Package/secondary prefix.
- Package/secondary containment.
- Blank query receives a low browse score.

The query result limit is clamped to 1–100; the default is 50.

## 12. Current Query Snapshot and Failure Isolation

`IndexQueryEngine` returns `IndexSearchSnapshot`, which contains:

- Successful normalized results.
- Sanitized `IndexProviderIssue` records.

A provider exception must not suppress healthy results from other providers. Provider failures are reported by provider identity/name without exposing provider exception contents to the UI.

The current engine catches provider `Exception` failures, deduplicates results by `providerId:resultId`, sorts deterministically, and applies the final result bound.

## 13. Common Result Model

A normalized result must preserve only the information needed for consistent search behavior and safe handoff, including:

- Provider-scoped result identifier.
- Provider identifier.
- Resource/result type.
- Primary and secondary labels.
- Relevance signal.
- Source/provenance.
- Authorized action.
- Safe handoff identity.

Future fields may include visual metadata, location/origin, availability, and privacy/security indicators only when those states are backed by authoritative evidence.

Normalization must never make Index appear to own the underlying resource.

## 14. Ranking, Grouping, and Deduplication

Ranking must prioritize useful results without bypassing permissions or hiding provenance.

Target signals may include textual relevance, provider-supplied scores, resource type, source preferences, authorized recency/frequency, application context, locality, and availability.

Personalization signals must be minimized and Privacy Shield-governed.

Current Android application ranking uses deterministic `IndexTextMatcher` scores. Result deduplication is provider-scoped.

## 15. Local Indexing and Storage

Local indexing should exist only when it materially improves search latency or quality.

Requirements:

- Prefer derived searchable metadata over unnecessary full-content duplication.
- Scope data to applicable user/profile/device authority.
- Protect sensitive index data according to platform requirements.
- Support provider removal and index cleanup.
- Support rebuild/corruption recovery.
- Support incremental updates where providers expose change events.
- Distinguish reconstructible cache/index state from authoritative user data.
- Reconcile stale entries before sensitive actions.

No local content index is implemented yet.

## 16. Third-Party and Extension Providers

Third-party providers are optional and must never be required for core local search.

They must be explicitly enabled, narrowly authorized, transparent about resource types and processing location, independently revocable, isolated from unrelated provider data, and cleaned up according to retention policy after disconnection.

Extension/third-party providers must not be treated as trusted merely because they implement the provider interface. Applicable Wardveil, Privacy Shield, Identity, compatibility, and lifecycle checks remain required.

## 17. Internet Search

Internet/web/current-information search must be delegated to GoreeCloud Search as a clearly identified remote provider.

Index must not silently upload unrelated local resource data or silently replace a failed local provider with a less-private remote path.

No GoreeCloud Search runtime provider is currently implemented.

## 18. Privacy Shield Requirements

Privacy Shield is authoritative for applicable consent, purpose limitation, minimization, retention, sharing, and local-versus-remote data use.

Target controls include:

- Provider enable/disable.
- Permission/purpose explanations.
- Processing-location disclosure.
- History/personalization controls.
- Index/cache clearing.
- Third-party revocation.
- Retention limits.

Current source behavior is local-only for the applications provider, requests no Internet permission, and adds no intentional query analytics, telemetry, or persistent search history. These source properties do not establish accepted Privacy Shield runtime integration.

## 19. Wardveil Security Requirements

Wardveil Security is authoritative for applicable provider trust, threat, protection, and security evidence.

Index must:

- Preserve least privilege between providers.
- Treat provider output as untrusted input.
- Validate provider actions before execution where applicable.
- Avoid positive security claims without current evidence.
- Fail safely when a required trust decision is unavailable.
- Avoid leaking private query/result content into security diagnostics.

Current application actions are typed to exact Android launcher components. No Wardveil runtime acceptance is claimed.

## 20. GoreeCloud Identity Requirements

GoreeCloud Identity is authoritative for applicable user, profile, device, caller, application, service, and provider identity.

Target behavior includes user/profile isolation, caller identity for programmatic search, scoped provider authorization, third-party account association where appropriate, and local identity paths where cloud identity is unnecessary.

Authentication must not be treated as universal search permission.

No accepted Identity runtime integration exists yet.

## 21. GoreeCloud Mesh Requirements

Mesh may provide bounded first-party provider/capability discovery, availability coordination, and service routing. It must not take over provider data ownership or the independent authorities of Identity, Privacy Shield, Wardveil Security, or Everkeep.

No Mesh runtime provider discovery is currently implemented.

## 22. Everkeep Requirements

Everkeep governs continuity, recovery, portability, and preservation for durable Index state.

Current search query state is transient. No durable Index database or provider-configuration store is established by the current slice.

When durable state is introduced, reconstructible indexes/caches should normally be rebuilt from authoritative providers, while durable preferences/configuration should receive applicable continuity coverage.

## 23. Glaze UI 2.1.0 Requirements

The current Stable design target is Glaze UI 2.1.0. Index must satisfy the complete applicable consumer contract before Stable qualification.

The current Android source includes:

- Search-first interaction.
- Immediate focus.
- Current source/processing disclosure: **Applications · On-device**.
- Distinct browse, no-match, degraded-provider, and result states.
- Safe-drawing insets under edge-to-edge rendering.
- 56 px minimum search-field height.
- 72 px minimum result rows with a 48 px identity surface.
- Semantic headings.
- No decorative motion dependency.

Formal Glaze UI conformance, reduced-transparency, increased-contrast/forced-colors, reduced-motion, large-text/reflow, and representative form-factor acceptance remain pending.

## 24. Accessibility

Implemented surfaces must support screen readers, logical focus, keyboard operation where applicable, adequate touch targets, alternatives to gesture-only behavior, scalable text/layout, reduced motion, contrast/theme compatibility, and understandable loading/offline/permission/degraded states.

The current source establishes basic semantic headings, safe insets, interaction sizing, source labels, and explicit state messaging. Formal accessibility acceptance remains pending.

## 25. Reliability and Performance

Target requirements include fast local result startup, incremental provider results, cancellation, provider timeouts, partial results, low idle CPU/battery impact, bounded memory use, and corruption/rebuild behavior for future local indexes.

The current applications provider filters an in-memory immutable snapshot and caps query output. Exact performance thresholds must be established by representative testing rather than invented in the specification.

## 26. Observability

Operational telemetry must be privacy-minimized and must not log raw private queries or result contents by default.

Future observability may include provider availability/latency, aggregate query timing, index status, provider errors, contract mismatch, and resource impact.

The current slice adds no intentional analytics or remote telemetry.

## 27. Configuration

Planned configuration includes provider enable/disable, local-only mode, Internet-provider preference, third-party connections, result-category visibility/priority, history controls, index rebuild/clear, and permission review/revocation.

Defaults must favor privacy-preserving local behavior and must not silently activate external services.

## 28. Testing and Build Evidence

The repository validation path must cover source contracts, Android unit tests, lint, development APK assembly, package/version/label identity, exact source revision, checksum evidence, and artifact publication.

The first merged Android foundation at main commit `331e97507a7b3b7ca3d930771915f1026bf2d4a8` passed exact-main GitHub Actions run `33418751538`.

Validated development artifact evidence for that baseline:

- Package: `com.goreecloud.index.dev`
- Version: `0.1.0-dev`
- Version code: `1`
- Label: `GoreeCloud Index Dev`
- APK SHA-256: `8fee493995d500cd09579e15fe17915b7800117463f68fbc85f18cfd24b0ea3f`
- Artifact ID: `9768208441`

Later revisions require their own exact-candidate validation.

## 29. Current Implementation State

Current source implements:

- Android/Compose application foundation.
- Production/development application identity.
- Launcher→Index search-entry contract.
- Provider-neutral query/result/type/action/provider issue/search snapshot model.
- Query engine with bounded results, failure isolation, issue reporting, deterministic ranking, and provider-scoped deduplication.
- Android launcher-applications provider using scoped visibility and exact component identity.
- API 33+ package-manager query path with compatibility fallback.
- Source-aware search UI with safe insets and browse/no-match/degraded/result states.
- User-visible application-launch failure feedback.
- Unit tests and CI/build evidence path.

Not currently implemented or accepted:

- Files/folders, contacts, calendar, media, settings, connected-device, GoreeCloud Search, first-party Mesh-discovered, extension, or third-party providers.
- Local metadata/content index.
- Asynchronous provider runtime, cancellation, and provider timeouts.
- Search history, saved search, provider settings, or permission center.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device/Launcher/provider/accessibility/performance acceptance.
- Controlled production signing, release, deployment, production acceptance, or Stable qualification.

## 30. Production and Stable Gates

Index must not be represented as production-ready or Stable until the exact release revision has evidence for all applicable gates, including:

- Successful source/build validation.
- Supported local-search behavior on representative devices.
- Provider permission/isolation behavior.
- Glaze UI 2.1.0 consumer conformance.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, and Mesh integration where applicable.
- Third-party security/privacy review if third-party providers are included.
- Accessibility validation.
- Representative performance/resource-use validation.
- Controlled packaging, signing, deployment, rollback, and release documentation.
- README, SPECIFICATIONS, FEATURES, CAPABILITIES, BENEFITS, COMPETITIVE-OBJECTIVES, architecture, conformance, user manual, project specification, and canonical changelog reconciled to release state.

## 31. Next Development Sequence

1. Validate and merge the current foundation hardening against exact candidate source.
2. Perform representative Android and Launcher→Index device checks without converting source/build evidence into device acceptance prematurely.
3. Replace synchronous provider execution with asynchronous execution, superseded-query cancellation, and bounded provider timeouts.
4. Add explicit authorization context and provider capability declaration.
5. Add permission-aware file, contact, and calendar providers one at a time as platform APIs and authority contracts permit.
6. Integrate Privacy Shield, Wardveil Security, GoreeCloud Identity, GoreeCloud Mesh, and Everkeep through approved runtime contracts.
7. Add GoreeCloud Search as the explicit Internet provider.
8. Add extension and optional third-party providers only after isolation, authorization, trust, revocation, privacy, and lifecycle controls are accepted.
9. Complete Glaze UI 2.1.0 conformance, accessibility, performance, platform acceptance, release engineering, and production gates.
