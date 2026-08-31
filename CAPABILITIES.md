# GoreeCloud Index — Capabilities

## Overview

**Release lifecycle: Development.** GoreeCloud Index is in active implementation. This record describes current source capability separately from accepted-main build evidence, representative-device behavior, platform-integration acceptance, production acceptance, and Stable qualification.

The accepted main baseline before this branch is `19737c11c59a30a94ee8b6dad8855b449c011eca`. The asynchronous-provider-runtime source described here requires exact-candidate CI and merge acceptance before it becomes accepted-main evidence.

## Current Source Capabilities

The repository contains an original native Android search foundation with:

- Kotlin and Jetpack Compose application source.
- Provider-neutral query/result/type/action contracts.
- Suspendable provider operations.
- Provider processing-location and timeout declarations.
- A fail-closed execution context that lists eligible providers and can prohibit remote/mixed dispatch.
- Structured concurrent provider execution under `supervisorScope`.
- Per-provider `withTimeout` enforcement with a global timeout ceiling.
- Explicit `FAILED` and `TIMED_OUT` provider issue kinds.
- External cancellation propagation instead of cancellation-to-error conversion.
- Deterministic ranking followed by provider-scoped deduplication.
- Bounded result counts.
- A real launcher-visible Android applications provider.
- Application label/package search and exact component launch handoff.
- Launcher→Index search invocation contract with optional initial query.
- A source-aware native search interface with searching, provider-failed, provider-timed-out, blank, no-match, and result states.
- Application-launch failure feedback.
- Unit tests, repository validation, Android lint/build, APK identity/checksum, and artifact-publication automation.

## Accepted Main Build Baseline

Exact main commit `19737c11c59a30a94ee8b6dad8855b449c011eca` passed GitHub Actions run `33420873144`. Repository validation, Android unit tests, lint, Development APK assembly, package/application-label verification, checksum capture, and artifact publication succeeded for the accepted `0.1.0-dev` baseline.

Accepted-main evidence:

- Package: `com.goreecloud.index.dev`
- Version: `0.1.0-dev`
- Version code: `1`
- Label: `GoreeCloud Index Dev`
- APK SHA-256: `a867073c433941297da985a1c8ec3e3972e7ecd6db4883f18e935a8d6fd72f83`
- Artifact ID: `9768893227`
- Artifact name: `goreecloud-index-development-apk-19737c11c59a30a94ee8b6dad8855b449c011eca`

This branch advances Development identity to `0.2.0-dev` / version code `2`. That identity is not accepted-main evidence until exact-candidate validation succeeds and the branch is merged.

## Query Runtime Boundary

The source can concurrently dispatch multiple eligible providers using Kotlin structured concurrency. One ordinary provider failure or provider timeout is converted into a sanitized provider issue while healthy-provider results remain available.

External cancellation is explicitly rethrown. This lets the Compose `LaunchedEffect(query)` lifecycle cancel superseded searches instead of allowing obsolete provider work to continue or be misreported as failure.

Each provider declares a timeout. The engine clamps that value to a five-second development safety ceiling. The current applications provider declares 500 ms. These values are engineering safety bounds, not accepted latency SLAs.

## Execution Eligibility Boundary

`IndexExecutionContext` currently supplies:

- an exact `allowedProviderIds` set;
- a `localOnly` flag.

A provider not in the allowlist is not dispatched. A provider declaring `REMOTE` or `MIXED` processing is not dispatched while `localOnly=true`.

This is a source-level fail-closed execution guard. It is **not** accepted Privacy Shield consent/permission logic and is **not** accepted GoreeCloud Identity authorization. Future platform authorities must feed or supersede this boundary through approved contracts rather than being inferred from it.

## Current User-Facing Capability Boundary

The Development application can browse/search launcher-visible Android applications and request Android to open the exact selected launcher component. The provider is local-only and the search screen identifies coverage as **Applications · On-device**.

The UI can now distinguish:

- active search;
- provider ordinary failure;
- provider timeout;
- no application match;
- no browseable applications;
- valid results;
- application launch failure.

This is not representative-device acceptance. Files, contacts, calendar, GoreeCloud service content, connected devices, extensions, third-party services, and Internet results are not currently implemented providers.

## Provider and Data Boundary

The current applications provider:

- Uses scoped `ACTION_MAIN` plus `CATEGORY_LAUNCHER` visibility.
- Does not request `QUERY_ALL_PACKAGES`.
- Does not request `INTERNET` in the current local-only application-search slice.
- Declares local processing.
- Does not intentionally transmit queries or application inventory.
- Does not intentionally persist search history.
- Preserves provider and Android component identity rather than copying application authority into Index.

No local file/content index, extension registry, third-party connector, or remote-provider cache exists yet.

## Launcher Integration Boundary

The Android manifest exposes `com.goreecloud.index.action.SEARCH`, and the shared contract defines `com.goreecloud.index.extra.QUERY`. This establishes a source/build handoff contract for GoreeCloud Launcher to invoke Index with an initial query.

Integrated Launcher→Index representative-device acceptance is still pending. Launcher remains an invocation/presentation surface; Index remains the universal search authority.

## Platform Integrations

The source targets Glaze UI 2.1.0 design semantics. Formal application-specific consumer conformance remains pending.

No accepted runtime integration is currently claimed for:

- Privacy Shield.
- Wardveil Security.
- Everkeep.
- GoreeCloud Mesh.
- GoreeCloud Identity.
- GoreeCloud Search.

## Privacy and Security

The current Android slice minimizes package visibility, disables cleartext traffic at the application level, requests no Internet permission, and adds no intentional remote query telemetry or analytics. Provider exceptions and timeout details are reduced to sanitized issue kinds rather than exposing exception content to the UI.

Local-only provider eligibility prevents accidental dispatch of a future provider declaring remote/mixed processing through the current MainActivity path. This does not establish accepted Privacy Shield or Wardveil runtime integration.

## Resilience

Current source resilience includes:

- structured provider concurrency;
- parent/supersession cancellation propagation;
- provider-specific timeout enforcement;
- ordinary provider failure isolation;
- timeout isolation;
- partial healthy results when another provider fails/times out;
- deterministic ranking and provider-scoped deduplication;
- exact provider allowlisting;
- local-only dispatch gating.

Incremental result streaming, remote retry policy, index corruption recovery, provider health negotiation, and cross-device resilience remain pending.

## Accessibility and UI

The current source includes:

- Semantic primary/section headings.
- Immediate query focus.
- Safe-drawing insets for edge-to-edge rendering.
- 56 px minimum search-field height.
- 72 px minimum result-row height and 48 px result identity surface.
- Explicit searching, blank/browse, no-match, provider-failed, provider-timed-out, and result states.
- Clear current provider coverage text.
- No required animated progress indicator.

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
- Incremental/streaming multi-provider result delivery.
- Provider capability/health negotiation.
- Search history, saved search, or provider settings center.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration.
- Formal Glaze UI 2.1.0 consumer conformance.
- Representative-device/provider/accessibility/performance acceptance.
- Controlled production signing, release, deployment, production acceptance, or Stable qualification.
