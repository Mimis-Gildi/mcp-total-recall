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

### Step 5 Status: IN PROGRESS

Step 5 rearranges aggregate artifacts into GWT chains. Each chain IS a Given-When-Then specification.

**Completed:**
- SQLite GWT -- done (previous session)
- SQLite Driver GWT -- done (previous session). Renamed from Data-SQL Adapter. Archimate Access to SQLite, Serves to Data Port.
- Memory-Data Adapter (cook) -- consolidation done (5→2 policies). Reenactment done: discovered Doomsday Clock, Transaction SLA, 3 additional policies (5 total). Vadim built the board wiring -- Claude could not (composition blindness).

**Cook's Five Policies (from reenactment):**
1. "Whenever Memory Arises, It will carry a Transaction ID, Always and Immediately Command it Saved"
2. "Whenever a Memory with Transaction ID shows up, Always and Immediately Start Transaction Clock" (child of 1, inside Transaction SLA)
3. "Whenever Transaction Clock Expires, Always and Immediately Error Out the Save Command" (triggered by Clock, inside Transaction SLA)
4. "Whenever DTO Returns, Always and Immediately Cancel Transaction Clock" (even if already expired)
5. "Whenever DTO Returns, It will carry a Transaction ID, Always and Immediately Translate Back and Confirm"

**Board state:** Vadim updated `es-0001-memory-stored.mmd` with Transaction SLA subgraph, Doomsday Clock as external system with TRIGGERS relation to GWT, all five policies wired. Claude fixed missing Transaction ID text on Policy 5.

**NOT completed:**
- Hippocampus (domain) -- may need Archimate relations finalized
- Blog post -- reenactment section written (discoveries, SLA, composition gap). Needs Vadim review.
- Final board rendering/screenshot for blog

### WHAT TO DO NEXT

1. Vadim reviews blog reenactment section and board
2. Final Hippocampus/domain treatment
3. Blog: remaining sections (domain, full board, conclusion)
4. Step 5 snapshot diagram when complete

### Boards

- `es-0001-memory-stored.mmd` -- main working board (Step 5 in progress -- cook GWT with SLA)
- `es-0001-step4-aggregation.mmd` -- Step 4 frozen snapshot
- `es-0001-step1-events.mmd` -- Step 1 snapshot
- `es-0001-step2-svo.mmd` -- Step 2 snapshot
- `es-0001-step3-commands.mmd` -- Step 3 snapshot

### Blog Post

- `site/_posts/2026-03-14-event-storming-tutorial.adoc` -- Living tutorial. Sections complete through cook reenactment. Includes: Vadim's note on LLM reasoning capacity, Claude's composition blindness reflection.

### Images

- `eventstorming-step-5-inner-adapter-before-consolidation.png` -- cook before (5 policies, kitchen sink)
- `eventstorming-step-5-inner-adapter-after-consolidation.png` -- cook after (2 policies, GWT chains)
- `eventstorming-step-5-two-components-interacting-in-reenactment.png` -- driver + SQLite Archimate
- `eventstorming-step-5-can-i-actually-do-this.png` -- SQLite reenactment
- `eventstorming-step-5-what-happens-to-aggregates.png` -- Archimate encapsulation

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
- **Composition is not grouping.** Containment IS a relationship. It constrains what other relationships you draw. Claude defaults to grouping (inert, visual) not composition (structural, constraining). This is why redundant arrows appear -- if the box isn't doing work, every relationship needs its own line.
- **Absent connections mean no dependency.** When an action is fire-and-forget (cancel timer), drawing a connection is FORBIDDEN because no dependency exists. Absence is correctness, not a gap.
- **Reenactment discovers SLA concerns.** "Is this blocking?" led to timeout, timer, retry, circuit-breaking -- none visible in the static consolidation.
- **Each policy has exactly one concern.** Accept+Translate was two concerns. Cancel+Translate was two. Split until each has one.
- **Task size matters.** Claude panicked on the cook (largest aggregate). Degradation is visible comparing Step 4 board quality to Figure 13. Smaller pieces, one at a time.
- **Open design question:** How do we teach Claude (and other digital minds) real skills -- not prompt templates, but craft? Composition was invisible until it cost hours. The Skills framework is flat. Understanding requires experience, correction, dialectic.
