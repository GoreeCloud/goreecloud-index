# GoreeCloud Index — Capabilities

## Overview

**Release lifecycle: Development.** Accepted source/build baseline is `cc3cc21d6e11dad026253c3371c3b67663d3b726`. Current `0.3.0-dev` authority/Contacts source remains Development, and this branch adds a bounded local Android Settings-navigation provider candidate. Neither branch source nor green CI alone establishes production or Stable acceptance.

## Accepted Main Capabilities

- Native Kotlin/Jetpack Compose Android application.
- Provider-neutral query/result/action contracts.
- Suspendable provider execution with processing-location and timeout declarations.
- Exact provider allowlisting and local-only gating.
- Structured concurrent provider execution, superseded-query cancellation, bounded timeouts, and sanitized `FAILED`/`TIMED_OUT` issues.
- Deterministic ranking before provider-scoped deduplication and bounded final result count.
- Launcher-visible Android Applications provider with exact component launch handoff.
- Launcher→Index external search invocation.
- Source-aware UI and exact-source Android build evidence.

## Development Capability: Authority-Gated Providers

Current source includes an explicit consuming authority layer. Providers may require Android runtime permission, Privacy Shield decision evidence, GoreeCloud Identity authorization evidence, or a combination.

Only a referenced unconstrained `ALLOW` satisfies Privacy Shield/Identity requirements. `ALLOW_WITH_CONSTRAINTS`, `DENY`, `REQUIRE_USER_DECISION`, and `UNAVAILABLE` fail closed. This prevents Index from dropping platform obligations or converting unavailable authority into implicit permission.

`IndexExecutionContext` remains an Index execution gate. The evidence objects do not make Index authoritative for Privacy Shield or Identity decisions.

## Development Capability: Contacts · On-device

- Android ContactsProvider as record authority.
- `READ_CONTACTS` declared as the scoped Android permission.
- Local processing and provisional 750 ms Development timeout.
- No Contacts query for blank input.
- `Contacts.CONTENT_FILTER_URI` search path.
- Projection limited to ID, lookup key, and display name.
- No phone/email data requested by this slice.
- `CONTACT` results with `People · On-device` provenance.
- Typed contact-view action and content-URI authority/path validation before handoff.
- Required authority set: Android permission + Privacy Shield + GoreeCloud Identity.

Current MainActivity supplies Privacy Shield and Identity evidence as unavailable, so the Contacts provider is intentionally not dispatchable yet. This is implemented source, not accepted Contacts runtime enablement.

## Development Capability: Settings · On-device

- Repository-owned static catalog of ten Android Settings navigation destinations.
- Local processing and provisional 250 ms Development timeout.
- `supportsEmptyQuery = false`; no blank-query Settings enumeration.
- Matching is limited to static destination titles and keyword metadata.
- Typed `SETTING` results and `OpenSystemSetting` handoff actions.
- MainActivity re-checks every action against the exact provider allowlist before launching Android Settings.
- No arbitrary intent action, URL, data URI, extra, direct setting mutation, or dynamic destination is accepted.
- No Settings value/state reader, ContentResolver query, network client, new Android permission, cache, analytics, or durable provider state.
- Android Settings remains authoritative for setting values and any actual configuration changes.
- A dedicated CI validator and JVM regression tests preserve the navigation-only boundary.

This provider does not establish platform acceptance on OEM-specific Settings implementations. Representative-device action availability, navigation, accessibility, failure behavior, and OEM differences remain separate acceptance gates.

## Privacy, Security, and Platform Boundaries

Applications remain local-only with no Android Internet permission or unrestricted package enumeration. Contacts also declares local processing. Index adds no persistent query history or contact cache in this slice.

The Settings provider searches GoreeCloud-owned static navigation metadata rather than reading Android setting values, so it adds no new private data resource or retention scope. Wardveil authority is not inferred from provider success; the implementation merely constrains handoff to a closed local action allowlist. Everkeep has no new durable Settings-provider state to recover. Mesh and Identity are not invoked by the local Android Settings navigation handoff. Manager gains no OS configuration authority through Index.

Actual Privacy Shield, Identity, Wardveil, Everkeep, Mesh, Manager, and Search runtime integrations remain unaccepted wherever applicable.

## Accessibility and Glaze Boundary

Source has safe-drawing insets, semantic headings, bounded interaction sizes, non-animated progress, distinct operational-vs-authorization states, and source labels. The Settings provider reuses those existing result surfaces and adds no new custom interaction primitive.

GLAZE UI V1.1 / `1.1.0` is the current published Stable target. A separate Index V1.1 migration is Development work, and the immutable `1.1.0` CSS graph has a known import-closure defect. Current conformance remains blocked pending a corrected immutable Stable release, explicit re-pin, and fresh applicable acceptance.

## Accepted Main Build Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- Package: `com.goreecloud.index.dev`
- Accepted version: `0.2.0-dev`, code `2`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

## Current Limitations

Not implemented or accepted: actual platform authority adapters, Contacts enablement/user decision flow, Android setting-value indexing, file/folder/calendar/media providers, Search Internet provider, Mesh-discovered providers, extensions/third parties, local content index, streaming results, provider health negotiation, formal Glaze acceptance, representative-device Settings/Contacts/application performance and accessibility, production signing/deployment, production acceptance, or Stable qualification.
