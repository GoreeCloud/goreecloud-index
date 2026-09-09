# GoreeCloud Index — Benefits

## Status

**Release lifecycle: Development.** Current repository `main` is `9f3aaa9543a8a8351fa2d4713481bccb8199069d`. The older accepted APK/build evidence remains bound to `cc3cc21d6e11dad026253c3371c3b67663d3b726`; current `0.3.0-dev` source and this V1.3 candidate require independent validation. No production or Stable benefit is claimed from source or green CI alone.

## Current Evidence-Bound Benefits

- Original GoreeCloud-owned native Android universal-search foundation.
- Real on-device application discovery and exact-component launch handoff.
- Structured concurrent provider execution with cancellation, timeout isolation, and healthy-result preservation.
- Fail-closed allowlisting and local-only processing.
- Explicit provider provenance instead of an opaque result pool.
- GoreeCloud Search remains a separate Internet authority rather than being hidden inside local Index logic.
- Exact-source Android CI and APK evidence discipline.

## Current Source Improvement: Authority Before Private Data

The Contacts source slice adds a stronger privacy/authorization architecture before expanding private-data coverage:

- Android permission, Privacy Shield, and GoreeCloud Identity are distinct prerequisites.
- Missing/denied/unavailable authority prevents dispatch instead of allowing a best-effort private-data query.
- Constrained authorization is not flattened into unrestricted allow.
- Contacts do not enumerate on a blank query.
- The provider requests only contact identity/display metadata needed for search presentation, not phone/email fields.
- Contact actions return control to Android's authoritative contact surface instead of creating an Index-owned contact database.
- The current runtime keeps Contacts non-dispatchable until real platform authority adapters exist.

These are source-level architectural benefits. Accepted Privacy Shield/Identity integration and user-visible Contacts functionality remain future evidence gates.

## Current Source Improvement: Bounded Settings Navigation

The Settings-navigation source slice improves utility without turning Index into an Android configuration database:

- only repository-defined static navigation destinations are searched;
- no Android setting values, device configuration state, accounts, permission state, or history are read;
- handoff is constrained to typed actions in a closed allowlist;
- no network access, telemetry, cache, or persistent query history is added;
- Android Settings remains authoritative for configuration truth and actual changes.

Representative-device and OEM-specific behavior remain acceptance gates.

## Current Candidate Improvement: GLAZE UI V1.3

The V1.3 candidate moves Index away from generic Android dynamic-color sampling to deterministic GoreeCloud-owned native presentation while preserving product truth boundaries.

- Current shared target: **GLAZE UI V1.3 / `1.3.0` — Adaptive Resonance**.
- Exact Stable source integration anchor: `fc7cc91d2eace8da2371371c2855c24cbcb326a1`.
- Deterministic Light, Dark, and Deep Dark Compose schemes.
- Neutral-first Deep Teal/Soft Amber atmosphere.
- 16/24/32 dp optical geometry and 48/56 dp target floors.
- Presentation color does not imply authorization, privacy, security, recovery, or result trust.

These are Development source improvements only until exact-head automated and representative rendered/native acceptance is complete.

## Accepted Historical Build Evidence

- Source `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow `33431294298`
- APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact `9772740479`
- Artifact digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

## Benefits Not Yet Claimable

Unified files/calendar/media search; enabled Contacts search; Android setting-value indexing; accepted Privacy Shield/Identity/Wardveil/Everkeep/Mesh/Manager integration; first-party Mesh provider discovery; optional third-party providers; cross-device search; Internet results through GoreeCloud Search; streaming results; complete Glaze UI application/accessibility acceptance; representative-device performance; production deployment; and Stable qualification remain unaccepted until their own evidence exists.
