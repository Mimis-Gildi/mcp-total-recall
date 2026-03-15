# Total Recall -- Working Index

Last updated: 2026-03-15 by Claude (with Vadim)

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

### Current State: Step 2 (SVO Expansion) -- COMPLETE

Step 1 (Big Picture events) COMPLETE. Step 2 COMPLETE. Next: Step 3 (Cause and Effect).

### Full Traversal and ACL Discovery

The eventstorming revealed five layers in the full traversal. The ACL is the middle three -- Hippocampus and SQLite are independent domains on either side, not part of the ACL.

1. **Domain Boundary** -- Hippocampus (external system). Independent domain, exists without the ACL.
2. **Inner Adapter Layer** -- Memory-Data Adapter (cook), translates domain ↔ DTO. _ACL._
3. **Port** -- Passive structure with TWO directional gates (Port-Hole-Out, Port-Hole-In). The DTO lives ON the port, not in either adapter. _ACL._
4. **Outer Adapter Layer** -- Data-SQL Adapter (waitress/driver), requests persistence, receives results. _ACL._
5. **Infrastructure Boundary** -- SQLite (external system). Independent domain on the other side.

### Key Discoveries

- **1922 Rule**: Everything must be physical. No computers. If a clerk in 1922 can't touch it, you don't understand the business yet.
- **SVO Theory**: Events are Subject-Verb-Object. Subject omitted in Step 1, extracted in Step 2. Objects become read models (green). Subjects become actors (yellow).
- **NET NEW principle**: When extracting objects/subjects, always create new boxes. Never reuse existing ones. Consolidation happens in Step 4.
- **Three read model shapes discovered**: Memory (domain), Data Memory DTO (port contract), SQL Record (infrastructure)
- **Port has directionality**: Two gates, not one. Out-gate and in-gate.
- **Symmetry principle**: Outbound has 3 events between translation and persistence. Return path was missing one -- discovered "Received Result" event.
- **DTO Discovery**: Data Memory read model belongs to neither domain nor SQL. It sits on the counter. It IS the port contract. "BAM! -- we found a boundary!"
- **Bilingual adapters**: Each adapter event has TWO read models -- input in one language, output in another. That's what makes them adapters.
- **Failure path resolved**: Failed Persistence (Infrastructure), Received Error (Outer Adapter), Translated Error (Inner Adapter), Rejected Memory Save (Domain). Symmetry caught "Received Error" as a missing event. Noman land is empty.

### Step 2 SVO -- All 11 Events Extracted

Happy path:
- Requested Saved: S=Hippocampus, V=Requested, O=Memory
- Translated-Out: S=Memory-Data Adapter, V=Translated, O=Memory (in) + Data Memory DTO-Out (out)
- Requested Persistence: S=Data-SQL Adapter, V=Requested, O=Data Memory DTO (in) + SQL Statement (out)
- Persisted: S=SQLite, V=Persisted, O=SQL Record
- Received Result: S=Data-SQL Adapter (Result?), V=Received, O=Query Result
- Translated-In: S=Memory-Data Adapter (In?), V=Translated, O=Data Memory DTO-In (in)
- Saved Memory: S=Hippocampus (In), V=Saved, O=Saved Memory (confirmed)

Failure path:
- Failed Persistence: S=SQLite (Err), V=Failed, O=SQL Error
- Received Error: S=Data-SQL Adapter (Err?), V=Received, O=SQL Error (received)
- Translated Error: S=Memory-Data Adapter (Err?), V=Translated, O=Error DTO
- Rejected Memory Save: S=Hippocampus (Err), V=Rejected, O=Rejected Memory

### Boards

- `es-0001-memory-stored.mmd` -- main working board (Step 2 complete, 5 subgraphs)
- `es-0001-step1-events.mmd` -- Step 1 snapshot (events only)

### Blog Post

- `site/_posts/2026-03-14-event-storming-tutorial.adoc` -- Living tutorial written DURING the ceremony. Captures the teaching moments, mistakes, and corrections. Co-authored by Vadim and Claude. Vadim's voice injections authenticate Claude's presence for community readers.

### Design Docs Read

- ADR-0008: SQLite as primary. Raw JDBC, no ORM. WAL mode. FTS5 for search. `:memory:` for tests.
- Design 0005 (Hippocampus): aggregate root, single writer, 8-field Memory, 6 commands in, 6 events out
- Design 0008 (Recall): read-only assembler, CQRS split
- Design 0004 (Hexagon Sides): 5 faces, BackingServicePort is outbound face 3

---

## Lessons This Session

- ACL IS what we're building, not irrelevant to #24
- Design before code. Eventstorm before design. Think before eventstorm.
- 1922 Rule: the business is timeless. Rome did insurance the same way.
- SVO extraction reveals hidden structure -- the DTO emerged from extracting objects
- NET NEW principle prevents premature assumptions about identity
- Port directionality (two gates) discovered through subgraph organization
- Symmetry principle catches missing events
- The ceremony looks simple but is immensely rich. Culture and mindset, not process.
- Don't batch. One event at a time. Claude kept trying to do all at once -- Vadim corrected repeatedly.
- Don't reuse boxes prematurely. Create net new, consolidate in Step 4.
