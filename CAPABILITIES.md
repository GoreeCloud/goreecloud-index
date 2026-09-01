# GoreeCloud Index — Capabilities

## Overview

**Release lifecycle: Development.** Accepted source/build baseline is `cc3cc21d6e11dad026253c3371c3b67663d3b726`. The current `0.3.0-dev` Contacts/authority implementation remains branch source pending exact-head validation and merge acceptance.

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

## Branch Capability: Authority-Gated Providers

The branch adds an explicit consuming authority layer. Providers may require Android runtime permission, Privacy Shield decision evidence, GoreeCloud Identity authorization evidence, or a combination.

Only a referenced unconstrained `ALLOW` satisfies Privacy Shield/Identity requirements. `ALLOW_WITH_CONSTRAINTS`, `DENY`, `REQUIRE_USER_DECISION`, and `UNAVAILABLE` fail closed. This prevents Index from dropping platform obligations or converting unavailable authority into implicit permission.

`IndexExecutionContext` remains an Index execution gate. The new evidence objects do not make Index authoritative for Privacy Shield or Identity decisions.

## Branch Capability: Contacts · On-device

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

## Privacy and Security Boundary

Applications remain local-only with no Android Internet permission or unrestricted package enumeration. Contacts also declares local processing. Index adds no persistent query history or contact cache in this slice.

Provider success never establishes Wardveil trust/protection. Actual Privacy Shield, Identity, Wardveil, Everkeep, Mesh, and Search runtime integrations remain unaccepted.

## Accessibility and Glaze Boundary

Source has safe-drawing insets, semantic headings, bounded interaction sizes, non-animated progress, distinct operational-vs-authorization states, and source labels. Glaze UI 2.1.0 is the current Stable target; formal consumer and representative-device acceptance remain pending.

## Accepted Main Build Evidence

- Source: `cc3cc21d6e11dad026253c3371c3b67663d3b726`
- Workflow: `33431294298`
- Package: `com.goreecloud.index.dev`
- Accepted version: `0.2.0-dev`, code `2`
- APK SHA-256: `54139051e4243ca83b245338ed5e40680edd4ffd3e673a12dfff6b75eed3e99f`
- Artifact: `9772740479`
- Artifact digest: `sha256:87162d517a95622f35c46a63992ed1c545e125ee620c0fa544e265285d61a22c`

## Current Limitations

Not implemented or accepted: actual platform authority adapters, Contacts enablement/user decision flow, file/folder/calendar/media/settings providers, Search Internet provider, Mesh-discovered providers, extensions/third parties, local content index, streaming results, provider health negotiation, formal Glaze acceptance, representative-device performance/accessibility, production signing/deployment, production acceptance, or Stable qualification.
