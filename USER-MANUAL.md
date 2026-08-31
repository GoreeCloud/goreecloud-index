# GoreeCloud Index — User Manual

## Current Development Scope

**Release lifecycle: Development.** GoreeCloud Index is **not Stable or production accepted**. The current Android application implements one bounded search provider: launcher-visible applications on the current Android device.

The `0.2.0-dev` branch adds an asynchronous provider-runtime foundation, cancellation, provider timeouts, and local-only provider eligibility. Those changes require exact-candidate CI and merge acceptance before they become accepted-main evidence.

Files, contacts, calendar, GoreeCloud service content, connected devices, extensions, optional third-party services, and Internet results are not available providers yet.

## Opening Index

Open **GoreeCloud Index Dev** from the Android launcher. GoreeCloud Launcher may also invoke Index through the first-party search-entry action `com.goreecloud.index.action.SEARCH` and may include an initial query in `com.goreecloud.index.extra.QUERY`.

The Launcher→Index contract exists in accepted source/build; integrated representative-device acceptance remains pending.

## Browsing Applications

When the search field is blank, Index presents a bounded list of launcher-visible applications returned by the current Applications provider.

The provider card identifies active coverage as **Applications · On-device**.

## Searching Applications

1. Open GoreeCloud Index Dev.
2. Enter part or all of an application name in **Search applications**.
3. Index may briefly show **Searching applications…** while the current query is running.
4. Matching launcher applications appear under **Application results**.
5. Each result shows the application label, package identity, and on-device source label.
6. Tap a result to ask Android to launch the exact launcher component represented by that result.

Matching currently considers exact application names, title prefixes, word prefixes, title containment, exact package names, package prefixes, and package containment.

When the query changes, the previous in-flight search is cancelled before the new query proceeds. This prevents superseded provider work from continuing as if it were still relevant.

## Search States

Index distinguishes the following current source states:

- **Searching:** the current query is still executing.
- **Browse state:** blank query with available launcher applications.
- **No searchable applications:** the provider returned no launcher entries for the current bounded view.
- **No application matches:** the provider completed successfully but found no match for the entered query.
- **Provider temporarily unavailable:** a provider failed; Index isolates the failure instead of pretending the provider successfully returned nothing.
- **Provider took too long:** a provider exceeded its bounded timeout; Index stops waiting for that provider and can preserve healthy-provider results.
- **Launch failure:** Android could not open the selected launcher component; Index shows a short failure notification.

A provider failure or timeout does not automatically become a remote fallback.

## Local-Only Execution Boundary

The current MainActivity supplies an internal execution context that explicitly allows the Applications provider and sets `localOnly=true`.

This means a future provider that declares remote or mixed processing would not be dispatched through the current path merely because it was registered. This is an engineering fail-closed guard, not a user-facing Privacy Shield policy and not accepted GoreeCloud Identity authorization.

## Provider Timeout

The current Applications provider declares a provisional 500 ms timeout. The engine also caps provider timeout declarations to a global five-second safety ceiling.

These are Development safety bounds. They are not accepted latency targets, guarantees, or representative-device performance results. Values may change after device testing.

## Privacy in This Development Slice

The current Android applications provider operates locally. The application manifest does not request Android `INTERNET` permission or unrestricted `QUERY_ALL_PACKAGES` visibility.

This slice does not intentionally add query analytics, remote telemetry, or a persistent search-history store.

These source properties do **not** mean Privacy Shield runtime integration has been accepted. Future private-content or remote providers require explicit applicable privacy authorization and retention decisions.

## Security Boundary

Application results are typed to exact Android launcher components. Index does not claim that an application or provider is trusted, protected, clean, or Wardveil-approved merely because it appears in search.

Wardveil Security runtime evidence remains pending.

## Known Limitations

The current development source does not yet provide:

- File/folder search.
- Contact search.
- Calendar search.
- Media or settings providers.
- GoreeCloud Search Internet results.
- Mesh-discovered first-party GoreeCloud providers.
- Extension or third-party providers.
- Local content indexing.
- Incremental/streaming multi-provider result delivery.
- Provider capability/health negotiation.
- Search history, saved searches, or a provider-settings center.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device, Launcher integration, accessibility, performance, production-signing, deployment, or Stable acceptance.

## Development Package

Current branch identity:

- Application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Version: `0.2.0-dev`
- Version code: `2`

The accepted main baseline before this branch is commit `19737c11c59a30a94ee8b6dad8855b449c011eca`, which passed exact-main GitHub Actions run `33420873144`. Its accepted Development APK SHA-256 is `a867073c433941297da985a1c8ec3e3972e7ecd6db4883f18e935a8d6fd72f83`, artifact ID `9768893227`.

The `0.2.0-dev` branch requires its own exact-candidate validation before its build can be represented as accepted.
