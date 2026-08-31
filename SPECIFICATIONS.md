# GoreeCloud Index — Specifications

## 1. Product Status

- **Product:** GoreeCloud Index
- **Repository:** `GoreeCloud/goreecloud-index`
- **Release lifecycle:** Development
- **Current accepted milestone:** Native Android application search with asynchronous provider runtime
- **Development version:** `0.2.0-dev` / version code `2`
- **Production acceptance:** No
- **Stable qualification:** No
- **Current Stable Glaze UI target:** 2.1.0

Authoritative `main` commit `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` is the accepted Development source/build baseline for the asynchronous runtime. It passed exact-main workflow run `33429792389` after pull request #4 passed exact-candidate run `33429486374`.

Source/build evidence does not establish representative-device, platform-runtime, production, or Stable acceptance.

## 2. Purpose and Product Boundary

GoreeCloud Index is the universal search and indexing authority for GoreeCloud. It coordinates authorized providers, normalizes and ranks results, preserves provenance, and exposes provider-authorized actions.

GoreeCloud Search remains authoritative for Internet/web/current-information search. GoreeCloud Launcher is a primary invocation/presentation surface, not a competing universal index. Source applications/services remain authoritative for their own data.

## 3. Current Android Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Version: `0.2.0-dev`
- Version code: `2`
- Minimum API: 26
- Compile API: 37
- Target API: 36

The application is original GoreeCloud-owned native Kotlin/Jetpack Compose software.

## 4. Provider Contract

`IndexProvider` currently declares:

- stable provider ID;
- human-readable display name;
- processing location: `LOCAL`, `REMOTE`, or `MIXED`;
- bounded timeout declaration;
- suspendable search operation.

Mature external/provider ecosystems additionally require resource types, permissions/scopes, capabilities, health, result/action contracts, retention characteristics, versioning, and capability negotiation.

## 5. Execution Eligibility and Authorization Boundary

`IndexExecutionContext` carries exact `allowedProviderIds` and `localOnly`.

The engine fails closed: unlisted providers are not dispatched; `REMOTE` or `MIXED` providers are not dispatched while `localOnly=true`.

This internal runtime eligibility guard **does not constitute accepted Privacy Shield authorization** and is not accepted GoreeCloud Identity authorization. Future authority integrations must feed provider eligibility through approved contracts rather than being inferred from application-local state.

## 6. Asynchronous Query Runtime

`IndexQueryEngine.search` is suspendable and uses structured concurrency. Current accepted behavior:

- trim query text;
- clamp requested results to 1–100;
- filter providers through execution eligibility before dispatch;
- dispatch eligible providers concurrently under `supervisorScope`;
- run provider work on an injectable dispatcher;
- apply each provider timeout with `withTimeout`;
- cap timeout declarations at a five-second Development safety ceiling;
- convert provider timeout to sanitized `TIMED_OUT` issue;
- convert ordinary provider exception to sanitized `FAILED` issue;
- rethrow external `CancellationException`;
- preserve healthy-provider results when another provider fails or times out;
- rank by score/title/provider before deduplication;
- deduplicate by provider-scoped identity;
- apply the final result cap.

Timeout values are Development safety bounds, not latency SLAs.

## 7. Superseded-Query Cancellation

`IndexRoot` invokes search from `LaunchedEffect(query)`. When the query changes, Compose cancels the previous effect and its structured provider coroutine tree. The engine preserves cancellation rather than misreporting it as provider failure.

Incremental/streaming results are not yet implemented; current searches return a final `IndexSearchSnapshot` after eligible provider outcomes resolve.

## 8. Provider Failure Model

`IndexSearchSnapshot` contains successful results plus sanitized provider issues. `FAILED` and `TIMED_OUT` are distinct. Exception contents are not surfaced directly to users. One provider failure or timeout must not suppress healthy-provider results.

## 9. Current Applications Provider

The sole implemented provider is **Applications · On-device**. It:

- uses `ACTION_MAIN` + `CATEGORY_LAUNCHER`;
- avoids unrestricted `QUERY_ALL_PACKAGES`;
- requires no Android `INTERNET` permission;
- declares `LOCAL` processing;
- declares a provisional 500 ms Development timeout;
- searches launcher labels and package names;
- preserves exact `ComponentName` identity;
- returns typed launcher-component actions;
- refreshes its immutable application snapshot when Index resumes.

Representative-device acceptance of its timeout/performance behavior remains pending.

## 10. Search UI

The accepted Compose surface provides immediate query focus, source disclosure, non-animated searching state, browse/no-match states, separate provider-failure and provider-timeout states, healthy result preservation, launch-failure feedback, safe-drawing insets, bounded interaction sizing, and semantic headings.

The UI targets Glaze UI 2.1.0, but formal application-specific consumer conformance and human visual/accessibility acceptance remain pending.

## 11. Planned Provider Categories

Planned categories include files/folders, contacts, calendar, media, settings/platform actions, first-party GoreeCloud application/service resources, connected-device resources, extensions, optional third-party services, and GoreeCloud Search Internet results.

A provider must not be activated merely because the asynchronous runtime can dispatch it. Each provider requires applicable source authority, permission, Privacy Shield, Identity, Wardveil, retention, failure, action, and acceptance evidence.

## 12. Local Indexing

No local content index is implemented. Future indexes must prefer privacy-minimized derived metadata, remain user/profile/device scoped, support provider cleanup/rebuild/corruption recovery, preserve source authority, and distinguish reconstructible cache/index state from authoritative user data.

## 13. Platform Authority Requirements

- **Privacy Shield:** consent, purpose, minimization, retention, sharing, local/remote processing.
- **Wardveil Security:** applicable provider trust, threat, protection, and action-security evidence.
- **GoreeCloud Identity:** user/profile/device/caller/provider identity and scoped authorization.
- **GoreeCloud Mesh:** bounded first-party capability discovery/coordination without source takeover.
- **Everkeep:** continuity decisions for durable Index configuration; reconstructible indexes normally rebuild.
- **Glaze UI:** current Stable 2.1.0 consumer contract before Stable qualification.

No accepted runtime integration is currently claimed for Privacy Shield, Wardveil Security, Everkeep, Identity, Mesh, or GoreeCloud Search.

## 14. Automated Validation Evidence

Pull request #4 candidate `d8b563705ccf1d05444df18e7a593a454d4c4103` passed exact-candidate workflow run `33429486374`.

The merged exact-main source `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` passed workflow run `33429792389`, including repository validation, coroutine unit tests, lint, Development APK assembly, package/version/label verification, checksum capture, and artifact publication.

Accepted-main APK SHA-256: `a2605bb1e993dd027afc68b0900b60f2fcf9567ec59399e5a02a178af1cc815f`.

Accepted-main artifact: `9772201920`.

Artifact archive digest: `sha256:0aa9c334b980f558fa983ef059f4fc7a73cf57fdf8c82d39ca2414ec60c77b75`.

## 15. Current Limitations

Not implemented or accepted:

- files/folders, contacts, calendar, media, settings, connected-device, Search, Mesh-discovered, extension, or third-party providers;
- local metadata/content index;
- incremental/streaming result delivery;
- provider health/capability negotiation;
- user-facing provider/privacy settings center;
- accepted Privacy Shield/Wardveil/Everkeep/Identity/Mesh runtime integrations;
- formal Glaze UI 2.1.0 conformance;
- representative-device/provider/accessibility/performance acceptance;
- controlled production signing/deployment;
- production acceptance or Stable qualification.

## 16. Next Development Sequence

1. Select one second provider with a clear authoritative source and permission model.
2. Define the Privacy Shield/Identity decision path required before dispatch.
3. Validate its cancellation, timeout, provenance, result actions, and failure behavior end to end.
4. Add incremental result delivery only after multi-provider snapshot semantics remain coherent and accessible.
5. Expand providers one authority boundary at a time.

## 17. Production and Stable Gates

GoreeCloud Index must not be called production-ready or Stable until the exact release revision has all applicable source/build, representative-device, permission/isolation, Glaze UI, platform-integration, accessibility, performance, signing/deployment/rollback, and reconciled documentation evidence.
