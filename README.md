# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It coordinates authorized search providers while preserving source ownership, provenance, least privilege, and platform authority boundaries.

## Status

**Release lifecycle: Development.** GoreeCloud Index is not Stable or production accepted.

The current accepted source/build baseline remains `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Exact-main workflow run `33431294298` passed repository validation, coroutine unit tests, Android lint, Development APK assembly, package/version/label verification, checksum capture, and artifact publication. The accepted APK SHA-256 is `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, and accepted artifact ID `9772740479` records that Development build evidence.

The authoritative repository `main` used to cut this branch is `6f292862ab7cb2b402cdc64868f02758beb3c2be`, which includes later approved product-identity integration beyond that accepted APK baseline. This branch is newer Development source until its own exact-head CI and review complete; neither later mainline source nor this branch inherits the accepted APK evidence automatically.

## Current source capability

Index currently includes:

- launcher-visible Applications · On-device search;
- a permission-aware Contacts · On-device provider source slice;
- concurrent eligible-provider dispatch with structured concurrency;
- cancellation of superseded query work;
- bounded provider timeouts with explicit failed/timed-out issues;
- healthy-provider result preservation when another provider fails;
- provider-scoped deduplication and ranking;
- exact provider allowlisting and `localOnly` execution gating;
- Launcher→Index `com.goreecloud.index.action.SEARCH` handoff;
- fail-closed Privacy Shield / GoreeCloud Identity evidence boundaries for sensitive providers.

The Contacts provider does not become dispatchable merely because source exists. Current platform authority adapters still fail closed where authoritative Privacy Shield/Identity decisions are unavailable.

## Product boundary

**GoreeCloud Index** is the universal local/authorized indexing and federated provider authority. **GoreeCloud Search** remains authoritative for Internet/web/current-information search. **GoreeCloud Launcher** is an invocation/presentation surface, not a competing universal index. Provider applications and services remain authoritative for their own resources.

## GLAZE UI V1.1 migration

The sole current design-system consumer target is **GLAZE UI V1.1 (`1.1.0`)**. This branch adds a repository-local Index contract and replaces the generic dynamic Material color baseline with an Index-owned V1.1 Compose theme.

Current V1.1 authority recorded by Index:

- Stable tag `v1.1.0`;
- Stable release revision `15cc76d2bcd4065552dc31c77145b63f34d9e7b2`;
- approved visual source `8ea1f789bbabf943c3359514dc1506b24fa3c51b`;
- optical contract `contracts/v1.1/optical-refinement.json`.

The theme applies the canonical neutral-first **Deep Teal + Soft Amber** atmosphere, V1.1 16/24/32 optical geometry, explicit Light/Dark/Deep Dark schemes, and preserved 48dp ordinary / 56dp Touch Assistance target contracts. It does not enable nested backdrop blur, require Environmental Color Memory, transmit content for color derivation, or use atmosphere as security/privacy/authorization meaning.

System dark mode currently selects the Dark scheme. The explicit Deep Dark scheme is implemented as a callable theme mode but is not yet exposed through a user-facing appearance preference; claiming a complete Deep Dark preference workflow would therefore be premature.

This migration is source implementation evidence only. Exact-revision Android rendering, 200% text, TalkBack/accessibility, RTL/localization, contrast/high-contrast behavior, Reduced Motion, Reduced Transparency, form-factor, performance, Human Visual Excellence, and production acceptance remain separate gates.

## Android Development identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Version: `0.3.0-dev`, version code `3`
- Minimum API: 26
- Compile API: 37
- Target API: 36

## Contacts authority slice

- Android ContactsProvider remains authoritative for contact records.
- The provider uses `Contacts.CONTENT_FILTER_URI` and does not enumerate contacts for a blank query.
- Only contact ID/lookup key/display name are read in the current slice; phone/email fields are not requested.
- Results preserve an on-device source label and a typed contact-view action.
- Contact actions are validated as `content://com.android.contacts/contacts/...` before handoff.
- Dispatch requires Android `READ_CONTACTS`, an unconstrained Privacy Shield allow decision, and GoreeCloud Identity authorization evidence.
- Missing/denied/constrained/unavailable evidence fails closed and the query is not sent to Contacts.

`IndexExecutionContext` remains an application execution gate. It consumes platform decisions and does not make Index the Privacy Shield or Identity authority.

## Accepted Main evidence

- Accepted source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`

This remains Development source/build evidence only and is not evidence for the later V1.1 migration branch.

## Remaining provider work

Files/folders, calendar, media, settings, first-party GoreeCloud content, connected devices, extensions, optional approved third-party services, and Internet results through GoreeCloud Search remain separately gated planned work. Production multi-user authorization, accepted platform adapters, observability, recovery, and production deployment evidence also remain incomplete.

## Documentation

- [Specifications](SPECIFICATIONS.md)
- [Features](FEATURES.md)
- [Capabilities](CAPABILITIES.md)
- [Architecture](ARCHITECTURE.md)
- [Conformance](CONFORMANCE.md)
- [User manual](USER-MANUAL.md)
- [Benefits](BENEFITS.md)
- [Competitive objectives](COMPETITIVE-OBJECTIVES.md)
- [GLAZE UI adoption](docs/GLAZE_UI_ADOPTION.md)

## License

GNU Affero General Public License v3.0. See [LICENSE](LICENSE).
