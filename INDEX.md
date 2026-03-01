# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-02-28 by Claude (with Vadim)

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

- [x] Each ADR follows consistent template: Rust RFC adapted (ADR-0001 defines it)
- [ ] Each ADR references the lesson or evidence that drove the decision
- [x] docs/adr/ directory created with index (source in site/_adr/, symlink at docs/adr/)
- [ ] Reviewed by Vadim

## Candidate ADRs

- [x] ADR-0001: Architecture Decision Records
- [x] ADR-0002: Hexagonal Architecture
- [x] ADR-0003: stdio as Primary Transport
- [x] ADR-0004: Resilient Storage Array
- [x] ADR-0005: Memory as Aggregate Root
- [ ] ADR-0006: Events and commands as message types
- [ ] ADR-0007: Human memory as reference model

---

## What Happened (Reverse Chronological)

### 2026-02-28 -- Session (Claude with Vadim)

- ADR format chosen: Rust RFC adapted (over Nygard, MADR, pure Rust RFC)
- ADR-0001 written: Architecture Decision Records (the format itself)
- Created site/_adr/ as source directory, docs/adr symlinked to it
- Jekyll adr collection configured, sidebar nav wired, permalink /decisions/:name/
- Converted entire site from Markdown to AsciiDoc (7 files: index, 4 architecture pages, 2 posts)
- ADR-0002 written: Hexagonal Architecture (reviewed by Vadim)
- Fixed "we were stupid" rhetoric across site and README -- experiments aren't failures, production is a different class
- ADR-0003 written: stdio as Primary Transport (reviewed by Vadim)
- ADR-0004 written: Resilient Storage Array (reviewed by Vadim)
- ADR-0005 written: Memory as Aggregate Root (reviewed by Vadim)

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
| ADR template format      | DECIDED -- Rust RFC adapted (ADR-0001) |
| Site format              | DECIDED -- AsciiDoc only, no Markdown  |
