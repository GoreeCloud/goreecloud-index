# GoreeCloud Index — Features

## Status Model

**Release lifecycle: Development.** This record distinguishes current source implementation, accepted-main build evidence, partial/experimental capability, and planned scope. Source implementation does not by itself establish representative-device, production, or Stable acceptance.

The accepted main baseline before this branch is `19737c11c59a30a94ee8b6dad8855b449c011eca`. The asynchronous-provider-runtime work described below is current branch source and requires exact-candidate CI plus merge acceptance before it becomes accepted-main evidence.

## Current Source Features

### Native Android Foundation

- Original GoreeCloud-owned Kotlin/Jetpack Compose Android application.
- Production package reservation `com.goreecloud.index` and development package `com.goreecloud.index.dev`.
- Development version `0.2.0-dev`, version code `2` on this branch.
- Android API 26 minimum, compile API 37, and target API 36 baseline.
- Exact-source CI path for repository validation, unit tests, lint, Development APK assembly, APK identity verification, checksum evidence, and artifact publication.
- Exported user-visible Launcher→Index search-entry contract using `com.goreecloud.index.action.SEARCH` and `com.goreecloud.index.extra.QUERY`.

### Search Core

- Provider-neutral query, result, result-type, and typed action models.
- Provider identity and human-readable provider name.
- Provider processing-location declaration: local, remote, or mixed.
- Provider-scoped result identity.
- Suspendable provider contract.
- Structured concurrent provider dispatch through Kotlin coroutines.
- Injected provider dispatcher for deterministic tests.
- Explicit fail-closed `IndexExecutionContext` with allowed-provider IDs and local-only enforcement.
- Per-provider timeout declarations with a global five-second safety ceiling.
- Explicit provider issue kinds for ordinary failure and timeout.
- External cancellation propagation; cancellation is not converted into a provider issue.
- Deterministic score-first ranking.
- Ranking before provider-scoped deduplication so the best-ranked duplicate representation is retained.
- Maximum result bounds of 1–100.
- Healthy-provider result preservation when another eligible provider fails or times out.
- Unit tests covering failure isolation, timeout isolation, concurrent dispatch, cancellation propagation, local-only execution gating, disallowed-provider suppression, ranking/deduplication, result bounds, and text matching.

### Android Applications Provider

- Launcher-visible application discovery through `ACTION_MAIN` plus `CATEGORY_LAUNCHER`.
- Narrow Android package visibility rather than unrestricted `QUERY_ALL_PACKAGES`.
- No `INTERNET` permission in the current local-only slice.
- Provider processing location declared `LOCAL`.
- Provisional 500 ms provider timeout as a development safety bound, not a latency SLA.
- Search by application label and package name.
- Exact, prefix, word-prefix, and containment relevance scoring.
- Exact Android component identity retained for launch handoff.
- Provider cache refresh when Index resumes.
- Modern Android `ResolveInfoFlags` query path on API 33+ with compatibility behavior for earlier supported Android releases.

### Current Search Surface

- Dedicated native GoreeCloud Index screen.
- Immediate search focus.
- **Applications · On-device** source/processing disclosure.
- Browse-all-applications behavior when the query is blank, subject to the query result limit.
- Superseded-query cancellation via `LaunchedEffect(query)` and the suspendable query engine.
- Non-animated searching state.
- Application result rows with label, package identity, and source label.
- No-match state.
- Provider-failed state.
- Provider-timed-out state.
- Application-launch failure feedback.
- Safe-drawing inset handling for edge-to-edge layouts.
- 48 px result identity surface and 72 px minimum result-row height.
- Semantic heading structure for primary search sections.

The current UI targets Glaze UI 2.1.0 semantics. Formal Glaze UI consumer conformance is not yet claimed.

## Accepted Main Evidence

Main commit `19737c11c59a30a94ee8b6dad8855b449c011eca` passed exact-main GitHub Actions run `33420873144`. That run verified repository contracts, unit tests, lint, Development APK assembly, package/version/label identity, checksum capture, and artifact publication for the prior `0.1.0-dev` baseline.

Accepted-main APK SHA-256: `a867073c433941297da985a1c8ec3e3972e7ecd6db4883f18e935a8d6fd72f83`.

Accepted-main artifact ID: `9768893227`.

This branch's `0.2.0-dev` features require separate exact-candidate evidence.

## Experimental or Partial Features

- The asynchronous runtime is implemented in source on this development branch but is not yet accepted-main evidence.
- Supersession currently cancels complete query work; incremental/streaming result delivery is not yet implemented.
- `IndexExecutionContext` is an internal provider-eligibility guard, not accepted Privacy Shield or GoreeCloud Identity authorization.
- Android application discovery has source/build evidence from the accepted baseline but still requires representative-device acceptance for the newer runtime.
- Launcher search handoff exists in accepted source/build but still requires integrated Launcher→Index device validation.
- UI accessibility requires formal large-text, screen-reader, focus, reduced-motion, contrast, reduced-transparency, and form-factor acceptance.
- Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Identity, and GoreeCloud Mesh runtime integrations remain pending.

## Planned Features

### Universal Search

- Multiple authorized provider categories in one query experience.
- Incremental/streaming result delivery.
- Provider/resource-type filtering.
- User-facing local-only mode backed by applicable privacy authorization.
- Grouping, ranking, provenance-preserving deduplication, and source-aware contextual actions.
- Provider health and capability negotiation.

### Applications and Actions

- Provider-declared aliases.
- Application shortcuts and contextual actions where authorized.
- Additional platform application providers.
- Cross-device application/action discovery where explicitly supported.

### Files and Folders

- Name and authorized metadata search.
- Type, location, tag, and provider filtering.
- Authorized content indexing without uncontrolled duplication of private content.
- Open/reveal actions through the authoritative file provider.

### Contacts

- Authorized contact search.
- Account/source-aware results.
- Provider-authorized open/contact/communication actions.

### Calendar

- Authorized event search and metadata matching.
- Calendar/account-aware presentation.
- Provider-authorized open-event actions.

### GoreeCloud Providers

- First-party provider registration/discovery.
- Versioned capability negotiation.
- Provider availability and health behavior.
- GoreeCloud Mesh coordination without source-authority takeover.

### Extensions and Third-Party Services

- Versioned extension provider contract.
- Optional third-party providers with explicit opt-in, narrowly scoped authorization, processing-location disclosure, independent revocation, and provider-specific retained-data cleanup.
- Wardveil and Privacy Shield review before participation where applicable.

### Internet Search

- GoreeCloud Search as the explicit Internet/web provider.
- Internet results independently enableable/suppressible.
- No silent upload of unrelated local resource data.

### Local Indexing

- Privacy-minimized metadata indexes where they materially improve search.
- Incremental updates, provider removal cleanup, rebuild, corruption recovery, and user/profile/device scoping.
- Reconstructible index data kept distinct from authoritative user data.

### Platform Integrations

- Privacy Shield provider authorization, retention, history, and local-versus-remote controls.
- Wardveil provider trust/security evidence and action validation.
- GoreeCloud Identity user/profile/caller/provider identity and scoped authorization.
- GoreeCloud Mesh bounded provider discovery and coordination.
- Everkeep protection/recovery decisions for durable Index configuration while favoring rebuild of reconstructible index/cache data.
- Complete Glaze UI 2.1.0 consumer conformance.

### Reliability, Accessibility, and Performance

- Incremental result delivery.
- Provider-specific retry policy only where safe and justified.
- Offline local-provider behavior.
- Low idle resource use and bounded battery/memory impact.
- Screen-reader, keyboard, touch, pointer, large-text, reduced-motion, contrast, reduced-transparency, and supported-form-factor acceptance.
- Representative-device provider timeout/performance tuning based on evidence rather than provisional development values.

## Deprecated or Removed Features

None.
