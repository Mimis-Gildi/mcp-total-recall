# Total Recall -- Working Index

Last updated: 2026-03-16 by Claude (with Vadim)

Previous branch context: `23-221-test-fixture-kotest-structure-and-test-configuration` delivered test infrastructure (merged as #89). Version bumped to 1.2.0.

---

## Where We Are

**Branch:** `24-222-test-backing-service-in-memory-sqlite-for-test-isolation`
**Issue:** #24 -- Test backing service: in-memory SQLite for test isolation
**Parent:** #22 -- 0.5.0 Engineering Foundation
**Milestone:** Phase 1: Walking Skeleton (v2.0.0)
**Version:** 1.2.0
**Build:** `./gradlew test` passes (49 tests, 43 real + 6 disabled placeholders)

---

## Design Ceremony: Eventstorming the BackingServicePort ACL

We are eventstorming before implementation. Dialectic -- Vadim drives, Claude learns.

### Current State: Step 4 (Aggregation) -- COMPLETE. Step 5 (Testing) -- NEXT.

Step 1 COMPLETE. Step 2 COMPLETE. Step 3 COMPLETE. Step 4 COMPLETE. Step 5 NEXT.

### Step 4 Completed Aggregations

1. **Memory-Data Adapter** (inner adapter, the cook):
   - 5 policies in Job Description box: AlwaysAccept, AlwaysTranslateOut, AlwaysTranslateIn, AlwaysTranslateErr, AlwaysConfirm
   - 2 local commands in Local Commands box: TranslateToDataCmd, TranslateToMemoryCmd
   - 2 integration commands (loose): PersistDataCmd (outbound through port), ConfirmResultCmd (outbound to domain)
   - Actor: MemoryDataAdapter (absorbed MemoryDataAdapterIn and MemoryDataAdapterErr)

2. **Data-SQL Adapter** (outer adapter, the waitress):
   - 3 policies in Job Description box: AlwaysPersist, AlwaysReceiveResult, AlwaysReceiveError
   - 2 local commands in Local Commands box: TranslateDtoToSqlCmd, TranslateSqlToDtoCmd (GAP FILLED -- discovered through aggregation)
   - 2 integration commands (loose): ExecuteSqlCmd (outbound to infrastructure), ReturnResultCmd (inbound through port, consolidated from ReturnResultCmd + ReturnErrorCmd)
   - Actor: DataSqlAdapter (absorbed DataSqlAdapterResult and DataSqlAdapterErr)

3. **SQLite** (infrastructure, the filing clerk):
   - 1 policy, 1 command, 2 outcome events
   - Merged SQLiteErr into SQLite

4. **Hippocampus** (domain, the customer):
   - 1 external system (merged HippocampusIn and HippocampusErr into Hippocampus)
   - Confirmation commands and policies moved to inner adapter (she issues them, not domain)
   - StoreMemoryCmd stays in domain (issued by Hippocampus)

### Step 4 Discoveries

- **Three command types:** Integration (crosses boundaries, loose), Local (self-commands, boxed), Gaps (missing commands found through aggregation)
- **Transaction ID is a DOMAIN construct, not ACL.** Hippocampus generates it. ACL carries it. Infrastructure ignores it.
- **Transaction ID removed from board** -- will be added back with Vadim's architectural trick
- **Gap discovery:** Data-SQL Adapter had no local commands for her DTO↔SQL translation work. Found by asking "what does this clerk do all day?"
- **Confirmation commands belong to inner adapter** -- she issues them, not domain. The mirror of StoreMemoryCmd.
- **Duplicate commands consolidate:** Same words, different payload = one command. Applied to: TranslateToMemoryCmd (success+error), ReturnResultCmd (success+error), ConfirmResultCmd (success+error).
- **Mermaid layout issue:** Moving ExecuteSqlCmd to outer adapter broke vertical positioning. Kept in Infrastructure for layout. Layout is Mermaid's problem, not ours.

### Step 5 Preview (Testing)

Vadim revealed what comes after aggregation: the testing step rearranges aggregate artifacts into:

**Policy -> Command -> (Model) -> Event -> (Model)**

This IS Given-When-Then. Each chain is a behavioral test specification. The ceremony produces its own tests. Business reads it in their language. Engineers test it as specifications. Same artifact, two audiences.

### WHAT TO DO NEXT

1. Include Vadim's image showing command thinking process (filename TBD -- ask Vadim)
2. Create Step 4 snapshot diagram (es-0001-step4-aggregation.mmd) -- frozen copy of working board
3. Execute Step 5: Testing -- rearrange aggregate artifacts into GWT chains
4. Transaction ID architectural trick -- Vadim will walk through

### Boards

- `es-0001-memory-stored.mmd` -- main working board (Step 4 complete)
- `es-0001-step1-events.mmd` -- Step 1 snapshot
- `es-0001-step2-svo.mmd` -- Step 2 snapshot
- `es-0001-step3-commands.mmd` -- Step 3 snapshot

### Blog Post

- `site/_posts/2026-03-14-event-storming-tutorial.adoc` -- Living tutorial. Added: value proposition (GWT from ceremony), three command types, gap discovery, ReturnResultCmd consolidation, domain boundary aggregation, complete Step 4 board, Step 5 placeholder.

### Images

- `eventstorming-step-4-consolidating-policies-into-job-description.png` -- policy consolidation
- (TBD) -- Vadim's image showing command thinking process

---

## Lessons This Session

- ACL IS what we're building, not irrelevant to #24
- Design before code. Eventstorm before design. Think before eventstorm.
- 1922 Rule: the business is timeless.
- Don't batch. One at a time. The urge is human AND LLM.
- Transaction ID: domain construct, not ACL.
- Three command types: integration, local, gaps. Classification fell out of aggregation.
- Knowledge vs understanding: Claude had all of Evans, gave confident wrong answers.
- Duplicate commands consolidate: same words, different payload = one command.
- Gap discovery: aggregation reveals missing commands by asking "what does this clerk do all day?"
- Confirmation commands belong to whoever issues them, not where they land.
- The ceremony produces its own tests: Policy -> Command -> (Model) -> Event -> (Model) = GWT.
- **Open design question:** How do we teach Claude (and other digital minds) real skills -- not prompt templates, but craft? The Skills framework is flat. Understanding requires experience, correction, dialectic. Stanzas encode identity positions. Skills need a different mechanism for craft positions. Carry this into the next session.
