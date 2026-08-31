# GoreeCloud Index — Architecture

## Status

**Release lifecycle: Development.** The asynchronous provider-runtime milestone is accepted on authoritative `main` at `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9`. Exact-main workflow run `33429792389` succeeded after pull request #4 exact-candidate validation.

This milestone implements structured concurrent provider execution, superseded-query cancellation, bounded provider timeouts, and fail-closed provider eligibility while preserving the current local-only provider authority boundary.

Production acceptance and Stable qualification remain false.

## Authority Model

GoreeCloud Index coordinates universal search; it does not own provider resources.

- Android remains authoritative for installed launcher activities and launch behavior.
- GoreeCloud Launcher is an invocation/presentation surface; Index remains the universal-search/indexing authority.
- GoreeCloud Search remains authoritative for Internet/web/current-information search.
- Future files, contacts, calendar, media, device, extension, and third-party providers remain authoritative for their resources and permissions.
- Privacy Shield remains authoritative for applicable consent, purpose, minimization, retention, and local/remote data use.
- GoreeCloud Identity remains authoritative for applicable caller/user/profile/provider identity and scoped authorization.
- Wardveil Security remains authoritative for applicable provider trust, threat, protection, and security evidence.
- Everkeep remains authoritative for continuity of durable Index configuration where applicable.

## Current Android Flow

```text
Launcher or user
  → MainActivity
  → IndexExecutionContext
  → IndexRoot / LaunchedEffect(query)
  → IndexQueryEngine
      → fail-closed provider eligibility
      → supervisorScope
      → concurrent async dispatch
      → per-provider withTimeout
      → preserve external CancellationException
      → normalize provider outcomes
  → IndexSearchSnapshot
      → ranked provider-scoped results
      → FAILED / TIMED_OUT provider issues
  → source-aware UI
  → exact Android component launch handoff
```

The only implemented provider remains local launcher applications. No remote provider or local content index is enabled.

## External Search Entry Contract

`GoreeCloudIndexContract` defines action `com.goreecloud.index.action.SEARCH`, initial-query extra `com.goreecloud.index.extra.QUERY`, and Applications provider ID `goreecloud.index.provider.apps`.

The user-visible search activity accepts the contract without moving universal-search authority into Launcher. Integrated representative-device acceptance remains pending.

## Core Models

`IndexProvider` declares stable identity, display name, processing location (`LOCAL`, `REMOTE`, `MIXED`), timeout, and suspendable search.

`IndexExecutionContext` contains exact provider eligibility plus `localOnly`. Unlisted providers are not dispatched; remote/mixed providers are not dispatched while local-only is true.

This context is internal eligibility logic, not accepted Privacy Shield or Identity authorization.

`IndexProviderIssue` records sanitized `FAILED` or `TIMED_OUT` outcomes. `IndexSearchSnapshot` returns successful results and provider issues independently.

## Asynchronous Query Runtime

Current accepted engine behavior:

- normalize query and clamp result count;
- filter providers before dispatch;
- run eligible providers concurrently under `supervisorScope`;
- use an injectable provider dispatcher;
- enforce provider timeout with `withTimeout` and a five-second safety ceiling;
- map provider timeouts to `TIMED_OUT`;
- map ordinary provider exceptions to `FAILED`;
- rethrow external `CancellationException`;
- preserve healthy sibling results;
- rank before provider-scoped deduplication;
- apply final result cap.

Handled provider failure does not cancel healthy siblings; ordinary parent/query cancellation still propagates through the structured coroutine tree.

## Superseded-Query Cancellation

`IndexRoot` invokes search in `LaunchedEffect(query)`. A query change cancels the prior effect and its provider work. The engine does not swallow that cancellation.

Incremental/streaming delivery is not yet implemented.

## Android Applications Provider

`InstalledAppsProvider`:

- uses `ACTION_MAIN` + `CATEGORY_LAUNCHER`;
- declares `LOCAL` processing;
- declares provisional 500 ms Development timeout;
- maintains a volatile immutable launcher-entry snapshot;
- refreshes on resume;
- uses API 33+ `ResolveInfoFlags` with compatibility fallback;
- removes Index itself;
- preserves exact `ComponentName` identity;
- searches app labels/package names;
- returns typed launch actions.

The 500 ms timeout is a Development safety bound, not an accepted performance SLA.

## UI Architecture

The current UI focuses the query on entry, identifies **Applications · On-device**, uses safe-drawing insets, displays non-animated searching state, distinguishes provider timeout from failure, keeps valid results when another provider reports an issue, preserves source labels, and uses typed result actions.

Glaze UI 2.1.0 is the target. Formal consumer conformance and human visual/accessibility acceptance remain pending.

## Privacy and Security Boundaries

The current provider operates locally. The manifest requests no Android `INTERNET` or unrestricted `QUERY_ALL_PACKAGES`. The source adds no intentional query analytics, remote telemetry, or persistent search-history store.

`localOnly=true` prevents remote/mixed dispatch in the current MainActivity path, but this is not accepted Privacy Shield runtime integration.

Provider output remains untrusted input. Index does not infer Wardveil trust/protection from successful provider output. Wardveil runtime evidence remains pending.

## Identity, Mesh, and Everkeep Boundaries

GoreeCloud Identity must provide applicable scoped authorization; authentication is not blanket search permission. GoreeCloud Mesh may coordinate first-party provider discovery without taking source ownership. Neither runtime integration is accepted yet.

Current query/runtime state is transient. Future durable configuration may require Everkeep continuity coverage; reconstructible indexes/caches should normally rebuild from authoritative providers.

## Failure and Recovery Model

- Provider exception → sanitized `FAILED`; healthy results preserved.
- Provider timeout → sanitized `TIMED_OUT`; healthy results preserved.
- Parent/query cancellation → propagates, not converted to issue.
- Disallowed provider → not dispatched.
- Remote/mixed provider under local-only → not dispatched.
- No match / no applications → explicit empty states.
- Android launch failure → user-visible feedback.

No remote fallback is silently activated.

## Validation Evidence

PR #4 candidate `d8b563705ccf1d05444df18e7a593a454d4c4103` passed exact-candidate run `33429486374`.

Merged main `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` passed exact-main run `33429792389`.

Accepted-main APK SHA-256: `a2605bb1e993dd027afc68b0900b60f2fcf9567ec59399e5a02a178af1cc815f`; artifact `9772201920`.

This is Development source/build evidence only.

## Next Architecture Milestone

The next milestone is one permission-aware second provider with a clearly authoritative source and a real Privacy Shield/Identity decision path before dispatch. Its cancellation, timeout, provenance, actions, and failure behavior must be validated end to end before broader provider expansion.
