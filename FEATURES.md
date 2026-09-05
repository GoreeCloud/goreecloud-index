# GoreeCloud Index — Features

## Status Model

**Release lifecycle: Development.** Accepted `main` is `cc3cc21d6e11dad026253c3371c3b67663d3b726`. The current `0.3.0-dev` Contacts/authority work is branch source pending exact-head validation and merge acceptance. Neither state is production or Stable acceptance.

## Accepted Current Features

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

## Contacts / Authority Branch Features

The `0.3.0-dev` branch adds source for:

- `IndexAuthorityRequirement`: Android runtime permission, Privacy Shield, and GoreeCloud Identity.
- `IndexAuthorityEvidence` outcomes including `ALLOW`, `ALLOW_WITH_CONSTRAINTS`, `DENY`, `REQUIRE_USER_DECISION`, and `UNAVAILABLE`.
- Fail-closed dispatch: only unconstrained referenced `ALLOW` satisfies Privacy Shield/Identity requirements.
- `AUTHORIZATION_REQUIRED` provider state when an allowlisted/applicable provider lacks required authority.
- Blank-query applicability so private providers can opt out of browse/enumeration.
- **Contacts · On-device** provider using Android ContactsProvider filtering.
- Contacts projection limited to contact ID, lookup key, and display name; no phone/email field reads in this slice.
- Typed contact-view actions with URI validation before Android handoff.
- Contacts local processing, 750 ms provisional timeout, and no blank-query enumeration.
- UI disclosure that Applications are active while Contacts remain authority-gated.
- Neutral authorization-required state distinct from operational provider failure.

Current MainActivity deliberately supplies Privacy Shield and Identity evidence as unavailable. Therefore Contacts source exists, but Contacts search is not enabled or represented as an accepted runtime integration.

## Platform Boundaries

- Privacy Shield remains data-use/consent/purpose/retention authority.
- GoreeCloud Identity remains platform identity and GoreeCloud-level authorization authority; authentication is not blanket record access.
- Wardveil Security remains security/trust evidence authority.
- Everkeep remains continuity/recovery authority for applicable durable state.
- GoreeCloud Mesh may coordinate first-party providers later without taking source ownership.
- GoreeCloud Search remains the Internet/web/current-information authority.
- GLAZE UI V1.1 / `1.1.0` is the current published Stable consumer target, and this branch contains a bounded V1.1 source mapping. The published `1.1.0` CSS graph has a known import-closure defect, so Index remains fail-closed for current Glaze conformance until a corrected immutable Stable release is published, explicitly re-pinned, and independently revalidated.

## Accepted Main Evidence

- Source `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow `33431294298`
- APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact `9772740479`
- Artifact digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

## Partial / Not Yet Accepted

- Contacts runtime enablement and user permission flow.
- Actual Privacy Shield and Identity runtime adapters/evidence.
- Wardveil/Everkeep/Mesh runtime integration.
- Representative-device Contacts/application performance, cancellation, action, accessibility, and Glaze UI acceptance.
- Files/folders, calendar, media/settings, first-party service, connected-device, Search, extension, or third-party providers.
- Local content indexing and incremental/streaming result delivery.
- Production signing/deployment or Stable qualification.

## Deprecated or Removed Features

None.
