# GoreeCloud Index — Architecture

## Status

**Release lifecycle: Development.** Accepted `main` remains `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Current branch work is Development source pending exact-head validation and merge acceptance. Production acceptance and Stable qualification remain false.

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

## Query Flow

```text
Launcher, Browser, or user
  → Index invocation
  → MainActivity
  → IndexExecutionContext
      → exact provider allowlist
      → local/remote processing boundary
      → provider authority evidence
  → IndexRoot / query lifecycle
  → IndexQueryEngine
      → blank-query applicability
      → fail-closed authority evaluation
      → structured concurrent dispatch
      → bounded per-provider timeout
      → validate provider provenance/identity/title/source-order/result-count contract
      → keep at most the requested number of strongest distinct provider-local results
      → normalize cross-provider textual relevance
      → prefer healthy source only when normalized relevance ties
      → prefer LOCAL, then MIXED, then REMOTE only when relevance and health tie
  → IndexSearchSnapshot
      → ranked provider-scoped results
      → AUTHORIZATION_REQUIRED / FAILED / TIMED_OUT / DEGRADED / INVALID_RESULT issues
  → source-aware UI
  → typed validated handoff
```

## Core Authority Model

`IndexAuthorityRequirement` currently supports Android runtime permission, Privacy Shield, and GoreeCloud Identity requirements.

`IndexProviderAuthority` consumes evidence. Android permission is a platform state; Privacy Shield/Identity entries require an accepted outcome plus a non-empty reference. `ALLOW_WITH_CONSTRAINTS` fails closed until Index can enforce every returned obligation and must not be treated as `ALLOW`. Missing, denied, user-decision-required, unavailable, stale, mismatched, or otherwise unenforceable evidence also fails closed.

This is a **consumer contract**, not substitute authority. No Identity endpoint is invented by Index and no Privacy Shield decision is fabricated locally.

## Provider Contract

`IndexProvider` declares stable identity, display name, processing location, timeout, authority requirements, blank-query support, contract version, and suspendable search behavior.

The engine considers only providers applicable to the current query. This prevents non-browsing private sources from generating unnecessary authority prompts or enumerating data on blank input.

Providers must be independently cancellable, bounded by timeout, and unable to suppress healthy sibling providers through ordinary failure. Providers receive `IndexQuery.maxResults` and are expected not to return more results than requested. The engine independently enforces that boundary: an over-limit response is reported as `INVALID_RESULT`, then reduced to the strongest distinct provider-local results up to the requested limit before cross-provider composition. This prevents a buggy or hostile provider from expanding federation work simply by ignoring the query contract.

Bounding does not trust response order. Index applies the same-provider score/source-order semantics first, preserves the strongest duplicate for each provider-local ID, and only then takes the requested result count. Invalid-result detection still precedes an ordinary `DEGRADED` signal so contract violations remain visible.

The provider-declared `processingLocation` is available to Index-owned composition only after the provider passes scope and authority gates. It is not authorization: a remote provider must first be eligible and authorized before any ranking tie-break can consider its results.

## Applications Provider

`InstalledAppsProvider` is a local provider using scoped launcher discovery, label/package matching, exact component actions, and no `QUERY_ALL_PACKAGES` requirement. It already respects `query.maxResults` before returning results.

## Contacts Provider

The Contacts provider uses Android ContactsProvider authority through `ContactsContract` with local processing and a permission/authority gate.

- no blank-query enumeration;
- filtered lookup through `Contacts.CONTENT_FILTER_URI`;
- projection limited to contact identity/display fields required by the result;
- no phone/email field read in the current slice;
- typed contact-view action;
- Android permission plus Privacy Shield and Identity authority evidence;
- bounded result collection using the requested result count and the provider's own hard ceiling.

Incomplete or unenforceable authority must prevent provider dispatch.

## Settings Provider

The Settings provider is a bounded local navigation provider over repository-defined destination metadata. It does not read device setting values or configuration state. It already limits output to the requested result count.

Actions must remain inside the exact reviewed allowlist. Arbitrary Android actions, URLs, data URIs, and extras are not supported by this provider contract.

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

See [`docs/SEARCH_INTEGRATION.md`](docs/SEARCH_INTEGRATION.md).

## Result and Action Boundary

Application actions use exact package/class components.

Contact actions are accepted only when the parsed URI has the expected Android contacts scheme/authority/path before `ACTION_VIEW` is issued.

Settings actions are accepted only from the static reviewed allowlist.

Web actions are accepted only after Index validates an HTTP(S) destination with a valid host and no embedded user-info credentials. Browser remains responsible for its own navigation/security policy after handoff.

Invalid executable actions fail closed with sanitized user-visible/provider issue handling.

## Ranking and Deduplication

Index is responsible for cross-provider composition. Provider-local scores are authoritative only inside the provider that produced them; raw score magnitudes are not compared across provider boundaries.

The current Development engine implements explicit normalization, bounded source health, bounded local-first composition, and bounded provider fan-out:

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
- final provider-scoped deduplication remains as defense in depth without collapsing distinct resources owned by different providers.

This is a bounded baseline, not the final blending model. Future ranking work can add explicit factors such as result type, source confidence, source-owned recency, user-declared source preference, richer capability/health evidence, and privacy cost more specific than processing location. Those factors must remain Index-owned, explainable, and independently authorized where applicable rather than inferred from incomparable provider score scales.

## Cancellation and Performance

Interactive search must cancel superseded work. Parent cancellation propagates to providers and must not be converted into ordinary failure.

Provider timeouts remain bounded. Healthy sibling results survive a provider timeout or failure. Provider fan-out into federation is now bounded by the current query result limit after local validation/ranking/deduplication, reducing unnecessary cross-provider sorting and result retention when a provider violates its requested limit.

Future optimization should favor incremental result delivery and stable result ordering without allowing late remote results to cause excessive UI churn. A future bounded-selection implementation may reduce provider-local sorting cost further if evidence shows that result-volume pressure justifies the additional complexity.

## UI Architecture

Index UI must remain source-aware and expose meaningful provider health/authority states without disclosing sensitive internal evidence.

The UI should distinguish authorization required, failed provider, timed out provider, degraded provider, invalid/bounded provider output, and no results. `INVALID_RESULT` user-facing text intentionally covers provenance/required-field/source-order failures and requested-result-limit violations without exposing sensitive internal payloads.

## Glaze UI

The current official Stable consumer target is **Glaze UI V1.4 / `1.4.0`**.

Earlier Index documentation referenced historical/inconsistent targets. They are not current conformance authority.

Index is migration-required until Index-owned surfaces and native mappings have repository-local V1.4 source, rendered/native, accessibility, and representative-device acceptance evidence.

## Failure and Recovery Model

- Missing authority → provider not dispatched; sanitized `AUTHORIZATION_REQUIRED`.
- Provider exception → sanitized `FAILED`; healthy sibling results preserved.
- Provider timeout → sanitized `TIMED_OUT`; healthy sibling results preserved.
- Provider degraded response → valid results may survive with `DEGRADED` status and health-aware equal-relevance composition.
- Provider returns more results than requested → strongest distinct bounded results may survive, but the provider is reported as `INVALID_RESULT`.
- Provider returns invalid provenance/required fields/source order → invalid entries are removed and `INVALID_RESULT` remains visible.
- Equal relevance and health across different processing locations → prefer local execution without overriding a stronger remote result.
- Parent/query cancellation → propagates.
- Disallowed provider → not dispatched.
- Remote provider under local-only execution → not dispatched and not preflighted.
- Blank query + non-browsing provider → provider not considered and no authority issue emitted.
- Invalid result action → blocked at handoff.
- No silent remote fallback.

Durable Index preferences/configuration introduced in future work must define Everkeep backup/recovery behavior before Stable acceptance.

## Accepted Main Evidence

`cc3cc21d6e11dad026253c3371c3b67663d3b726` passed exact-main workflow `33431294298` with APK SHA-256 `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`, artifact `9772740479`, digest `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`.

This is Development evidence only.

## Next Architecture Milestones

1. complete accepted Privacy Shield and Identity adapter paths with explicit user-decision handling;
2. validate Contacts on representative devices;
3. harden the Search provider capability lifecycle and production-acceptance gate;
4. extend the implemented textual + degraded-state + local-first composition baseline with intent-aware, result-type, source-confidence, richer source-health/capability, and more specific privacy-cost blending;
5. add incremental result delivery while preserving deterministic cancellation and source status;
6. expand files/calendar/media providers only after authority and privacy boundaries are proven;
7. complete Glaze UI V1.4 migration and Index-local acceptance evidence;
8. add Everkeep recovery semantics for any new durable Index preference/provider state.
