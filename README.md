# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It coordinates authorized search providers while preserving source ownership, provenance, and platform authority boundaries.

## Status

**Release lifecycle: Development.**

The native Android `0.2.0-dev` asynchronous provider-runtime foundation is now accepted on authoritative `main` at `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` after pull request #4 passed exact-candidate validation and its post-merge exact-main workflow completed successfully.

GoreeCloud Index is **not Stable or production accepted**. Representative-device behavior, formal accessibility/Glaze UI conformance, platform-runtime integrations, controlled production signing/deployment, and Stable qualification remain separate gates.

## Current Accepted Development Capability

The current Android Development source can:

- Browse and search launcher-visible applications on the current device.
- Match application labels and package names with deterministic relevance scoring.
- Preserve exact Android launcher-component identity for result handoff.
- Dispatch eligible providers concurrently with Kotlin structured concurrency.
- Cancel superseded query work through the Compose query lifecycle.
- Apply bounded provider timeouts and distinguish `FAILED` from `TIMED_OUT` provider issues.
- Preserve healthy-provider results when another eligible provider fails or times out.
- Rank before provider-scoped deduplication.
- Fail closed through `IndexExecutionContext` using exact provider allowlisting and `localOnly` processing gating.
- Expose the Launcher→Index `com.goreecloud.index.action.SEARCH` handoff.
- Present searching, browse, no-match, provider-failure, provider-timeout, result, and launch-failure states.
- Keep the current Applications provider local-only without Android `INTERNET` or unrestricted `QUERY_ALL_PACKAGES` permission.

`IndexExecutionContext` is an internal execution-eligibility guard. It is **not** accepted Privacy Shield consent/permission logic and is **not** accepted GoreeCloud Identity authorization.

## Current Provider

The only implemented provider remains **Applications · On-device**. It uses `ACTION_MAIN` plus `CATEGORY_LAUNCHER`, declares `LOCAL` processing, preserves exact launcher-component identity, and uses a provisional 500 ms Development timeout. The timeout is a safety bound, not a representative-device latency SLA.

## Planned Search Sources

- Files and folders.
- Contacts.
- Calendar events.
- Media and settings where approved provider contracts exist.
- First-party GoreeCloud applications and services.
- Connected GoreeCloud devices and authorized remote resources.
- GoreeCloud extensions.
- Optional third-party services explicitly connected and authorized by the user.
- Internet results delegated to GoreeCloud Search.

Additional providers require their own source-authority, permission, privacy, identity, security, retention, failure, and acceptance evidence before activation.

## Product Boundary

**GoreeCloud Index** is the universal search/indexing authority. **GoreeCloud Search** remains authoritative for Internet/web/current-information search. **GoreeCloud Launcher** is an invocation/presentation surface, not a competing universal index. Provider applications and services remain authoritative for their own resources.

## GoreeCloud Platform Requirements

The current UI targets **Glaze UI 2.1.0**. Formal application-specific Glaze UI conformance remains pending. Accepted runtime integration also remains pending for **Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, and GoreeCloud Identity**.

## Android Development Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Label: `GoreeCloud Index Dev`
- Version: `0.2.0-dev`
- Version code: `2`
- Minimum API: 26
- Compile API: 37
- Target API: 36

## Validation Evidence

Pull request #4 exact candidate `d8b563705ccf1d05444df18e7a593a454d4c4103` passed workflow run `33429486374` before merge.

Authoritative main commit `e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9` then passed exact-main workflow run `33429792389`, including repository validation, coroutine unit tests, Android lint, Development APK assembly, package/version/label verification, checksum capture, and artifact publication.

Accepted-main Development APK SHA-256: `a2605bb1e993dd027afc68b0900b60f2fcf9567ec59399e5a02a178af1cc815f`.

Accepted-main artifact: `9772201920`, `goreecloud-index-development-apk-e0576bd39e3793bf62c5b4b3f0b887ded4a6d0f9`.

Artifact archive digest: `sha256:0aa9c334b980f558fa983ef059f4fc7a73cf57fdf8c82d39ca2414ec60c77b75`.

This is Development source/build evidence only.

## Documentation

- [Specifications](SPECIFICATIONS.md)
- [Features](FEATURES.md)
- [Capabilities](CAPABILITIES.md)
- [Architecture](ARCHITECTURE.md)
- [Conformance](CONFORMANCE.md)
- [User manual](USER-MANUAL.md)
- [Benefits](BENEFITS.md)
- [Competitive objectives](COMPETITIVE-OBJECTIVES.md)

## License

GNU Affero General Public License v3.0. See [LICENSE](LICENSE).
