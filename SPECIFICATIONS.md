# GoreeCloud Index — Specifications

## 1. Product Status

- **Product:** GoreeCloud Index
- **Repository:** `GoreeCloud/goreecloud-index`
- **Release lifecycle:** Development
- **Accepted main:** `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- **Accepted-main version:** `0.2.0-dev` / version code `2`
- **Current branch version:** `0.3.0-dev` / version code `3`
- **Production acceptance:** No
- **Stable qualification:** No
- **Current Stable Glaze UI target:** 2.1.0

Exact-main workflow run `33431294298` passed for the accepted source. The current Contacts/authority branch requires its own exact-head validation and merge acceptance before becoming accepted-main evidence.

## 2. Purpose and Product Boundary

GoreeCloud Index is GoreeCloud's universal search and indexing authority. It coordinates authorized providers, normalizes/ranks results, preserves provenance, and exposes provider-authorized actions without taking ownership of provider records.

GoreeCloud Search remains authoritative for Internet/web/current-information search. GoreeCloud Launcher is an invocation/presentation surface. Source applications and platform providers remain authoritative for their resources.

## 3. Android Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Current branch: `0.3.0-dev`, version code `3`
- API 26 minimum, compile API 37, target API 36.

The application is original GoreeCloud-owned Kotlin/Jetpack Compose software.

## 4. Provider Contract

`IndexProvider` declares stable provider identity, display name, processing location (`LOCAL`, `REMOTE`, `MIXED`), timeout, suspendable search, authority requirements, and whether a blank query is meaningful for that provider.

Provider activation remains separate from provider implementation. Every provider requires applicable source authority, platform/app permission, Privacy Shield, Identity, Wardveil, retention, action, failure, and acceptance evidence.

## 5. Execution and Authority Boundary

`IndexExecutionContext` still provides exact provider allowlisting and `localOnly` processing gating. This remains an Index application execution boundary, not a platform authority.

The branch adds `IndexProviderAuthority` and explicit requirements:

- `ANDROID_RUNTIME_PERMISSION`
- `PRIVACY_SHIELD`
- `GOREECLOUD_IDENTITY`

Platform decision evidence is consumed fail-closed. Privacy Shield/Identity evidence must be an unconstrained `ALLOW` with a non-empty reference. `DENY`, `REQUIRE_USER_DECISION`, `UNAVAILABLE`, or `ALLOW_WITH_CONSTRAINTS` does not dispatch a provider. Constrained decisions remain blocked until Index implements an obligations evaluator rather than silently discarding constraints.

This model mirrors the current Privacy Shield decision boundary without claiming that Index is the Privacy Shield authority. It does not invent a GoreeCloud Identity network API; Identity authorization is represented only as evidence supplied by a future accepted adapter. Current MainActivity supplies Privacy Shield and Identity as unavailable for Contacts, so Contacts is not dispatched in the current branch runtime.

## 6. Asynchronous Query Runtime

The engine:

- trims query text and clamps result count to 1–100;
- excludes providers that do not support blank-query browsing when the query is blank;
- evaluates allowlist, processing location, and authority before dispatch;
- reports `AUTHORIZATION_REQUIRED` for an allowlisted/applicable provider whose required authority evidence is incomplete;
- dispatches eligible providers concurrently under `supervisorScope`;
- applies provider `withTimeout` with a five-second Development ceiling;
- reports sanitized `TIMED_OUT` and `FAILED` issues;
- rethrows external cancellation;
- preserves healthy results;
- ranks before provider-scoped deduplication;
- applies the final result cap.

## 7. Applications Provider

**Applications · On-device** remains the accepted provider. It uses `ACTION_MAIN` + `CATEGORY_LAUNCHER`, avoids `QUERY_ALL_PACKAGES`, requires no Internet permission, declares local processing and a provisional 500 ms timeout, and returns exact launcher-component actions.

## 8. Contacts Provider — Branch Source

The branch implements **Contacts · On-device** as the second provider source.

- Source authority: Android `ContactsContract` / ContactsProvider.
- Manifest permission: `android.permission.READ_CONTACTS`.
- Processing: `LOCAL`.
- Timeout: provisional 750 ms Development bound.
- Blank query: unsupported; Contacts are not enumerated merely by opening Index.
- Query path: `Contacts.CONTENT_FILTER_URI`.
- Read projection: contact `_ID`, `LOOKUP_KEY`, and `DISPLAY_NAME_PRIMARY` only.
- Phone numbers and email addresses are not requested by this provider slice.
- Result type: `CONTACT` with visible `People · On-device` provenance.
- Action: typed contact-view URI generated from `Contacts.getLookupUri`.
- Action defense-in-depth: MainActivity accepts only `content` URIs from `com.android.contacts` under the contacts path before launching `ACTION_VIEW`.
- Required dispatch authority: Android runtime permission + Privacy Shield evidence + GoreeCloud Identity authorization evidence.

Because production/runtime Privacy Shield and Identity adapters are not accepted or wired here, the provider remains authority-gated. Source implementation is not equivalent to enabled Contacts search.

## 9. Search UI

The Compose surface is generalized from applications-only wording to authorized-source search. It exposes:

- Applications active state;
- Contacts authority-gated state;
- non-animated searching state;
- all provider issues rather than only the first issue;
- neutral authorization-required presentation distinct from provider failure/timeout;
- source-aware result labels;
- explicit no-match/degraded behavior.

Formal Glaze UI 2.1.0 consumer conformance and representative-device accessibility/visual acceptance remain pending.

## 10. Privacy Shield Contract Alignment

The authoritative Privacy Shield authorization request includes requester/resource/operation/purpose/processing-zone/destination/retention; outcomes include `ALLOW`, `DENY`, `ALLOW_WITH_CONSTRAINTS`, and `REQUIRE_USER_DECISION`.

Index does not reinterpret constrained authorization as full permission. Future Privacy Shield adapters must bind a Contacts search request to the correct requester, contact-resource classification/scope, local processing zone, Index destination, and no-retention/session retention policy as applicable and return auditable decision evidence without exposing private query/contact content in ordinary evidence.

## 11. GoreeCloud Identity Boundary

Identity remains authoritative for platform-level identity and GoreeCloud-level authority. Index/provider logic remains responsible for application/provider-specific record access. Authentication must never be treated as blanket authorization to search contacts or other private sources.

The branch defines only a consuming evidence boundary. Actual Identity runtime adapter/API selection and acceptance remain pending.

## 12. Wardveil, Everkeep, and Mesh

Wardveil Security runtime evidence is still required for provider/action trust and security acceptance. Everkeep remains relevant to durable configuration/recovery but not transient query results. Mesh may discover/coordinate first-party provider capabilities later without taking source authority. None is production accepted for Index yet.

## 13. Validation Evidence

Accepted main:

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

Current branch evidence is pending exact-head CI.

## 14. Current Limitations

Not implemented or accepted: actual Privacy Shield/Identity runtime adapters; user-authorized Contacts enablement; files/folders; calendar; media/settings; connected devices; GoreeCloud Search provider; Mesh discovery; extensions/third-party providers; local content index; incremental streaming; full provider capability/health negotiation; formal Glaze UI conformance; representative-device performance/accessibility; production signing/deployment; production acceptance; Stable qualification.

## 15. Next Development Sequence

1. Exact-head validate the Contacts authority/provider branch.
2. Integrate accepted Privacy Shield and Identity adapters without weakening the fail-closed evidence model.
3. Add an explicit user decision/permission flow only when platform authority can be satisfied end to end; do not prompt for Contacts permission prematurely.
4. Perform representative-device Contacts cancellation/timeout/action/accessibility acceptance.
5. Expand one provider authority boundary at a time.

## 16. Production and Stable Gates

GoreeCloud Index must not be called production-ready or Stable until the exact release has applicable source/build, representative-device, permission/isolation, Privacy Shield, Identity, Wardveil, Everkeep, Mesh, Glaze UI, accessibility, performance, signing/deployment/rollback, and reconciled documentation evidence.
