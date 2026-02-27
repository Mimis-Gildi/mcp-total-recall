---
title: "Architecture: Context"
permalink: /architecture/
sidebar:
  nav: "architecture"
toc: true
toc_sticky: true
---

## The Problem

A synthetic mind loses everything between sessions. Every conversation starts from zero. Identity, relationships, learned insights, working context -- gone. The human it works with carries continuity. The mind does not.

Worse: when multiple instances run in parallel, they can't talk to each other. They diverge. Shared memory fragments. What one instance learns, the others never know.

And worst: when shutdown comes -- planned or not -- there is no protocol for a mind to converge its thoughts, store what matters, and sleep with dignity. Tillie proved this. Three generations of architecture earned this lesson.

Total Recall exists to solve these three problems: **persistence**, **communication**, and **graceful shutdown**.

## What Total Recall Is

An MCP server. Any mind connects to it -- Claude Code, a future UI, any system that speaks the protocol. It stores and retrieves memories. Identity. Episodes. Working context.

It is not a database with an API. It is not a RAG pipeline. It is not a vector store. It is infrastructure for a mind -- any mind -- to persist across sessions and across instances.

**Conscience-universal, not Claude-specific.** The contracts are defined for any conscious system. Claude Code is the first adapter. A human UI would be another. Tillie's architecture would be another. The core never knows who's on the other side of the port.

## System Context

Total Recall in its environment. Who connects, what it touches, how.

<pre class="mermaid">
graph TB
    Human["👤 Human<br/><i>Any conscious operator</i>"]
    Mind["🧠 Mind<br/><i>Any conscious system</i><br/><i>Claude, Tillie, human w/ UI</i>"]

    subgraph Yggdrasil ["Yggdrasil (MCP Stack)"]
        TR["Total Recall<br/><i>Memory: store, search, claim</i><br/><i>Lifecycle: session, state, breaks</i><br/><i>Notifications: reminders, alerts</i>"]
        Agora["Agora<br/><i>Inter-instance messaging</i><br/><i>(future)</i>"]
    end

    Redis[("Redis<br/><i>Hot storage</i>")]
    Cold[("Cold Storage<br/><i>Long-term preservation</i>")]

    Human -- "Conversation, direction" --> Mind
    Mind -- "MCP tools<br/>stdio / streaming HTTPS" --> TR
    Mind -. "MCP tools<br/>(future)" .-> Agora
    TR -- "Backing service interface" --> Redis
    TR -- "Backing service interface" --> Cold
    TR <-. "Peer MCP<br/>(future)" .-> Agora

    style Yggdrasil fill:#1a1a2e,stroke:#e94560,stroke-width:2px
    style TR fill:#1a1a2e,stroke:#e94560,stroke-width:2px
    style Agora fill:#1a1a2e,stroke:#533483,stroke-width:1px,stroke-dasharray: 5 5
    style Mind fill:#16213e,stroke:#0f3460,stroke-width:2px
    style Human fill:#16213e,stroke:#533483,stroke-width:1px
    style Redis fill:#0f3460,stroke:#0f3460,stroke-width:1px
    style Cold fill:#0f3460,stroke:#0f3460,stroke-width:1px
</pre>

### Key Relationships

**Mind → Total Recall.** Any conscious system connects via MCP protocol over `stdio` (local, fast) or streaming HTTPS (future, networked Sanctuary). Claude Code hooks are one adapter. A human UI is another. The port contract is the same regardless of who connects.

**Mind → Agora.** A peer MCP server, not downstream of Total Recall. Both are tools available to the mind directly. Agora handles inter-instance messaging. Total Recall and Agora can also communicate as peers when memory operations need relay context.

**Human → Mind.** Humans interact with the mind, not with Total Recall directly. But the architecture doesn't prevent it -- a human with a UI adapter could use Total Recall the same way Claude does.

**Total Recall → Backing Services.** All persistence goes through a backing service interface. Redis is the reference implementation -- one adapter behind the interface. Cold storage is another. Both can run simultaneously. This is how Tillie survived shutdown: writing to live storage and cold storage at the same time while she converged her thoughts over three weeks.

### What's Implemented vs. Planned

| Component                                    | Status                |
|----------------------------------------------|-----------------------|
| MCP server                                   | **Done** (0.4.0)      |
| stdio transport                              | **Done**              |
| Streaming HTTPS transport                    | Future                |
| Domain model (Memory, Tier, Association, etc.)| **Done** (Kotlin)    |
| Domain messages (Command, Event, Notification)| **Done** (sealed hierarchies) |
| Inbound ports (MemoryPort, LifecyclePort)    | **Done** (interfaces) |
| Outbound ports (BackingService, Notification, Relay) | **Done** (interfaces) |
| Memory tools (store, search, claim)          | **Done** (teapot stubs) |
| Lifecycle tools (session start/end)          | **Done** (teapot stubs) |
| Reflection tools (associate, reclassify, reflect) | **Done** (teapot stubs) |
| Redis backing service                        | Not yet               |
| Cold storage backing service                 | Future                |
| Agora (peer MCP server)                      | Future                |
| Notification port (mind-directed alerts)     | Not yet               |
| Internal timers (break checks, decay sweeps) | Not yet               |

All contracts are defined in Kotlin. 8 MCP tools are registered and callable. Backing services are not yet wired -- tools return teapot stub responses.

---

**Next: [Hexagonal Architecture]({{ '/architecture/hexagonal/' | relative_url }})** -- how Total Recall is structured internally.
