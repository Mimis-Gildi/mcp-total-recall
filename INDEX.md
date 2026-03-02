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
Now extracting diagrams (step 0.5), then resolving audit gaps (code + docs), then completing designs.

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
- [ ] **0.5b** Extract remaining 8 diagrams (one page at a time, review after each page)

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

### 2. Resolve Audit Gaps (code + docs)

Fix code issues and doc mismatches. This naturally fills in some detailed designs.

### 3. Complete Detailed Designs (after gaps resolved)

Decomposition-driven, each section is a separate view:

- [x] **A)** What is a Port? (Passive Structure)
- [x] **B)** What is an Adapter? (Active Structure, references Port)
- [ ] **C)** What is an ACL? (Adapter + Port + Adapter -- the full translator)
- [ ] **D)** What hexagon sides do we have?
- [ ] **E1)** Tiered Memory -- the aggregate root
- [ ] **E2)** Attention -- the scoring engine
- [ ] **E3)** Association Graph -- the relationship layer
- [ ] **E4)** Recollection -- the read-only assembler
- [ ] **E5)** Session Context -- the entry point
- [ ] **E6)** Daemon -- the maintenance worker

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

---

## Lessons Learned This Session

1. **Think and discuss first, implement after.** Testing one diagram before committing to nine caught three bugs: `<figure>` default margins break layout, `:page-liquid:` is required for Liquid in AsciiDoc, inline svgPanZoom forces 60vh height with massive empty space.
2. **Stop for review between steps.** Combining 0.3+0.4 without pausing skipped the checkpoint. Small steps feel slow but catch problems early.
3. **Inline pan-zoom was wrong.** Natural size for inline diagrams. Pan-zoom belongs in the fullscreen lightbox only.
4. **`:page-liquid:` gotcha.** Jekyll-asciidoc does not process Liquid tags by default. Each AsciiDoc file using `{% include %}` needs `:page-liquid:` in its document header.

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

### A. What is a Port?

TODO -- define precisely. A port is a face of the hexagon. An interface. The contract boundary between domain and outside world.

### B. What is an ACL on a Port?

TODO -- define precisely. The ACL translates domain language on one face to infrastructure/consumer language on the other. It protects the domain from external concerns. On an outbound port: domain speaks Memory, ACL translates to data operations. On an inbound port: external speaks MCP tool calls, ACL translates to domain commands.

### C. What hexagon sides do we have?

TODO -- enumerate all ports (inbound and outbound) with their responsibilities.

### D1. Tiered Memory -- the aggregate root

TODO -- perspective, core job, what it stores, what events it produces/consumes.
Key open question: does it own Association storage too, or is that separate?

### D2. Attention -- the scoring engine

TODO -- perspective, core job. Key insight from discussion: Attention computes, it doesn't store. It sends tier change events back to Tiered Memory which persists.

### D3. Association Graph -- the relationship layer

TODO -- perspective, core job. Open question: does it own its own storage, or do associations belong to the Memory aggregate?

### D4. Recollection -- the read-only assembler

TODO -- perspective, core job. Assembles from three sources at query time. No storage.

### D5. Session Context -- the entry point

TODO -- perspective, core job. Working state flows through ACTIVE_CONTEXT tier via Tiered Memory.

### D6. Daemon -- the maintenance worker

TODO -- perspective, core job. Runtime timer state, writes through Tiered Memory via commands.

---

## Audit Findings -- Documentation vs. Code Mismatches

### D-Audit-1. BackingServicePort operation names (Significant)

ADR-0004 and architecture-hexagonal.adoc say: `persist, retrieve, query, delete`.
Code (BackingServicePort.kt) says: `save, findById, search, delete, update`.
Three names don't match. Code has extra `update` not in ADR.

**Discussion (2026-03-02):** Deeper issue identified. The question isn't which names -- it's what the port IS. A port is an ACL face of the hexagon. BackingServicePort translates domain (Memory) to data (CRUD). Tillie's experience: orchestrated composite storage ate 50% compute, 90% IO. This time: simplicity first, additive enhancement later. Each bounded context owns its own data. Only Tiered Memory actually goes to storage. Attention computes, Association Graph TBD, others don't persist. Resolution will come from the detailed design pass (D1-D6 above).

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

ADR-0006 line 115 says SearchQuery goes to Tiered Memory.
Architecture-contexts page correctly routes it to Recollection.

### D-Audit-6. ReflectQuery routing in ADR-0006 (Significant)

ADR-0006 line 115 says ReflectQuery goes to Association Graph.
Architecture-contexts page correctly routes it to Recollection.

### D-Audit-7. "Five actors" should be six (Moderate)

Architecture-hexagonal.adoc line 226 says "five actors."
There are six bounded contexts -- Recollection missing from hexagonal diagram.

### D-Audit-8. Tiered Memory command list incomplete in contexts page (Minor)

Architecture-contexts.adoc omits `ReclassifyCommand`, `TierPromoted`, `TierDemoted` from Tiered Memory's "Commands Accepted" text. ADR-0005 includes them. Mermaid diagram shows `ReclassifyCommand`.

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

### C-Audit-8. AttentionScored duplicates AttentionScore fields

Event `AttentionScored` has the same fields as model `AttentionScore`. Maintenance risk if either changes independently. Could reference the model type directly.

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
| Who owns Association storage | UNDER DISCUSSION -- part of D3       |
| BackingServicePort design    | UNDER DISCUSSION -- depends on D1-D6 |
