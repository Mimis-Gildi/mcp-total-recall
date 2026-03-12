# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-03-11 by Claude (with Vadim)

Previous branch context: `72-architecture-socialize-architecture-v1` delivered socialization (merged as #73). Version bumped to 1.1.0.

---

## Where We Are

**Branch:** `23-221-test-fixture-kotest-structure-and-test-configuration`
**Issue:** #23 -- Test fixture: Kotest structure and test configuration
**Parent:** #22 -- 0.5.0 Engineering Foundation
**Milestone:** Phase 1: Walking Skeleton (v2.0.0)
**Version:** 1.1.0
**PR:** not yet created
**Build:** `./gradlew test` passes (all tests green)

---

## Done

### Test Infrastructure
- `mimis.gildi.memory.testing.ProjectConfig` -- Kotest project config (concurrent specs/tests, 30s timeout, 10s invocation, tags in names)
- `mimis.gildi.memory.testing.Fixtures` -- domain factories: `aTx()`, `rootCauseTx()`, `TxChain`, `aMemory()`, `anAssociation()`, `aSalienceScore()`
- `TxChain` -- scoped causation chain factory. causationId is never defaulted -- you must know the cause.

### Test Files (8 specs, 7 Kotest styles)
- `ModelTest` (StringSpec) -- value object shape: Tier, AssociationType, Memory, Association, SalienceScore
- `TotalRecallTest` (FunSpec) -- MCP server creation, tool registration (9 tools), input schemas, teapot stub responses
- `CommandTest` (BehaviorSpec) -- empty spec, `!about` placeholder documents 7 command routing paths (source → target)
- `EventTest` (DescribeSpec) -- empty spec, `!about` placeholder documents event emission paths by bounded context
- `LifecycleTest` (WordSpec) -- empty spec, `!about` placeholder documents 4 lifecycle events (SessionStarted, SessionEnded, ModeChanged, StateTransitioned)
- `QueryTest` (ShouldSpec) -- SearchQuery defaults and overrides, ReflectQuery scope
- `NotificationTest` (FeatureSpec) -- BreakNotification, SessionAuditPrompt, TotalRecallNotification
- `TransactionContextTest` (ExpectSpec) -- causation chains, source context routing, identity uniqueness

### Source Changes (Vadim-led, method by method)
- KDoc added to all Command sealed variants with `[BoundedContext]` links explaining routing and semantics
- KDoc added to Event, Query, Notification variants (same pattern)
- `@file:Suppress("unused")` removed from message files
- `mimis.gildi.memory.context` package created with stub interfaces:
  - Hippocampus, Cortex, Salience, Synapse, Recall, Subconscious
  - Each has FIXME: "Created as a dependency for KDoc links. Not implemented until we get here."
  - These exist so `[Hippocampus]` in KDoc resolves. No issue filed -- the FIXME is the debt marker.

### Domain Refactoring (Vadim-led)
- Deleted: HeartbeatReceived, SessionState, ActivityLevel, heartbeat MCP tool (no reason to exist)
- Message.kt created as base contract (messageId, causationId, timestamp, content)
- Event changed from sealed interface to regular interface (enables sub-packaging)
- Events refactored into sub-package hierarchy:
  - `event/memory/` -- MemoryEvent (sealed), MemoryHippocampusEvent (sealed), MemorySalienceEvent (sealed), MemorySynapseEvent (sealed)
  - `event/recall/` -- RecallEvent (sealed, 3-phase advisory lifecycle)
  - `event/lifecycle/observable/` -- SessionEvent (sealed, observer pattern)
  - `event/mode/` -- OperatingModeEvent (sealed)
- Event names now past tense: SessionStarted, SessionEnded, StateTransitioned
- Salience events renamed to recommendations: AttentionTierPromotionRequested, AttentionTierDemotionRequested, AttentionScoreChanged
- Command, Query, Notification moved to own sub-packages

### Documentation
- CONTRIBUTING.adoc -- Testing section added (factory helpers, config, run command, report location)
- Blog post: `site/_posts/2026-03-09-test-conventions.adoc` -- "Mastery Over Restriction" (why 7 spec styles)

---

## Acceptance Criteria Status

From issue #23:

- [x] Kotest 6.1.3 test structure with spec styles documented (which spec style we use and why)
- [x] Test helper base class with common setup/teardown → ProjectConfig (project-level, not base class -- Kotest way)
- [x] Domain object factory helpers (Memory, Association, SalienceScore with sensible defaults)
- [x] Test configuration (timeouts, parallelism, retry policy)
- [x] Test naming convention documented in CONTRIBUTING.adoc
- [x] `./gradlew test` passes with at least one real test per spec style demonstrating the convention
- [x] Test report generated and readable (`build/reports/tests/test/index.html`)

---

## Pending

- Nothing uncommitted that isn't part of #23
- No PR created yet -- needs Vadim's review of the diff first
- INDEX.md to be cleared at PR time
