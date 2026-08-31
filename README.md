# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It is designed to search authorized resources on the current device and, as additional providers are implemented, combine them with approved GoreeCloud, extension, optional third-party, connected-device, and Internet sources through one consistent search experience.

## Status

**Release lifecycle: Development.**

**Active development — native Android application-search foundation with an asynchronous provider-runtime candidate.** The current source contains a real Android/Jetpack Compose application, provider-neutral query/result/action contracts, structured concurrent provider execution, bounded provider timeouts, superseded-query cancellation through the Compose query lifecycle, an explicit fail-closed execution context, a launcher-visible Android applications provider, the Launcher→Index search-entry contract, a source-aware search UI, unit tests, repository validation, and an Android build-evidence workflow.

GoreeCloud Index is **not Stable or production accepted**. Current source/build validation does not establish representative-device, accessibility, platform-integration, production-signing, deployment, or Stable acceptance. The asynchronous-runtime changes on this development branch require their own exact-candidate CI and merge acceptance before they become accepted-main evidence.

## Current Source Capability

The current Android source can:

- Browse or search launcher-visible applications on the current device.
- Match application labels and package names with deterministic relevance scoring.
- Preserve exact Android component identity for application launch handoff.
- Run eligible providers through structured concurrent coroutine execution.
- Enforce a provider-specific timeout with a global safety cap.
- Preserve external cancellation instead of converting cancellation into a provider error.
- Let Compose cancel a superseded search when the query changes.
- Distinguish provider failure from provider timeout without exposing exception contents.
- Keep healthy-provider results when another eligible provider fails or times out.
- Rank results before provider-scoped deduplication so the best-scoring representation of a provider/resource identity is retained.
- Apply a fail-closed `IndexExecutionContext` that explicitly lists eligible providers and can prohibit non-local processing.
- Accept the exported user-visible `com.goreecloud.index.action.SEARCH` handoff with `com.goreecloud.index.extra.QUERY` for GoreeCloud Launcher integration.
- Present current provider coverage explicitly as **Applications · On-device**.
- Surface searching, blank/browse, no-match, provider-failed, provider-timed-out, result, and application-launch-failure states.
- Keep the current application provider local-only without requesting Android `INTERNET` or unrestricted `QUERY_ALL_PACKAGES` permission.

`IndexExecutionContext` is an internal execution-eligibility boundary for the current development runtime. It is **not** a claim of accepted Privacy Shield or GoreeCloud Identity authorization integration.

## Current Provider

The only implemented provider remains **Applications · On-device**. It uses `ACTION_MAIN` plus `CATEGORY_LAUNCHER`, preserves exact launcher-component identity, operates over an in-memory snapshot, declares `LOCAL` processing, and uses a provisional 500 ms development timeout. That timeout is a safety bound for the current provider runtime, not a product latency SLA or representative-device performance claim.

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

Additional providers must not be enabled merely because asynchronous execution exists. Private-content, indexed, first-party service, extension, third-party, and remote providers still require their applicable permission, identity, privacy, security, retention, and source-authority contracts.

## Product Boundary

GoreeCloud Index is not a second Internet search engine. **GoreeCloud Search remains the GoreeCloud Internet/web/current-information search authority.** Index owns universal provider coordination, authorization-aware query dispatch, result normalization, ranking, grouping, filtering, source labeling, and safe provider-authorized actions.

Source applications and services remain authoritative for their own resources. GoreeCloud Launcher is a primary invocation/presentation surface and may contribute Launcher-specific context, but it must not become a competing universal index.

## Design Principles

- Local-first processing where practical.
- Explicit, fail-closed provider eligibility.
- Least privilege and provider-specific authorization.
- Structured concurrency, supersession cancellation, and bounded provider latency.
- Explicit opt-in for third-party and remote providers.
- Clear local-versus-remote processing boundaries.
- Privacy-minimized indexing and storage.
- Provider-scoped identity and visible provenance.
- Failure isolation so one provider cannot break all search.
- Versioned provider contracts rather than hidden database access.
- Original GoreeCloud-owned native implementation built from the ground up.

## GoreeCloud Platform Requirements

The current UI targets **Glaze UI 2.1.0**, but formal application-specific consumer conformance remains pending. Runtime integration and acceptance also remain pending for **Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, and GoreeCloud Identity**. Those systems define required authority boundaries; their names must not be interpreted as completed runtime integrations.

## Android Development Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Development label: `GoreeCloud Index Dev`
- Version: `0.2.0-dev`
- Version code: `2`
- Minimum Android API: 26
- Compile API: 37
- Target API: 36

## Accepted Main Baseline

The currently accepted main baseline before this branch is commit `19737c11c59a30a94ee8b6dad8855b449c011eca`. Exact-main GitHub Actions run `33420873144` passed repository validation, Android unit tests, lint, Development APK assembly, package/version/label verification, checksum capture, and artifact publication.

Accepted-main Development APK SHA-256: `a867073c433941297da985a1c8ec3e3972e7ecd6db4883f18e935a8d6fd72f83`.

Accepted-main artifact: `9768893227`, named `goreecloud-index-development-apk-19737c11c59a30a94ee8b6dad8855b449c011eca`.

The `0.2.0-dev` asynchronous provider-runtime source is newer than that accepted baseline and must pass its own exact-candidate validation before its build evidence can replace the accepted-main record.

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
