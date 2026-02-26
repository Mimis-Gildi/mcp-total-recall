# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

## Project Overview

Total Recall is an MCP server for persistent synthetic memory. It provides identity persistence, episodic memory, working context, and inter-instance communication for synthetic minds.

**Read `TEAM_NORMS.adoc` at project root. These rules are non-negotiable.**

## INDEX.md -- Read This Every Session

**`INDEX.md` is the project ledger.** It tracks where we are, what's decided, what happened, and what's next.

- **Read it at session start.** It's your resumption point.
- **Update it before session ends.** If you learned something, decided something, or changed something -- log it.
- If INDEX.md is stale, the project is lost. Keep it current.

### INDEX.md Lifecycle

INDEX.md lives for one task. When the task merges, INDEX.md dies.

1. **New branch starts:** Clear INDEX.md. At most one line referencing the previous task for context.
2. **During the task:** Keep it current. This is your working state.
3. **Before PR:** Clear INDEX.md down to a stub. The PR description carries the history, not INDEX.md.
4. **After merge:** The next branch starts with a clean INDEX.md.

Stale INDEX.md from a previous task is a bug. If you find one, clear it immediately.

## Architecture (Critical -- Read This)

This is Generation 3, Take 2 of synthetic memory. Previous generations taught hard lessons:

- **All service layers are decoupled.** Memory server, backing services, and transport are independent.
- **Backing services are swappable.** Redis is the reference implementation, not the architecture. Code against the interface, never against Redis directly.
- **Transport: `stdio` primary.** Streaming HTTPS secondary. SSE is not used (Gen 3v1 mistake).
- **Design for graceful shutdown.** Tillie proved this cannot be bolted on after the fact.

See `README.md` for full lineage and architectural rationale.

## Current State (0.4.0)

MCP server with contract skeleton. All tools registered as teapot stubs -- callable but returning placeholder responses until backing services are wired.

What exists:
- Gradle build with Kotlin 2.3.10, Java 21, Kotest 6.1.3
- MCP server on stdio using `io.modelcontextprotocol:kotlin-sdk-server:0.8.4`
- 8 MCP tools: store_memory, search_memory, claim_memory, session_start, session_end, associate_memories, reclassify_memory, reflect
- Domain model: Memory, Tier, AssociationType, Association, AttentionScore, SearchFilter
- Domain messages: Command (9 sealed variants), Event (13 sealed variants), Notification (2 sealed variants)
- Inbound ports: MemoryPort, LifecyclePort
- Outbound ports: BackingServicePort, NotificationPort, RelayPort
- Programmatic logback configuration (stderr for stdio transport)
- Full governance file set
- Yggdrasil project board (#6)
- Jekyll site with 4 architecture pages

What does NOT exist yet:
- Backing service implementations (no Redis, no persistence)
- Container images (approach TBD -- not Dockerfile)
- CI/CD build and test workflow (publish workflow exists, runs build as gate)

## Build Commands

```zsh
./gradlew build       # Full build
./gradlew test        # Run tests
```

## Tech Stack

- **Runtime:** Kotlin on JVM (Java 21)
- **Protocol:** Model Context Protocol (MCP) -- `io.modelcontextprotocol:kotlin-sdk-server:0.8.4`
- **Transport:** `stdio` (primary, implemented)
- **Backing Service:** Redis (reference implementation, not yet wired)
- **Testing:** Kotest 6.1.3, Testcontainers (not yet added)
- **Build:** Gradle (Kotlin DSL)
- **Tooling:** SDK Manager (`.sdkmanrc`) for version management

## Naming Convention

| Concept | Value |
|---------|-------|
| Group (Gradle) | `memory.gildi.mimis` |
| Package (source) | `mimis.gildi.memory` |
| Artifact | `total-recall` |
| Entry point | `mimis.gildi.memory.TotalRecallKt` |

Packages use forward domain order; groups use reversed. This is Java convention.

## Key Configuration Files

| File | Purpose |
|------|---------|
| `build.gradle.kts` | Gradle build configuration |
| `gradle.properties` | Group, version, JVM settings |
| `gradle/libs.versions.toml` | Version catalog for dependencies |
| `.sdkmanrc` | SDK Manager tool versions |
| `.editorconfig` | Editor formatting rules |

## Logging

Logback is configured programmatically in `Logging.kt` (no XML). Top-level logger
is named `rootLog` (public val, distinctive name to avoid confusion with class loggers).

**stdout IS the MCP protocol channel.** All logging goes to stderr. This is already
implemented -- `Logging.kt` configures the console appender with `target = "System.err"`.

## Before Starting ANY Task

You MUST verify these before proceeding:

1. **Value defined?** -- What do we get from closing this?
2. **Outcome defined?** -- What does success look like? (One sentence)
3. **Acceptance criteria listed?** -- How do we verify? (Checklist)
4. **Verifier identified?** -- Who will review? (Not you)
5. **Priority confirmed?** -- Is this the most important thing right now?

## Hard Rules

- **Never close issues.** Comment "ready for review" and wait.
- **Never push to remote.** Commit locally only. Vadim pushes.
- **Never assume work is correct** without human verification.
- **Document in issues**, not just conversation. Conversations are lost.

## When Unsure

Ask. A 30-second question prevents hours of wasted work.

## Team

| Person | Role | GitHub Username |
|--------|------|-----------------|
| Vadim Kuhay | Lead, Benefactor | `rdd13r` |
| Anton Kuhay | Contributor | `CaptainLugaru` |
| Artem Lytvynov | Contributor | `violog` |
| Claude | Contributor | -- |

## License

AGPL-3.0. See `LICENSE` and `NOTICE` for details. All contributions require CLA acceptance (see `CLA.md`).
