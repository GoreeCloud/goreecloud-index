# GoreeCloud Index — Capabilities

## Overview

GoreeCloud Index is in active development. This file records current source capability separately from device, integration, production, and Stable acceptance.

## Current Source Capabilities

The repository now contains an original native Android search foundation with:

- Kotlin and Jetpack Compose application source.
- Typed query, provider, result, action, and provider-issue models.
- A provider-oriented `SearchEngine` that suppresses blank queries, isolates provider failures, ranks results, and deduplicates provider-scoped identities.
- A real Android launcher-application provider using the operating system's launcher activity registry.
- Application-label and package-name matching.
- Typed handoff to Android for application launch.
- A native search UI with source labeling and blank, no-result, provider-failure, result, and launch-failure states.
- Unit tests for search-engine invariants.
- Repository validation and Android CI/build definitions.

These are source implementation claims. Representative-device and production acceptance remain pending.

## User Capability Boundary

The current development build is intended to let a user search launcher-visible applications on the current Android device and open a selected result through Android.

This behavior must still be validated on representative devices before it is treated as accepted device capability. No file, contact, calendar, Internet, extension, or third-party search capability is currently implemented.

## Provider and Data Boundary

The current provider:

- Uses `ACTION_MAIN` plus `CATEGORY_LAUNCHER` discovery.
- Does not request unrestricted `QUERY_ALL_PACKAGES` visibility.
- Declares local processing.
- Does not intentionally transmit queries or application inventory.
- Does not persist search history in this slice.
- Preserves Android/package ownership of application identity and launch behavior.

No local content index, file-content store, provider database, extension registry, or third-party connector is implemented yet.

## Platform Integrations

The current source targets Glaze UI 2.1.0 design semantics, but formal consumer conformance has not been accepted.

No accepted runtime integration is currently claimed for:

- Privacy Shield.
- Wardveil Security.
- Everkeep.
- GoreeCloud Mesh.
- GoreeCloud Identity.
- GoreeCloud Search.

The repository documents the required authority boundaries and pending integration work without treating those boundaries as completed runtime integration.

## Security and Privacy

Current source behavior minimizes Android package visibility to launcher applications and adds no intentional remote query path, analytics, or telemetry. Search-engine provider failures are converted into sanitized provider-unavailable states instead of exposing exception details to the user.

This does not establish accepted Privacy Shield or Wardveil runtime enforcement.

## Resilience

The current `SearchEngine` isolates provider exceptions so one failing provider does not suppress healthy provider results. Unit tests cover this invariant together with blank-query suppression, ranking, and provider-scoped deduplication.

Timeouts, asynchronous cancellation, index recovery, cross-device resilience, and remote-provider retry policy remain pending.

## Accessibility and UI

The current Compose source includes semantic headings, descriptive action labels, minimum interaction sizing for primary controls, source labeling, and explicit blank/no-result/error states.

Formal accessibility acceptance, reduced-motion behavior, reduced-transparency behavior, contrast modes, large-text/reflow validation, and representative phone/tablet visual acceptance remain pending.

## Build and Validation Capability

The repository includes a GitHub Actions workflow intended to:

- Check out the exact candidate revision.
- Record the exact source SHA.
- Run repository-contract validation.
- Use JDK 17 and Gradle 9.5.0.
- Run Android unit tests.
- Run Android lint.
- Assemble the development APK.
- Verify development package identity and label.
- Record an APK SHA-256 checksum.
- Publish the APK and build evidence as a short-retention workflow artifact.

Until that workflow passes for an exact candidate, build validation must not be claimed.

## Current Limitations

Not currently implemented or accepted:

- File and folder search.
- Contacts search.
- Calendar search.
- GoreeCloud Search Internet provider.
- First-party GoreeCloud provider discovery through Mesh.
- Extension provider runtime.
- Third-party provider runtime.
- Local metadata/content index.
- Asynchronous provider execution, cancellation, and timeouts.
- Search history or saved searches.
- User/provider settings and permission center.
- Runtime Privacy Shield, Wardveil, Everkeep, Identity, or Mesh integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device, accessibility, performance, production-signing, deployment, or Stable acceptance.
