# GoreeCloud Index — Capabilities

## Overview

GoreeCloud Index is in active development. This record describes verified source/build capability separately from representative-device, platform-integration, production, and Stable acceptance.

## Current Source Capabilities

The repository contains an original native Android search foundation with:

- Kotlin and Jetpack Compose application source.
- Provider-neutral query/result/type/action contracts.
- A query engine that bounds result counts, ranks deterministically, deduplicates provider-scoped identity, isolates provider exceptions, and reports sanitized provider issues independently of successful results.
- A real launcher-visible Android applications provider.
- Application label/package search and exact component launch handoff.
- Launcher→Index search invocation contract with optional initial query.
- A source-aware native search interface with provider coverage disclosure and degraded-state presentation.
- Application-launch failure feedback.
- Unit tests, repository validation, Android lint/build, APK identity/checksum, and artifact-publication automation.

## Validated Build Baseline

Exact main commit `331e97507a7b3b7ca3d930771915f1026bf2d4a8` passed GitHub Actions run `33418751538` after PR #1 merged. Repository validation, Android unit tests, lint, development APK assembly, package/application-label verification, checksum capture, and artifact publication succeeded.

Validated development identity for that baseline:

- Package: `com.goreecloud.index.dev`
- Version: `0.1.0-dev`
- Version code: `1`
- Label: `GoreeCloud Index Dev`
- APK SHA-256: `8fee493995d500cd09579e15fe17915b7800117463f68fbc85f18cfd24b0ea3f`
- Artifact ID: `9768208441`
- Artifact name: `goreecloud-index-development-apk-331e97507a7b3b7ca3d930771915f1026bf2d4a8`

Any later revision requires its own exact-candidate validation before its build can be represented as accepted.

## Current User-Facing Capability Boundary

The development application can browse/search launcher-visible Android applications and request Android to open the exact selected launcher component. The provider is local-only and the search screen identifies its coverage as **Applications · On-device**.

This is not representative-device acceptance. Files, contacts, calendar, GoreeCloud service content, connected devices, extensions, third-party services, and Internet results are not currently implemented providers.

## Provider and Data Boundary

The current provider:

- Uses scoped `ACTION_MAIN` plus `CATEGORY_LAUNCHER` visibility.
- Does not request `QUERY_ALL_PACKAGES`.
- Does not request `INTERNET` in the current local-only application-search slice.
- Does not intentionally transmit queries or application inventory.
- Does not intentionally persist search history.
- Preserves provider and Android component identity rather than copying application authority into Index.

No local file/content index, extension registry, third-party connector, or remote-provider cache exists yet.

## Launcher Integration Boundary

The Android manifest exposes `com.goreecloud.index.action.SEARCH`, and the shared contract defines `com.goreecloud.index.extra.QUERY`. This establishes a source/build handoff contract for GoreeCloud Launcher to invoke Index with an initial query.

Integrated Launcher→Index device acceptance is still pending. Launcher remains an invocation/presentation surface; Index remains the universal search authority.

## Platform Integrations

The source targets Glaze UI 2.1.0 design semantics. Formal consumer conformance remains pending.

No accepted runtime integration is currently claimed for:

- Privacy Shield.
- Wardveil Security.
- Everkeep.
- GoreeCloud Mesh.
- GoreeCloud Identity.
- GoreeCloud Search.

## Privacy and Security

The current Android slice minimizes package visibility, disables cleartext traffic at the application level, requests no Internet permission, and adds no intentional remote query telemetry or analytics. Provider exceptions are converted into sanitized degraded state rather than exposing exception details to the UI.

These source properties do not establish accepted Privacy Shield or Wardveil runtime integration.

## Resilience

Provider exceptions are isolated so a failure does not suppress successful results from other providers. Provider issues are returned separately from valid results so the UI can distinguish failure from a legitimate empty result.

Asynchronous cancellation, provider timeouts, remote retry policy, index corruption recovery, and cross-device resilience remain pending.

## Accessibility and UI

The current source includes:

- Semantic primary/section headings.
- Immediate query focus.
- Safe-drawing insets for edge-to-edge rendering.
- 56 px minimum search-field height.
- 72 px minimum result-row height and 48 px result identity surface.
- Explicit blank/browse, no-match, provider-unavailable, and result states.
- Clear current provider coverage text.

Formal screen-reader, keyboard, large-text/reflow, reduced-motion, reduced-transparency, increased-contrast/forced-colors, and representative phone/tablet acceptance remain pending.

## Current Limitations

Not implemented or accepted:

- File/folder provider.
- Contacts provider.
- Calendar provider.
- GoreeCloud Search Internet provider.
- Mesh-discovered first-party provider runtime.
- Extension provider runtime.
- Third-party provider runtime.
- Local metadata/content index.
- Async provider execution, cancellation, and timeouts.
- Search history, saved search, or provider settings center.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device/provider/accessibility/performance acceptance.
- Controlled production signing, release, deployment, production acceptance, or Stable qualification.
