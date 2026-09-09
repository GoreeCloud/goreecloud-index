# GoreeCloud Index — Features

## Status Model

**Release lifecycle: Development.** Current repository `main` is `9f3aaa9543a8a8351fa2d4713481bccb8199069d`. The older accepted APK/build evidence remains bound to exact source `cc3cc21d6e11dad026253c3371c3b67663d3b726`, workflow `33431294298`, APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, and artifact `9772740479`.

Current `0.3.0-dev` source contains the Applications provider, Contacts authority slice, and bounded Settings-navigation provider. This V1.3 reconciliation branch is newer Development source and does not inherit production, Stable, or artifact acceptance from older evidence.

## Current Source Features

### Native Android Foundation

- Original GoreeCloud-owned Kotlin/Jetpack Compose application.
- Production package `com.goreecloud.index`; Development package `com.goreecloud.index.dev`.
- Launcher→Index `com.goreecloud.index.action.SEARCH` contract.
- API 26 minimum, compile API 37, target API 36.

### Asynchronous Search Core

- Provider-neutral query/result/action types.
- Provider identity, processing-location, timeout, and suspendable search contracts.
- Exact provider allowlisting and local-only gating.
- Structured concurrent provider dispatch.
- Superseded-query cancellation.
- Bounded provider timeouts.
- Sanitized `FAILED` and `TIMED_OUT` issues.
- Healthy-result preservation across sibling provider failures/timeouts.
- Deterministic ranking before provider-scoped deduplication.
- 1–100 final result bounds.

### Applications · On-device

- Scoped launcher-visible app discovery using `ACTION_MAIN` + `CATEGORY_LAUNCHER`.
- No unrestricted `QUERY_ALL_PACKAGES` and no Android Internet permission.
- Local processing, provisional 500 ms timeout, label/package matching, and exact component launch actions.

## Contacts / Authority — Current Source

Current source includes:

- `IndexAuthorityRequirement`: Android runtime permission, Privacy Shield, and GoreeCloud Identity.
- `IndexAuthorityEvidence` outcomes including `ALLOW`, `ALLOW_WITH_CONSTRAINTS`, `DENY`, `REQUIRE_USER_DECISION`, and `UNAVAILABLE`.
- Fail-closed dispatch: only unconstrained referenced `ALLOW` satisfies Privacy Shield/Identity requirements.
- `AUTHORIZATION_REQUIRED` provider state when an allowlisted/applicable provider lacks required authority.
- Blank-query applicability so private providers can opt out of browse/enumeration.
- **Contacts · On-device** provider using Android ContactsProvider filtering.
- Contacts projection limited to contact ID, lookup key, and display name; no phone/email field reads in this slice.
- Typed contact-view actions with URI validation before Android handoff.
- Contacts local processing, 750 ms provisional timeout, and no blank-query enumeration.
- UI disclosure that Contacts remains authority-gated.
- Neutral authorization-required state distinct from operational provider failure.

The current unavailable platform-authority gateway deliberately keeps Contacts non-dispatchable until accepted Privacy Shield and GoreeCloud Identity evidence can be supplied. Source presence is not represented as accepted Contacts runtime integration.

## Settings · On-device — Current Source

Current source includes a bounded local Android Settings navigation provider:

- Static reviewed destination catalog for Android Settings surfaces.
- Local processing with a provisional bounded timeout.
- Blank queries do not enumerate Settings destinations.
- Search matches only repository-defined destination labels and keywords.
- No Android Settings values, device-configuration values, accounts, permission state, history, or other private state are read.
- No new permission, network capability, telemetry, cache, or persistent search history is introduced.
- Results use typed Settings navigation actions.
- MainActivity verifies the action against the exact static allowlist before handoff.
- Arbitrary intent actions, URLs, data URIs, extras, and direct setting mutations are unsupported.
- Android Settings remains authoritative for every setting and every actual configuration change.

A dedicated source validator and JVM tests guard the bounded provider/action contract. Representative-device/OEM behavior remains an acceptance gate.

## GLAZE UI V1.3 / 1.3.0 Candidate

This branch reconciles Index presentation source with the live current Stable Glaze authority without changing provider truth or authorization.

- Current Stable target: **GLAZE UI V1.3 / `1.3.0` — Adaptive Resonance**.
- Exact Stable source integration anchor: `fc7cc91d2eace8da2371371c2855c24cbcb326a1`.
- Stable aggregate web/runtime identifiers are recorded for provenance; Index remains a native Compose consumer.
- Rollback baseline: `1.2.0`.
- Repository-local native contract replaces generic Android dynamic-color sampling with deterministic GoreeCloud-owned Light, Dark, and Deep Dark schemes.
- Native theme uses neutral-first Deep Teal/Soft Amber atmosphere and 16/24/32 dp optical geometry.
- 48dp ordinary and 56dp Touch Assistance target floors are encoded in the local contract.
- Presentation colors do not create or imply provider authorization, Privacy Shield state, Identity state, Wardveil findings, Everkeep state, Mesh state, or Search provenance.
- Machine-readable Platform declaration remains Development/nonconformant and `applicable-migration-required` until independent acceptance completes.

Exact-head Android/Platform Contract validation, representative rendering, TalkBack/accessibility, 200% text/reflow, RTL/localization, Reduced Motion/Transparency, contrast/high-contrast, form-factor, performance, and production acceptance remain pending until observed.

## Platform Boundaries

- Privacy Shield remains data-use/consent/purpose/retention authority.
- GoreeCloud Identity remains platform identity and GoreeCloud-level authorization authority.
- Wardveil Security remains security/trust evidence authority.
- Everkeep remains continuity/recovery authority for applicable durable state.
- GoreeCloud Mesh may coordinate first-party providers later without taking source ownership.
- GoreeCloud Manager does not gain OS configuration authority through Index.
- GoreeCloud Search remains the Internet/web/current-information authority.
- GLAZE UI governs presentation and interaction contracts, not underlying search/provider truth.

## Accepted Historical Main Evidence

- Source `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow `33431294298`
- APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact `9772740479`
- Artifact digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

This evidence is retained for its exact source only and is not automatically evidence for current `main` or the V1.3 candidate.

## Partial / Not Yet Accepted

- Contacts runtime enablement and user permission flow.
- Actual Privacy Shield and Identity runtime adapters/evidence for private providers.
- Wardveil/Everkeep/Mesh/Manager runtime integration where applicable.
- Representative-device Settings action availability, OEM behavior, navigation, accessibility, and performance acceptance.
- Representative-device Contacts/application performance, cancellation, action, accessibility, and Glaze UI acceptance.
- Files/folders, calendar, media, first-party service, connected-device, Search, extension, or third-party providers.
- Any indexing or reading of actual Android setting values/state.
- Local content indexing and incremental/streaming result delivery.
- Production signing/deployment or Stable qualification.

## Deprecated or Removed Features

None.
