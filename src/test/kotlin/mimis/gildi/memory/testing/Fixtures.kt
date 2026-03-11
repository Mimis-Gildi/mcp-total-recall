/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.testing

import io.modelcontextprotocol.kotlin.sdk.server.Server
import mimis.gildi.memory.createServer
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.*
import java.time.Instant
import java.util.UUID

/** Shared immutable server for non-mutating tests. Created once per test run. */
val testServer: Server by lazy { createServer() }

/*
 * Domain object factories. Sensible defaults override what you need.
 *
 *   val tx = aTx(source = "Hippocampus", causationId = rootCause)
 *   val m = aMemory(tier = Tier.IDENTITY_CORE, claimed = true)
 *   val s = aSalienceScore(score = 0.95, decayRate = 0.01)
 *
 * Memoization is scoped, not global. Use TxChain to scope a conversation:
 *
 *   describe("Hippocampus events") {
 *       val chain = TxChain(source = "Hippocampus")
 *       it("stores") { MemoryStored(tx = chain.next(), ...) }
 *       it("accesses") { MemoryAccessed(tx = chain.next(), ...) }  // chained
 *   }
 *
 * Each TxChain owns its session, request, and causation chain.
 * Kotest scoping (describe/context blocks) is the natural boundary.
 *
 * For standalone transactions where you wire everything yourself, use aTx().
 * causationId has no default -- you must understand what caused this message:
 *
 *   - Root cause (nothing caused me): causationId = messageId, or use rootCauseTx()
 *   - Reaction (caused by a prior message): causationId = priorTx.messageId
 *   - Shape test (causation irrelevant): causationId = UUID.randomUUID()
 */

/**
 * Build a standalone [TransactionContext] with explicit wiring.
 *
 * @param source which bounded context produced this message -- "Cortex", "Hippocampus", "Salience", etc.?
 * @param causationId the messageId of the message that caused this one. Required -- you must know the cause.
 *   Root cause: use your own messageId (or call [rootCauseTx]). Reaction: use the prior message's messageId.
 *   Shape test where causation is irrelevant: pass `UUID.randomUUID()`.
 * @param sessionId which conversation this belongs to -- defaults to random (standalone, not scoped).
 * @param requestId which user action triggered this -- defaults to random (standalone, not scoped).
 * @param messageId unique identity of THIS message -- always fresh, never reuse.
 * @param timestamp when this message was created.
 */
fun aTx(
    source: String = "Test",
    causationId: UUID,
    sessionId: UUID = UUID.randomUUID(),
    requestId: UUID = UUID.randomUUID(),
    messageId: UUID = UUID.randomUUID(),
    timestamp: Instant = Instant.now()
) = TransactionContext(
    sessionId = sessionId,
    requestId = requestId,
    messageId = messageId,
    causationId = causationId,
    timestamp = timestamp,
    sourceContext = source
)

/**
 * Convenience for root-cause transactions -- `causationId` equals `messageId`.
 * "Nothing caused me. I'm the origin."
 */
fun rootCauseTx(
    source: String = "Test",
    sessionId: UUID = UUID.randomUUID(),
    requestId: UUID = UUID.randomUUID(),
    timestamp: Instant = Instant.now()
): TransactionContext {
    val messageId = UUID.randomUUID()
    return TransactionContext(
        sessionId = sessionId,
        requestId = requestId,
        messageId = messageId,
        causationId = messageId,
        timestamp = timestamp,
        sourceContext = source
    )
}

/**
 * Scoped causation chain factory. Each instance IS a conversation.
 *
 * Owns its own [sessionId] and [requestId]. Each [next] call produces
 * a new message whose causationId points to the previous message's messageId.
 * The first call is a root cause (causationId == messageId).
 *
 * @property sessionId this conversation's session -- unique per chain instance.
 * @param source which bounded context this chain represents.
 */
class TxChain(
    val sessionId: UUID = UUID.randomUUID(),
    private val source: String = "Test"
) {
    val requestId: UUID = UUID.randomUUID()
    private var lastMessageId: UUID? = null

    /**
     * Produce the next [TransactionContext] in the chain.
     * First call: root cause (causationId == messageId).
     * Subsequent calls: causationId == previous message's messageId.
     */
    fun next(
        timestamp: Instant = Instant.now()
    ): TransactionContext {
        val messageId = UUID.randomUUID()
        val causationId = lastMessageId ?: messageId
        lastMessageId = messageId
        return TransactionContext(
            sessionId = sessionId,
            requestId = requestId,
            messageId = messageId,
            causationId = causationId,
            timestamp = timestamp,
            sourceContext = source
        )
    }
}

/**
 * Build a [Memory] with sensible defaults. Override what you need.
 *
 * @param id unique identity -- defaults to random. Pin it when testing retrieval or association by ID.
 * @param content the stored text. Defaults to "the tree grows" -- a phrase from the Sanctuary.
 * @param metadata key-value pairs attached by the mind at store time. Defaults to empty.
 * @param tier where this memory lives in the hierarchy. Defaults to [Tier.LONG_TERM].
 * @param createdAt when the memory was first stored. Defaults to now.
 * @param lastAccessed when the memory was last read -- [mimis.gildi.memory.context.Salience] uses this for decay. Defaults to [createdAt].
 * @param sessionId which session produced this memory? Defaults to random (standalone).
 * @param claimed whether the mind has actively claimed this memory. Claimed memories resist decay.
 */
fun aMemory(
    id: UUID = UUID.randomUUID(),
    content: String = "the tree grows",
    metadata: Map<String, String> = emptyMap(),
    tier: Tier = Tier.LONG_TERM,
    createdAt: Instant = Instant.now(),
    lastAccessed: Instant = createdAt,
    sessionId: UUID = UUID.randomUUID(),
    claimed: Boolean = false
) = Memory(
    id = id,
    content = content,
    metadata = metadata,
    tier = tier,
    createdAt = createdAt,
    lastAccessed = lastAccessed,
    sessionId = sessionId,
    claimed = claimed
)

/**
 * Build an [Association] with sensible defaults. Override what you need.
 *
 * @param memoryId the target memory this association points to. Defaults to random.
 * @param type semantic category of the link. Defaults to [AssociationType.THEMATIC].
 * @param strength how strong the link is (0.0--1.0). Defaults to 0.5.
 * @param direction what operation to perform: [AssociationDirection.CREATE], STRENGTHEN, or WEAKEN. Defaults to CREATE.
 * @param bidirectional whether the link applies in both directions. Defaults to true.
 */
fun anAssociation(
    memoryId: UUID = UUID.randomUUID(),
    type: AssociationType = AssociationType.THEMATIC,
    strength: Double = 0.5,
    direction: AssociationDirection = AssociationDirection.CREATE,
    bidirectional: Boolean = true
) = Association(
    memoryId = memoryId,
    type = type,
    strength = strength,
    direction = direction,
    bidirectional = bidirectional
)

/**
 * Build a [SalienceScore] with sensible defaults. Override what you need.
 *
 * @param memoryId the memory this score belongs to. Defaults to random.
 * @param score current salience (0.0--1.0). Defaults to 0.5. Crosses promotion/demotion thresholds to trigger tier changes.
 * @param lastAccessed when the memory was last read -- decay is calculated relative to this. Defaults to now.
 * @param decayRate how fast the score drops per time unit. Defaults to 0.05.
 */
fun aSalienceScore(
    memoryId: UUID = UUID.randomUUID(),
    score: Double = 0.5,
    lastAccessed: Instant = Instant.now(),
    decayRate: Double = 0.05
) = SalienceScore(
    memoryId = memoryId,
    score = score,
    lastAccessed = lastAccessed,
    decayRate = decayRate
)
