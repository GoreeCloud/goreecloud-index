# GoreeCloud Index — GoreeCloud Search Integration

**Status:** Development contract  
**Role:** Remote Internet/web provider for GoreeCloud Index

## Authority

GoreeCloud Index is the universal search/indexing coordinator. GoreeCloud Search remains authoritative for Internet, web, and current-information search. Index may federate Search results into a universal result set, but it must not duplicate Search provider orchestration or convert Search into a local source.

## Eligibility

The Search provider is eligible only when all of the following are true:

- the query is non-empty;
- the execution context permits remote processing;
- `search` is explicitly present in the provider allowlist;
- applicable Privacy Shield authorization evidence is present and enforceable;
- the Search `search.query` capability is current, authoritative, version-compatible, structurally valid, and appropriate for the current build lifecycle.

Local-only execution must not perform Search capability preflight or network search.

## Capability lifecycle

The initial Search capability contract is version `1`, capability ID `search.query`, endpoint `/api/v1/search`.

Development builds may consume explicitly non-production capability evidence only when the build is itself Development and the exception is recorded in repository-local evidence. Stable/production Index builds must require Search capability evidence that is explicitly production accepted.

A Development exception must never be interpreted as permission to promote Index or Search to Stable.

## Minimized request

Index delegates only:

- the normalized query string;
- the selected Search category;
- a bounded result limit.

Index must not send installed apps, contacts, files, calendar items, local result sets, device inventory, Identity identifiers, or unrelated authorization evidence payloads to Search.

## Result handling

Index independently validates every executable Search destination. Only normalized HTTP(S) URLs with a valid host and no embedded user-info credentials may become `OpenWeb` actions.

Invalid results are dropped as executable actions and must produce sanitized provider issue evidence without suppressing healthy sibling results.

Search-owned ranking metadata may inform future blending, but Index remains responsible for cross-provider composition and must avoid allowing one remote score scale to dominate unrelated local providers without a documented normalization strategy.

## Degraded responses

When Search reports partial degradation, Index may preserve valid results while surfacing degraded provider status. Degradation must not be hidden or rewritten as healthy availability.

## Cancellation

Superseded user queries must cancel previous Search delegation through the Index query lifecycle. Cancellation is not a provider failure.

## Browser handoff

Web results selected from Index hand off to Browser through a typed validated web action. Index does not gain tab/session authority, and Browser does not gain Index provider authority by opening the destination.

## Glaze UI

Index-owned search surfaces must use the latest approved Stable Glaze UI release and retain Index-local acceptance evidence. Search results do not inherit Search's Glaze status, and Search availability cannot satisfy Index design-system acceptance.

## Stability boundary

The existence of the Search provider adapter is source integration evidence only. Stable acceptance requires current Search capability evidence, Privacy Shield enforcement, supported runtime behavior, accessibility, degradation handling, cancellation behavior, and representative real-device validation.
