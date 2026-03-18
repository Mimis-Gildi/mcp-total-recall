# Total Recall -- Working Index

Last updated: 2026-03-16 by Claude (with Vadim)

---

## Where We Are

**Branch:** `24-222-test-backing-service-in-memory-sqlite-for-test-isolation`
**Issue:** #24 -- Test backing service: in-memory SQLite for test isolation
**Design completed** -- we have the design of the SQLite port - we can make the test now.

---

## What we have left do:

. Correct architecture documentation -- Vadim drives. Currently, two concerns
- Wording inside docs that contributors complain about - Vadim find and we remove.
- Admonitions don't work with Jekyll; i.e., 'NOET:' - it should be removed or converted to 'Note::' which will render acceptably.


### Files left to check

~~0011-transaction-context.adoc~~ -- DONE. Reviewed and pushed.

Architecture pages:
- ~~architecture-messages.adoc~~ -- DONE. Reviewed and pushed.
- architecture-contexts.adoc -- we added the table and fixed links but never checked the full body text

Blog posts:
- 2026-02-25-architecture-modeling-complete.adoc
- 2026-02-26-contract-definitions-in-kotlin.adoc
- 2026-03-01-architecture-decision-records.adoc
- 2026-03-04-architecture-completion.adoc
- 2026-03-06-architecture-socialization.adoc
- 2026-03-14-event-storming-tutorial.adoc
- 2026-03-16-backing-service-implementation.adoc -- the one we started tonight

Other:
- ~~_catalog/0000-catalog.adoc~~ -- DONE. Reviewed and pushed.
- _pages/roadmap.adoc -- only fixed the "who" line
- Mermaid diagrams (hp-0001-hippocampus.mmd has BackingServicePort reference)
