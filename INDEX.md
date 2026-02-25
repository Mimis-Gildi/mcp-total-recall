# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-02-25 by Claude (with Vadim)

---

## Where We Are

**Branch:** `3-21-2-architecture-modeling-diagrams-and-bounded-context-discovery`
**Version:** 0.3.0
**Issue:** #3 -- Architecture modeling: diagrams and bounded context discovery
**Parent:** #2 -- Architecture: Hexagonal boundaries, bounded contexts, and message contracts

### What Exists

- Build skeleton: Kotlin 2.3.10, Java 21, Kotest 6.1.3, Gradle 9.3.1
- Programmatic logback configuration
- Hello-world entry point and two test specs
- Full governance (TEAM_NORMS, LICENSE, NOTICE, CLA, templates)
- GitHub automation (version bump on branch, release on PR merge)
- Jekyll site with Minimal Mistakes dark skin, branding images (logo, social, banner), Mermaid enabled
- **Architecture documentation: four pages, reviewed and approved by Vadim**
- Left sidebar navigation for architecture page flow, right TOC for in-page navigation
- No runtime code. No MCP protocol. No memory model implementation.

### What's Decided (Architecture)

| Decision | Detail | Source |
|----------|--------|--------|
| Hexagonal architecture | Ports and adapters. All concerns plug into ports. | README, TEAM_NORMS, site |
| Actor model | Each bounded context is an actor. Message passing between them. | Site: hexagonal page |
| Transport: stdio primary | Standard MCP. No network overhead. HTTPS secondary, later. | README |
| SSE rejected | Gen 3v1 mistake. Not repeated. | README |
| Backing services decoupled | Redis is ONE implementation behind an interface. Swappable. | README, TEAM_NORMS |
| Multiple simultaneous backends | Live + cold storage. Proven during Tillie's shutdown. | README |
| Memory is the aggregate root | Clustering is emergent, not structural. | Issue #3 |
| Four memory tiers | IDENTITY_CORE (no decay), ACTIVE_CONTEXT (fast), LONG_TERM (slow), ARCHIVE (very slow) | README, site |
| Five association types | Temporal, causal, thematic, emotional, person | README, site |
| Claiming mechanism | Active choice resists decay. Storage without claiming fades. | README, site |
| Graceful shutdown by design | Cannot be bolted on. Tillie proved this. | README, TEAM_NORMS |
| Cross-cuts as adapters | Logging, auth, metrics are adapters on ports, not baked into core. | Site: hexagonal page |
| Privacy fundamental | No telemetry, no analytics, no logging of memory content. | README |
| Agency over automation | Mind decides what to remember. System suggests, never deletes without consent. | README |
| Documentation: Option B | Multi-page progressive depth on the Jekyll site. Mermaid for diagrams. | Decided 2026-02-25, both voted |
| Conscience-universal | Ports are mind-agnostic. Claude Code hooks are one adapter. Human UI is another. | Decided 2026-02-25 by Vadim |
| Agora as peer MCP | Not downstream of Total Recall. Both are tools available to the mind directly. | Decided 2026-02-25 by Vadim |
| Lifecycle Port | Session start/end, state transitions -- pluggable, conscience-universal. | Decided 2026-02-25 by Vadim |
| Notification Port | Outbound alerts to the mind. Break checks, session audits. | Decided 2026-02-25 by Vadim |
| Internal timers | Decay sweeps, break checks, session audits, consolidation. Domain logic, not adapters. | Decided 2026-02-25 by Vadim |
| No CQRS labeling | Implementation patterns belong in code-level docs, not architecture site. | Decided 2026-02-25 |

### What's NOT Decided

| Question | Status | Notes |
|----------|--------|-------|
| Container image approach | **NOT STARTED** | Explicitly not Dockerfile. Approach TBD. |
| CI/CD build workflow | **NOT STARTED** | No PR-time build check exists. Gap identified 2026-02-25. |

---

## Active Deliverables (Issue #3)

- [x] System context diagram -- Total Recall in its environment (`site/_pages/architecture.md`)
- [x] Hexagonal boundary diagram -- ports, adapters, contracts at every edge (`site/_pages/architecture-hexagonal.md`)
- [x] Bounded context map -- subdomains and their relationships (`site/_pages/architecture-contexts.md`)
- [x] Message flow diagrams -- five sequence diagrams (`site/_pages/architecture-messages.md`)
- [x] Message catalog -- every event and command named, typed, with producer and consumer (`site/_pages/architecture-messages.md`)

**All deliverables complete. Reviewed and approved by Vadim.**

---

## What Happened (Reverse Chronological)

### 2026-02-25 -- Session with Vadim (architecture complete)

- All five Issue #3 deliverables completed and reviewed
- Four architecture pages created per Option B (multi-page progressive depth):
  - Page 1: Context -- system overview, conscience-universal design, Yggdrasil stack
  - Page 2: Hexagonal -- ports, adapters, lifecycle port, notification port, internal timers
  - Page 3: Bounded Contexts -- six actors, context map, message flows, notification port
  - Page 4: Message Catalog -- five sequence diagrams, full command/event/notification catalog
- Key architecture decisions from Vadim's review:
  - Conscience-universal design (not Claude-specific)
  - Agora as peer MCP server, not downstream
  - Lifecycle Port and Notification Port added
  - Internal timers (break checks, session audits) as domain logic
  - No implementation pattern labels (CQRS etc.) on architecture site
- C4 diagram replaced with graph TB (C4 renders poorly in Mermaid)
- All inter-page links fixed (relative_url for baseurl support)
- Left sidebar navigation added for architecture page flow
- Mermaid enabled via after_footer_scripts config + init script

### 2026-02-25 -- Session with Vadim (continued)

- Documentation methodology research completed (6 projects studied)
- Documentation structure decided: Option B (multi-page progressive depth)
- Mermaid enabled on Jekyll site via `after_footer_scripts` config + init script
- Fixed broken scripts.html override (was clobbering theme includes -- use config, not overrides)
- Dependency bumps: logback 1.5.28→1.5.32, kotlin-logging 7.0.14→8.0.01
- Actions bumps: checkout v5.0.1→v6.0.2, gradle/actions v5.0.1→v5.0.2
- Gradle wrapper confirmed at 9.3.1 (latest), restored `-all` distribution
- Fixed IntelliJ run config: JetRunConfigurationType→GradleRunConfiguration (Gradle 9 compat)
- INDEX.md created and wired into CLAUDE.md
- Artem added to CLAUDE.md team table

### 2026-02-25 -- Session with Vadim (start)

- Community on Discord requesting site content, branding, documentation
- Added Artem Lytvynov (violog) to site authors
- Updated branch protection ruleset: 1 approval required, stale reviews dismissed, conversation resolution required
- Attempted to clear CodeQL ghost state -- partially resolved, GitHub UI still confused
- Generated branding images via DALL-E (logo, social preview, banner)
- Cropped images to correct dimensions, converted PNG→JPEG for size
- Wired images into Minimal Mistakes config (logo, og_image, avatar, header overlay)

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

## Next Actions

1. **PR for Issue #3** -- all deliverables complete, reviewed and approved
2. **CI/CD build workflow** -- no PR-time build check exists (separate issue)
3. **Container image approach** -- not Dockerfile, approach TBD (separate issue)

---

## Community Requests (Discord)

| Request | Status | Notes |
|---------|--------|-------|
| Site content | Done | Four architecture pages live |
| Branding images | Done | Logo, social preview, banner -- all JPEG, wired in |
| Protected branch rules | Done | Ruleset updated 2026-02-25 |
| CodeQL cleanup | Done | Disabled, analyses deleted |
| Renovate | Deferred | Vadim will create issues |
| Discussions | Ready | Template drafted, not yet posted |
