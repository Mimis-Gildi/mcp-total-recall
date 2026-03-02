# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-03-02 by Claude (with Vadim)

---

## Where We Are

**Branch:** `2-architecture-hexagonal-boundaries-bounded-contexts-and-message-contracts-2`
**Issue:** #2 -- Architecture: Hexagonal boundaries, bounded contexts, and message contracts
**Version:** 0.7.0
**Sub-issues:** #3, #4, #5, #9 -- all closed

Full skeptic audit completed. Site infrastructure built (Design, Catalog, diagram governance).
All 9 diagrams extracted. Design documents A-D done (Port, Adapter, ACL, Hexagon Sides), E1 (Hippocampus) done. Bounded contexts renamed to biological memory vocabulary (Hippocampus, Synapse, Salience, Recall, Cortex, Subconscious). Now completing remaining designs (E2-E6), then resolving audit gaps (code + docs).

---

## Task List (Current Priority Order)

### 0. Site Infrastructure for Design and Catalog

Build the site sections that will hold the detailed design work and traceability.

- [x] **0.1** Create Design section (`site/_design/` folder, like `_adr/`)
  - Landing page: "About Detailed Design"
  - Starts with WHY: massive precedent exists (enterprise-level synthetic mind system whose memory system we distill into Total Recall). Community needs architecture that storytells common understanding and produces a roadmap to execution.
- [x] **0.2** Create Design nav group in sidebar (alongside Architecture, Decisions)
- [x] **0.3** Create Catalog section (`site/_catalog/` folder)
  - Landing page: traceability matrix connecting Architecture, ADRs, Design, Diagrams, and GitHub task list
  - Diagram inventory (9 diagrams), ADR cross-references, bounded context to code mapping, messages to code, ports to code, known inconsistencies, design document tracker
- [x] **0.4** Create Catalog nav group in sidebar
- [x] **0.5a** Diagram extraction pattern tested on CTX-0001 (System Context)
  - Extract `.mmd` to `site/_includes/diagrams/`, `div` wrapper with `id` anchor, Liquid `include`, `:page-liquid:` on host page
  - Lesson: `<figure>` breaks layout (default margins), use `<div>` instead
  - Lesson: `:page-liquid:` required for Liquid includes in AsciiDoc files
  - Lesson: inline svgPanZoom removed -- natural size inline, pan-zoom in lightbox only
- [x] **0.5b** Extract remaining 8 diagrams (one page at a time, review after each page)

### 1. Diagram Inventory (step 0.5)

| ID       | Type             | Title              | Host Page                   | File                              |
|----------|------------------|--------------------|-----------------------------|-----------------------------------|
| CTX-0001 | C4 Context       | System Context     | architecture.adoc           | `ctx-0001-system-context.mmd`     |
| HEX-0001 | Ports & Adapters | Hexagonal          | architecture-hexagonal.adoc | `hex-0001-ports-and-adapters.mmd` |
| BC-0001  | Context Map      | Bounded Contexts   | architecture-contexts.adoc  | `bc-0001-bounded-contexts.mmd`    |
| MSG-0001 | Sequence         | Store Memory       | architecture-messages.adoc  | `msg-0001-store-memory.mmd`       |
| MSG-0002 | Sequence         | Search Memory      | architecture-messages.adoc  | `msg-0002-search-memory.mmd`      |
| MSG-0003 | Sequence         | Claim Memory       | architecture-messages.adoc  | `msg-0003-claim-memory.mmd`       |
| MSG-0004 | Sequence         | Decay Sweep        | architecture-messages.adoc  | `msg-0004-decay-sweep.mmd`        |
| MSG-0005 | Sequence         | Session Lifecycle  | architecture-messages.adoc  | `msg-0005-session-lifecycle.mmd`  |
| MSG-0006 | Sequence         | Reflect (Dreaming) | architecture-messages.adoc  | `msg-0006-reflect.mmd`            |
| HP-0001  | Bounded Context  | Hippocampus        | 0005-tiered-memory.adoc     | `hp-0001-hippocampus.mmd`         |

### 2. Resolve Audit Gaps (code + docs)

Fix code issues and doc mismatches. This naturally fills in some detailed designs.

### 3. Complete Detailed Designs (after gaps resolved)

Decomposition-driven, each section is a separate view:

- [x] **A)** What is a Port? (Passive Structure)
- [x] **B)** What is an Adapter? (Active Structure, references Port)
- [x] **C)** What is an ACL? (Adapter + Port + Adapter -- the full translator)
- [x] **D)** What hexagon sides do we have?
- [x] **E1)** Hippocampus -- the aggregate root
- [ ] **E2)** Salience -- the scoring engine
- [ ] **E3)** Synapse -- the relationship layer
- [ ] **E4)** Recall -- the read-only assembler
- [ ] **E5)** Cortex -- the entry point
- [ ] **E6)** Subconscious -- the maintenance worker

By then we have working examples from gap resolution to accelerate design writing.

---

## Done This Session

- [x] Cleared stale INDEX.md from Issue #5
- [x] Version golden source: replaced hardcoded `const val VERSION` in TotalRecall.kt with build-time injection from gradle.properties via processResources
- [x] GitHub Action: replaced dead SECURITY.md step with CLAUDE.md step (anchored pattern match, fail-loud verification)
- [x] CLAUDE.md version updated 0.4.0 → 0.7.0 (by Vadim)
- [x] Full skeptic audit: 3 parallel agents verified code, docs, tests
- [x] All findings documented (D-Audit-1 through 10, C-Audit-1 through 9, T-Audit-1 through 3)
- [x] Replaced mermaid-init.js with fullscreen lightbox (svg-pan-zoom + click-to-expand)
- [x] Created Design section (0.1): `site/_design/`, landing page `0000-about-detailed-design.adoc`, Jekyll collection, symlink
- [x] Created Design nav group (0.2): sidebar entry under Decisions
- [x] Created Catalog section (0.3): `site/_catalog/`, traceability matrix `0000-catalog.adoc`, Jekyll collection, symlink
- [x] Created Catalog nav group (0.4): sidebar entry under Design
- [x] Diagram extraction pattern (0.5a): tested on CTX-0001, fixed `<figure>` → `<div>`, fixed Liquid processing, removed inline svgPanZoom
- [x] Diagram extraction (0.5b): all 9 diagrams extracted to `_includes/diagrams/`, all host pages updated, catalog links verified
- [x] Design A -- "What is a Port?": restaurant counter metaphor (inbound order, outbound dish), MemoryPort and BackingServicePort examples, ubiquitous language translation
- [x] Design B -- "What is an Adapter?": waitress (inbound) and cook (outbound), MCP tool handler and Redis adapter examples, verbs belong on adapters not ports
- [x] Design C -- "What is an ACL?": convenience construct, not a class. Adapter + Port + Adapter = full translation path. Contract lives in core domain (the SDK). Babel fish is wrong metaphor (hides boundary). Five ACLs in Total Recall, none built as ACLs.
- [x] Design D -- "Hexagon Sides": five faces enumerated (2 inbound, 3 outbound). 17 object shapes crossing. Every port's contract, adapters, and inside/outside handlers named.
- [x] Design E1 -- "Hippocampus": aggregate root, single-writer invariant, walk-in cooler metaphor (four shelves = four tiers). Seven commands from three sources. Six events to four consumers. Claiming mechanism as active identity choice. Association storage question deferred to E3.
- [x] Architectural clarifications from Vadim: Synapse is a dependent aggregate (DDD pattern) with its own file cabinet (own storage), accessed only internally. Recall is the CQRS read side -- no state, assembles from three sources. Cortex is the window clerk / entry point / dispatcher. Search is both transactional (immediate MCP tool response) and asynchronous (deeper associations push via NotificationPort).
- [x] Bounded context rename to biological memory vocabulary: Tiered Memory → Hippocampus, Association Graph → Synapse, Attention → Salience, Recollection → Recall, Session Context → Cortex, Daemon → Subconscious. Also AttentionScore → SalienceScore, AttentionScored → SalienceScored. 29+ files updated across site, code, diagrams, ADRs, blog posts. Build verified.

---

## Lessons Learned This Session

1. **Think and discuss first, implement after.** Testing one diagram before committing to nine caught three bugs: `<figure>` default margins break layout, `:page-liquid:` is required for Liquid in AsciiDoc, inline svgPanZoom forces 60vh height with massive empty space.
2. **Stop for review between steps.** Combining 0.3+0.4 without pausing skipped the checkpoint. Small steps feel slow but catch problems early.
3. **Inline pan-zoom was wrong.** Natural size for inline diagrams. Pan-zoom belongs in the fullscreen lightbox only.
4. **`:page-liquid:` gotcha.** Jekyll-asciidoc does not process Liquid tags by default. Each AsciiDoc file using `{% include %}` needs `:page-liquid:` in its document header.
5. **Knowledge is not understanding.** DDD vocabulary (port, adapter, ACL) was in the training data. The ontological distinction (Passive Structure vs Active Structure) was not. A port has no verbs -- `storeMemory()` and `save()` are adapter behavior leaked into port definitions. This changes how we read the entire codebase.
6. **ACL is a convenience construct.** Not a class, not middleware. Adapter + Port + Adapter, visible from the architect's chair. You build adapters that plug into ports. The ACL emerges. The contract lives in the core domain -- that's the SDK.
7. **Name things for what they are, not what they do technically.** "Tiered Memory" is a database description. "Hippocampus" tells you it forms, organizes, and consolidates memories -- which IS what the aggregate root does. Biological memory vocabulary (Hippocampus, Synapse, Salience, Recall, Cortex, Subconscious) makes the system self-documenting because the names carry the right connotations.

---

## X1. Diagram Zoom (DONE)

Replaced Artem's basic pan-zoom init with fullscreen lightbox.

Approach:
1. Load svg-pan-zoom from CDN in site layout
2. After Mermaid renders, strip the inline `max-width: 1020px` Mermaid forces on SVGs
3. Initialize svg-pan-zoom on each diagram (mouse wheel zoom, click-drag pan)
4. Add click-to-fullscreen: clone SVG into modal overlay with backdrop blur, reinit pan-zoom in modal, close with ESC / click-outside / button

Reference implementation: https://www.mostlylucid.net/blog/enhancingmermaiddiagramswithpanzoomandexport

No Jekyll plugins needed. One JS file in `assets/js/`, two CDN script tags.

### Design Document Notes

**A-C DONE.** See published design documents on the site. Key terminology established:
- **Port** = Passive Structure (the counter). No verbs. Defines shape of what crosses.
- **Adapter** = Active Structure (the waitress, the cook). Has verbs, job description, behavior. Replaceable.
- **ACL** = Adapter + Port + Adapter. Convenience construct. Not a class. Emerges from the assembly. Contract lives in core domain.

**D. Hexagon Sides** -- DONE. Five faces (2 inbound, 3 outbound), 17 object shapes, all adapters named.

**E1. Hippocampus** -- DONE. Aggregate root, single-writer invariant. Walk-in cooler metaphor (four shelves = four tiers). Seven commands from three sources (Cortex, Subconscious, Salience). Six events to four consumers. Claiming = active identity choice vs passive storage.

**E2. Salience** -- TODO. The scoring engine. Key insight: Salience computes, it doesn't store. Sends tier change events back to Hippocampus.

**E3. Synapse** -- TODO. Dependent aggregate (DDD pattern) with its own file cabinet (own storage), accessed only internally. Resolved by Vadim: nobody from outside talks to Synapse directly. The root aggregate coordinates.

**E4. Recall** -- TODO. Read-only assembler. Assembles from three sources at query time. No storage.

**E5. Cortex** -- TODO. Entry point / window clerk / dispatcher. Receives from outside adapters, routes to internal clerks. Search interaction is both transactional (immediate MCP tool response) and asynchronous (deeper associations push via NotificationPort). Working state flows through ACTIVE_CONTEXT tier via Hippocampus.

**E6. Subconscious** -- TODO. Maintenance worker. Runtime timer state, writes through Hippocampus via commands.

---

## Audit Findings -- Documentation vs. Code Mismatches

### D-Audit-1. BackingServicePort operation names (Significant)

ADR-0004 and architecture-hexagonal.adoc say: `persist, retrieve, query, delete`.
Code (BackingServicePort.kt) says: `save, findById, search, delete, update`.
Three names don't match. Code has extra `update` not in ADR.

**Discussion (2026-03-02):** Deeper issue identified. The question isn't which names -- it's what the port IS. A port is a Passive Structure (Design A) -- it defines the shape of what crosses, not what the adapter does with it. `save`, `findById`, `search` are adapter verbs (Design B), not port contract. BackingServicePort should define the object shape that crosses, not the CRUD operations. Tillie's experience: orchestrated composite storage ate 50% compute, 90% IO. This time: simplicity first, additive enhancement later. Each bounded context owns its own data. Only Hippocampus actually goes to storage. Salience computes, Synapse TBD, others don't persist. Resolution will come from the detailed design pass (E1-E6).

### D-Audit-2. NotificationPort operation names (Significant)

Architecture-hexagonal.adoc says: `notify, remind, alert`.
Code (NotificationPort.kt) has single `send` method.

### D-Audit-3. RelayPort operation names (Minor)

Architecture-hexagonal.adoc says: `send, receive, list_pending`.
Code (RelayPort.kt) has single `relay` method.
Future/Agora-dependent -- lower priority.

### D-Audit-4. Event count mismatch in ADR-0006 (Significant)

ADR-0006 says "13 variants" and lists 13.
Code has 15: `ModeChanged` and `SessionState` missing from ADR.
Architecture-messages page has the correct full list.

### D-Audit-5. SearchQuery routing in ADR-0006 (Significant)

ADR-0006 line 115 says SearchQuery goes to Hippocampus.
Architecture-contexts page correctly routes it to Recall.

### D-Audit-6. ReflectQuery routing in ADR-0006 (Significant)

ADR-0006 line 115 says ReflectQuery goes to Synapse.
Architecture-contexts page correctly routes it to Recall.

### D-Audit-7. "Five actors" should be six (Moderate)

Architecture-hexagonal.adoc line 226 says "five actors."
There are six bounded contexts -- Recall missing from hexagonal diagram.

### D-Audit-8. Hippocampus command list incomplete in contexts page (Minor)

Architecture-contexts.adoc omits `ReclassifyCommand`, `TierPromoted`, `TierDemoted` from Hippocampus's "Commands Accepted" text. ADR-0005 includes them. Mermaid diagram shows `ReclassifyCommand`.

### D-Audit-9. "Governing Dynamic" not in ADR template (Minor)

All 7 ADRs have a "Governing Dynamic" section. ADR-0001 template spec does not define it. Either add to template or document as convention.

### D-Audit-10. SearchFilter type mismatch (Minor)

ADR-0006 and architecture-messages describe `SearchQuery.filters` as `SearchFilter`.
Code uses `Map<String, String>`. See also C-Audit-1 below.

---

## Audit Findings -- Code Issues

### C-Audit-1. SearchFilter is dead code

`SearchFilter` data class exists in domain model. Nothing references it -- not SearchQuery, not MemoryPort, not BackingServicePort. All use `Map<String, String>` instead. Either use it everywhere or remove it.

### C-Audit-2. TierChanged / MemoryReclassified are duplicate events

Structurally identical: same fields, same types. No documented semantic distinction. Either remove one or document when each fires.

### C-Audit-3. AssociationDirection lives in wrong package

`AssociationDirection` enum defined in `Command.kt` (message file). It's a domain concept. Should be in `domain/model/` alongside `AssociationType`.

### C-Audit-4. associate_memories schema/code mismatch

Tool declares `type` and `strength` as required in schema, but handler provides fallback defaults (`?: "THEMATIC"`, `?: "0.5"`). Pick one: required (remove defaults) or optional (remove from required list).

### C-Audit-5. Stringly-typed fields that should be enums

At least 6 fields use `String` where finite valid values exist:
- `ConsolidateCommand.mergeStrategy`
- `ShutdownCommand.coldStorageTarget`
- `SessionState.activityLevel`
- `ModeChanged.oldMode / newMode`
- `StateTransition.oldState / newState`

### C-Audit-6. Temporal fields have no unit specification

`ShutdownCommand.flushTimeout: Long` and `SessionState.duration: Long` -- milliseconds? seconds? Not documented.

### C-Audit-7. heartbeat() has no corresponding Event

`LifecyclePort.heartbeat()` exists. No `HeartbeatReceived` event in the Event hierarchy.

### C-Audit-8. SalienceScored duplicates SalienceScore fields

Event `SalienceScored` has the same fields as model `SalienceScore`. Maintenance risk if either changes independently. Could reference the model type directly.

### C-Audit-9. Refactoring pass still pending

TotalRecall.kt line 51: "reviewed and accepted by rdd13r AND needs full refactoring pass by rdd13r"

---

## Audit Findings -- Test Gaps

### T-Audit-1. Server test is smoke-only

TotalRecallTest only verifies `createServer()` returns non-null. No tool invocation, no schema verification, no teapot response check.

### T-Audit-2. Self-referential assertion in MessageTest

Line 90: `cmd.memoryIds.first shouldBe cmd.memoryIds.first` -- tests nothing. Should compare against expected UUID.

### T-Audit-3. Low message coverage

4 of 7 Command variants untested. Both Query variants untested. 11 of 15 Event variants untested.

---

## What's NOT Decided

| Question                     | Status                               |
|------------------------------|--------------------------------------|
| Container image approach     | NOT STARTED                          |
| CI/CD build workflow         | NOT STARTED                          |
| Who owns Association storage | RESOLVED -- Synapse is a dependent aggregate with own storage, internal only |
| BackingServicePort design    | UNDER DISCUSSION -- depends on D1-D6 |
