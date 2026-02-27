---
title: "Architecture Modeling Complete"
categories: [announcements]
tags: [hexagonal, bounded-contexts, messages, milestone]
excerpt: >
    Issue #3 merged. Four architecture pages document the hexagonal design, bounded contexts, and message catalog.
classes: wide
comments: true
hidden: false
search: true
---

Issue #3 is merged. Total Recall has its architectural foundation.

## What Was Built

Four architecture pages now document the system design:

- **[System Context](/mcp-total-recall/architecture/)** -- what Total Recall is, who connects, how it fits into Yggdrasil
- **[Hexagonal Architecture](/mcp-total-recall/architecture/hexagonal/)** -- ports and adapters, the actor model, cross-cut concerns
- **[Bounded Contexts](/mcp-total-recall/architecture/contexts/)** -- six actors (Tiered Memory, Association Graph, Attention, Recollection, Session Context, Daemon) and their message flows
- **[Message Catalog](/mcp-total-recall/architecture/messages/)** -- every command, event, and notification named and typed

All diagrams are live Mermaid -- rendered in the browser, not static images. Sequence diagrams trace the full path through the domain for store, search, claim, decay sweep, session lifecycle, and reflect operations.

## Key Decisions

| Decision                   | Rationale                                                                                |
|----------------------------|------------------------------------------------------------------------------------------|
| Hexagonal architecture     | Generations 1 and 3v1 failed from coupling. Ports and adapters prevent this.             |
| Actor model internally     | Isolation, message passing, no shared mutable state. Each bounded context owns its data. |
| Memory as aggregate root   | All writes go through Tiered Memory. No other context persists directly.                 |
| Conscience-universal ports | Contracts are defined for any mind, not Claude specifically.                             |
| SSE rejected               | Gen 3v1 mistake. stdio primary, streaming HTTPS secondary.                               |

## What's Next

Issue #4: port these designs into Kotlin. Interfaces, domain types, sealed message hierarchies. Code that matches the diagrams.
