# GoreeCloud Index — User Manual

## Current Build Scope

GoreeCloud Index is in active development and is **not Stable or production accepted**. The current Android source implements only the first bounded provider: launcher-visible applications on the current Android device.

Files, contacts, calendar, GoreeCloud services, Internet results, extensions, and third-party services are not available in this build yet.

## Searching Applications

1. Open **GoreeCloud Index Dev**.
2. Enter part or all of an application name in **Search applications**.
3. Matching launcher applications appear under **Applications**.
4. Each result shows the application label and Android package name.
5. Tap a result to ask Android to launch that application.

The active source indicator reads **Applications · On-device**. This means the current provider performs its lookup locally through Android's launcher-application APIs.

## Search Behavior

- A blank search does not query providers.
- Matching prefers exact application names, then name prefixes, then name/package containment.
- Duplicate launcher entries for the same package are collapsed into one result.
- The provider returns at most 50 matches for one query.
- If the application provider fails, Index shows an unavailable state instead of silently presenting a successful empty result.
- If Android cannot launch a selected application, Index reports that it could not open the application.

## Privacy in This Development Slice

This source does not intentionally send application-search queries to the Internet or a third party, persist search history, or add analytics/remote telemetry. This is a property of the current source slice and is **not** a claim that Privacy Shield runtime integration has been accepted.

## Known Limitations

The current development build does not yet provide:

- File or folder search.
- Contact search.
- Calendar search.
- GoreeCloud Search Internet results.
- First-party GoreeCloud service providers beyond the Android application provider.
- Extension or third-party provider integrations.
- Search-history or saved-search controls.
- Local content indexing.
- Cross-device search.
- GoreeCloud Identity or Mesh runtime integration.
- Accepted Privacy Shield, Wardveil Security, or Everkeep runtime integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device, tablet, accessibility, performance, production-signing, deployment, or Stable acceptance.

## Development Package

The development package is expected to use:

- Application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Version: `0.1.0-dev`
- Version code: `1`

These values should be verified from the exact built APK before the artifact is treated as build evidence.
