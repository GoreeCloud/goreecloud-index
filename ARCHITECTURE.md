# GoreeCloud Index — Architecture

## Status

This document describes the current native Android foundation and the approved architecture direction. The application-search foundation has source/build evidence; broader provider, platform-integration, device, production, and Stable acceptance remain pending.

## Authority Model

GoreeCloud Index coordinates universal search. It does not become the owner of provider resources.

- Android remains authoritative for installed launcher activities and application launch behavior.
- GoreeCloud Launcher is an invocation/presentation surface and may contribute Launcher-specific context, but Index remains the universal-search/indexing authority.
- GoreeCloud Search remains authoritative for Internet/web/current-information search.
- Future files, contacts, calendar, media, device, extension, and third-party providers remain authoritative for their own resources and permissions.

## Current Android Flow

```text
Launcher or user
  → MainActivity
  → IndexRoot
  → IndexQueryEngine
  → InstalledAppsProvider
  → Android PackageManager launcher registry
  → IndexSearchSnapshot
      → ranked provider-scoped results
      → provider issues
  → source-aware UI
  → exact Android component launch handoff
```

The current slice has no remote provider and no local content index.

## External Search Entry Contract

`GoreeCloudIndexContract` defines:

- Action: `com.goreecloud.index.action.SEARCH`
- Initial-query extra: `com.goreecloud.index.extra.QUERY`
- Applications provider ID: `goreecloud.index.provider.apps`

The Android manifest exports the user-visible search activity for the search action. This contract permits GoreeCloud Launcher to invoke Index with an initial query without moving universal search authority into Launcher.

Integrated device acceptance of this handoff remains pending.

## Core Model

`IndexQuery` contains normalized query text and a bounded result limit.

`IndexResult` preserves provider-scoped identity, result type, display fields, relevance score, and an optional typed action.

`IndexAction` currently supports exact Android launcher-component handoff through `LaunchActivity(packageName, className)`.

`IndexProvider` declares stable provider identity, a human-readable display name, and a bounded search operation.

`IndexProviderIssue` identifies a provider whose query failed without exposing provider exception details.

`IndexSearchSnapshot` returns successful results and provider issues independently, allowing partial success and clear degraded-state presentation.

## Query Engine

`IndexQueryEngine`:

- Trims query text.
- Bounds requested results to 1–100.
- Invokes each registered provider independently.
- Catches provider `Exception` failures at the provider boundary.
- Records one sanitized issue per failed provider.
- Preserves healthy provider results.
- Deduplicates by `providerId:resultId`.
- Sorts by descending score, then case-insensitive title, then provider ID.
- Applies the final result cap.

The current synchronous execution path is acceptable only for the first bounded local provider. Before remote, indexed, or expensive providers are enabled, execution must move behind asynchronous/cancellable boundaries with provider-specific timeouts.

## Android Applications Provider

`InstalledAppsProvider` uses `ACTION_MAIN` plus `CATEGORY_LAUNCHER` discovery. The manifest declares that same narrow query contract and does not request `QUERY_ALL_PACKAGES`.

Provider behavior:

- Maintains a volatile immutable snapshot of launcher entries.
- Refreshes when Index resumes.
- Uses modern `ResolveInfoFlags` on Android 13/API 33+ and the compatibility API on older supported releases.
- Removes Index itself from results.
- Preserves exact `ComponentName` identity so different launchable components do not collapse into a package-only record.
- Matches application label and package name.
- Returns provider-scoped typed launch actions.

## Ranking

`IndexTextMatcher` uses deterministic textual scoring:

- Exact title.
- Title prefix.
- Word prefix.
- Title containment.
- Exact secondary/package match.
- Secondary/package prefix.
- Secondary/package containment.
- Blank query receives a low browse score so the local provider can present a bounded application list.

This is an initial deterministic ranker, not the final multi-provider relevance system.

## UI Architecture

`IndexRoot` is a presentation adapter over `IndexSearchSnapshot`. It does not manufacture provider authority or security/privacy state.

The current UI:

- Focuses the search field on entry.
- Identifies provider coverage as **Applications · On-device**.
- Uses safe-drawing insets under edge-to-edge rendering.
- Shows provider issues separately from valid results.
- Distinguishes browse/blank, no-match, degraded-provider, and result states.
- Preserves source labels in result rows.
- Uses typed result actions for handoff.

Glaze UI 2.1.0 is the current target; formal conformance is pending.

## Privacy Boundary

The current Android application provider operates locally. The manifest does not request `INTERNET`, and the source adds no intentional query analytics, telemetry, or persistent search-history store.

This source behavior is not accepted Privacy Shield runtime integration. Future providers that handle private content or remote processing require explicit Privacy Shield authorization and retention decisions before activation.

## Security Boundary

Provider output is data for normalized presentation and typed actions. Index does not infer that a provider or result is trusted, protected, clean, or Wardveil-approved merely because the provider returned successfully.

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

- Provider exception → sanitized provider issue; healthy results preserved.
- No matching application → explicit no-match state.
- No available launcher applications → explicit empty browse state.
- Failed Android launch handoff → user-visible failure feedback.

No remote fallback is activated if a local provider fails. Search queries have no destructive side effects.

## Next Architecture Milestone

The next foundation milestone is asynchronous provider execution with superseded-query cancellation, bounded provider timeouts, and explicit authorization context. Additional providers should then be added one at a time, beginning with a provider whose platform permission and privacy boundaries can be validated end to end.
