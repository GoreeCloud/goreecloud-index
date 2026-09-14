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

Current Search capability evidence may advertise `POST` and `GET`, with `POST` preferred for first-party consumers. The preferred production query transport is `json_body`, with `application/json` request/response media types, an explicit Privacy Shield authorization requirement, a 16 KiB request ceiling, and a bounded result maximum.

Development Index builds may consume explicitly non-production or legacy-GET capability evidence only when the build is itself Development and the exception is recorded in repository-local evidence.

Stable/production Index builds must require Search capability evidence that is explicitly production accepted **and** matches the complete private transport contract: POST available and preferred, `json_body` preferred, JSON request/response media types, Privacy Shield authorization required, and the accepted request-size bound. A merely reachable endpoint or GET-compatible capability is insufficient.

A Development exception must never be interpreted as permission to promote Index or Search to Stable.

## Minimized request

Index delegates only:

- the normalized query string;
- the selected Search category;
- a bounded result limit.

Index must not send installed apps, contacts, files, calendar items, local result sets, device inventory, Identity identifiers, or unrelated authorization evidence payloads to Search.

The preferred production request representation is a bounded `application/json` POST body. Query text must not be duplicated into the request URL when the POST transport is used.

## Result handling

Index independently validates every executable Search destination. Only normalized HTTP(S) URLs with a valid host and no embedded user-info credentials may become `OpenWeb` actions.

Invalid results are dropped as executable actions by the Index validation pipeline and must produce sanitized provider issue evidence without suppressing healthy sibling results. If Search returns more entries than the delegated limit, Index preserves its consumer-owned bounded cap rather than widening the result set.

Search-owned raw numeric ranking metadata is transport/source metadata; Index must not compare that scale directly with app, contact, setting, file, or other provider scores. Current source integration instead preserves Search-owned result order only as a same-provider tie-breaker when Index-normalized relevance is equal.

## Degraded responses

When Search reports partial degradation, Index may preserve valid results while surfacing degraded provider status. Degradation must not be hidden or rewritten as healthy availability. Invalid-result evidence takes precedence where applicable while valid sibling results remain usable.

## Cancellation

Superseded user queries must cancel previous Search delegation through the Index query lifecycle. Cancellation is not a provider failure.

## Browser handoff

Web results selected from Index hand off to Browser through a typed validated web action. Index does not gain tab/session authority, and Browser does not gain Index provider authority by opening the destination.

## Glaze UI

Index-owned search surfaces must use the latest approved Stable Glaze UI release and retain Index-local acceptance evidence. Search results do not inherit Search's Glaze status, and Search availability cannot satisfy Index design-system acceptance.

## Stability boundary

The existence of the Search provider adapter is source integration evidence only. Stable acceptance requires current Search capability evidence, Privacy Shield enforcement, supported runtime behavior, accessibility, degradation handling, cancellation behavior, and representative real-device validation.
