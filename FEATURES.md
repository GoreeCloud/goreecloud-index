# GoreeCloud Index — Features

## Status Model

**Release lifecycle: Development.** This record distinguishes accepted current Development features from planned scope. Current source/build evidence does not establish representative-device, production, or Stable acceptance.

## Current Features

### Native Android Foundation

- Original GoreeCloud-owned Kotlin/Jetpack Compose application.
- Production package `com.goreecloud.index`; Development package `com.goreecloud.index.dev`.
- Development version `0.2.0-dev`, version code `2`.
- Android API 26 minimum, compile API 37, target API 36.
- Launcher→Index search-entry contract using `com.goreecloud.index.action.SEARCH` and `com.goreecloud.index.extra.QUERY`.

### Search Core

- Provider-neutral query/result/type/action models.
- Stable provider identity and human-readable display name.
- Provider processing location: local, remote, or mixed.
- Suspendable provider contract.
- Fail-closed `IndexExecutionContext` with exact provider allowlisting and local-only gating.
- Structured concurrent provider dispatch using Kotlin coroutines.
- Superseded-query cancellation through the Compose query lifecycle.
- Per-provider timeout declarations with a five-second Development safety ceiling.
- Separate sanitized `FAILED` and `TIMED_OUT` provider issue kinds.
- Healthy-provider results preserved when another eligible provider fails or times out.
- Deterministic score-first ranking before provider-scoped deduplication.
- Result-count bounds of 1–100.
- Unit coverage for concurrency, timeout isolation, cancellation propagation, execution gating, failure isolation, ranking/deduplication, bounds, and text matching.

### Applications · On-device Provider

- Launcher-visible application discovery via `ACTION_MAIN` + `CATEGORY_LAUNCHER`.
- Narrow package visibility; no unrestricted `QUERY_ALL_PACKAGES`.
- No Android `INTERNET` permission for the current local-only slice.
- `LOCAL` processing declaration.
- Provisional 500 ms Development timeout.
- Label and package-name matching.
- Exact Android component identity retained for launch handoff.
- Provider snapshot refresh when Index resumes.
- API 33+ `ResolveInfoFlags` path with compatibility behavior for older supported APIs.

### Current Search Surface

- Dedicated native GoreeCloud Index screen.
- Immediate search focus.
- **Applications · On-device** source disclosure.
- Blank-query application browsing.
- Non-animated searching state.
- Superseded-query cancellation.
- Result rows with application label, package identity, and source label.
- No-match state.
- Provider-failed state.
- Provider-timed-out state.
- Application-launch failure feedback.
- Safe-drawing insets, bounded interaction sizes, and semantic headings.

The UI targets Glaze UI 2.1.0. Formal application-specific consumer conformance remains pending.

## Accepted Main Evidence

Pull request #4 candidate `d8b563705ccf1d05444df18e7a593a454d4c4103` passed exact-candidate run `33429486374`.

Authoritative main `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` passed exact-main run `33429792389`.

Accepted-main APK SHA-256: `a2605bb1e993dd027afc68b0900b60f2fcf9567ec59399e5a02a178af1cc815f`.

Accepted-main artifact: `9772201920`.

## Partial / Not Yet Accepted

- Representative-device application-provider performance and timeout tuning.
- Integrated Launcher→Index device acceptance for the `0.2.0-dev` runtime.
- Formal screen-reader, keyboard, large-text, reduced-motion, contrast, reduced-transparency, and form-factor acceptance.
- Formal Glaze UI 2.1.0 consumer conformance.
- Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Identity, GoreeCloud Mesh, and GoreeCloud Search runtime integrations.

`IndexExecutionContext` is internal eligibility logic, not accepted Privacy Shield or Identity authorization.

## Planned Features

### Universal Search

- Multiple authorized provider categories in one experience.
- Incremental/streaming result delivery.
- Provider/resource-type filtering.
- User-facing local-only controls backed by applicable privacy authority.
- Provider health and capability negotiation.
- Source-aware grouping and contextual actions.

### Resource Providers

- Files and folders.
- Contacts.
- Calendar events.
- Media.
- Settings/platform actions.
- First-party GoreeCloud application/service content.
- Connected-device resources.

### Extensions and Third-Party Services

- Versioned extension provider contract.
- Explicit opt-in and narrowly scoped authorization.
- Processing-location disclosure.
- Independent revocation and provider-specific retained-data cleanup.
- Applicable Privacy Shield/Wardveil/Identity review before participation.

### Internet Search

- GoreeCloud Search as the explicit Internet/web provider.
- Independently enableable remote results.
- No silent upload of unrelated local resource data.

### Local Indexing

- Privacy-minimized metadata indexes where materially useful.
- Incremental updates, provider cleanup, rebuild/corruption recovery, and user/profile/device scoping.
- Clear separation between reconstructible index/cache data and authoritative user data.

### Platform Integrations

- Privacy Shield provider authorization/retention/local-remote controls.
- Wardveil trust/security evidence and sensitive-action validation.
- GoreeCloud Identity user/profile/caller/provider authorization.
- GoreeCloud Mesh bounded provider discovery/coordination.
- Everkeep continuity decisions for durable configuration.
- Complete Glaze UI 2.1.0 consumer conformance.

## Deprecated or Removed Features

None.
