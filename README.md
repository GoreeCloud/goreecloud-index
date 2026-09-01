# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It coordinates authorized search providers while preserving source ownership, provenance, and platform authority boundaries.

## Status

**Release lifecycle: Development.** GoreeCloud Index is not Stable or production accepted.

The current accepted source/build baseline is authoritative `main` commit `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Exact-main workflow run `33431294298` passed repository validation, coroutine unit tests, Android lint, Development APK assembly, package/version/label verification, checksum capture, and artifact publication.

This branch advances source to `0.3.0-dev` with a second **Contacts · On-device** provider and a fail-closed authority model. The branch is development source until its own exact-head CI and merge acceptance complete.

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

The current branch adds source for a permission-aware Contacts provider without treating source presence as platform acceptance.

- Android ContactsProvider remains authoritative for contact records.
- The provider uses `Contacts.CONTENT_FILTER_URI` and does not enumerate contacts for a blank query.
- Only contact ID/lookup key/display name are read; phone and email fields are not requested by this provider slice.
- Results preserve an on-device source label and a typed contact-view action.
- Contact actions are validated as `content://com.android.contacts/contacts/...` before handoff.
- The provider declares `LOCAL` processing and a provisional 750 ms Development timeout.
- Dispatch requires Android `READ_CONTACTS` permission **and** unconstrained Privacy Shield decision evidence **and** GoreeCloud Identity authorization evidence.
- Missing, denied, user-decision-required, unavailable, or `ALLOW_WITH_CONSTRAINTS` evidence fails closed and produces `AUTHORIZATION_REQUIRED`; the query is not sent to Contacts.
- The application does not fabricate Privacy Shield or Identity approval. Current MainActivity supplies those platform decisions as unavailable, so Contacts remains authority-gated until real adapters are implemented and accepted.

`IndexExecutionContext` remains an application execution gate. The new authority-evidence model consumes platform decisions; it does not make Index the Privacy Shield or Identity authority.

## Product Boundary

**GoreeCloud Index** is the universal search/indexing authority. **GoreeCloud Search** remains authoritative for Internet/web/current-information search. **GoreeCloud Launcher** is an invocation/presentation surface, not a competing universal index. Provider applications and services remain authoritative for their own resources.

## Platform Requirements

The UI targets **Glaze UI 2.1.0**. Formal consumer conformance remains pending. Runtime acceptance also remains pending for **Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, and GoreeCloud Identity**.

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

Files/folders, calendar, media/settings, first-party GoreeCloud content, connected devices, extensions, optional third-party services, and Internet results through GoreeCloud Search remain separately gated work.

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
