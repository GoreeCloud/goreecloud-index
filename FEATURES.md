# GoreeCloud Index — Features

## Status Model

This file distinguishes current source implementation from planned product scope. A feature is not current merely because it appears in a specification, design, issue, or future-provider list. Source implementation also does not imply production or Stable acceptance.

## Current Features

### Native Android Foundation

- Original GoreeCloud-owned Android application source.
- Kotlin and Jetpack Compose application shell.
- Production package reservation `com.goreecloud.index` and development package `com.goreecloud.index.dev`.
- Android API 26 minimum, compile API 37, and target API 36 baseline.
- Automated repository validation, unit-test, lint, development-APK build, package/label verification, checksum evidence, and artifact-publication workflow.

### Search Core

- Typed query model.
- Typed provider descriptor with provider identity, resource types, and local/remote/mixed processing mode.
- Typed result model with provider-scoped identity, resource type, relevance score, labels, and authorized action representation.
- Provider failure isolation.
- Provider-scoped result deduplication.
- Deterministic score-first result ordering.
- Blank-query suppression so providers are not queried for empty input.
- Unit tests for blank-query behavior, provider failure isolation, ranking, and deduplication.

### Android Applications Provider

- Search of launcher-visible Android applications through `ACTION_MAIN` plus `CATEGORY_LAUNCHER`.
- Narrow manifest package visibility for launcher applications rather than unrestricted `QUERY_ALL_PACKAGES` access.
- Search by application label and package name.
- Exact, prefix, and containment relevance scoring.
- Duplicate launcher entries collapsed by package identity.
- Maximum of 50 application matches per query.
- Typed application-launch action resolved by Android at action time.

### Current Search Surface

- Dedicated native GoreeCloud Index Android surface.
- Prominent application search field.
- Explicit `Applications · On-device` source labeling.
- Result cards with application label, package identity, and open action.
- Blank-query guidance.
- No-results state.
- Provider-unavailable state.
- Application-launch failure feedback.
- Minimum primary interaction sizing consistent with the current Glaze UI direction.

The current UI targets Glaze UI 2.1.0 semantics. Formal consumer conformance is not yet claimed.

## Experimental or Partial Features

The following source capabilities are real but remain partial because broader runtime acceptance is pending:

- Android launcher-application search requires representative-device validation.
- The current synchronous provider contract is suitable only for the first bounded local provider; remote and expensive providers require asynchronous cancellation and timeout support.
- Search UI accessibility and form-factor behavior require formal device, large-text, reduced-motion, contrast, and assistive-technology acceptance.
- Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Identity, and GoreeCloud Mesh runtime integrations remain pending.

## Planned Features

### Universal Search

- One query experience across multiple authorized resource sources.
- Incremental result delivery as providers respond.
- Search by source, provider, or resource type.
- Local-only search mode.
- Result grouping, filtering, ranking, and provenance-preserving deduplication.
- Source labels and authorized contextual actions.
- Query cancellation and supersession.
- Per-provider timeouts and health state.

### Applications

- Provider-declared application aliases.
- Application shortcuts and contextual actions where platform APIs and provider authorization permit them.
- Cross-device application discovery where explicitly supported.

### Files and Folders

- File and folder name search.
- Authorized metadata search.
- Type, location, tag, and provider filtering.
- Authorized content indexing without uncontrolled duplication of private content.
- Open and reveal actions through the authoritative file provider.

### Contacts

- Authorized contact search.
- Account/source-aware result presentation.
- Open-contact and authorized communication actions through the appropriate provider.

### Calendar

- Authorized event search.
- Event metadata matching.
- Calendar/account-aware result presentation.
- Open-event actions through the authoritative calendar provider.

### GoreeCloud Providers

- First-party provider registration and discovery.
- Search providers exposed by compatible GoreeCloud applications and services.
- Versioned capability negotiation.
- Provider availability, health, cancellation, and timeout behavior.

### Extensions

- Versioned extension search-provider contract.
- Provider-declared resource types, permissions, processing location, and actions.
- Extension enable/disable controls.
- Trust and authorization enforcement before participation.

### Third-Party Services

- Optional user-connected third-party providers.
- Explicit opt-in, scoped authorization, and independent revocation.
- Clear local-versus-remote processing disclosure.
- Provider-specific permission review.
- Isolation from unrelated providers and local index data.
- Provider-data cleanup after disconnection according to policy.

### Internet Search

- GoreeCloud Search exposed as the explicit Internet/web-search provider.
- Ability to disable or suppress Internet results.
- No silent upload of local resource data to obtain Internet results.

### Indexing

- Privacy-minimized local metadata index where beneficial.
- Incremental updates.
- Provider removal and cleanup.
- Rebuild and corruption recovery.
- User/profile/device scoping.
- Clear distinction between reconstructible index data and authoritative source data.

### Ranking and Relevance

- Provider-supplied relevance signals.
- Metadata and context-aware ranking within authorization boundaries.
- Bounded personalization governed by Privacy Shield.
- Related-result grouping while preserving provenance.

### Privacy Shield

- Per-provider enable/disable controls.
- Permission and purpose explanations.
- Local-versus-remote processing visibility.
- Search-history controls when history exists.
- Index/cache clearing controls.
- Third-party connection revocation.
- Privacy-minimized telemetry and retention.

### Wardveil Security

- Provider trust validation through approved Wardveil contracts.
- Untrusted provider-output handling.
- Least-privilege provider isolation.
- Security-aware result-action validation.
- Fail-safe behavior when required trust decisions are unavailable.

### GoreeCloud Identity

- User/profile isolation.
- Caller identity for programmatic search.
- Scoped provider authorization.
- Third-party account association where appropriate.
- Local identity paths when cloud identity is unnecessary.

### GoreeCloud Mesh

- Provider/capability discovery for first-party GoreeCloud components.
- Bounded cross-application search coordination.
- Provider availability communication without duplicating source authority.

### Everkeep

- Recovery of durable Index preferences and provider configuration where appropriate.
- Rebuild-first handling of reconstructible index/cache data.
- Continuity boundaries that keep index state distinct from authoritative source data.

### Glaze UI

- Complete Glaze UI 2.1.0 consumer conformance.
- Responsive phone, tablet, laptop, desktop, and large-display layouts as those platforms are implemented.
- Keyboard, touch, pointer, and assistive-technology support.
- Reduced motion, reduced transparency, increased contrast, forced-colors, and large-text behavior where applicable.
- Clear local, remote, loading, unavailable, permission-required, and partial-result states.

### Reliability and Performance

- Asynchronous provider execution.
- Query cancellation and supersession.
- Per-provider timeout behavior.
- Partial results when some providers fail.
- Offline local-provider operation where possible.
- Low idle resource use.
- Fast local result startup.
- Bounded memory and battery use on supported devices.

## Deprecated or Removed Features

None.
