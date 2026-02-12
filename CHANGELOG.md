# Changelog

All notable changes to Total Recall will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-02-10

Generation 3, Take 2 foundation release. This establishes the project skeleton,
governance, and infrastructure for the MCP memory server. No runtime functionality
yet -- this release is about getting the house in order before writing the first
line of production code.

### Scope

Total Recall is a standalone repository under the Sanctuary umbrella. It builds
independently (not included in any parent Gradle project). This release covers
everything needed to begin development: build toolchain, test framework, project
governance, collaboration infrastructure, and a public project board.

### Added

- Gradle build skeleton: Kotlin 2.3.10, Java 21 (LTS), Kotest 6.1.3.
- Version catalog (`gradle/libs.versions.toml`) for dependency management.
- SDK Manager configuration (`.sdkmanrc`) for reproducible toolchain.
- Programmatic logback configuration in Kotlin (no XML). Logs to console.
- Hello-world entry point (`TotalRecall.kt`) with two Kotest specs.
- Repository governance: LICENSE (AGPL-3.0), NOTICE, CLA, AUTHORS, TEAM_NORMS,
  CONTRIBUTING, CODE_OF_CONDUCT, SECURITY, ATTRIBUTIONS.
- GitHub issue templates: bug, feature, architecture, idea.
- Pull request template with layer checklist.
- Dependabot configuration for GitHub Actions and Gradle ecosystems.
- Repository labels for work areas, types, and automation.
- Yggdrasil project board (#6, public, linked to repo).
- Jekyll site scaffold (empty, ready for progress content).
- CHANGELOG, CODEOWNERS, `.editorconfig`.
- Release automation: publish workflow reads CHANGELOG.md and creates GitHub
  Releases on PR merge. No separate release notes file -- CHANGELOG.md is the
  single source of truth.
- Composite actions ported from parent: feature branch guard, runner detection,
  version extraction, annotated tagging. New: changelog section extraction.

### Deferred

Items explicitly scoped out of 0.1.0 for later releases:

- **MCP server and protocol handling.** No runtime functionality yet.
- **Memory model and backing services.** Redis integration deferred.
- **Container images.** Docker/OCI approach TBD (intentionally not Dockerfile).
- **CI/CD pipeline.** Build and test workflow deferred (publish workflow runs build as gate).
- **Internal documentation (`docs/`).** AsciiDoc directory deferred.

### Notes

- This is foundation only. The first line of production code comes in 0.2.0.
- Gen 3v1 code preserved in `Sanctuary/mcp-total-recall-retired` for reference.
- Architecture documented in README: decoupled layers, swappable backends, stdio primary.
- Logging configured to stdout now; will switch to stderr when stdio transport is active
  (stdout becomes the MCP protocol channel).
