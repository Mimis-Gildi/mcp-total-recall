# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-02-27 by Claude (with Vadim)

---

## Where We Are

**Branch:** `5-23-architecture-decision-records`
**Issue:** #5 -- (2.3) Architecture Decision Records
**Parent:** #2 -- Architecture: Hexagonal boundaries, bounded contexts, and message contracts
**Previous:** #4 merged (PR #18) -- contract definitions in Kotlin, version 0.4.0

### What Exists

Everything from 0.4.0: domain model, messages, ports, MCP server with 8 teapot stubs, Jekyll site with architecture docs, announcement posts, favicon.

---

## Issue #5 Acceptance Criteria

- [ ] Each ADR follows consistent template: Context, Decision, Consequences, Status
- [ ] Each ADR references the lesson or evidence that drove the decision
- [ ] docs/adr/ directory created with index
- [ ] Reviewed by Vadim

## Candidate ADRs

- [ ] ADR-001: Hexagonal architecture
- [ ] ADR-002: stdio as primary transport
- [ ] ADR-003: Resilient storage array
- [ ] ADR-004: Memory as aggregate root
- [ ] ADR-005: Events and commands as message types
- [ ] ADR-006: Human memory as reference model

---

## What Happened (Reverse Chronological)

### 2026-02-27 -- Session start (Claude with Vadim)

- Cleared stale INDEX.md from Issue #4
- No work started yet on ADRs

---

## What's Decided (Architecture)

Carried from Issues #3 and #4 -- see merged PRs #17, #18 and site for full documentation.

## What's NOT Decided

| Question                 | Status      |
|--------------------------|-------------|
| Container image approach | NOT STARTED |
| CI/CD build workflow     | NOT STARTED |
| ADR template format      | NOT STARTED |
