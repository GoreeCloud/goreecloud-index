# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It coordinates authorized search providers while preserving source ownership, provenance, least privilege, and platform authority boundaries.

## Status

**Release lifecycle: Development.** GoreeCloud Index is not Stable or production accepted.

Authoritative `main` at the start of this candidate is `9f3aaa9543a8a8351fa2d4713481bccb8199069d`, which includes the current `0.3.0-dev` provider/authority source and Platform Contract v0.2 declaration. The latest separately accepted Development APK evidence remains bound to older source `cc3cc21d6e11dad026253c3371c3b67663d3b726` and workflow `33431294298`; that artifact evidence is not automatically upgraded to later main or to this branch.

This Draft branch adds a current **GLAZE UI V1.3 / `1.3.0`** native source mapping and current Platform Contract reconciliation. It remains Development source until its own exact-head CI and normal review/integration complete.

## Current Development Capability

Current main source includes:

- launcher-visible **Applications · On-device** search;
- a permission-aware **Contacts · On-device** provider source slice;
- a bounded static **Settings · On-device** navigation provider;
- concurrent eligible-provider dispatch with Kotlin structured concurrency;
- cancellation of superseded query work;
- bounded provider timeouts with explicit failed/timed-out issues;
- healthy-provider result preservation when another provider fails;
- provider-scoped deduplication and ranking;
- exact provider allowlisting and `localOnly` execution gating;
- the Launcher→Index `com.goreecloud.index.action.SEARCH` handoff;
- fail-closed Privacy Shield / GoreeCloud Identity evidence boundaries for sensitive providers.

Source presence does not establish runtime platform acceptance. Current authority adapters continue to fail closed when authoritative Privacy Shield or GoreeCloud Identity decisions are unavailable.

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
- Index does not fabricate Privacy Shield or Identity approval. Current runtime wiring leaves those producer-authoritative decisions unavailable, so Contacts remains authority-gated until real adapters are implemented and accepted.

`IndexExecutionContext` remains an application execution gate. It consumes platform decisions; it does not make Index the Privacy Shield or Identity authority.

## Settings Navigation Development Slice

Current main source includes a local provider for a bounded static catalog of Android Settings destinations.

- Ten reviewed destinations cover Settings, Wi-Fi, Bluetooth, Display, Sound, Accessibility, Location, Security, Apps, and Battery Saver.
- The provider is local-only, has a provisional 250 ms timeout, and never runs for a blank query.
- It searches only repository-defined labels and keywords; it does **not** read Android setting values, device configuration values, permission state, accounts, history, or other user data.
- It adds no Android permission, network capability, local cache, analytics, or persistent query history.
- Results use a typed `OpenSystemSetting` action.
- MainActivity revalidates the requested action against the exact static allowlist before Android handoff; arbitrary actions, URLs, data URIs, and extras are not supported.
- Android Settings remains authoritative for every setting and for any authentication, permission, confirmation, or modification performed there.

This bounded provider does not create new Privacy Shield data authority, Wardveil trust, Everkeep durable state, Mesh authority, Identity authority, Manager authority, or Glaze acceptance.

## Product Boundary

**GoreeCloud Index** is the universal local/authorized indexing and federated provider authority. **GoreeCloud Search** remains authoritative for Internet/web/current-information search. **GoreeCloud Launcher** is an invocation/presentation surface, not a competing universal index. Provider applications, Android Settings, and services remain authoritative for their own resources and operations.

## GLAZE UI V1.3 Development Mapping

This candidate maps the native Compose theme to current Stable **GLAZE UI V1.3 — Adaptive Resonance (`1.3.0`)** using exact Stable source integration anchor `fc7cc91d2eace8da2371371c2855c24cbcb326a1` and rollback baseline `1.2.0`.

The repository-local mapping replaces the generic dynamic Material color baseline with deterministic Index-owned Light, Dark, and Deep Dark source schemes, neutral-first Deep Teal / restrained Soft Amber atmosphere, 16/24/32 dp shape hierarchy, and 48/56 dp interaction-target floors. It keeps nested backdrop blur disabled and does not require remote color derivation.

Historical manifest text describing pre-reset `2.1.0` targeting and Draft PR #16's V1.1 theme remain historical Development provenance only. They are not current consumer authority.

This source mapping is **not** whole-application Glaze conformance. Rendered/native visual review, TalkBack/accessibility, 200% text/reflow, RTL/localization, Reduced Motion/Transparency, contrast/high-contrast behavior, adaptive/form-factor and representative-device acceptance, performance, Human Visual Excellence, rollback, signing/distribution, and production acceptance remain separate gates.

Runtime acceptance also remains pending for applicable **Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, GoreeCloud Identity, and GoreeCloud Manager** contracts.

## Android Development Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Current source version: `0.3.0-dev`, version code `3`
- Accepted APK baseline version: `0.2.0-dev`, version code `2`
- Minimum API: 26
- Compile API: 37
- Target API: 36

## Accepted Development APK Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

This evidence belongs to that exact older Development source and is not evidence for later main or this V1.3 candidate.

## Planned Search Sources

Files/folders, calendar, media, first-party GoreeCloud content, connected devices, extensions, optional approved third-party services, and Internet results through GoreeCloud Search remain separately gated work. Settings **value/state indexing** is not implemented by the static navigation provider and would require a separate authority/privacy review.

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
