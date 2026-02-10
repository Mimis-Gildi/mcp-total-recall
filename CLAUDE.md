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

## Build Commands

```zsh
./gradlew build       # Full build
./gradlew test        # Run tests
```

## Tech Stack

- **Runtime:** Kotlin on JVM (Java 21)
- **Framework:** Ktor
- **Protocol:** Model Context Protocol (MCP)
- **Transport:** `stdio` (primary)
- **Backing Service:** Redis (reference implementation)
- **Testing:** Kotest, Testcontainers
- **Build:** Gradle (Kotlin DSL)
- **Tooling:** SDK Manager (`.sdkmanrc`) for version management

## Key Configuration Files

| File | Purpose |
|------|---------|
| `build.gradle.kts` | Gradle build configuration |
| `gradle/libs.versions.toml` | Version catalog for dependencies |
| `.sdkmanrc` | SDK Manager tool versions |

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
