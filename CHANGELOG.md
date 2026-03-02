# Changelog

All notable changes to Total Recall will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.6.0] - 2026-03-01

Repository governance and CI/CD foundation. Continuous integration, security
scanning, dependency management, and automated maintenance -- everything
needed before contributors start building.

### Added

- CI: Verify workflow (`gradle-verify-and-submit.yml`). Runs
  `gradle clean build test` on every push and PR. SDKMAN bootstrap
  for self-hosted runners.
- Security: Qodana scanning (`security-scan-by-qodana.yml`). JetBrains
  Qodana JVM Community 2025.3 on push, PR, and monthly schedule. Includes
  dependency license checking.
- Dependencies: Renovate (`renovate.json5`). Automated dependency updates
  with grouped PRs (Kotlin, MCP SDK, Kotest, Logging, Gradle, Actions,
  Bundler). All automerge.
- Maintenance: Actions prune (`github-actions-prune.yml`). Keeps one run
  per workflow, deletes the rest. Parallel deletion.
- Maintenance: Cache prune (`github-cache-prune.yml`). Cleans stale GitHub
  Actions caches after prune runs.
- README badges: License, Verify, Security Scan, Kotlin, Java, MCP.
- Qodana config (`qodana.yaml`): JVM Community linter, Temurin 21,
  dependency license checks, auto-fix.
- GitHub automatic dependency submission enabled (Gradle).
- Release progression planned (Issue #22): 45 sub-issues across 6 releases
  (0.5.0 Foundation through 4.0.0 Architecture Complete).

### Changed

- CODE_OF_CONDUCT: Consolidated `.adoc` into `.md` (GitHub Community
  Standards requires Markdown). Preserved substrate-of-consciousness
  clause and AI teammates paragraph.

### Removed

- `CODE_OF_CONDUCT.adoc` -- replaced by updated `.md`.
- `dependabot.yml` -- replaced by Renovate.

### Deferred

- Engineering foundation: test fixtures, health checks, metrics (Issue #22,
  release 0.5.0).
- Backing service implementations (release 1.0.0).

## [0.4.0] - 2026-02-27

Contract definitions release. Every port, message, and domain type from the
architecture model (Issue #3) now exists as Kotlin code. MCP server runs on
stdio with 8 teapot-stub tools. The design and the code agree.

### Added

- Domain model (6 types in `mimis.gildi.memory.domain.model`):
  Memory, Tier, Association, AssociationType, SalienceScore, SearchFilter.
- Domain messages (3 sealed hierarchies in `mimis.gildi.memory.domain.message`):
  Command (9 variants), Event (13 variants), Notification (2 variants).
- Inbound ports: MemoryPort, LifecyclePort.
- Outbound ports: BackingServicePort, NotificationPort, RelayPort.
- MCP server on stdio with 8 registered tools (teapot stubs):
  store_memory, search_memory, claim_memory, session_start, session_end,
  associate_memories, reclassify_memory, reflect.
- Domain model and message tests (Kotest).
- Site: announcement posts for Issues #3 and #4.
- Site: favicon from circuit-tree logo.
- Site: architecture pages updated to reference Kotlin types.
- `.mcp.json` for local MCP server configuration.
- IntelliJ run configuration for server testing.

### Changed

- Logging target: stdout → stderr (stdout is the MCP protocol channel).
- MCP SDK dependency added: `io.modelcontextprotocol:kotlin-sdk-server:0.8.4`.
- Site: post ordering fix (documents-collection.html override).

### Deferred

- Redis backing service behind BackingServicePort.
- Domain logic wiring between ports and tools.
- Container images (approach TBD).
- CI/CD build workflow for PR-time checks.

## [0.3.0] - 2026-02-25

Architecture modeling release. Four-page progressive architecture documentation
with Mermaid diagrams and a complete message catalog. All bounded contexts,
ports, and message contracts defined. No runtime code -- this release defines
what we build next.

### Added

- Architecture documentation on the Jekyll site (four pages, progressive depth):
  - Context: system overview, conscience-universal design, Yggdrasil stack.
  - Hexagonal: ports, adapters, actor model, internal timers.
  - Bounded Contexts: six actors and their relationships.
  - Message Catalog: five sequence diagrams, full command/event/notification catalog.
- Mermaid diagram support via `after_footer_scripts` config.
- Left sidebar navigation for architecture page flow.
- Branding images: logo, social preview, banner (JPEG, wired into site config).
- Artem Lytvynov (violog) added to site authors.

### Architecture Decisions

- Conscience-universal port contracts: mind-agnostic, not Claude-specific.
- Agora as peer MCP server alongside Total Recall, not downstream.
- Lifecycle Port: session start/end, state transitions -- pluggable.
- Notification Port: outbound alerts (break checks, session audits).
- Internal timers (decay sweeps, break checks, consolidation) as domain logic.
- Six bounded contexts: Hippocampus, Synapse, Salience,
  Recall, Cortex, Subconscious.
- Message catalog: 6 commands, 9 domain events, 5 lifecycle events,
  2 notification types -- all named, typed, with producer and consumer.

### Changed

- Dependencies: logback 1.5.28 → 1.5.32, kotlin-logging 7.0.14 → 8.0.01.
- GitHub Actions: checkout v5.0.1 → v6.0.2, gradle/actions v5.0.1 → v5.0.2.
- IntelliJ run config: JetRunConfigurationType → GradleRunConfiguration (Gradle 9).
- Branch protection: 1 approval required, stale reviews dismissed,
  conversation resolution required.

### Deferred

- MCP server and protocol handling. Architecture is defined; code comes next.
- Redis backing service adapter.
- Container images (approach TBD).
- CI/CD build workflow for PR-time checks.

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
