# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-03-05 by Claude (with Vadim)

Previous branch context: `2-architecture-hexagonal-boundaries-bounded-contexts-and-message-contracts-2` delivered full architecture (merged as #71).

---

## Where We Are

**Branch:** `72-architecture-socialize-architecture-v1`
**Issue:** #72 -- Socialize Architecture v1
**Version:** 1.0.0

---

## Done This Session

### ADR-0008: SQLite as Primary Backing Service
- ADR written and published. SQLite primary, Redis deferred to Agora.
- Vadim added MATILDA context, origin honesty ("copied without much thought"), prefilled DB consequence.
- BackingServicePort.kt KDoc updated (Redis reference -> SQLite/ADR-0008).
- Added to site navigation, config, and docs/adr/README.md.

### Delivery Roadmap
- `site/_pages/roadmap.adoc` created. Phase 0 (v1.0.0) through Phase 8 (v9.0.0) + Research.
- Phase 1 = all infrastructure (test fixtures, CI/CD, observability, packaging) before any functionality.
- Added to top navigation bar and sidebar.

### Claude Mind Adapter Issues
- Created issues #74-81, one per delivery phase.
- All bound to Yggdrasil project board (#6) and as sub-issues of #22.
- Issue #22 body updated with canonical roadmap link.

### Port Code Fixes
- LifecyclePort.kt: `reason: String` -> `SessionEndReason`, `oldState/newState: String` -> `WorkingMode`
- MemoryPort.kt: `scope: String` -> `Tier?`

### Build Fix
- `build.gradle.kts`: added `inputs.property("version", project.version)` to processResources.
  Fixes stale version caching when gradle.properties changes.

### Code Review (in progress with Vadim)
- VERSION loading block in TotalRecall.kt: Vadim refactoring from Java-style to idiomatic Kotlin (`run` scope function, `::load` method reference).
- Confirmed: no Kotlin-native wrapper for `java.util.Properties` exists.

### Architecture Soundness Audit
- Four parallel agents audited: domain model, ports vs MCP tools, site pages, ADRs.
- Findings documented below.

---

## Soundness Audit Findings

### CRITICAL -- Design Decisions Needed

**1. `sessionId` type conflict: UUID vs String**
- `TransactionContext.kt`: `sessionId: UUID`
- `Memory.kt`: `sessionId: String`
- `StoreCommand`, `SearchQuery`: `sessionId: String`
- Waiting for Vadim's decision.

**2. `reflect` port signature doesn't match MCP tool**
- Port: `reflect(criteria: Map<String, String>, scope: Tier?)`
- MCP tool: `scope` (string), `time_span_days` (integer), `max_candidates` (integer)
- Completely different interfaces.

**3. Two LifecyclePort methods have no MCP tools**
- `stateTransition` and `heartbeat` defined in port, not exposed as tools.
- Intentionally deferred or missed?

### MODERATE -- Code Fixes Needed

**4. `store_memory` and `search_memory` MCP tools missing `sessionId` argument**

**5. `search_memory` missing `filters` argument**

**6. `Association` model missing `direction` field**
- `AssociationDirection` enum exists, `AssociateCommand` uses it, but `Association` data class doesn't.

**7. `SalienceScore.claimed` duplicates `Memory.claimed`**
- Which is authoritative?

**8. String fields that should be enums**
- `ConsolidateCommand.mergeStrategy`, `ReflectQuery.scope`, `SessionState.activityLevel`

### DOCUMENTATION -- Fix Before Merge

**9. Message count wrong: 29, not 28**
- 7 + 2 + 17 + 3 = 29. Architecture page and roadmap both say 28.

**10. Broken nav link in `architecture-contexts.adoc`**
- "Next" link points to `/architecture/contexts/` instead of `/architecture/messages/`

**11. README tech stack still says Redis**
- Stale after ADR-0008. Should reference SQLite primary, Redis at Agora.

### CLEAN -- No Issues

- ADRs internally consistent. ADR-0008 properly amends ADR-0004.
- Bounded context names consistent everywhere.
- Tier and AssociationType enums consistent.
- Outbound ports correctly unwired (teapot phase).
- All site pages reachable from navigation.
- All ADRs in navigation, config, and index.

---

## Pending

| # | Task                                                   | Status        |
|---|--------------------------------------------------------|---------------|
| 1 | Vadim decides sessionId type (UUID vs String)          | waiting       |
| 2 | Vadim finishes VERSION refactoring in TotalRecall.kt   | in progress   |
| 3 | Fix doc issues (#9-11 above)                           | ready         |
| 4 | Fix moderate code issues (#4-8) after design decisions | blocked on #1 |
| 5 | INDEX.md cleanup                                       | done          |
