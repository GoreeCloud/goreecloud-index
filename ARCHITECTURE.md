# GoreeCloud Index — Architecture

## Status

This document describes the initial native Android source architecture. The Android application-search slice is source implementation only and is not production or Stable acceptance.

## Architecture Boundary

GoreeCloud Index coordinates search. It does not become the authority for provider-owned resources. Source applications and services remain authoritative for their own data and actions.

The initial Android slice is intentionally narrow:

`Search UI → SearchEngine → SearchProvider → Android launcher-app provider → Android PackageManager`

Only launcher-visible applications participate. The implementation does not request unrestricted package visibility and does not search files, contacts, calendar data, Internet results, GoreeCloud services, extensions, or third-party services yet.

## Core Model

`SearchQuery` holds the user-entered query and exposes a trimmed normalized form.

`SearchProviderDescriptor` declares provider identity, resource types, and processing mode.

`SearchResult` preserves provider-scoped identity, resource type, display fields, relevance score, and an optional typed action.

`SearchSnapshot` returns ranked results together with sanitized provider issues so a failed provider can degrade independently.

## Provider Contract

Every search source implements `SearchProvider` and exposes a descriptor plus a bounded query operation. The current contract is synchronous because the first source is a small on-device launcher-app lookup. Future remote, indexed, or expensive providers must move behind cancellable asynchronous boundaries before they are enabled in user-facing search.

Provider failures are isolated by `SearchEngine`: one provider exception becomes a provider-scoped unavailable state rather than crashing the entire query.

## Android Application Provider

`AndroidAppSearchProvider` queries only Android activities that declare `ACTION_MAIN` plus `CATEGORY_LAUNCHER`. The manifest declares that same narrow package-visibility query. The provider searches application labels and package names, assigns simple deterministic relevance scores, removes duplicate launcher entries by package identity, and caps a query at 50 results.

A result action carries only the target package identity. `MainActivity` resolves the launch intent at action time and reports an understandable failure if Android cannot launch the target.

## Privacy Boundary

The current provider operates on-device and does not transmit queries or application inventory. The source contains no analytics or remote telemetry. Search text is held in Compose UI state and is not written to persistent storage or logs by this slice.

This source behavior is not a claim of accepted Privacy Shield runtime integration. Privacy Shield integration remains pending.

## Security Boundary

Provider output is treated as data for presentation and typed actions. The application does not claim that a result or provider is safe, trusted, protected, or Wardveil-approved. Wardveil Security runtime integration remains pending.

## GoreeCloud Platform Boundaries

- **Glaze UI:** the Android surface targets Glaze UI 2.1.0 semantics and accessibility expectations. Consumer conformance is pending validation.
- **Privacy Shield:** runtime authorization and privacy-state integration are pending.
- **Wardveil Security:** provider trust/security evidence integration is pending.
- **Everkeep:** durable-state protection/recovery decisions are pending. The current query state is transient.
- **GoreeCloud Identity:** account/profile/device authority integration is pending.
- **GoreeCloud Mesh:** first-party provider discovery and coordination are pending.
- **GoreeCloud Search:** Internet-provider delegation is pending.

## Failure and Degraded States

The initial user surface deliberately handles:

- Blank query.
- No matching applications.
- Provider failure.
- Application launch failure.
- Successful result presentation and launch handoff.

No remote fallback is activated if the local provider fails.

## Future Architecture Work

The next architecture milestone should make provider execution asynchronous and cancellable, add explicit permission/authorization evaluation, and introduce additional providers one at a time with independent acceptance evidence. Files, contacts, calendar, GoreeCloud Search, extension, and third-party providers must not be enabled merely by adding UI categories; each requires a real provider implementation and applicable authority checks.
