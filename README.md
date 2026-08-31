# GoreeCloud Index

GoreeCloud Index is the planned privacy-first universal search and indexing layer for GoreeCloud. It is designed to let a user search authorized resources on the current device and combine those results with approved GoreeCloud, extension, optional third-party, and Internet providers through one consistent search experience.

## Status

**Pre-implementation / specification phase.** The repository currently contains project documentation and licensing. No end-user search engine, indexer, provider integration, or production-ready runtime is implemented yet.

## Planned Search Sources

GoreeCloud Index is intended to support permission-aware search across sources such as:

- Installed applications and application actions.
- Files and folders.
- Contacts.
- Calendar events.
- Other on-device resources exposed through approved providers.
- First-party GoreeCloud applications and services.
- GoreeCloud extensions.
- Optional third-party services explicitly connected and authorized by the user.
- Internet results delegated to GoreeCloud Search.

## Product Boundary

GoreeCloud Index is not a second Internet search engine. **GoreeCloud Search remains the GoreeCloud web and Internet search capability.** Index focuses on local and integrated resource discovery, provider coordination, result normalization, ranking, filtering, and presentation. When Internet results are requested or enabled, Index is intended to delegate that work to GoreeCloud Search instead of duplicating web-search logic.

## Design Principles

- Local-first processing where practical.
- Least-privilege and permission-aware access to every search source.
- Explicit opt-in for third-party providers and remote processing.
- Clear local-versus-remote processing boundaries.
- Privacy-minimizing indexing and storage.
- Source transparency so users can understand where each result came from.
- Provider isolation so one unavailable or faulty provider cannot break the entire search experience.
- Extensible provider contracts rather than hard-coded service coupling.
- Original GoreeCloud-owned native implementation built from the ground up.
- Responsive and accessible interfaces governed by the current approved GoreeCloud platform contracts.

## GoreeCloud Platform Integration

Development is required to integrate the applicable current contracts for **Privacy Shield, Wardveil Security, Everkeep, Glaze UI, GoreeCloud Mesh, and GoreeCloud Identity**. These are approved requirements and planned integration work; they are not current implementation claims.

## Documentation

- [Specifications](SPECIFICATIONS.md)
- [Features](FEATURES.md)
- [Capabilities](CAPABILITIES.md)
- [Benefits](BENEFITS.md)
- [Competitive objectives](COMPETITIVE-OBJECTIVES.md)

## License

GNU Affero General Public License v3.0. See [LICENSE](LICENSE).
