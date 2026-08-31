# GoreeCloud Index — Competitive Objectives

## Status

**Release lifecycle: Development.** This document defines benchmark and improvement objectives. It does not claim that GoreeCloud Index currently matches or exceeds any listed product. Index now has a native Android application-search foundation and an asynchronous provider-runtime candidate, while broader universal-search provider coverage, representative-device acceptance, production acceptance, and Stable qualification remain future work.

## Benchmark References

GoreeCloud Index should study mature system-search and productivity-search experiences, including:

### Primary Benchmarks

- Apple Spotlight.
- Windows Search and Start search.
- Android system/launcher search experiences.
- Samsung system search / Finder experiences.
- GNOME Shell search.
- KDE Plasma KRunner.

### Secondary and Adjacent Benchmarks

- Raycast.
- Alfred.
- Other mature desktop or mobile universal-search and command-launcher experiences when their behavior is relevant to GoreeCloud requirements.

These references are used to identify useful interaction patterns and capability expectations. They do not authorize copying proprietary source code, assets, trade dress, or product-specific visual identity.

## Capabilities Worth Matching

GoreeCloud Index should reach a high-quality baseline for:

- Immediate search invocation.
- Low-latency local results.
- Application discovery and launch.
- File and folder discovery.
- Contact discovery.
- Calendar/event discovery.
- Clear result categories and useful top-result ordering.
- Keyboard navigation on keyboard-capable devices.
- Touch-friendly interaction on mobile and tablet devices.
- Contextual actions on supported results.
- Incremental results rather than waiting for every source.
- Graceful handling of unavailable or slow search sources.

## Capabilities GoreeCloud Intends to Exceed

Where technically justified and evidence-backed, GoreeCloud Index should exceed typical universal-search experiences in the following areas:

### Provider Transparency

- Make source/provider identity understandable instead of blending all results into an opaque pool.
- Expose whether a provider operates locally, remotely, or through a mixed workflow when that distinction matters.
- Keep provider-specific permissions and availability visible and controllable.
- Distinguish ordinary provider failure from bounded timeout and cancellation where that distinction helps the user or operator.

### Privacy Control

- Preserve a useful local-only search mode.
- Prevent silent transmission of local search data to Internet or third-party services.
- Give users direct controls for provider enablement, search history, index clearing, connected accounts, and revocation.
- Minimize retained query and ranking data instead of building hidden behavioral profiles.
- Fail closed when provider eligibility or required authorization cannot be established.

### Extensibility

- Provide a documented, versioned provider model for first-party applications, GoreeCloud extensions, and optional third-party services.
- Use capability negotiation so providers declare what they actually support.
- Isolate provider failures and trust boundaries.
- Use structured concurrency, prompt supersession cancellation, and bounded provider execution before remote or expensive providers are activated.

### GoreeCloud Integration

- Coordinate first-party search providers through GoreeCloud Mesh without hard-coded data ownership.
- Use GoreeCloud Identity for scoped caller/user/provider identity where applicable.
- Apply Privacy Shield and Wardveil Security as real authorities rather than decorative status labels.
- Preserve Everkeep continuity boundaries for durable configuration without treating reconstructible indexes as irreplaceable data.
- Present the experience consistently through the applicable Stable Glaze UI contract.

### Search Boundary Clarity

- Keep universal resource search distinct from web search.
- Delegate Internet results to GoreeCloud Search instead of duplicating a second web-search engine inside Index.
- Let users suppress Internet results when they want a purely local/device search.

## Capabilities GoreeCloud Intentionally Rejects

GoreeCloud Index should not adopt behaviors that conflict with GoreeCloud product requirements, including:

- Mandatory cloud round-trips for searches that can be completed locally.
- Silent enrollment into third-party search providers.
- Advertising or sponsorship-driven ranking.
- Undisclosed query telemetry or behavioral profiling.
- Unrestricted provider access to unrelated local data.
- Treating authentication as blanket authorization to search every account or resource.
- Hiding whether a result came from the current device, another device, GoreeCloud Search, or a third-party service.
- Sending private local context to improve Internet results without explicit authority.
- A monolithic index that takes ownership of source data instead of preserving provider authority.
- Security badges or privacy claims that are not backed by authoritative platform state.
- Treating internal execution eligibility as a substitute for accepted Privacy Shield or GoreeCloud Identity authorization.

## Privacy and Security Objectives

GoreeCloud Index should establish a stronger privacy/security baseline by requiring:

- Least-privilege provider access.
- User/profile isolation.
- Explicit third-party authorization and revocation.
- Clear local-versus-remote processing boundaries.
- Privacy-minimized local indexing.
- Provider trust validation through approved Wardveil Security contracts.
- Fail-safe behavior when required trust or authorization decisions are unavailable.
- Safe result-action handoff that cannot bypass normal platform permissions.

## Control, Ownership, and Independence Objectives

- Local search should remain useful without an external account when the selected providers do not require one.
- GoreeCloud should own the Index architecture and product direction.
- Third-party services should be optional providers, not structural dependencies for core local search.
- Provider contracts should be portable enough to support multiple GoreeCloud platforms without forcing one proprietary external ecosystem.
- Reconstructible indexes should remain rebuildable from authoritative sources rather than becoming a hidden data silo.

## User Experience and Accessibility Objectives

- Search invocation should be fast and predictable.
- Results should be scannable without hiding important source context.
- Phone, tablet, desktop, and large-display layouts should adapt without changing the underlying result semantics.
- Keyboard, touch, pointer, screen-reader, text-scaling, focus, and reduced-motion behavior should be first-class acceptance requirements on applicable platforms.
- Permission, offline, unavailable-provider, timed-out-provider, remote-processing, searching, and partial-result states should be understandable rather than represented as generic failures.

## Performance and Reliability Objectives

- Common local searches should begin returning useful results quickly.
- Providers should run concurrently where safe.
- Slow providers should not block faster providers indefinitely.
- Superseded queries should be cancelled promptly.
- Provider timeouts should be evidence-tuned per provider and platform rather than treated as universal constants.
- Mobile implementations should minimize idle CPU, memory, network, and battery use.
- Local index corruption should be recoverable through rebuild rather than requiring destructive user-data recovery.

Exact performance targets must be established from implementation and representative-device testing rather than guessed during Development.

## Interoperability and Administrative Objectives

- Use stable provider contracts and capability negotiation.
- Support first-party GoreeCloud providers without private database coupling.
- Support optional third-party providers only through explicit integration boundaries.
- Allow appropriate administrative policy over provider availability without weakening individual privacy/security authority.
- Keep provider health and compatibility observable without logging raw private search contents by default.

## Data Portability and Recovery Objectives

- Keep durable user preferences and provider configuration exportable/recoverable where applicable.
- Prefer rebuilding derived index/cache data from authoritative sources.
- Avoid unnecessary backup of sensitive derived index contents when rebuild is sufficient.
- Allow removal of a provider to remove its credentials and provider-specific retained data according to policy.

## Current Development Differentiators

The current source already establishes several bounded architectural differentiators without claiming product-wide completion:

- A local-only first provider with narrowly scoped Android visibility.
- Explicit provider identity and processing-location declaration.
- Structured concurrent provider dispatch in the `0.2.0-dev` candidate.
- Superseded-query cancellation and bounded provider timeout handling in the candidate.
- Failure and timeout issue states separate from healthy results.
- Fail-closed provider eligibility through `IndexExecutionContext` while keeping Privacy Shield/Identity acceptance explicitly separate.
- GoreeCloud Search retained as a distinct Internet-search authority.

The broader intended GoreeCloud differentiation remains the combination of local-first universal search, explicit provider architecture, optional third-party integration, GoreeCloud Search delegation, Privacy Shield-governed data use, Wardveil-governed trust/security decisions, Identity-scoped authorization, Mesh-based first-party capability discovery, Everkeep-aware continuity boundaries, Glaze UI-consistent presentation, and original GoreeCloud-owned native implementation.

Those broader elements remain competitive objectives until implementation and validation evidence permits them to be documented as current capabilities or verified benefits.
