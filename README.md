# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It coordinates authorized search providers while preserving source ownership, provenance, privacy boundaries, and platform authority.

## Status

**Release lifecycle: Development.** GoreeCloud Index is not Stable or production accepted.

The current accepted source/build baseline is authoritative `main` commit `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Exact-main workflow run `33431294298` passed repository validation, coroutine unit tests, Android lint, Development APK assembly, package/version/label verification, checksum capture, and artifact publication.

Current `0.3.0-dev` development work expands provider coverage and authority handling. Branch source is not accepted runtime, release, production, or Stable evidence until exact-head validation and normal merge governance complete.

## Accepted Development Capability

Accepted `main` can:

- browse and search launcher-visible applications on the current device;
- dispatch eligible providers concurrently with Kotlin structured concurrency;
- cancel superseded query work through the Compose query lifecycle;
- apply bounded provider timeouts and distinguish `FAILED` from `TIMED_OUT` issues;
- preserve healthy-provider results when another eligible provider fails or times out;
- rank before provider-scoped deduplication;
- fail closed through exact provider allowlisting and local/remote processing gates;
- expose the Launcher→Index `com.goreecloud.index.action.SEARCH` handoff.

## Product Boundary

- **GoreeCloud Index** is the universal search/indexing authority.
- **GoreeCloud Search** remains authoritative for Internet/web/current-information search.
- **GoreeCloud Browser** remains authoritative for URL navigation, tabs, page lifecycle, and executable web-result navigation.
- **GoreeCloud Launcher** is an invocation/presentation surface, not a competing universal index.
- Provider applications, Android platform services, and GoreeCloud services remain authoritative for their own records and operations.

Index must not collapse these roles into a single global search authority.

## GoreeCloud Search Provider

The source tree contains a dedicated GoreeCloud Search provider adapter for remote Internet results.

The initial interoperable contract uses:

- capability ID `search.query`;
- contract version `1`;
- endpoint `/api/v1/search`;
- a minimized request containing only normalized query, category, and bounded result limit;
- independent Index-side validation of executable HTTP(S) destinations.

Local-only execution must not preflight or call GoreeCloud Search. Remote execution requires explicit provider eligibility and applicable Privacy Shield evidence.

Development builds may use explicitly non-production Search capability evidence only as Development evidence. A Stable/production Index path must require Search capability evidence that is explicitly production accepted.

See [`docs/SEARCH_INTEGRATION.md`](docs/SEARCH_INTEGRATION.md).

## Contacts Authority Development Slice

Current source includes a permission-aware Contacts provider without treating source presence as platform acceptance.

- Android ContactsProvider remains authoritative for contact records.
- The provider uses `Contacts.CONTENT_FILTER_URI` and does not enumerate contacts for a blank query.
- Only contact ID/lookup key/display name are read; phone and email fields are not requested by this provider slice.
- Results preserve an on-device source label and a typed contact-view action.
- Contact actions are validated as `content://com.android.contacts/contacts/...` before handoff.
- The provider declares `LOCAL` processing and a provisional 750 ms Development timeout.
- Dispatch requires Android `READ_CONTACTS` permission plus accepted Privacy Shield decision evidence and GoreeCloud Identity authorization evidence.
- Missing, denied, user-decision-required, unavailable, or unenforceable constrained evidence fails closed and produces `AUTHORIZATION_REQUIRED`; the query is not sent to Contacts.
- The application must not fabricate Privacy Shield or Identity approval.

`IndexExecutionContext` remains an application execution gate. The authority-evidence model consumes platform decisions; it does not make Index the Privacy Shield or Identity authority.

## Settings Navigation Development Slice

Development source includes a local provider for a bounded static catalog of Android Settings destinations.

- Reviewed destinations cover common Settings areas such as Wi-Fi, Bluetooth, Display, Sound, Accessibility, Location, Security, Apps, and Battery Saver.
- The provider is local-only and never runs for a blank query.
- It searches only repository-defined labels and keywords; it does not read Android setting values, device configuration values, permission state, accounts, history, or other user data.
- It adds no analytics or persistent query history.
- Results use a typed `OpenSystemSetting` action.
- Android Settings remains authoritative for every setting and any authentication, permission, confirmation, or modification performed there.

Any future Settings value/state indexing requires a separate authority and privacy review.

## Privacy and Authority Model

Index consumes authority; it does not manufacture it.

- Privacy Shield controls applicable purpose, minimization, destination, retention, and local/remote processing decisions.
- GoreeCloud Identity controls applicable identity and authorization evidence.
- Wardveil Security controls applicable trust/security evidence.
- Everkeep controls continuity requirements for durable Index state.
- GoreeCloud Mesh may coordinate first-party provider discovery without taking source authority.

A provider can run only when its declared requirements are satisfied by enforceable evidence available to the current Index execution context.

## Glaze UI Requirement

The current official Stable consumer target published by `GoreeCloud/goreecloud-glaze-ui` is **Glaze UI V1.4 / `1.4.0`**.

The previous Index documentation contained inconsistent historical targets (`1.1.0` and `2.1.0`). Those values are not current release authority and must not be used as present conformance evidence.

Index is therefore **migration-required** until Index-owned surfaces have been mapped to V1.4 and repository-local source, rendered/native, accessibility, and representative-device evidence has been accepted.

Glaze Stable status does not auto-promote Index. A superseded design baseline cannot satisfy Index Stable readiness.

## Android Development Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Branch version family: `0.3.0-dev`
- Accepted-main version: `0.2.0-dev`, version code `2`
- Minimum API: 26
- Compile API: 37
- Target API: 36

Version and package evidence must be read from the exact revision being validated; branch documentation does not itself authorize a release.

## Accepted Main Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

This is Development source/build evidence only.

## Planned Search Sources

Planned provider families include files/folders, calendar, media, first-party GoreeCloud content, connected devices, extensions, optional third-party services, and Internet results through GoreeCloud Search.

Every new provider must define:

- stable provider identity;
- source authority;
- processing location;
- permissions and authority requirements;
- blank-query behavior;
- bounded timeout/cancellation behavior;
- result/action validation;
- privacy minimization and retention behavior;
- availability and degradation semantics;
- recovery requirements for any durable state.

## Documentation

- [Specifications](SPECIFICATIONS.md)
- [Features](FEATURES.md)
- [Capabilities](CAPABILITIES.md)
- [Architecture](ARCHITECTURE.md)
- [Search integration](docs/SEARCH_INTEGRATION.md)
- [Conformance](CONFORMANCE.md)
- [User manual](USER-MANUAL.md)
- [Benefits](BENEFITS.md)
- [Competitive objectives](COMPETITIVE-OBJECTIVES.md)

## License

GNU Affero General Public License v3.0. See [LICENSE](LICENSE).
