# GoreeCloud Index — Architecture

## Status

**Release lifecycle: Development.** Current repository `main` is `9f3aaa9543a8a8351fa2d4713481bccb8199069d`. The last accepted APK/build evidence remains bound to exact source `cc3cc21d6e11dad026253c3371c3b67663d3b726`, workflow `33431294298`, APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, and artifact `9772740479`. Current `0.3.0-dev` source and this V1.3 candidate are newer Development states. Production acceptance and Stable qualification remain false.

## Authority Model

GoreeCloud Index coordinates universal search; it does not own provider resources.

- Android is authoritative for launcher applications, ContactsProvider records, platform permissions, Settings state, and Android handoff behavior.
- GoreeCloud Launcher is an invocation/presentation surface; Index remains the universal-search/indexing authority.
- GoreeCloud Search remains authoritative for Internet/web/current-information search.
- Privacy Shield remains authoritative for consent, purpose, minimization, retention, processing-zone, and destination decisions.
- GoreeCloud Identity remains authoritative for platform identity and GoreeCloud-level authority. Authentication is not blanket authorization to search application/provider records.
- Provider/application logic remains responsible for provider-specific record access and source semantics.
- Wardveil Security remains authoritative for applicable trust/protection/security evidence.
- Everkeep remains authoritative for continuity of applicable durable Index configuration.
- GoreeCloud Mesh may coordinate first-party provider discovery without taking source authority.
- GoreeCloud Manager may consume operational status but does not gain source/provider or OS configuration authority.
- GLAZE UI governs presentation/interaction contracts, not provider truth.

## Query Flow

```text
Launcher or user
  → MainActivity
  → IndexExecutionContext
      → exact provider allowlist
      → local-only processing boundary
      → provider authority evidence
          → Android runtime permission
          → Privacy Shield decision reference
          → GoreeCloud Identity authorization reference
  → IndexRoot / LaunchedEffect(query)
  → IndexQueryEngine
      → blank-query applicability
      → fail-closed authority evaluation
      → AUTHORIZATION_REQUIRED for incomplete authority
      → supervisorScope / concurrent async dispatch
      → per-provider withTimeout
      → preserve external CancellationException
      → normalize provider outcomes
  → IndexSearchSnapshot
      → ranked provider-scoped results
      → AUTHORIZATION_REQUIRED / FAILED / TIMED_OUT issues
  → source-aware UI
  → typed, validated Android action handoff
```

## Core Authority Model

`IndexAuthorityRequirement` currently supports Android runtime permission, Privacy Shield, and GoreeCloud Identity requirements.

`IndexProviderAuthority` consumes evidence. Android permission is a boolean platform state; Privacy Shield/Identity entries require an `ALLOW` outcome plus non-empty reference. `ALLOW_WITH_CONSTRAINTS` intentionally fails closed until Index can enforce the returned obligations. Missing/denied/user-decision-required/unavailable evidence also fails closed.

This is a **consumer contract**, not a substitute authority. No Identity endpoint is invented by Index and no Privacy Shield decision is fabricated locally.

## Provider Contract

`IndexProvider` declares stable identity, display name, processing location, timeout, authority requirements, blank-query support, and suspendable search.

The engine considers only providers applicable to the current query. This prevents non-browsing private sources from generating authority prompts or enumerating data on blank input.

## Applications Provider

`InstalledAppsProvider` supplies scoped launcher discovery, local processing, 500 ms provisional timeout, label/package matching, exact `ComponentName` actions, and no Internet permission or `QUERY_ALL_PACKAGES`.

## Contacts Provider — Current Source, Authority-Gated

`ContactsProvider` exists in current source with:

- Android ContactsProvider authority through `ContactsContract`;
- `LOCAL` processing;
- provisional 750 ms timeout;
- `supportsEmptyQuery=false`;
- `Contacts.CONTENT_FILTER_URI` query path;
- projection limited to `_ID`, `LOOKUP_KEY`, `DISPLAY_NAME_PRIMARY`;
- no phone/email field read in this slice;
- typed `ViewContact` result action;
- required Android permission + Privacy Shield + Identity authority evidence.

The current unavailable platform-authority gateway keeps Contacts non-dispatchable. The engine reports authorization-required for applicable nonblank queries without invoking Contacts until all required authority can be satisfied.

## Settings Navigation Provider — Current Source

The bounded Settings provider searches only repository-defined static navigation metadata and returns typed handoff actions constrained to a closed allowlist.

It does not read setting values, device configuration state, accounts, permission state, or history; it adds no network permission, telemetry, cache, or persistent query state. Android Settings remains the authority for configuration truth and all actual changes. Representative-device and OEM-specific action availability remain separate acceptance gates.

## Action Boundary

Application actions use exact package/class components. Contact actions are accepted only when the parsed URI matches the reviewed Contacts `content` authority/path contract before `ACTION_VIEW` handoff. Settings actions are accepted only when they match the repository-defined static allowlist. Invalid actions fail closed with user-visible feedback.

## UI Architecture and GLAZE UI V1.3

The UI searches authorized sources, shows Applications as active and Contacts as authority-gated, lists provider issues, and distinguishes authorization-required state from operational provider failure/timeout. It preserves safe-drawing insets, semantic headings, bounded targets, and non-animated progress.

The current shared presentation target is **GLAZE UI V1.3 / `1.3.0` — Adaptive Resonance**. This candidate records exact Stable source integration anchor `fc7cc91d2eace8da2371371c2855c24cbcb326a1`, Stable aggregate identifiers, and `1.2.0` rollback baseline. Because Index is a native Compose consumer, web/runtime identifiers are provenance references rather than native imports.

The candidate replaces generic Android dynamic color with deterministic GoreeCloud-owned Light, Dark, and Deep Dark schemes, retains neutral-first Deep Teal/Soft Amber atmosphere, 16/24/32 dp optical geometry, and 48/56 dp target floors. Presentation state does not manufacture authorization, privacy, security, recovery, or provenance truth.

Formal Index Glaze conformance remains pending exact-head automated validation and representative rendered/native, accessibility, form-factor, and performance acceptance.

## Failure and Recovery Model

- Missing authority → provider not dispatched; sanitized `AUTHORIZATION_REQUIRED`.
- Provider exception → sanitized `FAILED`; healthy sibling results preserved.
- Provider timeout → sanitized `TIMED_OUT`; healthy sibling results preserved.
- Parent/query cancellation → propagates.
- Disallowed provider or remote/mixed under local-only → not dispatched.
- Blank query + non-browsing provider → provider not considered and no authority issue emitted.
- Invalid result action → blocked at handoff.
- No silent remote fallback.

## Accepted Historical Build Evidence

`cc3cc21d6e11dad026253c3371c3b67663d3b726` passed exact-main workflow `33431294298` with APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, artifact `9772740479`, digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`.

## Next Architecture Milestone

1. Exact-head validate the current V1.3 candidate.
2. Complete representative rendered/native and accessibility/form-factor acceptance.
3. Establish accepted Privacy Shield/Identity adapter paths plus explicit user-decision flow without weakening fail-closed authority.
4. Perform representative-device Contacts and Settings acceptance.
5. Expand broader file/calendar/provider coverage only after those authority paths are proven rather than simulated.
