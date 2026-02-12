# Changelog

All notable changes to Total Recall will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-02-10

### Added

- Project foundation: Gradle build skeleton with Kotlin 2.3.10, Java 21, Kotest 6.1.3.
- Programmatic logback configuration (no XML).
- Repository governance: LICENSE (AGPL-3.0), NOTICE, CLA, AUTHORS, TEAM_NORMS, CONTRIBUTING, CODE_OF_CONDUCT, SECURITY, ATTRIBUTIONS.
- GitHub issue templates: bug, feature, architecture, idea.
- Pull request template with layer checklist.
- Dependabot configuration for GitHub Actions and Gradle.
- Repo labels for work areas, types, and automation.
- Yggdrasil project board (public, linked to repo).
- Jekyll site scaffold (empty, ready for content).
- CHANGELOG and CODEOWNERS.

### Notes

- This is foundation only. No MCP server, no memory model, no backing services yet.
- Gen 3v1 code preserved in `Sanctuary/mcp-total-recall-retired` for reference.
- Architecture documented in README: decoupled layers, swappable backends, stdio primary.
