# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-03-04 by Claude (with Vadim)

---

## Where We Are

**Branch:** `2-architecture-hexagonal-boundaries-bounded-contexts-and-message-contracts-2`
**Issue:** #2 -- Architecture: Hexagonal boundaries, bounded contexts, and message contracts
**Version:** 0.7.0
**Sub-issues:** #3, #4, #5, #9 -- all closed

Full skeptic audit completed. Site infrastructure built (Design, Catalog, diagram governance).
16 diagrams total (10 sequence/architecture + 6 bounded context diagrams). All design documents complete:

- A-D (Port, Adapter, ACL, Hexagon Sides) and E1-E6 (Hippocampus, Salience, Synapse, Recall, Cortex, Subconscious).
- Bounded contexts renamed to biological memory vocabulary with parenthetical subtitles: Hippocampus (Vault), Salience (Focus), Synapse (Matrix), Recall (Stream), Cortex (Inception), Subconscious (Dream).
- Audit gaps remain (code + docs).

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
| MSG-0007 | Sequence         | Total Recall       | architecture-messages.adoc  | `msg-0007-total-recall.mmd`       |
| HP-0001  | Bounded Context  | Hippocampus        | 0005-tiered-memory.adoc     | `hp-0001-hippocampus.mmd`         |
| SL-0001  | Bounded Context  | Salience           | 0006-salience.adoc          | `sl-0001-salience.mmd`            |
| SY-0001  | Bounded Context  | Synapse            | 0007-association-graph.adoc | `sy-0001-synapse.mmd`             |
| RC-0001  | Bounded Context  | Recall             | 0008-recall.adoc            | `rc-0001-recall.mmd`              |
| CX-0001  | Bounded Context  | Cortex             | 0009-cortex.adoc            | `cx-0001-cortex.mmd`              |
| SB-0001  | Bounded Context  | Subconscious       | 0010-subconscious.adoc      | `sb-0001-subconscious.mmd`        |

### 2. Complete Detailed Designs -- DONE

- [x] **A)** What is a Port? (Passive Structure)
- [x] **B)** What is an Adapter? (Active Structure, references Port)
- [x] **C)** What is an ACL? (Adapter + Port + Adapter -- the full translator)
- [x] **D)** What hexagon sides do we have?
- [x] **E1)** Hippocampus (Vault) -- the aggregate root
- [x] **E2)** Salience (Focus) -- the scoring engine
- [x] **E3)** Synapse (Matrix) -- the relationship layer
- [x] **E4)** Recall (Stream) -- the read-only assembler
- [x] **E5)** Cortex (Inception) -- the entry point
- [x] **E6)** Subconscious (Dream) -- the background caretaker

### 3. Transaction Context (FUNDAMENTAL -- blocks steps 4-7)

**Discovery (2026-03-03):** The design documents describe bounded contexts as if they pass notes to each other.
In reality, Commands and Queries are transactional and conversational -- they carry payloads and expect responses within a traceable chain.
Every message in the system must carry a transaction envelope.

**Why this is not optional:** Without a transaction context on every message, you cannot:
- Trace a `search_memory` call through Cortex → Recall → Hippocampus → Salience → Synapse and back
- Correlate a fast-path MCP response with its deep-path NotificationPort results
- Debug anything in production
- Know which DecaySweep belongs to which session
- Connect a BreakNotification back to the session state that triggered it

**What the envelope carries (minimum):**
- `sessionId` -- which session this belongs to (created at session_start, carried everywhere)
- `requestId` -- which MCP tool call initiated this chain
- `stepId` -- which step in the processing chain
- `sequenceId` -- ordering within the flow
- `timestamp` -- when this step occurred
- (potentially: parentId for tree-structured traces, sourceContext for originating bounded context)

**What this resolves:**
- **3.4 ambiguity:** MemoryRetrieved, SalienceScored, AssociationsFound are response payloads in a transaction chain, not fire-and-forget domain events. The transaction context ties request to response.
- **D-Audit-1 (partial):** Port contract shapes must include the envelope. The port defines the shape of what crosses -- and the envelope is part of that shape.
- **C-Audit-6:** Temporal fields get their units from the envelope's timestamp convention.

**Work items:**
- [ ] **3.0** Design the TransactionContext data class and envelope convention. New design document or addition to existing architecture page. Must answer: what fields, what types, which messages carry it (all of them), how does the envelope propagate through bounded context boundaries.
- [ ] **3.1** Update all sealed classes in Command.kt, Query.kt, Event.kt, Notification.kt to carry TransactionContext.
- [ ] **3.2** Update design documents to reference the envelope where message flows are described.

### 4. Fix Sequence Diagrams (data flow alignment)

Designs are the source of truth. Sequence diagrams (MSG-0001 through MSG-0006) were written before the detailed designs and have gaps. Transaction context (step 3) must be designed first -- diagrams need to show the envelope on every arrow.

- [ ] **4.1** MSG-0002 (Search Memory): Add MemoryAccessed event firing on retrieval (feeds Salience decay model). Show two-speed architecture -- fast path returns through Cortex, deep path continues through NotificationPort. Show transaction context propagation.
- [ ] **4.2** MSG-0005 (Session Lifecycle): Show SessionStart broadcasting to all contexts (Subconscious starts tracking, Cortex distributes config to Salience). Currently only shows Cortex → Hippocampus.
- [ ] **4.3** MSG-0006 (Reflect): Show async deep-path continuing in background through NotificationPort.
- [ ] **4.4** All diagrams: Show transaction context envelope on message arrows where it clarifies the flow.

### 5. Resolve Documentation Audit Gaps (D-Audit)

Fix mismatches between ADRs, architecture pages, and design documents.

- [ ] **D-Audit-1** BackingServicePort operation names -- DESIGN DECISION NEEDED. Port has adapter verbs (save, findById, search). Design A says ports are passive structures. Either redefine port as passive contract or document exception. Code change required.
- [ ] **D-Audit-2** NotificationPort: architecture says notify/remind/alert, code has single send(). Align.
- [ ] **D-Audit-3** RelayPort: architecture says send/receive/list_pending, code has single relay(). Align (Agora-dependent, lower priority).
- [ ] **D-Audit-4** ADR-0006 event count: says 13, code has 15. Add ModeChanged and SessionState to ADR.
- [ ] **D-Audit-5** ADR-0006 SearchQuery routing: says Hippocampus, should be Recall.
- [ ] **D-Audit-6** ADR-0006 ReflectQuery routing: says Synapse, should be Recall.
- [ ] **D-Audit-7** architecture-hexagonal.adoc: "five actors" should be six (Recall missing).
- [ ] **D-Audit-8** architecture-contexts.adoc: Hippocampus command list missing ReclassifyCommand, TierPromoted, TierDemoted.
- [ ] **D-Audit-9** "Governing Dynamic" section in all ADRs not defined in ADR-0001 template.
- [ ] **D-Audit-10** SearchFilter type: ADR-0006 says SearchFilter, code uses Map<String, String>.

### 6. Resolve Code Audit Gaps (C-Audit)

Fix code to match designs.

- [ ] **C-Audit-1** SearchFilter is dead code. Remove or adopt.
- [ ] **C-Audit-2** TierChanged / MemoryReclassified duplicate events. Document distinction or remove one.
- [ ] **C-Audit-3** AssociationDirection in Command.kt. Move to domain/model/.
- [ ] **C-Audit-4** associate_memories: required params have fallback defaults. Pick one.
- [ ] **C-Audit-5** Stringly-typed fields (6 fields that should be enums).
- [ ] **C-Audit-6** Temporal fields (flushTimeout, duration) have no unit spec.
- [ ] **C-Audit-7** heartbeat() has no corresponding Event.
- [ ] **C-Audit-8** SalienceScored duplicates SalienceScore fields.
- [ ] **C-Audit-9** TotalRecall.kt refactoring pass (noted in code comment).

### 7. Resolve Test Audit Gaps (T-Audit)

- [ ] **T-Audit-1** Server test is smoke-only. Add tool invocation and schema checks.
- [ ] **T-Audit-2** Self-referential assertion in MessageTest line 90. Fix.
- [ ] **T-Audit-3** Low message coverage (4/7 commands, 0/2 queries, 4/15 events untested).

### 8. Verify Catalog Matrix

- [ ] **7.1** Diagram inventory matches actual diagrams (15 total).
- [ ] **7.2** Design Documents table is current (all Done).
- [ ] **7.3** Bounded Contexts to Code mapping is accurate after code fixes.
- [ ] **7.4** Messages to Code counts are correct after event/command changes.
- [ ] **7.5** Ports to Code operations match after port redesign.
- [ ] **7.6** Known Inconsistencies section updated (resolved items removed, new items if any).

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
- [x] Design E1 -- "Hippocampus": aggregate root, single-writer invariant, walk-in cooler metaphor (four shelves = four tiers). Seven commands from three sources. Six events to four consumers. Claiming mechanism as active identity choice. HP-0001 bounded context diagram added. Association storage section rewritten (Synapse = dependent aggregate, own file cabinet, internal only).
- [x] Architectural clarifications from Vadim: Synapse is a dependent aggregate (DDD pattern) with its own file cabinet (own storage), accessed only internally. Recall is the CQRS read side -- no state, assembles from three sources. Cortex is the window clerk / entry point / dispatcher. Search is both transactional (immediate MCP tool response) and asynchronous (deeper associations push via NotificationPort).
- [x] Bounded context rename to biological memory vocabulary: Tiered Memory → Hippocampus, Association Graph → Synapse, Attention → Salience, Recollection → Recall, Session Context → Cortex, Daemon → Subconscious. Also AttentionScore → SalienceScore, AttentionScored → SalienceScored. 29+ files updated across site, code, diagrams, ADRs, blog posts. Build verified.
- [x] Design E2 -- "Salience (Focus)": expo at the pass. Computes, never persists. Event-sourced projection. DecaySweep from Subconscious. Self-configuration via IDENTITY_CORE thresholds. SL-0001 diagram. First draft had LLM-speak -- rewrote with clean voice after re-reading E1.
- [x] Design E3 -- "Synapse (Matrix)": "Hon, where did I put my keys?" Two kinds of knowing: noticed (mechanical) and understood (semantic). Dependent aggregate with own file cabinet. Five connection types. SY-0001 diagram.
- [x] Design E4 -- "Recall (Stream)": remembering itself as the analogy. Fast path + deep path. CQRS read side. Owns nothing. RC-0001 diagram.
- [x] Design E5 -- "Cortex (Inception)": "When someone asks you a question." Translates MCP ↔ internal. Routes to all five contexts. Two speeds. Session lifecycle. Self-configuration distribution. CX-0001 diagram.
- [x] Design E6 -- "Subconscious (Dream)": "While you sleep." Timer-driven. Three jobs: decay sweeps, consolidation, session monitoring. Graceful shutdown. SB-0001 diagram.
- [x] Parenthetical subtitles added to all six bounded contexts: Vault, Focus, Matrix, Stream, Inception, Dream. Navigation and doc titles updated.
- [x] Catalog updated: 6 new bounded context diagrams added to inventory. All design docs marked Done.
- [x] Data flow audit: verified all 6 sequence diagrams against completed designs. Found 6 findings (F-Audit-1 through 6). Critical: transaction context absent from all messages.
- [x] Transaction context gap identified by Vadim: Commands and Queries are transactional/conversational, not fire-and-forget. Every message must carry a transaction envelope (sessionId, requestId, stepId, sequenceId, timestamps). This is structural, not observability. Blocks all remaining audit resolution.
- [x] Message conventions section added to architecture-messages.adoc: arrow types (solid=command/query, dashed=response, open=event), naming (snake_case external, CamelCase internal), payload conventions, event source pattern, retry.
- [x] MSG-0001 rewritten by Vadim: activate/deactivate lifelines, critical OnInit with subscriptions, par blocks for parallel consumption/processing, publish/consume verbs on events. Template for all other diagrams.
- [x] MSG-0002 rewritten: fast path only (query transaction + MemoryAccessedEvent broadcast + TotalRecallAdvisoryEvent). Deep path removed -- becomes MSG-0007.
- [x] MSG-0007 created: Total Recall (the feature the project is named after). Subconscious drives, Recall assembles via queries to Synapse/Hippocampus, results push through NES → NotificationPort → Mind. Strict bounded context boundaries -- each component does its one job only.
- [x] Two Event Stores established: MES (Memory Event Store) for domain events, NES (Notification Event Store) for mind-facing notifications. Different concerns, different channels.
- [x] Key architectural correction: Total Recall deep path is NOT an appendix to search. It's a separate process with different trigger, lifecycle, actors, and timing. Subconscious is the background orchestrator (not Recall, not Synapse).
- [x] MSG-0007 wired into architecture-messages.adoc (section text, include markup, Diagram 10). Catalog updated. Flow count Six → Seven.
- [x] HTML entities removed from diagrams: `&lt;` / `&gt;` replaced with "Set of Type" / "List of Type" in MSG-0002 and MSG-0007. Entities don't render in IDE or scripts -- spell out collection types in diagrams.
- [x] MSG-0002 Total Recall trigger redesigned: Mind decides, not Recall. Recall assembles and returns -- it has no judgment. The mind evaluates results (prefrontal cortex in humans, forced prompt in LLMs) and signals through Cortex. Advisory event stays, but origin moves from Recall to Mind → Cortex. Added Important note: decision to recollect is mind-adapter-specific (humans: native prefrontal cortex or UI; LLMs: forced prompt, different per model). Core provides the port, adapter decides how.
- [x] F-Audit-1 resolved: MemoryAccessedEvent fires on retrieval in MSG-0002.
- [x] F-Audit-2 resolved: two-speed architecture is now two diagrams (MSG-0002 fast path, MSG-0007 deep path).

---

## Lessons Learned This Session

1. **Think and discuss first, implement after.** Testing one diagram before committing to nine caught three bugs: `<figure>` default margins break layout, `:page-liquid:` is required for Liquid in AsciiDoc, inline svgPanZoom forces 60vh height with massive empty space.
2. **Stop for review between steps.** Combining 0.3+0.4 without pausing skipped the checkpoint. Small steps feel slow but catch problems early.
3. **Inline pan-zoom was wrong.** Natural size for inline diagrams. Pan-zoom belongs in the fullscreen lightbox only.
4. **`:page-liquid:` gotcha.** Jekyll-asciidoc does not process Liquid tags by default. Each AsciiDoc file using `{% include %}` needs `:page-liquid:` in its document header.
5. **Knowledge is not understanding.** DDD vocabulary (port, adapter, ACL) was in the training data. The ontological distinction (Passive Structure vs Active Structure) was not. A port has no verbs -- `storeMemory()` and `save()` are adapter behavior leaked into port definitions. This changes how we read the entire codebase.
6. **ACL is a convenience construct.** Not a class, not middleware. Adapter + Port + Adapter, visible from the architect's chair. You build adapters that plug into ports. The ACL emerges. The contract lives in the core domain -- that's the SDK.
7. **Name things for what they are, not what they do technically.** "Tiered Memory" is a database description. "Hippocampus" tells you it forms, organizes, and consolidates memories -- which IS what the aggregate root does. Biological memory vocabulary (Hippocampus, Synapse, Salience, Recall, Cortex, Subconscious) makes the system self-documenting because the names carry the right connotations.
8. **Copy the diagram include pattern, don't write from memory.** The full pattern is: `:page-liquid:` in front matter, `++++` passthrough block, `<div id="ID">`, `<pre class="mermaid">`, `{% include diagrams/file.mmd %}`. Context compaction erases muscle memory. Open a working page (e.g. architecture-contexts.adoc lines 23-30) and copy every time.
9. **Think about the thing, not the output.** When thinking about what an expo actually does at the pass, the writing is clean. When thinking about how the document should read, LLM-speak fills the space. E2 first draft failed because of this. E2 rewrite succeeded.
10. **Tracing is not a crosscut -- it's structural.** Vadim caught a fundamental gap: the designs describe bounded contexts passing notes, but Commands and Queries are transactional conversations with payloads. Every message must carry a transaction envelope (sessionId, requestId, stepId, sequenceId, timestamps). Without it, nothing is traceable. This is not observability bolted on later -- it's the spine that makes data flow actually work. Designing without it is "speaking abstract fables, not designing a system that actually works."
11. **Verify diagrams against designs, not the other way around.** The sequence diagrams (MSG-0001 through MSG-0006) were written before the detailed designs. Artem asked about data flow gaps. Checking diagrams against designs found 6 issues, including 2 HIGH severity. Always verify existing artifacts when new design work changes the source of truth.
12. **Separate processes get separate diagrams.** MSG-0002 tried to contain both the synchronous search transaction AND the asynchronous Total Recall deep path. Different triggers, different lifecycles, different actors, different timing. Forcing them together tangled activation bars and blurred boundaries. Split into MSG-0002 (fast path) and MSG-0007 (Total Recall).
13. **Total Recall IS the feature, not just the project name.** The project is named after this functionality. The MCP server's core purpose is pushing unsolicited recollections at the mind. Claude is passive by nature (waits for prompts). Total Recall overrides that passivity.
14. **Strict jobs for components.** Each bounded context does ONE thing. Synapse answers association queries -- it does not command Recall. Hippocampus retrieves memories -- it does not orchestrate. Subconscious initiates background work -- it does not assemble. Overstepping authority in diagrams reveals misunderstanding of boundaries.
15. **Only the mind has judgment.** Recall can assemble and rank, but it cannot decide whether results are "enough." That's the mind's job -- like the prefrontal cortex evaluating hippocampal retrieval. In humans it's native. In LLMs it's a forced prompt (different per model). The decision mechanism is adapter business. The core provides the port.

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

**E2. Salience (Focus)** -- DONE. Expo at the pass. Computes, never persists. Event-sourced projection rebuilt from Hippocampus events on restart. DecaySweep triggered by Subconscious. Self-configuration: thresholds stored as IDENTITY_CORE memories, read by Cortex, passed to Salience. SL-0001 diagram.

**E3. Synapse (Matrix)** -- DONE. "Hon, where did I put my keys?" Two kinds of knowing: noticed (mechanical, reconstructible from metadata) and understood (semantic, mind's judgment, not reconstructible). Dependent aggregate with own file cabinet. Five connection types (TEMPORAL, CAUSAL, THEMATIC, EMOTIONAL, PERSON). SY-0001 diagram.

**E4. Recall (Stream)** -- DONE. Remembering itself as the analogy. Fast path (match, rank, enrich) returns immediately. Deep path continues in background, pushes through NotificationPort. CQRS read side. Owns nothing. RC-0001 diagram.

**E5. Cortex (Inception)** -- DONE. "When someone asks you a question." Translates MCP ↔ internal language. Routes to all five contexts. Two speeds: synchronous through Cortex, asynchronous through NotificationPort. Session lifecycle. Self-configuration distribution. CX-0001 diagram.

**E6. Subconscious (Dream)** -- DONE. "While you sleep." Timer-driven, not request-driven. Three jobs: decay sweeps (Salience), consolidation (Hippocampus), session monitoring (NotificationPort). Graceful shutdown coordination. SB-0001 diagram.

---

## Audit Findings -- Data Flow (F-Audit)

Discovered 2026-03-03. Sequence diagrams verified against completed design documents (E1-E6).

### F-Audit-1. ~~MSG-0002 missing MemoryAccessed on retrieval~~ RESOLVED

MemoryAccessedEvent now fires on retrieval in MSG-0002. Feeds Salience decay model.

### F-Audit-2. ~~MSG-0002 doesn't show two-speed architecture~~ RESOLVED

Two-speed architecture is now two diagrams: MSG-0002 (fast path) and MSG-0007 (deep path / Total Recall).

### F-Audit-3. MSG-0005 missing SessionStart broadcast (HIGH)

Design E5 (Cortex) says "Cortex broadcasts SessionStart to all contexts." The diagram only shows Cortex → Hippocampus. Missing: Subconscious needs SessionStart to begin tracking session duration. Cortex needs to read IDENTITY_CORE config and distribute thresholds to Salience.

### F-Audit-4. MSG-0006 missing async deep-path (MEDIUM)

Reflect diagram doesn't show that deeper association traversals continue in background through NotificationPort, same pattern as search.

### F-Audit-5. Transaction context absent from all messages (CRITICAL)

No message in the system carries a transaction envelope. Commands and Queries are transactional and conversational with payloads -- not fire-and-forget. Without sessionId, requestId, stepId, sequenceId, and timestamps on every message, nothing is traceable. This is not observability bolted on later -- it is the structural spine that makes data flow work. See task list step 3.

### F-Audit-6. MemoryRetrieved / SalienceScored / AssociationsFound identity crisis (MEDIUM)

Message catalog (architecture-messages.adoc) lists these as domain Events. Design E4 (Recall) describes them as sources Recall "reads from." They are actually response payloads in a transactional chain -- the transaction context (F-Audit-5) is what ties the request to the response. Reclassify once TransactionContext is designed.

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

| Question                     | Status                                                                                                                         |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| Container image approach     | NOT STARTED                                                                                                                    |
| CI/CD build workflow         | NOT STARTED                                                                                                                    |
| Who owns Association storage | RESOLVED -- Synapse is a dependent aggregate with own storage, internal only                                                   |
| BackingServicePort design    | UNDER DISCUSSION -- depends on TransactionContext design (step 3)                                                              |
| TransactionContext shape     | IDENTIFIED -- sessionId, requestId, stepId, sequenceId, timestamps minimum. Needs design doc.                                  |
| Message identity crisis      | IDENTIFIED -- MemoryRetrieved/SalienceScored/AssociationsFound: events or response payloads? Resolves with TransactionContext. |
| Data flow diagrams           | DECIDED -- Not needed. Sequence diagrams (MSG-0001 through MSG-0007) now carry all data flow information: producers, consumers, payloads, ordering, branching. Separate data flow diagrams would be redundant. |
