# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It coordinates authorized search providers while preserving source ownership, provenance, and platform authority boundaries.

## Status

**Release lifecycle: Development.** GoreeCloud Index is not Stable or production accepted.

The current accepted source/build baseline is authoritative `main` commit `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Exact-main workflow run `33431294298` passed repository validation, coroutine unit tests, Android lint, Development APK assembly, package/version/label verification, checksum capture, and artifact publication.

Current `0.3.0-dev` development source contains the Contacts/authority work and this branch adds a bounded **Settings · On-device** provider candidate. Branch source is not accepted runtime, release, production, or Stable evidence until exact-head validation and normal merge governance complete.

## Accepted Development Capability

Accepted `main` can:

- Browse and search launcher-visible applications on the current device.
- Dispatch eligible providers concurrently with Kotlin structured concurrency.
- Cancel superseded query work through the Compose query lifecycle.
- Apply bounded provider timeouts and distinguish `FAILED` from `TIMED_OUT` issues.
- Preserve healthy-provider results when another eligible provider fails or times out.
- Rank before provider-scoped deduplication.
- Fail closed through exact provider allowlisting and `localOnly` processing gating.
- Expose the Launcher→Index `com.goreecloud.index.action.SEARCH` handoff.

## Contacts Authority Development Slice

Current source includes a permission-aware Contacts provider without treating source presence as platform acceptance.

- Android ContactsProvider remains authoritative for contact records.
- The provider uses `Contacts.CONTENT_FILTER_URI` and does not enumerate contacts for a blank query.
- Only contact ID/lookup key/display name are read; phone and email fields are not requested by this provider slice.
- Results preserve an on-device source label and a typed contact-view action.
- Contact actions are validated as `content://com.android.contacts/contacts/...` before handoff.
- The provider declares `LOCAL` processing and a provisional 750 ms Development timeout.
- Dispatch requires Android `READ_CONTACTS` permission **and** unconstrained Privacy Shield decision evidence **and** GoreeCloud Identity authorization evidence.
- Missing, denied, user-decision-required, unavailable, or `ALLOW_WITH_CONSTRAINTS` evidence fails closed and produces `AUTHORIZATION_REQUIRED`; the query is not sent to Contacts.
- The application does not fabricate Privacy Shield or Identity approval. Current MainActivity supplies those platform decisions as unavailable, so Contacts remains authority-gated until real adapters are implemented and accepted.

`IndexExecutionContext` remains an application execution gate. The authority-evidence model consumes platform decisions; it does not make Index the Privacy Shield or Identity authority.

## Settings Navigation Development Slice

This branch adds a local provider for a bounded static catalog of Android Settings destinations.

- Ten reviewed destinations cover Settings, Wi-Fi, Bluetooth, Display, Sound, Accessibility, Location, Security, Apps, and Battery Saver.
- The provider is local-only, has a provisional 250 ms timeout, and never runs for a blank query.
- It searches only repository-defined labels and keywords; it does **not** read Android setting values, device configuration values, permission state, accounts, history, or other user data.
- It adds no Android permission, network capability, local cache, analytics, or persistent query history.
- Results use a typed `OpenSystemSetting` action.
- MainActivity revalidates the requested action against the exact static allowlist before Android handoff; arbitrary actions, URLs, data URIs, and extras are not supported.
- Android Settings remains authoritative for every setting and any authentication, permission, confirmation, or modification performed there.

Platform review for this bounded slice: Privacy Shield gains no new data resource because the provider searches static GoreeCloud-owned navigation metadata rather than reading setting state; Wardveil concerns are constrained through the closed action allowlist; Everkeep has no new durable state to recover; Mesh and GoreeCloud Identity are not invoked for this local OS-navigation handoff; Manager gains no administrative authority; and Glaze presentation continues through the existing Index result UI without claiming new Glaze acceptance.

## Product Boundary

**GoreeCloud Index** is the universal search/indexing authority. **GoreeCloud Search** remains authoritative for Internet/web/current-information search. **GoreeCloud Launcher** is an invocation/presentation surface, not a competing universal index. Provider applications, Android Settings, and services remain authoritative for their own resources and operations.

## Platform Requirements

GLAZE UI V1.1 / `1.1.0` is the current published Stable consumer target. Index's separate V1.1 migration remains Development work, and the immutable `1.1.0` CSS graph has a known import-closure defect. A corrected immutable Stable release must be published and explicitly re-pinned/revalidated before current Glaze conformance can be claimed. Runtime acceptance also remains pending for applicable **Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, GoreeCloud Identity, and GoreeCloud Manager** contracts.

## Android Development Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Branch version: `0.3.0-dev`, version code `3`
- Accepted-main version: `0.2.0-dev`, version code `2`
- Minimum API: 26
- Compile API: 37
- Target API: 36

## Accepted Main Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

This is Development source/build evidence only.

## Planned Search Sources

Files/folders, calendar, media, first-party GoreeCloud content, connected devices, extensions, optional third-party services, and Internet results through GoreeCloud Search remain separately gated work. Settings **value/state indexing** is not implemented by the static navigation provider and would require a separate authority/privacy review.

## Documentation

- [Specifications](SPECIFICATIONS.md)
- [Features](FEATURES.md)
- [Capabilities](CAPABILITIES.md)
- [Architecture](ARCHITECTURE.md)
- [Conformance](CONFORMANCE.md)
- [User manual](USER-MANUAL.md)
- [Benefits](BENEFITS.md)
- [Competitive objectives](COMPETITIVE-OBJECTIVES.md)

## License

GNU Affero General Public License v3.0. See [LICENSE](LICENSE).
