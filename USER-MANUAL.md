# GoreeCloud Index — User Manual

## Current Development Scope

**Release lifecycle: Development.** GoreeCloud Index is **not Stable or production accepted**. The current accepted Android `0.2.0-dev` application implements one bounded provider: launcher-visible applications on the current Android device, backed by the accepted asynchronous provider-runtime foundation.

Files, contacts, calendar, GoreeCloud service content, connected devices, extensions, optional third-party services, and Internet results are not available providers yet.

## Opening Index

Open **GoreeCloud Index Dev** from the Android launcher. GoreeCloud Launcher may also invoke Index through `com.goreecloud.index.action.SEARCH` and optionally pass `com.goreecloud.index.extra.QUERY`.

Representative-device Launcher→Index acceptance remains pending.

## Browsing and Searching Applications

When the search field is blank, Index presents a bounded list of launcher-visible applications from **Applications · On-device**.

To search:

1. Open GoreeCloud Index Dev.
2. Enter part or all of an application name in **Search applications**.
3. Index may briefly show **Searching applications…**.
4. Matching applications appear under **Application results**.
5. Tap a result to ask Android to launch the exact launcher component represented by that result.

Matching includes exact application names, title prefixes, word prefixes, title containment, package-name exact/prefix/containment matching, and deterministic ranking.

When the query changes, the previous in-flight search is cancelled before the new query proceeds.

## Search States

- **Searching:** current query executing.
- **Browse:** blank query with available launcher applications.
- **No searchable applications:** provider returned no current launcher entries.
- **No application matches:** provider completed successfully but found no match.
- **Provider temporarily unavailable:** provider failed; failure is isolated from healthy results.
- **Provider took too long:** provider exceeded its timeout; Index can preserve healthy sibling results.
- **Launch failure:** Android could not open the selected component.

Failure or timeout does not silently activate remote fallback.

## Local-Only Execution Boundary

Current MainActivity explicitly allows the Applications provider and sets `localOnly=true`. A future remote or mixed provider would not be dispatched through that path merely because it was registered.

This is an internal fail-closed guard, not accepted Privacy Shield policy and not accepted GoreeCloud Identity authorization.

## Provider Timeout

The Applications provider declares a provisional 500 ms timeout. The engine caps provider timeout declarations at a five-second Development safety ceiling.

These values are not representative-device performance guarantees and may change after testing.

## Privacy and Security

The Applications provider operates locally. The application requests neither Android `INTERNET` nor unrestricted `QUERY_ALL_PACKAGES` permission and intentionally adds no query analytics, remote telemetry, or persistent search-history store.

These source properties do not establish accepted Privacy Shield runtime integration.

Application results are typed to exact launcher components. Index does not claim that an application/provider is trusted, protected, clean, or Wardveil-approved merely because it appears in search. Wardveil runtime evidence remains pending.

## Known Limitations

The current Development build does not provide file/folder, contact, calendar, media, settings, Search Internet, Mesh-discovered first-party, extension, or third-party providers; local content indexing; incremental/streaming result delivery; provider health/capability negotiation; saved search/history/provider settings; accepted Privacy Shield/Wardveil/Everkeep/Identity/Mesh integrations; formal Glaze UI 2.1.0 conformance; representative-device accessibility/performance acceptance; production signing/deployment; or Stable qualification.

## Development Package and Validation

- Application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Version: `0.2.0-dev`
- Version code: `2`

Authoritative main commit `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` passed exact-main workflow run `33429792389` after PR #4 candidate `d8b563705ccf1d05444df18e7a593a454d4c4103` passed run `33429486374`.

Accepted-main APK SHA-256: `a2605bb1e993dd027afc68b0900b60f2fcf9567ec59399e5a02a178af1cc815f`.

Accepted-main artifact: `9772201920`.

This is Development source/build evidence only.
