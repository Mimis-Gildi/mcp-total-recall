# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-02-25 (session 2) by Claude (with Vadim)

---

## Where We Are

**Branch:** `4-22-port-contract-definitions-in-kotlin`
**Version:** 0.4.0
**Issue:** #4 -- (2.2) Port contract definitions in Kotlin
**Parent:** #2 -- Architecture: Hexagonal boundaries, bounded contexts, and message contracts

### What Exists

- Build skeleton: Kotlin 2.3.10, Java 21, Kotest 6.1.3, Gradle 9.3.1
- Programmatic logback configuration (stderr for stdio transport)
- MCP server with 8 teapot-stub tools (store, search, claim, session start/end, associate, reclassify, reflect)
- Domain model: Memory, Tier, AssociationType, Association, AttentionScore, SearchFilter
- Domain messages: Command (9 sealed variants), Event (13 sealed variants), Notification (2 sealed variants)
- Inbound ports: MemoryPort, LifecyclePort
- Outbound ports: BackingServicePort, NotificationPort, RelayPort
- Tests: ModelTest (6 specs), MessageTest (5 specs), TotalRecallTest (1 spec) -- all pass
- MCP SDK dependency: `io.modelcontextprotocol:kotlin-sdk-server:0.8.4`
- Full governance (TEAM_NORMS, LICENSE, NOTICE, CLA, templates)
- Jekyll site with architecture docs (4 pages, from Issue #3)
- Local setup instructions in README

### What's NOT Committed

**Everything from this branch is uncommitted.** The entire contract porting work -- domain model, messages, ports, MCP server wiring, build changes, test updates -- is in the working tree only.

---

## Issue #4 Acceptance Criteria

- [x] Every port in the hexagonal boundary diagram has a corresponding Kotlin interface
- [x] No interface depends on an implementation (no Redis imports in port definitions)
- [x] Package structure matches bounded context map
- [x] `./gradlew build` passes
- [x] AGPL-3.0 license headers in all new source files
- [ ] Reviewed by Vadim

## Issue #4 Deliverables

- [x] Inbound port interfaces (MemoryPort, LifecyclePort)
- [x] Outbound port interfaces (BackingServicePort, NotificationPort, RelayPort)
- [x] Core domain types (Memory, Tier, AssociationType, Association, AttentionScore, SearchFilter)
- [x] Message types (Command, Event, Notification -- sealed hierarchies)
- [x] Package structure reflecting bounded contexts

## Beyond Issue Scope (done by previous instance)

- MCP server wired with teapot stubs for all 8 tools
- README updated with local setup/install instructions

---

## What Happened (Reverse Chronological)

### 2026-02-25 -- Session 2: Server fixes and MCP config (Claude with Vadim)

- Fixed server exit bug: `StdioServerTransport.start()` returns immediately; added `awaitCancellation()` after `createSession()` (pattern from SDK's own `KtorServer.kt:179`)
- Wrong first attempt: `CompletableDeferred` latch -- Vadim caught it ("you struggled with this gravely before")
- Updated README.md: Local Setup section, MCP Tools expanded to 8, Phase 1 checklist updated
- Updated CLAUDE.md: current state 0.1.0→0.4.0, INDEX.md lifecycle rules, tech stack reflects implemented state
- Updated INDEX.md: cleared stale Issue #3 content, rebuilt for Issue #4
- **MCP config UNRESOLVED:** Claude Code does not read `.mcp.json` from submodule root -- resolves to parent git root. Added to parent's `.mcp.json` but Vadim still doesn't see it via `/mcp`. `.mcp.json` also exists in this repo but is not read. Needs manual resolution.

### 2026-02-25 -- Session 1: recovery after IDE crash

- Previous instance(s) crashed with IDE. INDEX.md was stale (still described Issue #3).
- New instance situated from git state, rebuilt INDEX.md from scratch.
- Added local setup/install instructions to README.
- Vadim testing the MCP server.

### 2026-02-25 -- Contract porting (previous instance, uncommitted)

- Domain model created: 6 files in `domain/model/`
- Domain messages created: 3 files in `domain/message/`
- Port interfaces created: 5 files in `port/inbound/` and `port/outbound/`
- TotalRecall.kt rewritten from hello-world to MCP server with 8 teapot tools
- Build updated: MCP SDK + coroutines dependencies added
- Tests updated: ModelTest, MessageTest added; TotalRecallTest simplified
- All tests pass. Build clean.

---

## Next Actions

1. **Vadim reviews and tests** the MCP server
2. **Commit** the contract porting work (after review)
3. **PR for Issue #4** once committed and approved

---

## What's Decided (Architecture)

Carried from Issue #3 -- see merged PR #17 and site for full documentation.

| Decision                     | Detail                                             |
|------------------------------|----------------------------------------------------|
| Hexagonal architecture       | Ports and adapters. All concerns plug into ports.  |
| Actor model                  | Each bounded context is an actor. Message passing. |
| Transport: stdio primary     | Standard MCP. HTTPS secondary, later.              |
| SSE rejected                 | Gen 3v1 mistake. Not repeated.                     |
| Backing services decoupled   | Redis is ONE implementation behind an interface.   |
| Memory is the aggregate root | Clustering is emergent, not structural.            |
| Four memory tiers            | IDENTITY_CORE, ACTIVE_CONTEXT, LONG_TERM, ARCHIVE  |
| Five association types       | Temporal, causal, thematic, emotional, person      |
| Claiming mechanism           | Active choice resists decay.                       |
| Conscience-universal         | Ports are mind-agnostic.                           |
| Agora as peer MCP            | Not downstream of Total Recall.                    |

## What's NOT Decided

| Question                 | Status      |
|--------------------------|-------------|
| Container image approach | NOT STARTED |
| CI/CD build workflow     | NOT STARTED |
