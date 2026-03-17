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

## Design Ceremony: COMPLETE

Eventstorming the BackingServicePort ACL -- all 5 steps done.

Steps 1-4 produced the aggregates. Step 5 rearranged them into GWT chains. The ceremony is the spec.

### The Board

`es-0001-memory-stored.mmd` -- completed Step 5 board. Four GWT chains:

1. **Hippocampus** (domain, the customer) -- 1 policy, 1 command, 2 outcome events
2. **Memory-Data Adapter** (inner adapter, the cook) -- 5 policies including Transaction SLA with Doomsday Clock
3. **SQLite Driver** (outer adapter, the waitress) -- 2 policies: persist outbound, receive and translate inbound
4. **SQLite** (infrastructure, the filing clerk) -- 1 policy, 2 outcome events

### #24 Scope

#24 picks up two of the four GWT chains -- port outward:
- SQLite Driver GWT (2 policies)
- SQLite GWT (1 policy)

The cook's 5 policies and Hippocampus's policy are separate implementation work.

### Blog Post

`site/_posts/2026-03-14-event-storming-tutorial.adoc` -- Complete living tutorial. Steps 1-5, consolidation, reenactment, composition blindness, summary. Ready for final Vadim review.

### Snapshot Boards

- `es-0001-memory-stored.mmd` -- completed Step 5 board
- `es-0001-step4-aggregation.mmd` -- Step 4 frozen snapshot
- `es-0001-step3-commands.mmd` -- Step 3 snapshot
- `es-0001-step2-svo.mmd` -- Step 2 snapshot
- `es-0001-step1-events.mmd` -- Step 1 snapshot

---

## WHAT TO DO NEXT

Implement #24: BackingServicePort with in-memory SQLite for test isolation. The two GWT chains are the acceptance tests.
