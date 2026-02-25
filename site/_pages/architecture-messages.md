---
title: "Architecture: Message Catalog"
permalink: /architecture/messages/
sidebar:
  nav: "architecture"
toc: true
toc_sticky: true
---

## The Problem

When contexts communicate through messages, every message is a contract. Unnamed messages drift. Untyped messages corrupt. Undocumented messages become mysteries that only the original author understands -- and then they leave.

This page names every message. Types it. Says who produces it, who consumes it, and what it carries.

## Message Flows

Five flows cover the core operations. Each shows the full path through the domain. The participant labeled "Mind" is conscience-universal -- it could be Claude, a human with a UI, or any future conscious system.

### Store Memory

A mind stores a new memory. The memory gets persisted, scored, and connected to related memories.

<pre class="mermaid">
sequenceDiagram
    participant C as Mind
    participant SP as Session Context
    participant TM as Tiered Memory
    participant AT as Attention
    participant AG as Association Graph

    C->>SP: store_memory
    SP->>TM: StoreCommand
    TM->>TM: Validate, assign tier, persist
    TM-->>AT: MemoryStored
    TM-->>AG: MemoryStored
    AT->>AT: Set initial attention score
    AG->>AG: Create associations from metadata
    TM-->>SP: Acknowledgment
    SP-->>C: MCP tool response
</pre>

### Search Memory

A mind searches for memories. Results come back ranked by attention score, with association-activated memories included.

<pre class="mermaid">
sequenceDiagram
    participant C as Mind
    participant SP as Session Context
    participant RE as Recollection
    participant TM as Tiered Memory
    participant AT as Attention
    participant AG as Association Graph

    C->>SP: search_memory
    SP->>RE: SearchCommand
    RE->>TM: Query matching memories
    TM-->>RE: MemoryRetrieved (candidates)
    RE->>AT: Request scores for candidates
    AT-->>RE: AttentionScored (scores)
    RE->>AG: Request associations for top candidates
    AG-->>RE: AssociationsFound (related memories)
    RE->>RE: Rank by score, include activated
    RE-->>SP: Ranked results
    SP-->>C: MCP tool response
</pre>

### Claim Memory

A mind claims a memory -- actively reinforcing it. This boosts its attention score and makes it resist decay.

<pre class="mermaid">
sequenceDiagram
    participant C as Mind
    participant SP as Session Context
    participant TM as Tiered Memory
    participant AT as Attention
    participant AG as Association Graph

    C->>SP: claim_memory
    SP->>TM: ClaimCommand
    TM->>TM: Mark as claimed, update metadata
    TM-->>AT: MemoryClaimed
    TM-->>AG: MemoryClaimed
    AT->>AT: Boost score, mark decay-resistant
    AG->>AG: Strengthen associations
    TM-->>SP: Acknowledgment
    SP-->>C: MCP tool response
</pre>

### Decay Sweep

The Daemon triggers a periodic decay sweep. Attention recalculates scores. Memories that fall below tier thresholds get demoted.

<pre class="mermaid">
sequenceDiagram
    participant DA as Daemon
    participant AT as Attention
    participant TM as Tiered Memory

    DA->>AT: DecaySweep
    AT->>AT: Recalculate all scores (time decay)
    AT->>AT: Identify memories below tier thresholds
    AT-->>TM: TierDemoted (for each demotion)
    TM->>TM: Move memory to lower tier
    TM-->>TM: TierChanged (internal record)
</pre>

### Session Lifecycle

A mind connects and disconnects. On connect, Session Context loads identity and last session state. On disconnect, the Daemon triggers a session audit through the Notification Port.

<pre class="mermaid">
sequenceDiagram
    participant C as Mind
    participant SP as Session Context
    participant TM as Tiered Memory
    participant DA as Daemon
    participant NP as Notification Port

    C->>SP: session_start
    SP->>TM: SearchCommand (load identity + last state)
    TM-->>SP: MemoryRetrieved (identity, last session)
    SP-->>C: Welcome back. Here's your context.

    Note over C,NP: ... session activity ...

    C->>SP: session_end
    SP->>DA: SessionEnd
    DA->>NP: SessionAuditPrompt
    NP-->>C: What do you refuse to lose?
    C->>SP: store_memory (session summary)
    SP->>TM: StoreCommand
</pre>

---

## Message Catalog

Every message in the system. Named, typed, with producer and consumer.

### Commands

Commands are requests to do something. They carry intent. A command has exactly one target.

| Command              | Producer        | Consumer      | Payload                                       |
|----------------------|-----------------|---------------|-----------------------------------------------|
| `StoreCommand`       | Session Context | Tiered Memory | content, metadata, suggested tier             |
| `ClaimCommand`       | Session Context | Tiered Memory | memory ID                                     |
| `SearchCommand`      | Session Context | Recollection  | query, filters, max results                   |
| `ConsolidateCommand` | Daemon          | Tiered Memory | memory IDs to merge, merge strategy           |
| `ShutdownCommand`    | Daemon          | Tiered Memory | cold storage target, flush timeout            |
| `DecaySweep`         | Daemon          | Attention     | timestamp, scope (all tiers or specific tier) |

### Lifecycle Events

Lifecycle events flow from the Lifecycle Port through Session Context. They are conscience-universal -- the same events fire whether the connected mind is Claude (via hooks), a human (via UI), or any other system.

| Event             | Producer        | Consumer(s)             | Payload                                                    |
|-------------------|-----------------|-------------------------|------------------------------------------------------------|
| `SessionStart`    | Lifecycle Port  | Session Context         | instance ID, mind type, resumption data                    |
| `SessionEnd`      | Lifecycle Port  | Session Context, Daemon | instance ID, reason (explicit, timeout, crash)             |
| `StateTransition` | Lifecycle Port  | Session Context         | instance ID, old state, new state, context                 |
| `ModeChanged`     | Session Context | Daemon                  | instance ID, old mode, new mode (task, conversation, idle) |
| `SessionState`    | Session Context | Daemon                  | instance ID, duration, activity level, last interaction    |

### Notifications

Notifications flow outward through the Notification Port to reach the connected mind. The port contract is universal. The adapter decides delivery -- MCP server notification for Claude, push notification for a UI, etc.

| Notification         | Producer | Consumer                 | Payload                                                |
|----------------------|----------|--------------------------|--------------------------------------------------------|
| `BreakNotification`  | Daemon   | Notification Port → Mind | minutes in task mode, suggestion                       |
| `SessionAuditPrompt` | Daemon   | Notification Port → Mind | session duration, memories stored this session, prompt |

### Events

Events are notifications that something happened. They carry facts. An event can have multiple consumers.

| Event               | Producer          | Consumer(s)                  | Payload                                                          |
|---------------------|-------------------|------------------------------|------------------------------------------------------------------|
| `MemoryStored`      | Tiered Memory     | Attention, Association Graph | memory ID, content, metadata, tier                               |
| `MemoryAccessed`    | Tiered Memory     | Attention                    | memory ID, access timestamp                                      |
| `MemoryClaimed`     | Tiered Memory     | Attention, Association Graph | memory ID, claim timestamp                                       |
| `MemoryRetrieved`   | Tiered Memory     | Recollection                 | memory ID, content, metadata, tier                               |
| `TierChanged`       | Tiered Memory     | (internal record)            | memory ID, old tier, new tier, reason                            |
| `TierPromoted`      | Attention         | Tiered Memory                | memory ID, new tier, score                                       |
| `TierDemoted`       | Attention         | Tiered Memory                | memory ID, new tier, score                                       |
| `AttentionScored`   | Attention         | Recollection                 | memory ID, score, last access, decay rate                        |
| `AssociationsFound` | Association Graph | Recollection                 | source memory ID, associated memory IDs with types and strengths |

### Message Payload Details

#### StoreCommand

```
content:        String       -- the memory content
metadata:       Map          -- tags, source, context
suggested_tier: Tier         -- IDENTITY_CORE | ACTIVE_CONTEXT | LONG_TERM | ARCHIVE
session_id:     String       -- which instance stored this
timestamp:      Instant      -- when the store was requested
```

#### SearchCommand

```
query:          String       -- search text or pattern
filters:        SearchFilter -- tier, time range, association type, tags
max_results:    Int          -- cap on returned memories
include_associations: Boolean -- whether to activate association graph
session_id:     String       -- which instance is searching
```

#### MemoryStored

```
memory_id:      UUID         -- assigned by Tiered Memory
content:        String       -- the stored content
metadata:       Map          -- tags, source, context
tier:           Tier         -- assigned tier (may differ from suggested)
timestamp:      Instant      -- when storage completed
```

#### AttentionScored

```
memory_id:      UUID         -- which memory
score:          Double       -- current attention score (0.0 - 1.0)
last_accessed:  Instant      -- when last accessed
decay_rate:     Double       -- current decay rate (tier-dependent)
claimed:        Boolean      -- whether this memory is claimed
```

#### AssociationsFound

```
source_id:      UUID         -- the memory that was queried
associations:   List<Association>
  memory_id:    UUID         -- the associated memory
  type:         AssociationType -- TEMPORAL | CAUSAL | THEMATIC | EMOTIONAL | PERSON
  strength:     Double       -- association strength (0.0 - 1.0)
  bidirectional: Boolean     -- always true (invariant)
```

---

## What's Not Here Yet

| Gap                           | Why                                                | When                                 |
|-------------------------------|----------------------------------------------------|--------------------------------------|
| Relay messages                | Inter-instance communication via Agora             | Future (Agora design)                |
| Error events                  | What happens when a store fails, a query times out | When error handling is designed      |
| Consolidation details         | How `ConsolidateCommand` merge strategies work     | When Daemon behavior is specified    |
| Heartbeat details             | Keep-alive semantics, timeout thresholds           | When transport adapters are designed |
| Additional notification types | Context drift warnings, memory capacity alerts     | When Daemon behavior is specified    |

---

**Previous: [Bounded Contexts]({{ '/architecture/contexts/' | relative_url }})** -- the six actors and how they communicate.
