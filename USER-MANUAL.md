# GoreeCloud Index — User Manual

## Current Development Scope

**Release lifecycle: Development.** GoreeCloud Index is not Stable or production accepted.

Current repository `main` is `9f3aaa9543a8a8351fa2d4713481bccb8199069d` and contains the current `0.3.0-dev` Applications, Contacts-authority, and bounded Settings-navigation source. The older accepted APK/build evidence remains bound to exact source `cc3cc21d6e11dad026253c3371c3b67663d3b726`, workflow `33431294298`, APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, and artifact `9772740479`. Current source and the V1.3 candidate require independent acceptance.

## Opening Index

Open **GoreeCloud Index Dev** from Android, or invoke `com.goreecloud.index.action.SEARCH` with optional `com.goreecloud.index.extra.QUERY`.

## Searching Available Sources

The search field searches authorized device sources. Applications can be browsed when the field is blank. Contacts does not enumerate records on blank input.

For nonblank queries, Index evaluates each applicable provider before dispatch. A provider with incomplete permission or platform authority is not sent the query.

## Contacts Current Development State

Current source contains an Android Contacts provider, but it is intentionally **authority-gated and not accepted as enabled runtime functionality**.

Contacts requires all three gates:

1. Android `READ_CONTACTS` runtime permission.
2. An unconstrained Privacy Shield `ALLOW` decision with evidence reference.
3. An unconstrained GoreeCloud Identity authorization decision/evidence reference.

The current unavailable platform-authority gateway does not fabricate either platform decision. As a result, a nonblank query can show a Contacts authorization-required state and no ContactsProvider query is executed.

`ALLOW_WITH_CONSTRAINTS` also remains blocked until Index can enforce the returned obligations. This prevents a constrained platform decision from being silently treated as unrestricted permission.

No Android contacts permission prompt is represented as a complete authorization flow until the end-to-end platform authority path is accepted.

## Contact Data Boundary

When the provider is eventually authorized, the current source implementation:

- searches through Android ContactsProvider's filter URI;
- reads only contact ID, lookup key, and display name;
- does not request phone-number or email columns;
- returns on-device provenance;
- uses a typed Android contact-view handoff rather than copying contact details into Index-owned storage;
- rejects malformed/non-Contacts content actions before launch.

The provider does not maintain a contact cache or persistent search history.

## Settings Navigation Current Development State

Current source also includes a bounded Settings-navigation provider.

- It searches only a repository-defined static list of Android Settings destinations.
- It does not read Android setting values, device configuration values, account state, permission state, or history.
- It uses local processing only and does not add network access, telemetry, cache, or durable query state.
- Results use typed Settings actions that are revalidated against a closed allowlist before Android handoff.
- Arbitrary URLs, extras, dynamic intent actions, and direct setting mutations are not supported.
- Android Settings remains authoritative for all configuration truth and actual changes.

Representative-device and OEM-specific destination availability remain unaccepted.

## Search States

- **Searching:** applicable authorized providers are executing.
- **Applications browse:** blank query can show launcher-visible apps.
- **Authorization required:** a private provider was applicable but required authority evidence was incomplete; the provider was not queried.
- **Provider temporarily unavailable:** an invoked provider failed.
- **Provider took too long:** an invoked provider exceeded its timeout.
- **No matches:** available providers completed without a match.
- **Action failure:** Android could not open the chosen app/contact/Settings action.

No state silently activates remote fallback.

## Provider Timeouts

Applications: provisional 500 ms Development bound. Contacts: provisional 750 ms Development bound. Settings uses a bounded Development timeout defined by its provider contract. These are engineering bounds, not representative-device SLAs.

## GLAZE UI V1.3 Candidate

The current shared design-system target is **GLAZE UI V1.3 / `1.3.0` — Adaptive Resonance**. The current candidate replaces generic Android dynamic-color sampling with deterministic GoreeCloud-owned Light, Dark, and Deep Dark Compose schemes and records the exact Stable source integration anchor `fc7cc91d2eace8da2371371c2855c24cbcb326a1` plus rollback baseline `1.2.0`.

This source mapping does not itself establish rendered/native accessibility, representative-device, performance, Release Candidate, Stable, or production acceptance.

## Privacy and Security

Applications request no Android Internet permission and avoid unrestricted package enumeration. Contacts declares `READ_CONTACTS`, but provider dispatch still requires the separate platform authority gates above. Settings navigation reads no setting values and remains constrained to a closed action allowlist.

Privacy Shield, GoreeCloud Identity, Wardveil Security, Everkeep, Mesh, and Manager runtime integrations are not accepted merely because source contracts exist.

## Development Package

- Application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Current source version: `0.3.0-dev`, code `3`

Historical accepted APK/build evidence: source `cc3cc21d6e11dad026253c3371c3b67663d3b726`; workflow `33431294298`; APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`; artifact `9772740479`; artifact digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`.

## Known Limitations

Actual Contacts enablement/user permission flow, accepted Privacy Shield/Identity adapters, files/folders, calendar, media, GoreeCloud service sources, connected devices, extensions, third-party providers, Internet results through Search, local content indexing, incremental streaming, Android setting-value indexing, formal Glaze UI application conformance, representative-device accessibility/performance, production signing/deployment, and Stable qualification remain pending.
