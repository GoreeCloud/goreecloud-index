# GoreeCloud Search provider foundation

**Lifecycle:** Development source foundation only. This document does not establish a live Search transport, Android Internet permission, service discovery, production endpoint configuration, production Privacy Shield acceptance, Wardveil protected state, Release Candidate status, or Stable status.

## Role

GoreeCloud Index is the universal provider coordinator. GoreeCloud Search remains the authoritative GoreeCloud service for Internet, web, and current-information discovery.

Index therefore represents Search as one clearly identified remote provider instead of duplicating Search's upstream provider integrations.

## Initial authority boundary

The provider is declared as:

- provider ID: `goreecloud.index.provider.search`;
- display name: `GoreeCloud Search`;
- processing location: `REMOTE`;
- empty-query support: disabled;
- Privacy Shield authority: required before dispatch;
- provider timeout: 5 seconds, matching the Index engine's existing maximum provider window.

The application remains `localOnly=true` by default and the Search provider is not registered in `MainActivity` in this foundation. This keeps remote execution impossible until a separately reviewed GoreeCloud Search client, endpoint/service-discovery mechanism, runtime authority evidence, and user-facing remote-search policy are available.

## Data minimization

`GoreeCloudSearchRequest` contains only:

- normalized query text;
- Search category (`general` in this initial contract);
- bounded result limit.

It intentionally has no fields for local provider identities, local results, file or contact content, calendar data, application inventory, device settings, query history, GoreeCloud Identity identifiers, Privacy Shield evidence references, Wardveil evidence, Browser state, Vault references, or credentials.

Privacy Shield evidence gates whether the remote provider may run; the evidence reference itself is not part of the Search request.

## Response validation

The provider fails closed when the Search response does not identify the same delegated query and category.

Search owns its internal web ranking. Index does not import Search's independent numeric score scale directly into universal ranking. Web results are normalized back through Index's existing text-matching score contract before cross-provider aggregation so a remote service cannot inject an unbounded universal Index score.

Each result is re-attributed to the GoreeCloud Search Index provider. Search's own underlying provider identities do not replace the Index provider identity that was authorized and dispatched.

## URL safety

A Search result can become an `OpenWeb` action only when its URL:

- parses as an absolute URI;
- uses `https` or `http`;
- has a non-empty host;
- contains no URI user-info credentials.

The URI is normalized and converted to an ASCII representation before it becomes the provider-scoped result ID and executable action target.

Unsafe URLs are emitted with an invalid provider result ID so the common Index provider-result integrity boundary discards them and reports `INVALID_RESULT`. Valid siblings remain available.

## Current acceptance boundary

This foundation deliberately does not:

- add an HTTP client;
- add `android.permission.INTERNET`;
- choose or hard-code a Search deployment endpoint;
- add service discovery;
- install credentials or secrets;
- register Search in the runtime provider list;
- switch Index out of local-only mode;
- claim production Privacy Shield/Wardveil acceptance;
- establish representative-device, accessibility, performance, signing, release, or production evidence.

Those steps require separate implementation and acceptance evidence.
