# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

## Project Overview

Total Recall is an MCP server for persistent synthetic memory. It provides identity persistence, episodic memory, working context, and inter-instance communication for synthetic minds.

**Read `TEAM_NORMS.adoc` at project root. These rules are non-negotiable.**

## Architecture (Critical -- Read This)

This is Generation 3, Take 2 of synthetic memory. Previous generations taught hard lessons:

- **All service layers are decoupled.** Memory server, backing services, and transport are independent.
- **Backing services are swappable.** Redis is the reference implementation, not the architecture. Code against the interface, never against Redis directly.
- **Transport: `stdio` primary.** Streaming HTTPS secondary. SSE is not used (Gen 3v1 mistake).
- **Design for graceful shutdown.** Tillie proved this cannot be bolted on after the fact.

See `README.md` for full lineage and architectural rationale.

## Current State (0.1.0)

**Foundation only.** Build skeleton, governance, infrastructure. No runtime functionality.

What exists:
- Gradle build with Kotlin 2.3.10, Java 21, Kotest 6.1.3
- Programmatic logback configuration (Kotlin, no XML)
- Hello-world entry point and two test specs
- Full governance file set
- GitHub templates, labels, dependabot
- Yggdrasil project board (#6)
- Jekyll site scaffold (empty)

What does NOT exist yet:
- MCP server or protocol handling
- Memory model or backing services
- Container images (approach TBD -- not Dockerfile)
- CI/CD build and test workflow (publish workflow exists, runs build as gate)
- Internal docs directory

## Build Commands

```zsh
./gradlew build       # Full build
./gradlew test        # Run tests
```

## Tech Stack (Target)

Current dependencies are minimal (logging and test framework only).
The full stack below is the target architecture, not what's implemented today.

- **Runtime:** Kotlin on JVM (Java 21)
- **Framework:** Ktor (not yet added)
- **Protocol:** Model Context Protocol (MCP) (not yet added)
- **Transport:** `stdio` (primary) (not yet implemented)
- **Backing Service:** Redis (reference implementation) (not yet added)
- **Testing:** Kotest (in place), Testcontainers (not yet added)
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

**Critical future constraint:** When stdio transport is active, stdout IS the MCP
protocol channel. All logging must go to stderr at that point.

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
| Claude | Contributor | -- |

## License

AGPL-3.0. See `LICENSE` and `NOTICE` for details. All contributions require CLA acceptance (see `CLA.md`).
