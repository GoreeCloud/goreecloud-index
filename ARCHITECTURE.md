# GoreeCloud Index — Architecture

## Status

**Release lifecycle: Development.** Accepted `main` remains `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Current branch work is Development source pending normal merge acceptance. Production acceptance and Stable qualification remain false.

The latest verified implementation checkpoint is `b201e182d0698bee55f19e37e46f9d42f419c638`. Platform Contract #47 and Android Index foundation validation #174 passed on that exact revision, including repository and provider contract validation, unit tests, lint, Development APK assembly, APK identity verification, and evidence upload.

## Authority Model

GoreeCloud Index coordinates universal search; it does not own provider resources.

- Android is authoritative for launcher applications, ContactsProvider records, platform permissions, Android Settings destinations, and Android handoff behavior.
- GoreeCloud Launcher is an invocation/presentation surface; Index remains the universal-search/indexing authority.
- GoreeCloud Search remains authoritative for Internet/web/current-information search.
- GoreeCloud Browser remains authoritative for browser navigation, tab/session state, page lifecycle, and executable web-result navigation.
- Privacy Shield remains authoritative for consent, purpose, minimization, retention, processing-zone, and destination decisions.
- GoreeCloud Identity remains authoritative for platform identity and GoreeCloud-level authorization. Authentication is not blanket authorization to search provider records.
- Provider/application logic remains responsible for provider-specific record access and source semantics.
- Wardveil Security remains authoritative for applicable trust/protection/security evidence.
- Everkeep remains authoritative for continuity of applicable durable Index configuration.
- GoreeCloud Mesh may coordinate first-party provider discovery without taking source authority.

User source selection is **not** authority. A selected provider must still pass scope, provider-contract, processing-location, Android permission, Privacy Shield, GoreeCloud Identity, and other applicable authority gates before Index can dispatch it.

## Query Flow

```text
Launcher, Browser, or user
  → Index invocation
  → MainActivity
  → IndexDevelopmentSourcePolicy
      → sanitize session-selected provider IDs against reviewed local selectable set
      → reject unknown providers and GoreeCloud Search from Development user selection
      → force localOnly=true
      → preserve provider authority evidence without granting new authority
  → IndexExecutionContext
      → exact provider allowlist
      → local/remote processing boundary
      → provider authority evidence
  → IndexRoot / query lifecycle
      → query text + current session source-selection state
      → changing query or enabled sources replaces the active collection
  → IndexQueryEngine.searchIncrementally
      → normalize query + clamp result limit
      → blank-query applicability
      → fail-closed scope / contract / authority evaluation
      → emit initial snapshot with static compatibility / authorization issues
      → launch eligible providers concurrently
      → bounded per-provider timeout
      → validate provider provenance / identity / title / source-order / result-count contract
      → keep at most the requested number of strongest distinct provider-local results
      → after each provider completion, rebuild the complete accumulated snapshot through one composition path
          → normalize cross-provider textual relevance
          → prefer healthy source only when normalized relevance ties
          → prefer LOCAL, then MIXED, then REMOTE only when relevance and health tie
          → deterministic identity fallback
          → provider-scoped deduplication and final result cap
      → emit incrementally updated snapshot
  → IndexRoot collects the Flow
      → earlier authorized results can render while slower providers remain in flight
      → late stronger results may deterministically re-rank the accumulated snapshot
      → replacing/cancelling collection cancels outstanding provider work
  → typed validated handoff
```

The one-shot `search()` API consumes the **final** `searchIncrementally()` snapshot rather than implementing an independent composition algorithm. This prevents one-shot and interactive search semantics from drifting apart.

## Core Authority Model

`IndexAuthorityRequirement` currently supports Android runtime permission, Privacy Shield, and GoreeCloud Identity requirements.

`IndexProviderAuthority` consumes evidence. Android permission is a platform state; Privacy Shield/Identity entries require an accepted outcome plus a non-empty reference. `ALLOW_WITH_CONSTRAINTS` fails closed until Index can enforce every returned obligation and must not be treated as `ALLOW`. Missing, denied, user-decision-required, unavailable, stale, mismatched, or otherwise unenforceable evidence also fails closed.

This is a **consumer contract**, not substitute authority. No Identity endpoint is invented by Index and no Privacy Shield decision is fabricated locally.

Incremental delivery does not weaken this boundary. Static compatibility and authorization issues are computed before provider dispatch and are present in the initial Flow snapshot. A provider that fails scope, contract, or authority checks is never launched merely to produce incremental UI state.

Session source controls also do not weaken the boundary. Turning a source on only places that reviewed local provider into the candidate allowlist; authority evaluation still happens independently for every query. Turning a source off prevents dispatch without altering the provider's underlying Android or GoreeCloud authority state.

## Provider Contract

`IndexProvider` declares stable identity, display name, processing location, timeout, authority requirements, blank-query support, contract version, and suspendable search behavior.

The engine considers only providers applicable to the current query. This prevents non-browsing private sources from generating unnecessary authority prompts or enumerating data on blank input.

Providers must be independently cancellable, bounded by timeout, and unable to suppress healthy sibling providers through ordinary failure. Providers receive `IndexQuery.maxResults` and are expected not to return more results than requested. The engine independently enforces that boundary: an over-limit response is reported as `INVALID_RESULT`, then reduced to the strongest distinct provider-local results up to the requested limit before cross-provider composition. This prevents a buggy or hostile provider from expanding federation work simply by ignoring the query contract.

Bounding does not trust response order. Index applies same-provider score/source-order semantics first, preserves the strongest duplicate for each provider-local ID, and only then takes the requested result count. Invalid-result detection still precedes an ordinary `DEGRADED` signal so contract violations remain visible.

Provider completion order controls only **when** a partial snapshot can be emitted. It does not control ranking. Every emission is recomposed from accumulated provider outcomes through the same comparator, validation, and deduplication logic.

The provider-declared `processingLocation` is available to Index-owned composition only after the provider passes scope and authority gates. It is not authorization: a remote provider must first be eligible and authorized before any ranking tie-break can consider its results.

## Development Source-Control Policy

`IndexDevelopmentSourcePolicy` is the current user-selectable source boundary for the Android Development application.

The policy intentionally exposes only the already integrated local providers:

- Applications;
- Settings; and
- Contacts.

It does **not** expose GoreeCloud Search, arbitrary provider IDs, future providers, or third-party providers as user-selectable Development sources. Requested provider IDs are intersected with the reviewed selectable set before an `IndexExecutionContext` is built.

Every execution context constructed through this policy forces `localOnly=true`. This is independent of the visual state of the UI and prevents a source-control regression from silently broadening processing to a remote provider.

The policy preserves already supplied provider-authority evidence, but it does not create, upgrade, or infer authority. For example, selecting Contacts cannot satisfy missing Android `READ_CONTACTS`, Privacy Shield, or GoreeCloud Identity authority; Contacts remains non-dispatchable until the underlying authority requirements are satisfied.

The current source-selection state is session-scoped Compose state only. It is deliberately not persisted to disk, synchronized, or backed up. Any future durable provider preferences require explicit profile/device scope plus applicable Everkeep continuity/recovery semantics before they become authoritative durable Index state.

## Applications Provider

`InstalledAppsProvider` is a local provider using scoped launcher discovery, label/package matching, exact component actions, and no `QUERY_ALL_PACKAGES` requirement. It already respects `query.maxResults` before returning results.

Applications may be disabled for the current Index session. This affects Index dispatch only; it does not change Android package visibility or application authority.

## Contacts Provider

The Contacts provider uses Android ContactsProvider authority through `ContactsContract` with local processing and a permission/authority gate.

- no blank-query enumeration;
- filtered lookup through `Contacts.CONTENT_FILTER_URI`;
- projection limited to contact identity/display fields required by the result;
- no phone/email field read in the current slice;
- typed contact-view action;
- Android permission plus Privacy Shield and Identity authority evidence;
- bounded result collection using the requested result count and the provider's own hard ceiling.

Incomplete or unenforceable authority must prevent provider dispatch. Enabling the Contacts source toggle is not an opt-in substitute for Android permission or GoreeCloud authority.

## Settings Provider

The Settings provider is a bounded local navigation provider over repository-defined destination metadata. It does not read device setting values or configuration state. It already limits output to the requested result count.

Actions must remain inside the exact reviewed allowlist. Arbitrary Android actions, URLs, data URIs, and extras are not supported by this provider contract.

Settings source eligibility is now centralized in `IndexDevelopmentSourcePolicy`; `MainActivity` no longer carries a second provider-ID allowlist. The dedicated Settings validation guard checks that Settings remains present in the centralized local-only source policy and that its typed handoff stays bounded.

## GoreeCloud Search Provider

The Search provider is the remote Internet/current-information provider.

- provider identity remains distinct from local providers;
- remote processing must be explicitly allowed;
- Privacy Shield evidence is required before delegation;
- local-only mode must not perform capability preflight or search;
- the initial Search contract uses capability `search.query`, contract version `1`, endpoint `/api/v1/search`;
- the delegated request is minimized to query, category, and result limit;
- Search response consumption is independently capped to the authorized/requested limit;
- returned destinations are revalidated by Index before becoming executable web actions;
- degraded Search responses may preserve valid results while retaining degraded status.

Development builds may consume explicitly Development-only Search capability evidence only as Development evidence. Stable/production Index builds must require explicitly production-accepted Search capability evidence.

The incremental engine is transport-neutral: it can incorporate a future authorized remote Search provider when that provider becomes eligible, but it does **not** itself enable remote Search, acquire Privacy Shield authority, or authenticate a requester.

The current session source-control UI cannot enable GoreeCloud Search. This is intentional fail-closed behavior while accepted requester/service authentication, real Privacy Shield decision acquisition, Search-side capability verification, and remote-processing product acceptance remain incomplete.

See [`docs/SEARCH_INTEGRATION.md`](docs/SEARCH_INTEGRATION.md).

## Result and Action Boundary

Application actions use exact package/class components.

Contact actions are accepted only when the parsed URI has the expected Android contacts scheme/authority/path before `ACTION_VIEW` is issued.

Settings actions are accepted only from the static reviewed allowlist.

Web actions are accepted only after Index validates an HTTP(S) destination with a valid host and no embedded user-info credentials. Browser remains responsible for its own navigation/security policy after handoff.

Invalid executable actions fail closed with sanitized user-visible/provider issue handling.

## Ranking and Deduplication

Index is responsible for cross-provider composition. Provider-local scores are authoritative only inside the provider that produced them; raw score magnitudes are not compared across provider boundaries.

The current Development engine implements explicit normalization, bounded source health, bounded local-first composition, bounded provider fan-out, and deterministic incremental recomposition:

- cross-provider ordering uses Index-owned normalized textual relevance derived from title and subtitle match quality;
- exact, prefix, token-prefix, contained-title, and secondary-text matches use one Index-owned scale;
- a provider with an arbitrarily large private score cannot outrank a stronger textual match from another provider merely because its numeric scale is larger;
- provider-local scores remain available for ordering results from the same provider;
- provider `sourceOrdinal` remains a same-provider tie-breaker when provider-local scores tie;
- provider results are validated, provider-locally ordered, deduplicated by provider-owned result ID, and capped to the requested result count before federation;
- an over-limit provider response remains visible as `INVALID_RESULT` rather than being silently accepted or relabeled as degradation;
- when different providers have equal normalized relevance, a healthy source is preferred over a source that explicitly returned `DEGRADED` status;
- source health is subordinate to relevance: a stronger match from a degraded provider still outranks a weaker match from a healthy provider;
- when normalized relevance and health are equal, processing location is a final privacy-preserving tie-breaker: `LOCAL` before `MIXED` before `REMOTE`;
- processing location is subordinate to relevance and health: a stronger remote result still outranks a weaker local result, and a healthy remote result can outrank an equally relevant degraded local result;
- location ranking never makes an ineligible provider eligible and never bypasses `localOnly`, allowlisting, Privacy Shield, Identity, or other authority gates;
- equal cross-provider normalized relevance, equal health, and equal location fall back to deterministic stable title/provider/id ordering rather than raw provider score;
- final provider-scoped deduplication remains as defense in depth without collapsing distinct resources owned by different providers;
- every incremental emission uses these same rules, so a slower but stronger result may safely move ahead of an earlier weaker result without introducing completion-order ranking authority.

This is a bounded baseline, not the final blending model. Future ranking work can add explicit factors such as result type, source confidence, source-owned recency, user-declared source preference, richer capability/health evidence, and privacy cost more specific than processing location. Those factors must remain Index-owned, explainable, and independently authorized where applicable rather than inferred from incomparable provider score scales.

## Incremental Delivery, Cancellation, and Performance

`searchIncrementally()` is a cold Kotlin `Flow<IndexSearchSnapshot>`.

- collection emits an initial snapshot containing static contract/authority issues before any eligible provider must finish;
- each eligible provider runs concurrently under the query supervisor scope;
- a bounded completion channel reports provider outcomes back to the composing coroutine;
- after each completion, the engine rebuilds the accumulated result set with the same final comparator and issue semantics;
- provider positions are retained independently of completion timing so issue ordering remains deterministic;
- cancelling the Flow collector cancels the supervisor scope and outstanding provider jobs;
- `CancellationException` is rethrown and is never converted into `FAILED` or `TIMED_OUT`;
- provider-owned timeouts remain separately reported as `TIMED_OUT`;
- the Compose query lifecycle collects the Flow, so a replacement query or source selection cancels the prior collection and its outstanding provider work;
- one-shot `search()` returns the last Flow snapshot, providing one composition authority rather than two.

This improves perceived latency when a fast eligible provider can produce useful results before a slower provider completes. It does not claim measured representative-device performance acceptance. Future optimization may add bounded UI-update coalescing or top-K selection if evidence shows that large provider counts/result volumes create churn or sorting pressure; such optimization must preserve deterministic final ordering and source-state evidence.

## UI Architecture

Index UI remains source-aware and exposes meaningful provider health/authority states without disclosing sensitive internal evidence.

The Compose surface renders incremental `IndexSearchSnapshot` updates from the query Flow. Existing results may stay visible and be deterministically reordered as later providers complete. The `searching` state remains active until the Flow completes or is cancelled.

The UI now also provides a `Search sources` control card for Applications, Settings, and Contacts. The controls are session-scoped and immediately update the query execution context. The card explicitly explains that source selection does not grant missing permissions or platform authority.

A disabled `Local-only mode` indicator communicates that local-only execution is enforced by the Development build rather than being a cosmetic switch. The same surface states that Internet/Web results remain unavailable and that Index will not silently activate GoreeCloud Search or another remote provider when a local source is disabled, unavailable, or unauthorized.

The UI distinguishes authorization required, failed provider, timed out provider, degraded provider, invalid/bounded provider output, intentionally empty source selection, and ordinary no results. `INVALID_RESULT` user-facing text intentionally covers provenance/required-field/source-order failures and requested-result-limit violations without exposing sensitive internal payloads.

## Glaze UI

The current official Stable consumer target is **Glaze UI V1.4 / `1.4.0`**.

Earlier Index documentation referenced historical/inconsistent targets. They are not current conformance authority.

Index is migration-required until Index-owned surfaces and native mappings have repository-local V1.4 source, rendered/native, accessibility, and representative-device acceptance evidence. Incremental rendering and the new source-control surface are source-level behavior and do not themselves satisfy those product-specific acceptance gates.

## Failure and Recovery Model

- Missing authority → provider not dispatched; sanitized `AUTHORIZATION_REQUIRED` can appear in the initial snapshot.
- Provider exception → sanitized `FAILED`; healthy sibling results preserved.
- Provider timeout → sanitized `TIMED_OUT`; healthy sibling results preserved.
- Provider degraded response → valid results may survive with `DEGRADED` status and health-aware equal-relevance composition.
- Provider returns more results than requested → strongest distinct bounded results may survive, but the provider is reported as `INVALID_RESULT`.
- Provider returns invalid provenance/required fields/source order → invalid entries are removed and `INVALID_RESULT` remains visible.
- Equal relevance and health across different processing locations → prefer local execution without overriding a stronger remote result.
- Late stronger provider result → accumulated snapshot is deterministically recomposed and may reorder earlier results.
- Collector/query/source-selection cancellation → outstanding provider work is cancelled; no synthetic provider failure is manufactured.
- Source disabled for current session → provider excluded before dispatch; unrelated source/authority state is unchanged.
- Unknown or remote provider injected into Development source-selection state → removed by the centralized selectable-source policy.
- Selected provider lacking required authority → remains non-dispatchable; selection is not authorization.
- Disallowed provider → not dispatched.
- Remote provider under local-only execution → not dispatched and not preflighted.
- Blank query + non-browsing provider → provider not considered and no authority issue emitted.
- Invalid result action → blocked at handoff.
- No silent remote fallback.

Durable Index preferences/configuration introduced in future work must define Everkeep backup/recovery behavior before Stable acceptance. Current session source-selection state is intentionally non-durable.

## Accepted Main Evidence

`cc3cc21d6e11dad026253c3371c3b67663d3b726` passed exact-main workflow `33431294298` with APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, artifact `9772740479`, digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`.

This is Development evidence only.

## Next Architecture Milestones

1. add permission-review and authority-explanation UX that guides users toward authoritative Android/Privacy Shield/Identity decisions without manufacturing those decisions locally;
2. define profile/device scoping plus Everkeep continuity semantics before persisting provider preferences;
3. complete accepted Privacy Shield and Identity adapter paths with explicit user-decision handling;
4. validate Contacts on representative devices;
5. harden the Search provider capability lifecycle and production-acceptance gate before making an Internet provider user-selectable;
6. extend the implemented textual + degraded-state + local-first composition baseline with intent-aware, result-type, source-confidence, richer source-health/capability, and more specific privacy-cost blending;
7. measure incremental rendering/provider completion and source-toggle behavior on representative devices and add bounded update coalescing only if evidence shows excessive UI churn;
8. expand files/calendar/media providers only after authority and privacy boundaries are proven;
9. complete Glaze UI V1.4 migration and Index-local acceptance evidence;
10. add Everkeep recovery semantics for any new durable Index preference/provider state.
