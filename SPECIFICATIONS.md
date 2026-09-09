# GoreeCloud Index — Specifications

## 1. Product Status

- **Product:** GoreeCloud Index
- **Repository:** `GoreeCloud/goreecloud-index`
- **Release lifecycle:** Development
- **Current repository main:** `9f3aaa9543a8a8351fa2d4713481bccb8199069d`
- **Historical accepted APK source:** `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- **Historical accepted APK workflow:** `33431294298`
- **Historical accepted APK SHA-256:** `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- **Historical accepted artifact:** `9772740479`
- **Current source version:** `0.3.0-dev` / version code `3`
- **Production acceptance:** No
- **Stable qualification:** No
- **Current Stable Glaze UI target:** `1.3.0`

The older APK/build evidence is authoritative only for its exact source revision. Current `main` and this V1.3 reconciliation branch are newer Development source and require independent exact-head validation and review.

## 2. Purpose and Product Boundary

GoreeCloud Index is GoreeCloud's universal search and indexing authority. It coordinates authorized providers, normalizes/ranks results, preserves provenance, and exposes provider-authorized actions without taking ownership of provider records.

GoreeCloud Search remains authoritative for Internet/web/current-information search. GoreeCloud Launcher is an invocation/presentation surface. Source applications, Android platform providers, and services remain authoritative for their own resources and operations.

## 3. Android Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Current source: `0.3.0-dev`, version code `3`
- API 26 minimum, compile API 37, target API 36.

The application is original GoreeCloud-owned Kotlin/Jetpack Compose software.

## 4. Provider Contract

`IndexProvider` declares provider identity, display name, processing location (`LOCAL`, `REMOTE`, `MIXED`), timeout, suspendable search, authority requirements, and whether a blank query is meaningful for that provider.

Provider activation remains separate from provider implementation. Every provider requires applicable source authority, platform/app permission, Privacy Shield, Identity, Wardveil, retention, action, failure, and acceptance evidence.

## 5. Execution and Authority Boundary

`IndexExecutionContext` provides exact provider allowlisting and `localOnly` processing gating. This is an Index application execution boundary, not a platform authority.

Current source defines explicit authority requirements including:

- `ANDROID_RUNTIME_PERMISSION`
- `PRIVACY_SHIELD`
- `GOREECLOUD_IDENTITY`

Platform decision evidence is consumed fail-closed. Privacy Shield/Identity evidence must be an unconstrained `ALLOW` with a non-empty reference. `DENY`, `REQUIRE_USER_DECISION`, `UNAVAILABLE`, or `ALLOW_WITH_CONSTRAINTS` does not dispatch a provider. Constrained decisions remain blocked until Index implements an obligations evaluator rather than silently discarding constraints.

This model does not make Index the Privacy Shield or GoreeCloud Identity authority. Current source uses an unavailable platform-authority gateway for Contacts, so Contacts remains non-dispatchable until accepted upstream decisions can be supplied.

## 6. Asynchronous Query Runtime

The engine:

- trims query text and clamps result count to 1–100;
- excludes providers that do not support blank-query browsing when the query is blank;
- evaluates allowlist, processing location, and authority before dispatch;
- reports `AUTHORIZATION_REQUIRED` for an allowlisted/applicable provider whose required authority evidence is incomplete;
- dispatches eligible providers concurrently under structured concurrency;
- applies bounded provider timeout behavior;
- reports sanitized `TIMED_OUT` and `FAILED` issues;
- propagates external cancellation;
- preserves healthy results;
- ranks before provider-scoped deduplication;
- applies the final result cap.

## 7. Applications Provider

**Applications · On-device** uses `ACTION_MAIN` + `CATEGORY_LAUNCHER`, avoids `QUERY_ALL_PACKAGES`, requires no Internet permission, declares local processing and a provisional 500 ms timeout, and returns exact launcher-component actions.

## 8. Contacts Provider — Current Source, Authority-Gated

Current source implements **Contacts · On-device** while keeping runtime activation fail-closed.

- Source authority: Android `ContactsContract` / ContactsProvider.
- Manifest permission: `android.permission.READ_CONTACTS`.
- Processing: `LOCAL`.
- Timeout: provisional 750 ms Development bound.
- Blank query: unsupported; Contacts are not enumerated merely by opening Index.
- Query path: `Contacts.CONTENT_FILTER_URI`.
- Read projection: contact `_ID`, `LOOKUP_KEY`, and `DISPLAY_NAME_PRIMARY` only.
- Phone numbers and email addresses are not requested by this provider slice.
- Result type: `CONTACT` with visible on-device provenance.
- Action: typed contact-view URI generated from Contacts authority data.
- Action defense-in-depth: MainActivity accepts only reviewed Contacts `content` URIs before handoff.
- Required dispatch authority: Android runtime permission + Privacy Shield evidence + GoreeCloud Identity authorization evidence.

Because production/runtime Privacy Shield and Identity adapters are not accepted or wired here, source implementation is not equivalent to enabled Contacts search.

## 9. Settings Navigation Provider — Current Source

Current source also contains a bounded Android Settings navigation provider.

- It searches a repository-defined static destination catalog rather than Android setting values.
- Processing remains local-only with a bounded Development timeout.
- Blank queries do not enumerate Settings destinations.
- It reads no setting values, device configuration values, account state, permission state, or history.
- It introduces no Internet permission, analytics, cache, or persistent query history.
- Results use typed Settings navigation actions constrained to a reviewed static allowlist.
- MainActivity revalidates the action before Android handoff.
- Arbitrary intent actions, URLs, data URIs, extras, and direct setting mutation are unsupported.
- Android Settings remains authoritative for all configuration state and changes.

Representative-device/OEM action availability and behavior remain unaccepted.

## 10. Search UI

The Compose surface supports authorized-source search and exposes:

- Applications active state;
- Contacts authority-gated state;
- bounded searching state;
- provider issues including authorization-required, timeout, and failure;
- source-aware result labels;
- explicit no-match/degraded behavior.

The V1.3 candidate changes presentation theming only; it does not change provider truth, authorization, or data handling.

## 11. GLAZE UI V1.3 / 1.3.0 Reconciliation

The current shared consumer target is **GLAZE UI V1.3 / `1.3.0` — Adaptive Resonance**.

Current authority recorded by this candidate:

- current Official Stable: `1.3.0`;
- exact Stable source integration anchor: `fc7cc91d2eace8da2371371c2855c24cbcb326a1`;
- Stable web entrypoint identifier: `css/glaze-v1.3.0.css`;
- Stable runtime entrypoint identifier: `js/glaze-v1.3.0.mjs`;
- rollback baseline: `1.2.0`.

Index is a native Compose consumer. The web/runtime entrypoint identifiers are recorded as lifecycle/provenance authority, not as native imports.

The repository-local native V1.3 contract:

- replaces generic Android dynamic-color sampling with deterministic GoreeCloud-owned Light, Dark, and Deep Dark schemes;
- keeps neutral structure dominant with Deep Teal primary atmosphere and Soft Amber secondary atmosphere;
- uses 16/24/32 dp optical shape geometry;
- encodes 48dp ordinary and 56dp Touch Assistance target floors;
- records that nested backdrop blur and remote color derivation are not required;
- keeps atmospheric color separate from privacy, security, identity, provider authorization, recovery, and provenance semantics.

The machine-readable `goreecloud.platform.yaml` declares Glaze source `1.3.0`, required target `1.3.0`, `applicable-migration-required`, lifecycle Development, and overall `nonconformant` until independent acceptance is complete.

## 12. Privacy Shield Contract Alignment

The authoritative Privacy Shield boundary remains external to Index. Index consumes producer-owned decisions and does not reinterpret constrained authorization as full permission. Future adapters must preserve requester/resource/operation/purpose/processing-zone/destination/retention boundaries and auditable evidence without exposing private query/contact content in ordinary evidence.

## 13. GoreeCloud Identity Boundary

GoreeCloud Identity remains authoritative for platform-level identity and GoreeCloud-level authorization. Index/provider logic remains responsible for application/provider-specific record access. Authentication must never be treated as blanket authorization to search Contacts or other private sources.

Current source defines only a consuming evidence boundary. Actual accepted Identity runtime integration remains pending.

## 14. Wardveil, Everkeep, Mesh, and Manager

Wardveil Security runtime evidence remains required for provider/action security acceptance. Everkeep remains relevant to applicable durable configuration/recovery, not transient query results. Mesh may discover/coordinate first-party provider capabilities later without taking source authority. Manager operational health/readiness visibility is also not yet accepted. None is production accepted for Index.

## 15. Validation Evidence

Historical accepted APK/build evidence:

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

Current `main` and this V1.3 branch require independent exact-head workflow evidence. No newer artifact is treated as accepted until its exact conclusion is observed and governance permits the corresponding state.

## 16. Current Limitations

Not implemented or accepted: actual Privacy Shield/Identity runtime adapters; user-authorized Contacts enablement; files/folders; calendar; media; connected devices; GoreeCloud Search provider; Mesh discovery; extensions/third-party providers; local content index; incremental streaming; full provider capability/health negotiation; formal Glaze UI application conformance; representative-device performance/accessibility; production signing/deployment; production acceptance; Stable qualification.

Settings **value/state indexing** is not implemented by the static navigation provider and would require a separate authority/privacy/security review.

## 17. Next Development Sequence

1. Exact-head validate this V1.3 reconciliation branch with the Android repository workflow and current Platform Contract validator.
2. Perform representative rendered/native visual, TalkBack/accessibility, large-text/reflow, RTL/localization, resilience, form-factor, and performance acceptance for the exact candidate.
3. Integrate accepted Privacy Shield and GoreeCloud Identity adapters without weakening the fail-closed evidence model.
4. Add explicit user decision/permission flows only when platform authority can be satisfied end to end.
5. Perform representative-device Contacts and Settings action/cancellation/timeout/accessibility acceptance.
6. Expand one provider authority boundary at a time.

## 18. Production and Stable Gates

GoreeCloud Index must not be called production-ready or Stable until the exact release has applicable source/build, representative-device, permission/isolation, Privacy Shield, Identity, Wardveil, Everkeep, Mesh, Manager, Glaze UI, accessibility, performance, signing/deployment/rollback, and reconciled documentation evidence.
