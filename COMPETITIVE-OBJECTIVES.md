# GoreeCloud Index — Competitive Objectives

## Status

**Release lifecycle: Development.** This document defines benchmark and improvement objectives; it does not claim current parity with or superiority over any listed product. GoreeCloud Index now has accepted Development source/build evidence for a native Android application-search foundation with structured asynchronous provider execution, while broader universal-search coverage, representative-device acceptance, production acceptance, and Stable qualification remain future work.

## Benchmark References

Primary benchmarks include Apple Spotlight, Windows Search/Start, Android system/launcher search, Samsung Finder/system search, GNOME Shell search, and KDE Plasma KRunner. Secondary references include Raycast, Alfred, and other mature universal-search/command-launcher experiences where relevant.

These references guide capability expectations only and do not authorize copying proprietary code, assets, trade dress, or product-specific visual identity.

## Capabilities Worth Matching

- Immediate predictable invocation.
- Low-latency local results.
- Application discovery and launch.
- File/folder, contact, and calendar discovery.
- Clear categories and useful top-result ordering.
- Keyboard, touch, pointer, and screen-reader usability on applicable devices.
- Contextual actions.
- Incremental results.
- Graceful unavailable/slow-provider handling.

## Areas GoreeCloud Intends to Exceed

### Provider Transparency

- Visible provider/source identity instead of an opaque result pool.
- Understandable local/remote/mixed processing location.
- Provider-specific permissions and availability.
- Distinct failure, timeout, cancellation, and healthy-result states where useful.

### Privacy and Authority

- Useful local-only operation.
- No silent local-query transmission to Internet/third-party services.
- Provider enablement, history, index clearing, account, and revocation controls.
- Privacy-minimized ranking/index data.
- Fail-closed behavior when provider eligibility or required authorization is unavailable.
- Privacy Shield and GoreeCloud Identity used as real authorities, not simulated by local UI state.

### Extensibility and Reliability

- Documented versioned provider contracts.
- Capability negotiation.
- Provider failure isolation.
- Structured concurrency, supersession cancellation, and bounded execution before remote/expensive providers.
- Provider-scoped provenance and actions.

### GoreeCloud Integration

- GoreeCloud Search retained as the distinct Internet-search authority.
- Mesh-based first-party capability coordination without source takeover.
- Wardveil-backed trust/security evidence where applicable.
- Everkeep-aware continuity for durable configuration while reconstructible indexes remain rebuildable.
- Current Stable Glaze UI presentation across supported devices.

## Behaviors GoreeCloud Intentionally Rejects

- Mandatory cloud round-trips for locally answerable queries.
- Silent third-party enrollment.
- Advertising/sponsorship-driven ranking.
- Undisclosed telemetry or behavioral profiling.
- Unrestricted provider access to unrelated local data.
- Authentication treated as blanket authorization.
- Hidden provider/source origin.
- Silent upload of private local context to improve Internet results.
- A monolithic index that takes ownership of authoritative source data.
- Unsupported security/privacy badges.
- Treating internal `IndexExecutionContext` eligibility as accepted Privacy Shield or Identity authorization.

## Current Development Differentiators

Accepted Development source now includes:

- local-only first provider with narrow Android visibility;
- explicit provider identity and processing-location declaration;
- structured concurrent provider dispatch;
- superseded-query cancellation;
- bounded provider timeout handling;
- failure/timeout issue states separate from healthy results;
- fail-closed provider eligibility while keeping platform authorization explicitly separate;
- GoreeCloud Search preserved as a distinct Internet authority.

Authoritative main `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` passed exact-main run `33429792389`. This is Development source/build evidence, not product-wide parity or production acceptance.

## Long-Term Competitive Objective

The broader intended differentiation is a fast local-first universal search layer with explicit provider architecture, optional third-party integration, Search delegation for the Internet, Privacy Shield-governed data use, Wardveil-governed trust decisions, Identity-scoped authorization, Mesh coordination, Everkeep-aware continuity, Glaze UI consistency, accessible interaction, and original GoreeCloud-owned native implementation.

These remain objectives until implementation and acceptance evidence makes each claim current.
