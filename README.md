# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It is designed to search authorized resources on the current device and, as additional providers are implemented, combine them with approved GoreeCloud, extension, optional third-party, connected-device, and Internet sources through one consistent search experience.

## Status

**Active development — native Android application-search foundation.** The current source contains a real Android/Jetpack Compose application, provider-neutral query/result/action contracts, a failure-isolated query engine, a launcher-visible Android applications provider, the Launcher→Index search-entry contract, a source-aware search UI, unit tests, repository validation, and an Android build-evidence workflow.

GoreeCloud Index is **not Stable or production accepted**. Current source/build validation does not establish representative-device, accessibility, platform-integration, production-signing, deployment, or Stable acceptance.

## Current Source Capability

The current Android slice can:

- Browse or search launcher-visible applications on the current device.
- Match application labels and package names with deterministic relevance scoring.
- Preserve exact Android component identity for application launch handoff.
- Rank and deduplicate provider-scoped results.
- Isolate a failing provider and expose a sanitized degraded-provider state rather than silently treating failure as a successful empty result.
- Accept the exported user-visible `com.goreecloud.index.action.SEARCH` handoff with `com.goreecloud.index.extra.QUERY` for GoreeCloud Launcher integration.
- Present current provider coverage explicitly as **Applications · On-device**.
- Surface blank/browse, no-match, provider-unavailable, result, and application-launch-failure states.
- Keep the current provider local-only without requesting Android `INTERNET` or unrestricted `QUERY_ALL_PACKAGES` permission.

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

## Product Boundary

GoreeCloud Index is not a second Internet search engine. **GoreeCloud Search remains the GoreeCloud Internet/web/current-information search authority.** Index owns universal provider coordination, authorization-aware query dispatch, result normalization, ranking, grouping, filtering, source labeling, and safe provider-authorized actions.

Source applications and services remain authoritative for their own resources. GoreeCloud Launcher is a primary invocation/presentation surface and may contribute Launcher-specific context, but it must not become a competing universal index.

## Design Principles

- Local-first processing where practical.
- Least privilege and provider-specific authorization.
- Explicit opt-in for third-party and remote providers.
- Clear local-versus-remote processing boundaries.
- Privacy-minimized indexing and storage.
- Provider-scoped identity and visible provenance.
- Failure isolation so one provider cannot break all search.
- Versioned provider contracts rather than hidden database access.
- Original GoreeCloud-owned native implementation built from the ground up.

## GoreeCloud Platform Requirements

The current UI targets **Glaze UI 2.1.0**, but formal consumer conformance remains pending. Runtime integration and acceptance also remain pending for **Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, and GoreeCloud Identity**. Those systems define required authority boundaries; their names must not be interpreted as completed runtime integrations.

## Android Development Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Development label: `GoreeCloud Index Dev`
- Version: `0.1.0-dev`
- Minimum Android API: 26
- Compile API: 37
- Target API: 36

## Validation Evidence

The first merged Android foundation at main commit `331e97507a7b3b7ca3d930771915f1026bf2d4a8` passed exact-main GitHub Actions run `33418751538`: repository validation, Android unit tests, lint, development APK assembly, package/application-label verification, checksum capture, and artifact publication all succeeded.

That APK had SHA-256 `8fee493995d500cd09579e15fe17915b7800117463f68fbc85f18cfd24b0ea3f`. Artifact `9768208441` was named `goreecloud-index-development-apk-331e97507a7b3b7ca3d930771915f1026bf2d4a8`.

Later revisions require their own exact-candidate validation before being accepted as build evidence.

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
