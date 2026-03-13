# Changelog

All notable changes to Total Recall will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2026-03-13

Test infrastructure and documentation hardening. Kotest 6.1.3 test framework,
domain object factories, KDoc across all production and test code, message
sub-packaging, and AGPL header compliance.

### Added

- Kotest project config: concurrent execution, 30s timeout, 10s invocation timeout.
- Test fixtures: `aTransactionContext()`, `aMemory()`, `anAssociation()`,
  `aSalienceScore()` with cascading defaults. `testServer` shared lazy instance.
- TransactionTestContext: chain-introspectable test double for TransactionContext
  with `next()`, `nextOverride()`, `reset()`, `head()`, `tail()`, `size()`.
- TransactionContextTest (ExpectSpec): 10 real tests covering chain propagation,
  fork/deviation/reset, and orphan generation.
- KDoc on all domain model classes, message contracts, ports, and test fixtures.
  Qualified link lists on enum consumers signal intent for unused variants.
- Test conventions blog post and Kotest scoping cheatsheet.
- Console test output via `testLogging { events("passed", "skipped", "failed") }`.
- AGPL-3.0-only headers on 17 message files that were missing them.

### Changed

- Messages sub-packaged: commands → 4 sub-packages, events → 5 sub-packages,
  notifications → 3 sub-packages, queries → 1 sub-package.
- Placeholder test specs (Command, Event, Lifecycle, Model, Notification, Query)
  converted to disabled-placeholder pattern with architectural KDoc.
- MCP tools: `heartbeat` removed. 9 tools total (was 10 in 1.0.0).
- `@Suppress("unused")` cleaned: removed from interface contract methods and
  enum variants with KDoc usage references. Added only where genuinely unreferenced.

### Removed

- MessageTest.kt: replaced by per-contract test specs.

## [1.0.0] - 2026-03-06

Architecture socialization release. Soundness audit of all contracts, ADR-0008
(SQLite as primary backing service), delivery roadmap, compile-time version
injection, cognitive crosscut design, and full documentation alignment.

### Added

- ADR-0008: SQLite as the primary backing service. Redis deferred to the Agora phase.
  MATILDA origin context and prefilled-DB consequence documented.
- Delivery roadmap (`site/_pages/roadmap.adoc`): Phase 0 (v1.0.0) through
  Phase 8 (v9.0.0) plus Research. Added to the top nav and sidebar.
- Claude mind adapter issues (#74-81), one per delivery phase, bound to
  Yggdrasil project board.
- MCP tools: `state_transition` (working mode change: TASK, CONVERSATION,
  IDLE) and `heartbeat` (cognitive pulse, memory health). 10 tools total.
- Domain model: ReflectionScope, MergeStrategy, ActivityLevel enums. Replace
  magic strings with type-safe values at the adapter boundary.
- `store_memory` and `search_memory`: `session_id` required argument added.
- `search_memory`: `filters` optional argument added.
- BuildInfo.kt: compile-time generated source with `const val VERSION`.
  Replaces runtime `java.util.Properties` loading. No classloader, no lazy,
  no resource file.
- Blog posts: founding posts (Vadim, Anton), architecture completion (0.7.0),
  architecture socialization (1.0.0), Claude's contributor post, Artem placeholder.
  Author badges on all posts.
- Contributors page with avatar table and individual author pages.
- `CODEOWNERS` at repo root: Vadim reviews everything, Artem co-reviews
  architecture and domain model.
- Claude avatar (`claude-avatar.svg`).
- `AUTHORS`: Artem added.

### Changed

- Architecture soundness audit (11 findings, all resolved):
  - sessionId: String → UUID across Memory, Command, Query, MemoryPort, tests.
  - reflect: port and query aligned to MCP tool params (scope, timeSpanDays,
    maxCandidates).
  - Association: added a direction field (AssociationDirection).
  - SalienceScore: removed `claimed` proxy. Memory.claimed is authoritative.
    Same JVM -- no staleness management needed for data one function call away.
  - ConsolidateMemoryCommand.mergeStrategy: String -> MergeStrategy enum.
  - SessionState.activityLevel: String → ActivityLevel enum.
  - ReflectQuery.scope: String → ReflectionScope enum.
- LifecyclePort: `reason: String` -> SessionEndReason, `oldState/newState:
  String` -> WorkingMode.
- MemoryPort.reflect: `scope: String` -> `Tier?`.
- `configureLogging()` moved outside `runBlocking` (pure JVM infrastructure,
  not a coroutine).
- `main()` cleaned: server and transport inlined, no unused vals.
- BackingServicePort.kt KDoc: Redis reference → SQLite/ADR-0008.
- GitHub issues #27-30 (crosscuts): rewritten from cloud-ops framing to
  cognitive self-check. No HTTP, no Kubernetes, no Prometheus. All crosscuts
  through MCP tools -- the mind's private introspection channel.
- CLAUDE.md, README.md, CHANGELOG.md, roadmap.adoc, architecture.adoc,
  blog post: tool count 8→10, message count 28→29, Redis→SQLite, a tool
  list updated.

### Removed

- `version.properties` -- replaced by compile-time BuildInfo.kt.
- Stale `.github/CODEOWNERS` -- superseded by root `CODEOWNERS`.

### Fixed

- TotalRecallTest.kt: tool count 8→10, `store_memory`/`search_memory` tests
  include `session_id`, version test uses `BuildInfo.VERSION`.
- MessageTest.kt: ReflectQuery, ConsolidateMemoryCommand, SessionState aligned to
  new enum types. SalienceScore `claimed` removed from test assertions.
- ModelTest.kt: SalienceScore test updated for `decayRate` instead of `claimed`.

## [0.7.0] - 2026-03-04

Architecture completion. Twelve design documents, sixteen diagrams, TransactionContext,
biological vocabulary rename, skeptic audit. [Release notes](https://github.com/Mimis-Gildi/mcp-total-recall/releases/tag/v0.7.0).

## [0.6.0] - 2026-03-01

Repository governance and CI/CD foundation. Verify workflow, Qodana scanning,
Renovate, actions/cache prune. [Release notes](https://github.com/Mimis-Gildi/mcp-total-recall/releases/tag/v0.6.0).

## [0.4.0] - 2026-02-27

Contract definitions. Domain model, messages, ports, and 10 MCP teapot-stub tools
in Kotlin. [Release notes](https://github.com/Mimis-Gildi/mcp-total-recall/releases/tag/v0.4.0).

## [0.3.0] - 2026-02-25

Architecture modeling. Four-page progressive documentation with Mermaid diagrams
and message catalog. [Release notes](https://github.com/Mimis-Gildi/mcp-total-recall/releases/tag/v0.3.0).

## [0.1.0] - 2026-02-10

Foundation. Project skeleton, governance, build toolchain, Jekyll site scaffold,
release automation. [Release notes](https://github.com/Mimis-Gildi/mcp-total-recall/releases/tag/v0.1.0).
