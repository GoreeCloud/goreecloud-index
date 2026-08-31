# GoreeCloud Index — User Manual

## Current Development Scope

GoreeCloud Index is in active development and is **not Stable or production accepted**. The current Android application implements one bounded search provider: launcher-visible applications on the current Android device.

Files, contacts, calendar, GoreeCloud service content, connected devices, extensions, optional third-party services, and Internet results are not available providers yet.

## Opening Index

Open **GoreeCloud Index Dev** from the Android launcher. GoreeCloud Launcher may also invoke Index through the first-party search-entry action `com.goreecloud.index.action.SEARCH` and may include an initial query in `com.goreecloud.index.extra.QUERY`.

The Launcher→Index contract exists in source/build; integrated representative-device acceptance remains pending.

## Browsing Applications

When the search field is blank, Index presents a bounded list of launcher-visible applications returned by the current Applications provider.

The provider card identifies the active coverage as **Applications · On-device**.

## Searching Applications

1. Open GoreeCloud Index Dev.
2. Enter part or all of an application name in **Search applications**.
3. Matching launcher applications appear under **Application results**.
4. Each result shows the application label, package identity, and on-device source label.
5. Tap a result to ask Android to launch the exact launcher component represented by that result.

Matching currently considers exact application names, title prefixes, word prefixes, title containment, exact package names, package prefixes, and package containment.

## Search States

Index distinguishes the following current states:

- **Browse state:** blank query with available launcher applications.
- **No searchable applications:** the provider returned no launcher entries for the current bounded view.
- **No application matches:** the provider completed successfully but found no match for the entered query.
- **Provider unavailable:** the Applications provider failed; Index shows a degraded-state message rather than pretending the provider successfully returned nothing.
- **Launch failure:** Android could not open the selected launcher component; Index shows a short failure notification.

A provider failure does not automatically become a remote fallback.

## Privacy in This Development Slice

The current Android applications provider operates locally. The application manifest does not request Android `INTERNET` permission or unrestricted `QUERY_ALL_PACKAGES` visibility.

This slice does not intentionally add query analytics, remote telemetry, or a persistent search-history store.

These source properties do **not** mean Privacy Shield runtime integration has been accepted. Future private-content or remote providers require explicit applicable privacy authorization and retention decisions.

## Security Boundary

Application results are typed to exact Android launcher components. Index does not claim that an application or provider is trusted, protected, clean, or Wardveil-approved merely because it appears in search.

Wardveil Security runtime evidence remains pending.

## Known Limitations

The current development build does not yet provide:

- File/folder search.
- Contact search.
- Calendar search.
- Media or settings providers.
- GoreeCloud Search Internet results.
- Mesh-discovered first-party GoreeCloud providers.
- Extension or third-party providers.
- Local content indexing.
- Multi-provider asynchronous execution, cancellation, or timeouts.
- Search history, saved searches, or a provider-settings center.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device, Launcher integration, accessibility, performance, production-signing, deployment, or Stable acceptance.

## Development Package

Current development identity:

- Application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Version: `0.1.0-dev`
- Version code: `1`

The first merged Android baseline at main commit `331e97507a7b3b7ca3d930771915f1026bf2d4a8` passed exact-main build validation in GitHub Actions run `33418751538`. Later revisions require their own exact-candidate validation.
