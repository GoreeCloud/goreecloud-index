# GoreeCloud Index — User Manual

## Current Development Scope

**Release lifecycle: Development.** GoreeCloud Index is not Stable or production accepted.

Accepted `main` (`cc3cc21d6e11dad026253c3371c3b67663d3b726`) provides the `0.2.0-dev` application-search/asynchronous-provider baseline. The current branch advances to `0.3.0-dev` with Contacts provider source and explicit authority gating, pending exact-head CI and merge acceptance.

## Opening Index

Open **GoreeCloud Index Dev** from Android, or invoke `com.goreecloud.index.action.SEARCH` with optional `com.goreecloud.index.extra.QUERY`.

## Searching Available Sources

The search field now uses **Search this device**. Applications remain the active accepted source. When the field is blank, Index may browse launcher applications; Contacts does not enumerate records on blank input.

For nonblank queries, Index evaluates each applicable provider before dispatch. A provider with incomplete permission or platform authority is not sent the query.

## Contacts Development State

The branch contains an Android Contacts provider, but it is intentionally **not enabled yet**.

Contacts requires all three gates:

1. Android `READ_CONTACTS` runtime permission.
2. An unconstrained Privacy Shield `ALLOW` decision with evidence reference.
3. An unconstrained GoreeCloud Identity authorization decision/evidence reference.

Current MainActivity does not fabricate either platform decision; it supplies Privacy Shield and Identity as unavailable. As a result, a nonblank query can show **Contacts not enabled**, and no ContactsProvider query is executed.

`ALLOW_WITH_CONSTRAINTS` also remains blocked until Index can enforce the returned obligations. This prevents a constrained platform decision from being silently treated as unrestricted permission.

No Android contacts permission prompt is automatically shown in this branch because requesting sensitive permission before the full platform authority path can succeed would be premature.

## Contact Data Boundary

When the provider is eventually authorized, this source implementation:

- searches through Android ContactsProvider's filter URI;
- reads only contact ID, lookup key, and display name;
- does not request phone-number or email columns;
- returns `People · On-device` provenance;
- uses a typed Android contact-view handoff rather than copying contact details into Index-owned storage;
- rejects malformed/non-Contacts content actions before launch.

The provider does not maintain a contact cache or persistent search history.

## Search States

- **Searching:** applicable authorized providers are executing.
- **Applications browse:** blank query can show launcher-visible apps.
- **Contacts not enabled:** Contacts was applicable but required authority evidence was incomplete; no contacts query was sent.
- **Provider temporarily unavailable:** an invoked provider failed.
- **Provider took too long:** an invoked provider exceeded its timeout.
- **No matches:** available providers completed without a match.
- **Action failure:** Android could not open the chosen app/contact action.

No state silently activates remote fallback.

## Provider Timeouts

Applications: provisional 500 ms Development bound. Contacts: provisional 750 ms Development bound. The engine caps provider declarations at five seconds. These are engineering bounds, not representative-device SLAs.

## Privacy and Security

Applications request no Android Internet permission and avoid unrestricted package enumeration. Contacts declares `READ_CONTACTS`, but provider dispatch still requires the separate platform authority gates above.

Privacy Shield, GoreeCloud Identity, Wardveil Security, Everkeep, and Mesh runtime integrations are not accepted merely because source contracts exist.

## Development Package

- Application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Branch version: `0.3.0-dev`, code `3`
- Accepted-main version: `0.2.0-dev`, code `2`

Accepted-main source `cc3cc21d6e11dad026253c3371c3b67663d3b726` passed workflow `33431294298`; APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`; artifact `9772740479`; artifact digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`.

## Known Limitations

Actual Contacts enablement/user permission flow, accepted Privacy Shield/Identity adapters, files/folders, calendar, media/settings, GoreeCloud service sources, connected devices, extensions, third-party providers, Internet results through Search, local content indexing, incremental streaming, formal Glaze UI 2.1.0 conformance, representative-device accessibility/performance, production signing/deployment, and Stable qualification remain pending.
