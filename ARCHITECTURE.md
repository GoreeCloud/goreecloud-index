# GoreeCloud Index — Architecture

## Status

**Release lifecycle: Development.** Accepted `main` is `cc3cc21d6e11dad026253c3371c3b67663d3b726`. The `0.3.0-dev` Contacts/authority architecture is branch source pending exact-head validation and merge acceptance. Production acceptance and Stable qualification remain false.

## Authority Model

GoreeCloud Index coordinates universal search; it does not own provider resources.

- Android is authoritative for launcher applications, ContactsProvider records, platform permissions, and Android handoff behavior.
- GoreeCloud Launcher is an invocation/presentation surface; Index remains the universal-search/indexing authority.
- GoreeCloud Search remains authoritative for Internet/web/current-information search.
- Privacy Shield remains authoritative for consent, purpose, minimization, retention, processing-zone, and destination decisions.
- GoreeCloud Identity remains authoritative for platform identity and GoreeCloud-level authority. Authentication is not blanket authorization to search application/provider records.
- Provider/application logic remains responsible for provider-specific record access and source semantics.
- Wardveil Security remains authoritative for applicable trust/protection/security evidence.
- Everkeep remains authoritative for continuity of applicable durable Index configuration.
- GoreeCloud Mesh may coordinate first-party provider discovery without taking source authority.

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

`InstalledAppsProvider` remains the accepted provider: scoped launcher discovery, local processing, 500 ms provisional timeout, label/package matching, exact `ComponentName` actions, and no Internet permission or `QUERY_ALL_PACKAGES`.

## Contacts Provider — Branch Source

`ContactsProvider` is the second provider implementation:

- Android ContactsProvider authority through `ContactsContract`;
- `LOCAL` processing;
- provisional 750 ms timeout;
- `supportsEmptyQuery=false`;
- `Contacts.CONTENT_FILTER_URI` query path;
- projection limited to `_ID`, `LOOKUP_KEY`, `DISPLAY_NAME_PRIMARY`;
- no phone/email field read in this slice;
- typed `ViewContact` result action generated from `Contacts.getLookupUri`;
- required Android permission + Privacy Shield + Identity authority evidence.

Current MainActivity registers Contacts but supplies Privacy Shield/Identity evidence as unavailable. Therefore the engine reports authorization-required for nonblank queries and does not invoke Contacts. This preserves source progress without claiming platform integration.

## Action Boundary

Application actions use exact package/class components. Contact actions are accepted only when the parsed URI has scheme `content`, authority `com.android.contacts`, and a contacts path before `ACTION_VIEW` is issued. Invalid action URIs fail closed with user-visible feedback.

## UI Architecture

The UI searches “authorized sources,” shows Applications as active and Contacts as authority-gated, lists all provider issues, and distinguishes authorization-required state from operational provider failure/timeout. It preserves safe-drawing insets, semantic headings, bounded targets, and non-animated progress.

Glaze UI 2.1.0 is the current Stable target; formal Index conformance remains pending.

## Failure and Recovery Model

- Missing authority → provider not dispatched; sanitized `AUTHORIZATION_REQUIRED`.
- Provider exception → sanitized `FAILED`; healthy sibling results preserved.
- Provider timeout → sanitized `TIMED_OUT`; healthy sibling results preserved.
- Parent/query cancellation → propagates.
- Disallowed provider or remote/mixed under local-only → not dispatched.
- Blank query + non-browsing provider → provider not considered and no authority issue emitted.
- Invalid result action → blocked at handoff.
- No silent remote fallback.

## Accepted Main Evidence

`cc3cc21d6e11dad026253c3371c3b67663d3b726` passed exact-main workflow `33431294298` with APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, artifact `9772740479`, digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`.

## Next Architecture Milestone

After this branch is source-valid, the next milestone is an accepted Privacy Shield/Identity adapter path plus explicit user decision flow, followed by representative-device Contacts acceptance. Broader file/calendar/provider expansion remains gated until that authority path is proven rather than simulated.
