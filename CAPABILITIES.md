# GoreeCloud Index — Capabilities

## Overview

**Release lifecycle: Development.** GoreeCloud Index has accepted Development source/build evidence for a native Android application-search and asynchronous provider-runtime foundation. This is not representative-device, platform-integration, production, or Stable acceptance.

## Accepted Current Capabilities

- Original Kotlin/Jetpack Compose Android application.
- Provider-neutral query/result/type/action contracts.
- Suspendable provider operations.
- Provider processing-location and timeout declarations.
- Fail-closed `IndexExecutionContext` with exact provider allowlisting and local-only gating.
- Structured concurrent provider execution under `supervisorScope`.
- Per-provider timeout enforcement using `withTimeout` with a five-second Development safety ceiling.
- Distinct sanitized `FAILED` and `TIMED_OUT` provider issues.
- External cancellation propagation, enabling superseded-query cancellation instead of cancellation-to-error conversion.
- Deterministic ranking before provider-scoped deduplication.
- Bounded result counts.
- Launcher-visible Android Applications provider.
- Application label/package search with exact component launch handoff.
- Launcher→Index search invocation contract with optional initial query.
- Source-aware UI with searching, browse, no-match, provider-failure, provider-timeout, result, and launch-failure states.
- Unit tests, repository validation, Android lint/build, APK identity/checksum, and artifact publication automation.

## Accepted Main Build Evidence

Authoritative main commit: `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9`.

Exact-main workflow: `33429792389`.

- Package: `com.goreecloud.index.dev`
- Version: `0.2.0-dev`
- Version code: `2`
- Label: `GoreeCloud Index Dev`
- APK SHA-256: `a2605bb1e993dd027afc68b0900b60f2fcf9567ec59399e5a02a178af1cc815f`
- Artifact ID: `9772201920`
- Artifact name: `goreecloud-index-development-apk-e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9`
- Artifact digest: `sha256:0aa9c334b980f558fa983ef059f4fc7a73cf57fdf8c82d39ca2414ec60c77b75`

Pull request #4 candidate `d8b563705ccf1d05444df18e7a593a454d4c4103` independently passed exact-candidate run `33429486374` before merge.

## Query Runtime Boundary

Eligible providers execute concurrently. One provider failure or timeout is isolated and represented as a sanitized issue while healthy sibling results remain available. External cancellation is rethrown so the Compose query lifecycle can stop obsolete searches.

Provider timeout values are engineering safety bounds, not accepted latency SLAs.

## Execution Eligibility Boundary

`IndexExecutionContext` supplies an exact allowed-provider set and a `localOnly` flag. Unlisted providers are not dispatched; remote/mixed providers are not dispatched while local-only is enabled.

This is **not** accepted Privacy Shield consent/permission logic and **not** accepted GoreeCloud Identity authorization.

## Current User-Facing Boundary

The Development application can browse/search launcher-visible Android applications and ask Android to launch the exact selected component. Current coverage is identified as **Applications · On-device**.

Files, contacts, calendar, GoreeCloud service content, connected devices, extensions, third-party services, and Internet results are not implemented providers.

## Provider and Privacy Boundary

The Applications provider uses scoped `ACTION_MAIN` + `CATEGORY_LAUNCHER` visibility, requests neither `QUERY_ALL_PACKAGES` nor Android `INTERNET`, declares local processing, does not intentionally transmit queries/application inventory, and does not persist search history.

No local content index, extension registry, third-party connector, or remote-provider cache exists.

## Platform Integrations

The UI targets Glaze UI 2.1.0; formal consumer conformance remains pending.

No accepted runtime integration is currently claimed for Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, GoreeCloud Identity, or GoreeCloud Search.

## Resilience

Accepted source resilience includes structured provider concurrency, supersession cancellation propagation, provider timeouts, ordinary failure isolation, timeout isolation, partial healthy results, deterministic ranking/deduplication, exact provider allowlisting, and local-only dispatch gating.

Incremental result streaming, remote retry policy, provider health negotiation, index corruption recovery, and cross-device resilience remain pending.

## Accessibility Boundary

Source includes semantic headings, immediate focus, safe-drawing insets, bounded search/result interaction sizes, explicit search/degraded states, and no required animated progress indicator.

Formal screen-reader, keyboard, large-text/reflow, reduced-motion, reduced-transparency, increased-contrast, and representative-device acceptance remain pending.

## Current Limitations

Not implemented or accepted:

- File/folder provider.
- Contacts provider.
- Calendar provider.
- GoreeCloud Search Internet provider.
- Mesh-discovered first-party provider runtime.
- Extension or third-party provider runtime.
- Local metadata/content index.
- Incremental/streaming result delivery.
- Provider capability/health negotiation.
- Search history, saved search, or provider settings center.
- Accepted Privacy Shield, Wardveil Security, Everkeep, Identity, or Mesh runtime integration.
- Formal Glaze UI 2.1.0 conformance.
- Representative-device/provider/accessibility/performance acceptance.
- Controlled production signing/deployment, production acceptance, or Stable qualification.
