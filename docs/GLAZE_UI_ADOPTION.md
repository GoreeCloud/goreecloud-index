# GoreeCloud Index — GLAZE UI V1.1 Adoption

## Status

Development source migration. This record does not establish downstream conformance, Stable qualification, or production approval.

## Current authority

- Target: **GLAZE UI V1.1 (`1.1.0`)**
- Tag: `v1.1.0`
- Stable release revision: `15cc76d2bcd4065552dc31c77145b63f34d9e7b2`
- Approved visual source: `8ea1f789bbabf943c3359514dc1506b24fa3c51b`
- Optical contract: `contracts/v1.1/optical-refinement.json`
- Atmosphere tokens: `tokens/glaze-v1.1-atmosphere.json`

## Known Stable-line blocker

The published `1.1.0` source remains the current Stable consumer target, but it has a known V1.1 CSS import-closure defect. The governed corrective line is GLAZE UI PR #129 / `1.1.1-rc.1`; that correction is still a Release Candidate with `consumerEligible: false` and is not a corrected immutable Stable release.

Index may retain this bounded Development source mapping for review, but this `1.1.0` pin must not be used to claim current GLAZE UI conformance, release eligibility, or production acceptance. After a corrected immutable Stable release is published, Index must explicitly re-pin the exact version and revision and repeat all applicable source, rendered/native visual, accessibility, representative-device, artifact/distribution, and production acceptance.

## Index mapping

Index uses native Jetpack Compose / Material 3 controls while owning its visual theme and interaction semantics.

The repository-local V1.1 mapping:

- keeps neutral structure dominant;
- uses Deep Teal as the primary environmental identity and Soft Amber as restrained secondary counter-light;
- implements Light, Dark, and Deep Dark theme schemes;
- uses 16 / 24 / 32 dp rounded geometry for the current Compose shape scale, matching V1.1 control/container/hero optical references;
- preserves 48dp ordinary interaction targets and records a 56dp Touch Assistance floor;
- keeps nested backdrop blur disabled;
- does not require Environmental Color Memory;
- does not remotely transmit content for atmospheric color derivation;
- leaves semantic error, focus, authorization, privacy, security, recovery, and provider-state meaning to their owning systems rather than atmospheric color.

Android system dark mode currently maps to the Dark scheme. Deep Dark is implemented as an explicit callable theme mode but is not yet exposed through a user-facing Index preference.

## Authority boundary

GLAZE UI controls presentation and interaction. It does not create provider authorization, search-result provenance, Privacy Shield decisions, GoreeCloud Identity decisions, Wardveil findings, Everkeep state, Mesh coordination truth, or GoreeCloud Search Internet-result authority.

## Acceptance still required

Repository source and unit tests are only the first gate. Exact Index revisions still require, as applicable:

- Android build/lint/unit-test evidence;
- representative rendered visual review;
- Human Visual Excellence acceptance;
- TalkBack and semantic accessibility review;
- 200% text and layout reflow;
- RTL/localization;
- Reduced Motion and Reduced Transparency behavior;
- contrast/high-contrast/forced-color-equivalent handling;
- supported phone/tablet/orientation/form-factor behavior;
- performance and responsiveness review;
- user-facing Deep Dark selection if that mode becomes part of the supported product contract;
- production signing, distribution, operational recovery, and production approval.
