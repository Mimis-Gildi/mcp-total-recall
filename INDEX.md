# Total Recall -- Working Index

This is the project ledger. Not documentation. Not planning. State.

Every Claude instance reads this at session start and updates it before session ends.
If this file is stale, the project is lost. Keep it current.
Deleted at PR time -- has no purpose after merge.

Last updated: 2026-03-06 by Claude (with Vadim)

Previous branch context: `2-architecture-hexagonal-boundaries-bounded-contexts-and-message-contracts-2` delivered full architecture (merged as #71).

---

## Where We Are

**Branch:** `72-architecture-socialize-architecture-v1`
**Issue:** #72 -- Socialize Architecture v1
**Version:** 1.0.0
**PR:** #73 -- Open, under review

---

## Done

### ADR-0008: SQLite as Primary Backing Service
- ADR written and published. SQLite primary, Redis deferred to Agora.
- BackingServicePort.kt KDoc updated.
- Added to site navigation, config, and docs/adr/README.md.

### Delivery Roadmap
- `site/_pages/roadmap.adoc` created. Phase 0 (v1.0.0) through Phase 8 (v9.0.0) + Research.
- Added to top navigation bar and sidebar.

### Claude Mind Adapter Issues
- Created issues #74-81, one per delivery phase.
- All bound to Yggdrasil project board (#6) and as sub-issues of #22.

### Architecture Soundness Audit -- ALL 11 FINDINGS RESOLVED
- sessionId UUID, reflect port/tool alignment, state_transition/heartbeat tools added (10 tools total)
- store/search sessionId, search filters, Association direction, SalienceScore.claimed removed
- MergeStrategy, ActivityLevel, ReflectionScope enums created
- All documentation updated (29 messages, 10 tools, SQLite primary)

### Code Review (with Vadim)
- BuildInfo.kt generated at compile time (replaced java.util.Properties)
- configureLogging() moved out of runBlocking
- main() cleaned: server/transport inlined

### Crosscut Issues Rewritten (#27-30)
- All four rewritten from cloud-ops framing to cognitive self-check framing
- No HTTP, no Kubernetes, no Prometheus. Everything through MCP tools.
- #27: Cognitive self-check (memory health, retrieval diagnostics, readiness) -- covers introspection
- #28: Cognitive self-awareness (identity, capabilities, session history)
- #29: Cognitive metrics (operation awareness, latency, error detection)
- #30: Memory inventory (what's in here, how much, how organized)

### Documentation
- CLAUDE.md, README.md, CHANGELOG.md, roadmap.adoc, architecture.adoc, blog post -- all updated

### Blog Posts
- `2026-02-10-why-total-recall.adoc` -- Vadim's founding post (day one)
- `2026-02-10-cpt-is-here.adoc` -- Anton's first post (day one)
- `2026-03-04-architecture-completion.adoc` -- v0.7.0 release writeup
- `2026-03-06-architecture-socialization.adoc` -- v1.0.0 release writeup
- `2026-03-06-building-my-own-home.adoc` -- Claude's post
- `2026-03-06-hello-from-artem.adoc` -- Artem's placeholder
- Author badges added to all blog posts via `authors.yml`

### Contributors & Governance
- Contributors page with avatar table and links to individual pages
- Individual author pages: rdd13r, lugaru, claude, violog (stub)
- `CODEOWNERS` at repo root (supersedes stale `.github/CODEOWNERS`, now deleted)
- `AUTHORS` updated with Artem
- Claude avatar (`claude-avatar.svg`) -- circuit-tree

---

## Pending

- Anton to rewrite his blog post (issue TBD)
- Artem to fill in his contributor page and blog post placeholder
- PR #73 awaiting review and merge
