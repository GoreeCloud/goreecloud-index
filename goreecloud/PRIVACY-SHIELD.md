# GoreeCloud Index — Privacy Shield Boundary

## Status

The files in this directory are **downstream Development declarations** owned by `GoreeCloud/goreecloud-index`.

They do not establish a centrally accepted Privacy Shield adapter, production approval, live Privacy Shield connectivity, or Contacts runtime authorization.

Privacy Shield remains authoritative for shared privacy contracts and applicable authorization/evidence semantics. Index remains authoritative for its Android runtime implementation and enforcement of provider dispatch.

## Application privacy manifest

`privacy-shield.application-manifest.json` declares the current bounded Index data-use scope under the Privacy Shield application-manifest v1 contract:

- application identity: `com.goreecloud.index`;
- purpose: explicit user-initiated universal search;
- current declared resources: launcher-visible Android applications and Android contacts;
- operation: search;
- processing zone: local only;
- destination: the GoreeCloud Index UI;
- retention mode: none;
- AI usage: false;
- external processors: none.

This manifest is deliberately narrower than the long-term Index provider roadmap. Files, calendar, GoreeCloud Search, extensions, third-party sources, connected-device resources, and other future providers must be added only when their actual source/runtime implementation and privacy boundary exist.

## Downstream adapter candidate

`privacy-shield.adapter.json` is a downstream adapter declaration candidate against Privacy Shield adapter schema version 1.

The current candidate declares only `data-minimization` because that behavior is directly reflected in the accepted source boundary: the Contacts provider reads only contact ID, lookup key, and primary display name and excludes phone-number/email fields from the current slice.

It intentionally does **not** claim `privacy-status`, `retention-controls`, `deletion-controls`, `portable-export`, or other Privacy Shield capabilities that Index has not implemented and validated as runtime capabilities.

The declaration sets:

- `runtime_acceptance_required: true`;
- `production_approved: false`;
- local-first processing;
- no raw private activity export for status;
- no remote tracker learning; and
- no remote tracker telemetry.

Privacy Shield's central `adapters/` directory remains the central-integration boundary. This downstream declaration must not be represented as centrally promoted merely because it passes Index repository validation.

## Contacts authorization relationship

The application manifest and downstream adapter declaration do not replace the runtime authorization path.

Contacts dispatch still requires all of the following:

1. Android `READ_CONTACTS` permission;
2. applicable Privacy Shield authorization evidence;
3. applicable GoreeCloud Identity authorization evidence; and
4. Index's existing fail-closed provider eligibility checks.

The shipped Development application currently uses `UnavailableIndexPlatformAuthorityGateway`, so Contacts remains non-dispatchable even when Android permission is granted.

## Acceptance boundary

Source/schema declaration validation can prove that Index has a coherent declared privacy boundary. It cannot prove:

- live Privacy Shield decision production;
- consent or user-decision UX acceptance;
- capability issuance or verification;
- central adapter promotion;
- representative-device behavior;
- production deployment; or
- Stable qualification.
