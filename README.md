# GoreeCloud Index

GoreeCloud Index is GoreeCloud's privacy-first universal search and indexing layer. It is designed to search authorized resources on the current device and, as additional providers are implemented, combine them with approved GoreeCloud, extension, optional third-party, and Internet sources through one consistent search experience.

## Status

**Active development — initial native Android application-search slice.** The current source contains a real Android application shell, typed search/provider models, a failure-isolated query engine, a launcher-visible Android application provider, a source-aware search UI, unit tests, repository validation, and an Android CI/build pipeline.

This project is **not Stable or production accepted**. The current application provider still requires representative-device acceptance, and the broader file, contact, calendar, GoreeCloud, Internet, extension, and third-party provider scope remains planned.

## Current Source Capability

The current Android slice can:

- Accept an application search query.
- Query Android launcher applications through a narrowly declared `ACTION_MAIN` / `CATEGORY_LAUNCHER` visibility contract.
- Match application labels and package names.
- Rank and deduplicate provider-scoped results.
- Isolate provider failure from the rest of the query engine.
- Present blank, no-result, provider-unavailable, result, and launch-failure states.
- Hand an application result back to Android for launch.
- Keep this provider local-only without adding query telemetry or remote search.

The source deliberately does **not** request `QUERY_ALL_PACKAGES`.

## Planned Search Sources

- Android and other platform applications/actions.
- Files and folders.
- Contacts.
- Calendar events.
- Other on-device resources exposed through approved provider contracts.
- First-party GoreeCloud applications and services.
- GoreeCloud extensions.
- Optional third-party services explicitly connected and authorized by the user.
- Internet results delegated to GoreeCloud Search.

## Product Boundary

GoreeCloud Index is not a second Internet search engine. **GoreeCloud Search remains the GoreeCloud web and Internet search capability.** Index owns provider coordination, authorization-aware query dispatch, result normalization, ranking, grouping, filtering, source labeling, and safe result actions. Source applications and services remain authoritative for their own resources.

## Design Principles

- Local-first processing where practical.
- Least privilege and permission-aware provider access.
- Explicit opt-in for third-party and remote providers.
- Clear local-versus-remote processing boundaries.
- Privacy-minimized indexing and storage.
- Provider-scoped identity and source transparency.
- Failure isolation so one provider cannot break all search.
- Extensible, versioned provider contracts rather than hidden database access.
- Original GoreeCloud-owned native implementation built from the ground up.

## GoreeCloud Platform Requirements

The current UI targets **Glaze UI 2.1.0**, but formal consumer conformance remains pending. Runtime integration and acceptance also remain pending for **Privacy Shield, Wardveil Security, Everkeep, GoreeCloud Mesh, and GoreeCloud Identity**. Those platform names describe required authority boundaries, not completed integrations.

## Android Development Identity

- Production application ID: `com.goreecloud.index`
- Development application ID: `com.goreecloud.index.dev`
- Development label: `GoreeCloud Index Dev`
- Version: `0.1.0-dev`
- Minimum Android API: 26
- Compile API: 37
- Target API: 36

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
