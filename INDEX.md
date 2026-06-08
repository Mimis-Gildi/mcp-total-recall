# Total Recall -- Working Index

This is the project ledger. State, not planning.

Last updated: 2026-06-07 by Claude (with Vadim)

Previous: `100-bug-fit-ops-to-mimis-gildi-4` delivered workflow migration to Mimis-Gildi/fluffle templated callers (slices through PR #116). Version walked 1.1.0 → 4.0.0.

---

## Where We Are

**Branch:** `92-transactioncontext-doc-vs-code-drift`
**Issue:** #92 -- TransactionContext: doc-vs-code drift
**Milestone:** Phase 1: Walking Skeleton (v2.0.0)
**Version:** 4.0.0 (on-branch major bump from 3.0.1)
**PR:** not yet created

## Task

Reconcile docs vs code on TransactionContext / Message split. Vadim's prior read (issue #92): code's 4+3 split is correct; docs need to follow. AC requires that on-the-record confirmation before docs change.

Primary artifacts:
- `site/_design/0011-transaction-context.adoc`
- `site/_pages/architecture-messages.adoc`
- `src/main/kotlin/mimis/gildi/memory/domain/message/TransactionContext.kt`
- `src/main/kotlin/mimis/gildi/memory/domain/message/Message.kt`
- All 29 concrete Command/Query/Event/Notification variants (boilerplate flagged)

## In Flight

- Team raid: exhaustive analysis across docs, design, and code (maestro / tinker / snoop).
- Pending: Vadim's on-the-record confirmation of the field-ownership split.

## Working tree (not part of #92)

- `src/test/.../TotalRecallTest.kt`: version assertion loosened from literal `"1.1.0"` to `shouldBeGreaterThan "3.0.0"` (String comparison; fragile shape -- revisit before committing).
- `.run/TotalRecallTest.run.xml`: IDE run config tweak.
