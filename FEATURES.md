# GoreeCloud Index — Features

## Status Model

This file distinguishes implemented functionality from planned product scope. A feature is not current merely because it appears in the project specification, a design, or this planned list.

## Current Features

No end-user GoreeCloud Index search capability is currently implemented or validated.

The repository currently provides only the project foundation needed to begin development:

- Version-controlled product documentation.
- GNU Affero General Public License v3.0 licensing.
- Explicit product boundaries between GoreeCloud Index and GoreeCloud Search.
- Documented requirements for native development and mandatory GoreeCloud platform integration.

These are repository-development capabilities, not a working search product.

## Experimental or Partial Features

None verified at this time.

## Planned Features

### Universal Search

- One query experience across multiple authorized resource sources.
- Incremental result delivery as providers respond.
- Search by source, provider, or resource type.
- Local-only search mode.
- Result grouping, filtering, ranking, and deduplication.
- Source labels and provenance-aware result presentation.
- Authorized contextual actions for results.

### Applications

- Installed application discovery.
- Application name and approved alias matching.
- Provider-declared application shortcuts and contextual actions where supported.

### Files and Folders

- File and folder name search.
- Authorized metadata search.
- Type, location, tag, and provider filtering where available.
- Authorized content indexing where supported without uncontrolled duplication of private file contents.
- Open and reveal actions through the authoritative file provider.

### Contacts

- Authorized contact search.
- Contact result presentation with source/account context where relevant.
- Open-contact and authorized communication actions through the appropriate provider.

### Calendar

- Authorized event search.
- Event metadata matching.
- Calendar/account-aware result presentation.
- Open-event actions through the authoritative calendar provider.

### GoreeCloud Providers

- First-party provider registration and discovery.
- Search providers exposed by compatible GoreeCloud applications and services.
- Capability negotiation instead of assuming all providers support the same behavior.
- Provider availability and health state.
- Provider failure isolation and cancellation.

### Extensions

- Versioned search-provider extension contract.
- Provider-declared resource types, permissions, processing location, and actions.
- Extension enable/disable controls.
- Trust and authorization enforcement before extension providers can participate.

### Third-Party Services

- Optional user-connected third-party search providers.
- Explicit opt-in and revocation.
- Scoped authentication/authorization.
- Clear local-versus-remote processing disclosure.
- Provider-specific permission review.
- Isolation from unrelated local providers and index data.

### Internet Search

- GoreeCloud Search exposed as the Internet/web-search provider.
- Explicit source identification for Internet results.
- Ability to disable or suppress Internet results.
- No silent upload of local resource data to obtain Internet results.

### Indexing

- Privacy-minimized local metadata index where beneficial.
- Incremental index updates.
- Provider removal and cleanup.
- Index rebuild and corruption recovery.
- User/profile and device scoping.
- Clear distinction between reconstructible index data and authoritative source data.

### Ranking and Relevance

- Common normalized result model.
- Textual relevance.
- Provider-supplied relevance signals.
- Context-aware ranking within authorization boundaries.
- Bounded personalization governed by Privacy Shield.
- Duplicate and related-result grouping while preserving provenance.

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
- Local identity paths where cloud identity is unnecessary.

### GoreeCloud Mesh

- Provider/capability discovery for first-party GoreeCloud components.
- Bounded cross-application search coordination.
- Provider availability communication without duplicating source authority.

### Everkeep

- Recovery of durable Index preferences and provider configuration where appropriate.
- Rebuild-first handling of reconstructible index/cache data.
- Clear continuity boundaries so index state is not confused with authoritative source data.

### Glaze UI

- Current applicable Stable Glaze UI consumer implementation.
- Responsive phone, tablet, laptop, desktop, and large-display layouts as platforms are implemented.
- Keyboard, touch, pointer, and assistive-technology support.
- Reduced-motion support.
- Clear local, remote, loading, unavailable, permission-required, and partial-result states.

### Reliability and Performance

- Query cancellation and supersession.
- Per-provider timeout behavior.
- Partial results when some providers fail.
- Offline local-provider operation where possible.
- Low idle resource use.
- Fast local result startup.
- Bounded memory and battery use on supported devices.

## Deprecated or Removed Features

None.
