# GoreeCloud Index — Features

## Status Model

This record distinguishes current source implementation, partial/experimental capability, and planned scope. Source implementation does not by itself establish representative-device, production, or Stable acceptance.

## Current Features

### Native Android Foundation

- Original GoreeCloud-owned Kotlin/Jetpack Compose Android application.
- Production package reservation `com.goreecloud.index` and development package `com.goreecloud.index.dev`.
- Android API 26 minimum, compile API 37, and target API 36 baseline.
- Exact-source CI path for repository validation, unit tests, lint, development APK assembly, APK identity verification, checksum evidence, and artifact publication.
- Exported user-visible Launcher→Index search-entry contract using `com.goreecloud.index.action.SEARCH` and `com.goreecloud.index.extra.QUERY`.

### Search Core

- Provider-neutral query, result, result-type, and typed action models.
- Provider identity and human-readable provider name.
- Provider-scoped result identity.
- Deterministic score-first ranking.
- Provider-scoped deduplication.
- Maximum result bounds.
- Provider exception isolation.
- Sanitized provider issue state returned separately from successful results.
- Unit tests covering failure isolation, issue reporting, ranking, deduplication, text matching, and bounded empty-query browsing.

### Android Applications Provider

- Launcher-visible application discovery through `ACTION_MAIN` plus `CATEGORY_LAUNCHER`.
- Narrow Android package visibility rather than unrestricted `QUERY_ALL_PACKAGES`.
- No `INTERNET` permission in the current local-only slice.
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
- Application result rows with label, package identity, and source label.
- No-match state.
- Provider-unavailable/degraded state.
- Application-launch failure feedback.
- Safe-drawing inset handling for edge-to-edge layouts.
- 48 px minimum result identity control and 72 px minimum result-row height.
- Semantic heading structure for primary search sections.

The current UI targets Glaze UI 2.1.0 semantics. Formal Glaze UI consumer conformance is not yet claimed.

## Experimental or Partial Features

- Android application discovery has source/build evidence but still requires representative-device acceptance.
- The provider contract is synchronous and intentionally suitable only for the current bounded local provider. Remote or expensive providers require asynchronous execution, cancellation, and timeouts before activation.
- Launcher search handoff exists in source/build but still requires integrated Launcher→Index device validation.
- UI accessibility requires formal large-text, screen-reader, focus, reduced-motion, contrast, reduced-transparency, and form-factor acceptance.
- Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Identity, and GoreeCloud Mesh runtime integrations remain pending.

## Planned Features

### Universal Search

- Multiple authorized provider categories in one query experience.
- Incremental result delivery.
- Provider/resource-type filtering.
- Local-only mode.
- Grouping, ranking, provenance-preserving deduplication, and source-aware contextual actions.
- Query cancellation/supersession and per-provider timeouts.
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
- Provider availability, health, cancellation, and timeout behavior.
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

- Asynchronous provider execution.
- Cancellation and timeouts.
- Partial result delivery when providers fail.
- Offline local-provider behavior.
- Low idle resource use and bounded battery/memory impact.
- Screen-reader, keyboard, touch, pointer, large-text, reduced-motion, contrast, reduced-transparency, and supported-form-factor acceptance.

## Deprecated or Removed Features

None.
