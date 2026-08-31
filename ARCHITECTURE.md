# GoreeCloud Index — Architecture

## Status

**Release lifecycle: Development.** This document describes the current source architecture on the asynchronous-provider-runtime development branch and the accepted-main evidence boundary. The accepted main baseline is `19737c11c59a30a94ee8b6dad8855b449c011eca`; the new asynchronous runtime is not accepted-main behavior until its exact candidate passes CI and is merged.

This milestone implements structured concurrent provider execution, superseded-query cancellation, and bounded provider timeouts while preserving the current local-only provider authority boundary.

Production acceptance and Stable qualification remain false.

## Authority Model

GoreeCloud Index coordinates universal search. It does not become the owner of provider resources.

- Android remains authoritative for installed launcher activities and application launch behavior.
- GoreeCloud Launcher is an invocation/presentation surface and may contribute Launcher-specific context, but Index remains the universal-search/indexing authority.
- GoreeCloud Search remains authoritative for Internet/web/current-information search.
- Future files, contacts, calendar, media, device, extension, and third-party providers remain authoritative for their own resources and permissions.
- Privacy Shield remains authoritative for applicable purpose, consent, minimization, retention, and local-versus-remote data-use decisions.
- GoreeCloud Identity remains authoritative for applicable caller/user/profile/provider identity and scoped authorization.
- Wardveil Security remains authoritative for applicable trust, protection, threat, and security-evidence decisions.

## Current Android Flow

```text
Launcher or user
  → MainActivity
  → explicit IndexExecutionContext
  → IndexRoot / LaunchedEffect(query)
  → IndexQueryEngine
      → filter providers through fail-closed execution eligibility
      → supervisorScope
      → concurrent async dispatch on provider dispatcher
      → per-provider withTimeout
      → preserve external CancellationException
      → normalize provider outcome
  → IndexSearchSnapshot
      → ranked provider-scoped results
      → FAILED / TIMED_OUT provider issues
  → source-aware UI
  → exact Android component launch handoff
```

The current implemented provider remains local launcher applications. No remote provider and no local content index are enabled.

## External Search Entry Contract

`GoreeCloudIndexContract` defines:

- Action: `com.goreecloud.index.action.SEARCH`
- Initial-query extra: `com.goreecloud.index.extra.QUERY`
- Applications provider ID: `goreecloud.index.provider.apps`

The Android manifest exports the user-visible search activity for this search action. This permits GoreeCloud Launcher to invoke Index with an initial query without moving universal-search authority into Launcher.

Integrated representative-device acceptance of this handoff remains pending.

## Core Models

`IndexQuery` contains normalized query text and a bounded result limit.

`IndexResult` preserves provider-scoped identity, result type, display fields, relevance score, and an optional typed action.

`IndexAction` currently supports exact Android launcher-component handoff through `LaunchActivity(packageName, className)`.

`IndexProvider` declares:

- stable provider ID;
- human-readable display name;
- processing location (`LOCAL`, `REMOTE`, or `MIXED`);
- provider timeout bound;
- suspendable search operation.

`IndexExecutionContext` is a fail-closed execution-eligibility record. It contains the exact set of provider IDs eligible for a query and a `localOnly` boundary. A provider that is not listed is not dispatched. A remote or mixed provider is not dispatched while `localOnly=true`.

This context is intentionally not named an authorization token or privacy decision. It is an internal runtime guard that future Privacy Shield and Identity authorization must feed through approved contracts rather than bypass.

`IndexProviderIssue` identifies a failed or timed-out provider without exposing provider exception details.

`IndexSearchSnapshot` returns successful results and provider issues independently, allowing partial success and clear degraded-state presentation.

## Asynchronous Query Runtime

`IndexQueryEngine.search` is suspendable and uses Kotlin structured concurrency.

Current behavior:

- Trim query text.
- Clamp requested results to 1–100.
- Filter providers through `IndexExecutionContext` before dispatch.
- Dispatch eligible providers concurrently under `supervisorScope`.
- Execute provider work on an injectable provider dispatcher.
- Apply each provider's timeout with `withTimeout`.
- Clamp provider timeout requests to a global 5-second safety ceiling.
- Convert a provider timeout into a sanitized `TIMED_OUT` provider issue.
- Convert an ordinary provider exception into a sanitized `FAILED` provider issue.
- Explicitly rethrow external `CancellationException` so supersession/lifecycle cancellation is never mislabeled as provider failure.
- Preserve healthy provider results when another provider fails or times out.
- Rank by descending score, then case-insensitive title, then provider ID.
- Deduplicate after ranking by `providerId:resultId`, preserving the best-ranked representation for that provider-scoped identity.
- Apply the final result cap.

`supervisorScope` prevents one handled provider failure from cancelling healthy siblings, while ordinary parent cancellation still propagates through the complete query tree.

## Superseded-Query Cancellation

`IndexRoot` invokes the suspendable engine inside `LaunchedEffect(query)`. When the query key changes, Compose cancels the previous effect before running the next query. Because the engine does not swallow external cancellation, provider coroutines for the superseded query are cancelled as part of the same structured concurrency tree.

This is the first supersession-cancellation implementation. Incremental/streaming result delivery is not yet implemented.

## Android Applications Provider

`InstalledAppsProvider` uses `ACTION_MAIN` plus `CATEGORY_LAUNCHER` discovery. The manifest declares the same narrow query contract and does not request `QUERY_ALL_PACKAGES`.

Provider behavior:

- Declares `LOCAL` processing.
- Declares a provisional 500 ms development timeout.
- Maintains a volatile immutable snapshot of launcher entries.
- Refreshes when Index resumes.
- Uses modern `ResolveInfoFlags` on Android 13/API 33+ and the compatibility API on earlier supported Android versions.
- Removes Index itself from results.
- Preserves exact `ComponentName` identity so different launcher components do not collapse into a package-only record.
- Matches application label and package name.
- Returns provider-scoped typed launch actions.

The 500 ms value is a development safety bound for this in-memory provider, not an accepted product latency target or representative-device performance SLA.

## UI Architecture

`IndexRoot` is a presentation adapter over `IndexSearchSnapshot`; it does not manufacture provider authority or security/privacy state.

The current UI:

- Focuses the search field on entry.
- Identifies provider coverage as **Applications · On-device**.
- Uses safe-drawing insets under edge-to-edge rendering.
- Shows a non-animated searching text state.
- Shows provider timeout separately from generic provider failure.
- Keeps valid results visible when another provider reports an issue.
- Distinguishes browse/blank, no-match, degraded-provider, searching, and result states.
- Preserves source labels in result rows.
- Uses typed result actions for handoff.

Glaze UI 2.1.0 is the current target. Exact application-specific consumer conformance and human visual/accessibility acceptance remain pending.

## Privacy Boundary

The current Android application provider operates locally. The manifest does not request `INTERNET`, and the source adds no intentional query analytics, telemetry, or persistent search-history store.

`IndexExecutionContext(localOnly=true)` adds a fail-closed runtime guard against dispatching remote/mixed providers in the current path. It is not accepted Privacy Shield runtime integration and must not be represented as one.

Future providers that handle private content or remote processing require explicit Privacy Shield authorization and retention decisions before activation.

## Security Boundary

Provider output is untrusted data for normalized presentation and typed actions. Index does not infer that a provider or result is trusted, protected, clean, or Wardveil-approved merely because the provider returned successfully.

Wardveil Security runtime evidence and trust policy remain pending. Extension and third-party providers must not be enabled before applicable trust, isolation, and action-validation requirements are implemented.

## Identity and Mesh Boundary

GoreeCloud Identity will provide applicable user/profile/device/caller/provider identity and scoped authorization. Authentication must not become blanket search permission.

GoreeCloud Mesh may coordinate first-party provider discovery and availability, but it must not take over provider data ownership or the independent authorities of Identity, Privacy Shield, Wardveil Security, or Everkeep.

Neither runtime integration is currently accepted.

## Everkeep Boundary

Current query state is transient. No durable Index database, provider preference store, or remote-provider configuration exists yet.

When durable state is introduced, Everkeep should protect only state that requires continuity. Reconstructible search indexes/caches should normally be rebuilt from authoritative providers rather than backed up as irreplaceable data.

## Failure and Recovery Model

Current handled conditions include:

- Ordinary provider exception → sanitized `FAILED` issue; healthy results preserved.
- Provider exceeds bounded timeout → sanitized `TIMED_OUT` issue; healthy results preserved.
- Parent/query cancellation → cancellation propagates; it is not converted into an issue.
- Provider disallowed by execution context → provider is not dispatched.
- Remote/mixed provider in local-only context → provider is not dispatched.
- No matching application → explicit no-match state.
- No available launcher applications → explicit empty browse state.
- Failed Android launch handoff → user-visible failure feedback.

No remote fallback is activated if a local provider fails. Search queries have no destructive side effects.

## Testing Boundary

Coroutine tests use an injected test dispatcher and virtual time to verify provider concurrency, timeout behavior, cancellation propagation, local-only fail-closed gating, provider failure isolation, result bounds, ranking-before-deduplication, and text matching.

Passing branch CI would establish exact-candidate source/build evidence only. It would not establish representative-device performance, platform-integration acceptance, production release, or Stable qualification.

## Next Architecture Milestone

After exact-candidate validation and merge of this asynchronous foundation, the next milestone is an end-to-end permission-aware second provider whose authoritative source, Privacy Shield/Identity decision path, cancellation/timeout behavior, and user-facing provenance can be validated without broadening authority implicitly. Files, contacts, or calendar should not be added until the chosen provider contract is verified against its governing source and permission requirements.
