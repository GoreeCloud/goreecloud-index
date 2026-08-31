# GoreeCloud Index — Specifications

## 1. Product Status

- **Product:** GoreeCloud Index
- **Repository:** `GoreeCloud/goreecloud-index`
- **Lifecycle state:** Active development — initial native Android application-search slice
- **Development model:** Original GoreeCloud-owned native software built from the ground up
- **License:** GNU Affero General Public License v3.0
- **Production acceptance:** No
- **Stable qualification:** No
- **Current Stable Glaze UI target:** 2.1.0

This specification defines both the approved target scope and the current source boundary. Only Section 29 and items explicitly marked implemented describe current source capability. Planned requirements remain planned until implementation and validation evidence exists.

## 2. Purpose and Responsibilities

GoreeCloud Index is the universal search and indexing layer for GoreeCloud. Its role is to accept a query, determine which authorized providers may participate, retrieve results, normalize and rank them, preserve source identity, and present safe provider-authorized actions through a consistent search experience.

Primary responsibilities are:

- Search authorized on-device resources.
- Coordinate first-party GoreeCloud search providers.
- Support an extensible provider model for approved extensions and optional third-party services.
- Normalize heterogeneous results into a common result model.
- Rank, group, filter, and deduplicate while preserving provenance.
- Expose only provider-authorized actions.
- Delegate Internet/web search to GoreeCloud Search.
- Preserve Privacy Shield, Wardveil Security, Everkeep, Identity, Mesh, and source-authority boundaries.

## 3. Product Boundary

GoreeCloud Index and GoreeCloud Search have distinct authority.

- **GoreeCloud Index** coordinates universal discovery across local resources, applications, platform services, extensions, connected providers, and optional Internet results.
- **GoreeCloud Search** remains authoritative for Internet and web search.

Index must not silently turn local-only queries into Internet queries. It also must not take ownership of provider data: contacts remain authoritative in their contact provider, calendar events in their calendar provider, files in their storage provider, installed applications in the operating-system/application registry, and third-party resources in their originating services.

## 4. Native Application and Service Model

The production implementation must remain original GoreeCloud-owned software. A complete third-party universal-search product may not become the permanent product foundation.

Narrow operating-system APIs, frameworks, databases, indexing primitives, protocol libraries, and other justified foundations may be used when replacing them would reduce compatibility, accessibility, safety, or maintainability. Such dependencies must not replace GoreeCloud product architecture, governance, authority, or UX.

The first native client is Android. The reserved production application ID is `com.goreecloud.index`; debug builds use `com.goreecloud.index.dev`.

## 5. Target Platforms and Surfaces

Long-term targets include phones, tablets, laptops, desktops, and other approved GoreeCloud device classes. Actual platform support requires implementation and platform-specific validation.

Potential surfaces include GoreeCloud Launcher search, a dedicated Index surface, embedded search fields in compatible GoreeCloud applications, keyboard-accessible desktop invocation, mobile/tablet system-search entry points, and authorized API/service calls.

A shared search core should preserve query semantics, permission boundaries, provider identity, ranking rules, and result contracts while each platform adapts presentation and input behavior natively.

## 6. Architecture

The target architecture is provider-oriented and authority-preserving:

```text
User query
  → Query intake
  → Identity / caller context
  → Privacy and permission evaluation
  → Provider discovery
  → Bounded provider dispatch
  → Provider-specific retrieval
  → Result normalization
  → Security / trust filtering where applicable
  → Ranking, grouping, and deduplication
  → Source-aware Glaze UI presentation
  → Authorized result action
```

Target component families are Query Controller, Provider Registry, Authorization Gate, Local Index Manager, Provider Runtime, Normalizer, Ranker, Deduplicator/Grouper, Action Resolver, and Search UI Adapter.

The current Android slice implements a bounded subset: typed query/result/provider/action models, `SearchEngine`, one Android launcher-application provider, deterministic ranking/deduplication, provider-failure isolation, and a native Compose search surface.

## 7. Search Provider Model

Every source must participate through an explicit provider contract rather than unrestricted database access.

A mature provider must be able to declare stable identity, ownership/type, searchable resource types, query capabilities, required scopes, local/remote/mixed processing, network requirements, result fields, authorized actions, health/availability, optional indexing/change events, privacy/retention characteristics, and contract version.

Providers must be independently cancellable, timeout-bounded, permission-aware, and failure-isolated before remote or expensive providers are enabled. The first Android provider uses a deliberately simpler synchronous contract because it is a bounded local platform lookup; asynchronous execution, cancellation, and timeout behavior remain required next steps.

## 8. Search Sources

### 8.1 Applications

**Current source:** Android launcher-visible applications are discoverable through `ACTION_MAIN` plus `CATEGORY_LAUNCHER`, with search by label/package name, deterministic relevance scoring, provider-scoped deduplication, and a typed launch action. The manifest uses the same narrow launcher query and does not request `QUERY_ALL_PACKAGES`.

**Planned expansion:** approved aliases, shortcuts/contextual actions, additional platforms, and cross-device application discovery where explicitly supported.

### 8.2 Files and Folders

Planned: permitted names, metadata, types, locations, tags, and authorized content indexes. Raw private file contents must not be copied into an uncontrolled Index store merely for convenience.

### 8.3 Contacts

Planned: authorized contact fields with account/source context and provider-authorized open or communication actions.

### 8.4 Calendar

Planned: authorized events and metadata while preserving account, calendar, visibility, and sharing boundaries.

### 8.5 Additional GoreeCloud Resources

Compatible GoreeCloud applications/services may expose search through provider contracts. First-party status does not justify unrestricted internal database access.

### 8.6 Extensions

Planned: versioned extension providers governed by the same permissions, privacy, security, identity, and lifecycle controls as other providers.

### 8.7 Internet Search

Planned: GoreeCloud Search as the clearly identified Internet provider. It must be independently suppressible and must not receive unrelated local resource data.

## 9. Third-Party Service Integration

Third-party providers are optional and must never be required for core local search. They must be explicitly enabled, narrowly authorized, clear about resource types and local/remote processing, independently revocable, isolated from unrelated providers, and cleaned up according to retention policy after disconnection.

Implementing the provider interface does not establish trust. Wardveil, Privacy Shield, Identity, and applicable lifecycle checks still apply.

## 10. Query Behavior

Target behavior includes incremental search, provider/resource filtering, exact/prefix/fuzzy matching where appropriate, metadata-aware matching, structured operators when useful, privacy-governed suggestions/history, local-only mode, cancellation, and provider timeouts.

Current Android application matching supports exact label, label prefix, label containment, package prefix, and package containment. Blank input does not dispatch the provider. The current synchronous query path is not the final multi-provider runtime.

## 11. Common Result Model

A normalized result must preserve provider-scoped identity, provider identifier, resource type, primary/secondary labels, relevance, source, availability where applicable, authorized actions, and safe handoff information. Optional visual or privacy/security fields may be added only when their authority exists.

Normalization must never make Index appear to own the underlying resource.

## 12. Ranking, Grouping, and Deduplication

Ranking may consider textual relevance, provider scores, resource type, user-selected source preferences, authorized recency/frequency, application context, locality, and availability. Personalization must be minimized and Privacy Shield-governed.

Current application results use a deterministic text score and deduplicate by provider plus resource identity. More sophisticated ranking remains planned.

## 13. Local Indexing and Storage

Local indexing should exist only when it materially improves latency or quality. It must prefer derived searchable metadata over unnecessary full-content duplication, scope data to the applicable user/profile/device, support provider removal, cleanup, rebuild, corruption recovery, and incremental updates, and distinguish reconstructible cache/index data from authoritative user data.

A stale index entry is not proof that a source resource still exists. Provider reconciliation is required before sensitive actions.

No local content index is implemented in the current slice.

## 14. Privacy Shield Requirements

Privacy Shield is authoritative for applicable consent, purpose limitation, minimization, retention, sharing, and local-versus-remote data use.

Target controls include provider enable/disable, permission explanations, processing-location disclosure, history/personalization controls, index/cache clearing, third-party revocation, and retention limits.

Current source behavior is local-only for the Android application provider, adds no intentional remote query path or analytics, and does not intentionally persist search text. This is source behavior only; accepted Privacy Shield runtime integration remains pending.

## 15. Wardveil Security Requirements

Wardveil Security is authoritative for provider trust, protection, threat, and security evidence where applicable.

Index must treat provider output as untrusted input, preserve least privilege, validate sensitive actions, avoid unsupported positive security claims, and fail safely when required security decisions are unavailable.

The current source does not claim Wardveil protection or trust evidence. Approved Wardveil runtime integration remains pending.

## 16. GoreeCloud Identity Requirements

Identity is authoritative for applicable user/profile/device/application/service/provider identity and scoped authorization. Authentication must not be interpreted as blanket provider permission.

User/profile isolation, caller identity, provider authorization, third-party account association, and local identity pathways remain planned for the appropriate providers.

## 17. GoreeCloud Mesh Requirements

Mesh should provide bounded first-party provider/capability discovery and coordination without taking over source ownership or the independent authorities of Identity, Privacy Shield, Wardveil Security, or Everkeep.

No Mesh runtime provider discovery is implemented yet.

## 18. Glaze UI Requirements

The current Stable target is **Glaze UI 2.1.0**. User-facing Index surfaces must satisfy the complete applicable consumer contract before Stable qualification.

The current Android surface implements a prominent search field, explicit source labeling, minimum primary interaction sizing, result hierarchy, and distinct blank/no-result/provider-error/result states. Formal Glaze UI 2.1.0 conformance, reduced-motion, reduced-transparency, increased-contrast/forced-colors, large-text/reflow, and representative form-factor acceptance remain pending.

## 19. Everkeep Requirements

Everkeep governs continuity, recovery, portability, and preservation for durable Index state. Reconstructible index/cache data should normally be rebuilt rather than protected as irreplaceable user data. Durable provider preferences/configuration may require recovery coverage when implemented.

The current query is transient; no durable Index configuration or accepted Everkeep runtime integration exists yet.

## 20. Cross-Device Behavior

Cross-device search is planned. Results must clearly distinguish current-device resources from synchronized/remote sources, connected third-party resources, and stale/unreachable resources. Discovery must not imply local/offline availability unless the applicable authority confirms it.

## 21. APIs and Extension Interfaces

Provider/extension contracts should be versioned and capability-negotiated. Expected families include provider registration/discovery, query/cancellation, streaming or pagination, result actions, health/availability, optional change notifications, scope declaration, and version negotiation.

No external provider API may be declared Stable until compatibility, migration, privacy/security requirements, and conformance are defined.

## 22. Error Handling and Resilience

Index must degrade gracefully when providers fail. Planned requirements include timeouts, cancellation, partial results, safe retry, offline local-provider behavior, index-rebuild recovery, and no privacy-weaker remote fallback.

Current `SearchEngine` catches a provider failure and returns a sanitized provider-specific issue while retaining healthy results. Application launch failure is surfaced to the user. No destructive side effects occur from ordinary current queries.

## 23. Accessibility

Implemented surfaces must support screen-reader semantics, logical focus, keyboard operation where applicable, adequate touch targets, alternatives to gesture-only behavior, scalable text/layouts, reduced motion, contrast/theme compatibility, and understandable state descriptions.

The current Compose source includes semantic headings, descriptive open/clear labels, source labels, and explicit states. Formal accessibility acceptance remains pending.

## 24. Performance Expectations

The target is fast local result startup, incremental multi-provider results, bounded provider latency, efficient indexing, low idle CPU/battery impact, bounded memory use, and cancellation of superseded work.

Exact thresholds must come from representative testing. The current provider caps output at 50 matching applications but has not yet undergone representative performance acceptance.

## 25. Observability

Operational telemetry must be privacy-minimized and must not log raw private queries or result content by default. Future observability may include provider availability/latency, aggregate timing, index status, provider errors, contract mismatch, and resource impact.

The current slice intentionally adds no analytics or remote telemetry.

## 26. Configuration

Planned configuration includes provider enable/disable, local-only mode, Internet-provider preference, third-party connections, category visibility/priority, history controls, index rebuild/clear, and permission review/revocation. Defaults must favor privacy-preserving local behavior and must not silently activate external services.

## 27. Testing and Validation

Implementation validation must cover provider contracts, permissions, user/profile isolation, cancellation/timeouts, normalization, ranking, failure isolation, local-only mode, third-party opt-in/revocation, index cleanup/rebuild, mandatory platform integrations, accessibility, and representative performance/resource use.

The repository now contains unit tests for blank-query suppression, provider failure isolation, ranking, and provider-scoped deduplication plus CI definitions for repository validation, unit tests, lint, development APK assembly, APK identity checks, exact-source capture, checksum evidence, and artifact publication. These checks must pass on the exact candidate before build validation is claimed.

## 28. Production and Stable Acceptance

Index must not be represented as production-ready or Stable until the exact release revision has evidence for successful source/build validation, supported local-search behavior, provider permission/isolation, Glaze UI 2.1.0 consumer conformance, accepted Privacy Shield/Wardveil/Everkeep/Identity/Mesh integration where applicable, third-party review if included, accessibility, representative performance, controlled packaging/signing/deployment/rollback, and reconciled repository/project/changelog documentation.

Source implementation, source validation, build validation, device validation, integration acceptance, release, deployment, production acceptance, and Stable qualification are distinct states.

## 29. Current Implementation State

The current source implements the first native Android vertical slice:

- Android/Compose application shell.
- Production application ID `com.goreecloud.index` and debug `com.goreecloud.index.dev`.
- API 26 minimum, compile API 37, target API 36, JDK 17 baseline.
- Typed query/provider/result/action/error model.
- Search engine with blank-query suppression, provider failure isolation, score ordering, and provider-scoped deduplication.
- Android launcher-application provider using narrow launcher activity visibility and no `QUERY_ALL_PACKAGES` request.
- Application label/package matching and typed launch actions.
- Native source-aware search UI with blank, no-result, provider-error, result, and launch-failure behavior.
- Unit-test, repository-validator, lint/build, APK identity/checksum, and artifact-publication CI definitions.

Not currently implemented or accepted:

- Files/folders, contacts, calendar, GoreeCloud Search, first-party Mesh-discovered, extension, or third-party providers.
- Local content index.
- Async provider runtime, cancellation, or provider timeouts.
- Search history/saved search/provider settings.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device/provider/accessibility/performance acceptance.
- Controlled production signing, release, deployment, production acceptance, or Stable qualification.

## 30. Development Sequence

1. **Current slice:** establish native query/result/provider contracts, Android application provider, source-aware search UI, unit tests, repository validation, and Android CI/build evidence path.
2. Validate the exact candidate in CI and on representative Android devices; correct any provider, UI, accessibility, or packaging defects.
3. Replace the bounded synchronous provider execution path with asynchronous execution, supersession cancellation, and provider timeouts before adding remote/expensive providers.
4. Add permission-aware file, contact, and calendar providers one at a time as platform APIs permit.
5. Integrate Privacy Shield, Wardveil Security, GoreeCloud Identity, GoreeCloud Mesh, and Everkeep through approved runtime contracts rather than placeholder branding.
6. Add GoreeCloud Search as the explicit Internet provider.
7. Add extension and optional third-party providers only after isolation, authorization, trust, revocation, privacy, and lifecycle controls are validated.
8. Complete Glaze UI 2.1.0 consumer conformance, accessibility, performance, platform acceptance, release engineering, and production gates.
