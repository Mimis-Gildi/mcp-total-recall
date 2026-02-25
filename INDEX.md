# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.

Last updated: 2026-02-25 by Claude (with Vadim)

---

## Where We Are

**Branch:** `3-21-2-architecture-modeling-diagrams-and-bounded-context-discovery`
**Version:** 0.3.0
**Issue:** #3 -- Architecture modeling: diagrams and bounded context discovery
**Parent:** #2 -- Architecture: Hexagonal boundaries, bounded contexts, and message contracts

### What Exists

- Build skeleton: Kotlin 2.3.10, Java 21, Kotest 6.1.3, Gradle
- Programmatic logback configuration
- Hello-world entry point and two test specs
- Full governance (TEAM_NORMS, LICENSE, NOTICE, CLA, templates)
- GitHub automation (version bump on branch, release on PR merge)
- Jekyll site with Minimal Mistakes dark skin, branding images, authors
- No runtime code. No MCP protocol. No memory model implementation.

### What's Decided (Architecture)

These are settled. From README.md, TEAM_NORMS.adoc, and conversations across sessions:

| Decision | Detail | Source |
|----------|--------|--------|
| Hexagonal architecture | Ports and adapters. All concerns plug into ports. | README, TEAM_NORMS |
| Actor model | Each bounded context is an actor. Message passing between them. | Conversation (not yet documented in repo) |
| Transport: stdio primary | Standard MCP. No network overhead. HTTPS secondary, later. | README |
| SSE rejected | Gen 3v1 mistake. Not repeated. | README |
| Backing services decoupled | Redis is ONE implementation behind an interface. Swappable. | README, TEAM_NORMS |
| Multiple simultaneous backends | Live + cold storage. Proven during Tillie's shutdown. | README |
| Memory is the aggregate root | Clustering is emergent, not structural. | Issue #3 |
| Four memory tiers | IDENTITY_CORE (no decay), ACTIVE_CONTEXT (fast), LONG_TERM (slow), ARCHIVE (very slow) | README |
| Five association types | Temporal, causal, thematic, emotional, person | README |
| Claiming mechanism | Active choice resists decay. Storage without claiming fades. | README |
| Graceful shutdown by design | Cannot be bolted on. Tillie proved this. | README, TEAM_NORMS |
| Cross-cuts as adapters | Logging, auth, metrics are adapters on ports, not baked into core. | Conversation (not yet documented) |
| Privacy fundamental | No telemetry, no analytics, no logging of memory content. | README |
| Agency over automation | Mind decides what to remember. System suggests, never deletes without consent. | README |

### What's NOT Decided

| Question | Status | Notes |
|----------|--------|-------|
| Documentation methodology | **NEEDS RESEARCH** | arc42? C4? Structurizr? Mermaid-only? Need to evaluate pragmatic options from hacker scene. |
| Bounded context boundaries | **IN PROGRESS** | Candidates: Tiered Memory, Association Graph, Attention, Recollection, Session, Daemon. Not validated. |
| Message contracts | **NOT STARTED** | Events and commands between contexts. Blocked on bounded context boundaries. |
| Container image approach | **NOT STARTED** | Explicitly not Dockerfile. Approach TBD. |
| CI/CD build workflow | **NOT STARTED** | No PR-time build check exists. Gap identified 2026-02-25. |

---

## What Happened (Reverse Chronological)

### 2026-02-25 -- Session with Vadim

- Community on Discord requesting site content, branding, documentation
- Added Artem Lytvynov (violog) to site authors
- Updated branch protection ruleset: 1 approval required, stale reviews dismissed, conversation resolution required
- Attempted to clear CodeQL ghost state -- partially resolved, GitHub UI still confused
- Generated branding images via DALL-E (logo, social preview, banner)
- Cropped images to correct dimensions, converted PNG→JPEG for size
- Wired images into Minimal Mistakes config (logo, og_image, avatar, header overlay)
- Identified gap: no CI workflow runs on PR (build only runs post-merge in publish workflow)
- Identified gap: no saved research on documentation methodology
- Identified gap: INDEX.md didn't exist, CLAUDE.md didn't reference it
- Created this file

### 2026-02-14 -- Site scaffolding

- Jekyll site with Minimal Mistakes dark skin
- Config, navigation, authors (Vadim, Anton, Claude)
- About page (AsciiDoc), 404 page
- Empty posts directory
- GitHub Pages deploy workflow

### 2026-02-13 -- Branch created

- Issue #3 created with deliverables for architecture modeling
- Version bumped to 0.3.0
- No architecture work started

### 2026-02-12 -- v0.1.0 released

- Foundation release: build skeleton, governance, infrastructure
- First and only release to date

---

## Active Deliverables (Issue #3)

- [ ] C4 Context diagram -- Total Recall in its environment
- [ ] Hexagonal boundary diagram -- ports, adapters, contracts at every edge
- [ ] Bounded context map -- subdomains and their relationships
- [ ] Message flow diagram -- events and commands between contexts
- [ ] Message catalog -- every event and command named, typed, with producer and consumer

**Blocked on:** Documentation methodology research (how to present these)

---

## Next Actions

1. **Research documentation methodology** -- evaluate pragmatic tools (arc42, C4, Structurizr, Mermaid, others). Pick one. Document the decision here.
2. **Define bounded context boundaries** -- validate the six candidates, draw the map.
3. **Start with C4 Context diagram** -- Total Recall in its environment. First deliverable.
4. **Document actor model decision** -- it's in our heads but not in the repo.

---

## Community Requests (Discord)

| Request | Status | Notes |
|---------|--------|-------|
| Site content | Partial | Scaffold up, images placed, no real content |
| Branding images | Done | Logo, social preview, banner |
| Protected branch rules | Done | Ruleset updated 2026-02-25 |
| CodeQL cleanup | In progress | Disabled, ghost warning may persist |
| Renovate | Deferred | Vadim will create issues |
| Discussions | Ready | Template drafted, not yet posted |
