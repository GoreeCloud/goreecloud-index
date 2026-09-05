# GoreeCloud Index — Features

## Status Model

**Release lifecycle: Development.** Accepted `main` is `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Current `0.3.0-dev` Contacts/authority source remains Development, and this branch adds a separate bounded Settings-navigation provider candidate. Neither state is production or Stable acceptance.

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

## Contacts / Authority Development Features

Current `0.3.0-dev` source includes:

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

Current MainActivity deliberately supplies Privacy Shield and Identity evidence as unavailable. Therefore Contacts source exists, but Contacts search is not represented as an accepted runtime integration.

## Settings · On-device Development Feature

This branch adds a bounded local Android Settings navigation provider:

- Static ten-destination catalog: Settings, Wi-Fi, Bluetooth, Display, Sound, Accessibility, Location, Security, Apps, and Battery Saver.
- Local processing with a provisional 250 ms timeout.
- Blank queries do not enumerate Settings destinations.
- Search matches only repository-defined destination labels and keywords.
- No Android Settings values, device-configuration values, accounts, permission state, or other private state are read.
- No new permission, network capability, telemetry, cache, or persistent search history is introduced.
- Results use typed `SETTING` results and `OpenSystemSetting` actions.
- MainActivity verifies the action against the exact static allowlist before handoff.
- Arbitrary intent actions, URLs, data URIs, extras, and direct setting mutations are unsupported.
- Android Settings remains authoritative for every setting and every actual configuration change.

The dedicated source validator fails closed if the provider gains setting-value readers, unrestricted package visibility, networking, arbitrary web destinations, or loses the typed/whitelisted action boundary. JVM tests cover local/non-browsing state, blank-query behavior, matching, result bounds, action uniqueness, and arbitrary-action rejection.

## Platform Boundaries

- Privacy Shield remains data-use/consent/purpose/retention authority. The Settings navigation provider adds no new Privacy Shield resource because it searches static GoreeCloud-owned destination metadata and reads no setting state.
- GoreeCloud Identity remains platform identity and GoreeCloud-level authorization authority; the Settings navigation slice creates no new identity decision.
- Wardveil Security remains security/trust evidence authority; the Settings handoff is constrained to a closed local action allowlist.
- Everkeep remains continuity/recovery authority for applicable durable state; this provider creates no durable state.
- GoreeCloud Mesh may coordinate first-party providers later without taking source ownership; this local Android Settings handoff does not require Mesh.
- GoreeCloud Manager does not gain OS configuration authority through Index.
- GoreeCloud Search remains the Internet/web/current-information authority.
- GLAZE UI V1.1 / `1.1.0` is the current published Stable consumer target. The immutable `1.1.0` CSS graph has a known import-closure defect, so Index remains fail-closed for current Glaze conformance until a corrected immutable Stable release is published, explicitly re-pinned, and independently revalidated.

## Accepted Main Evidence

- Source `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow `33431294298`
- APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact `9772740479`
- Artifact digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

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
