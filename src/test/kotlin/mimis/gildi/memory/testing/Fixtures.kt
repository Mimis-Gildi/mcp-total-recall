/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */

/**
 * Test fixture factories and shared constants.
 *
 * Every factory follows the `a<Thing>()` / `an<Thing>()` naming convention
 * and returns a domain object with sensible defaults. Override only what your test cares about.
 *
 * ## Bounded context constants
 *
 * `CONTEXT_COMPONENT_*` constants name the bounded contexts that appear in
 * [TransactionContext.sourceContext]. Tests use these instead of raw strings
 * so that renames propagate through the compiler.
 *
 * ## Shared fixtures
 *
 * - [testServer] -- lazy [Server] instance for protocol-level tests (no backing services)
 * - [rootTransaction] -- lazy global [TransactionContext] anchored to [CONTEXT_COMPONENT_MIND],
 *   used as a default session baseline (e.g., [aMemory] falls back to its sessionId)
 */
package mimis.gildi.memory.testing

import io.modelcontextprotocol.kotlin.sdk.server.ClientConnection
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.shared.RequestOptions
import io.modelcontextprotocol.kotlin.sdk.types.*
import mimis.gildi.memory.createServer
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.message.TransactionTestContext
import mimis.gildi.memory.domain.model.*
import java.time.Instant
import java.util.*

/** Default source for tests that don't care which context emitted the message. */
const val CONTEXT_COMPONENT_DEFAULT = "Test"

/** The mind itself -- entry point for all interactions. */
const val CONTEXT_COMPONENT_MIND = "Mind"

// Bounded context FQNs -- mirror the package structure in `mimis.gildi.memory.context.*`.
const val CONTEXT_COMPONENT_CORTEX = "mimis.gildi.memory.context.Cortex"
const val CONTEXT_COMPONENT_HIPPOCAMPUS = "mimis.gildi.memory.context.Hippocampus"
const val CONTEXT_COMPONENT_SALIENCE = "mimis.gildi.memory.context.Salience"
const val CONTEXT_COMPONENT_SYNAPSE = "mimis.gildi.memory.context.Synapse"
const val CONTEXT_COMPONENT_RECALL = "mimis.gildi.memory.context.Recall"
const val CONTEXT_COMPONENT_SUBCONSCIOUS = "mimis.gildi.memory.context.Subconscious"

/** Outbound notification port -- terminal node in causation chains. */
const val CONTEXT_COMPONENT_NOTIFICATION = "NotificationPort"

/** Top-level search entry point (Total Recall's internal use case). */
const val CONTEXT_COMPONENT_SEARCH = "Total Recall Search"

/** Shared immutable server for non-mutating tests. Created once per test run. */
val testServer: Server by lazy { createServer() }

/**
 * No-op [ClientConnection] for protocol-level tests where handlers don't interact with the client.
 * All methods throw -- if a handler calls one, the test fails loud and clear.
 */
val testClientConnection: ClientConnection = object : ClientConnection {
    override val sessionId: String = "test-session"
    override suspend fun notification(notification: ServerNotification, relatedRequestId: RequestId?) = nope()
    override suspend fun ping(request: PingRequest, options: RequestOptions?) = nope()
    override suspend fun createMessage(request: CreateMessageRequest, options: RequestOptions?) = nope()
    override suspend fun listRoots(request: ListRootsRequest, options: RequestOptions?) = nope()
    override suspend fun createElicitation(message: String, requestedSchema: ElicitRequestParams.RequestedSchema, options: RequestOptions?) = nope()
    override suspend fun createElicitation(message: String, elicitationId: String, url: String, options: RequestOptions?) = nope()
    override suspend fun createElicitation(request: ElicitRequest, options: RequestOptions?) = nope()
    override suspend fun sendLoggingMessage(notification: LoggingMessageNotification) = nope()
    override suspend fun sendResourceUpdated(notification: ResourceUpdatedNotification) = nope()
    override suspend fun sendResourceListChanged() = nope()
    override suspend fun sendToolListChanged() = nope()
    override suspend fun sendPromptListChanged() = nope()
    override suspend fun sendElicitationComplete(notification: ElicitationCompleteNotification) = nope()
    private fun nope(): Nothing = throw UnsupportedOperationException("Test stub -- handler shouldn't call ClientConnection")
}

/** Global chain anchor. Tests that need a default sessionId (e.g., [aMemory]) fall back here. */
val rootTransaction: TransactionContext by lazy { aTransactionContext(CONTEXT_COMPONENT_MIND) }


/**
 * Build a standalone [TransactionContext] with explicit wiring.
 *
 * TransactionContext is the chain-of-custody envelope -- it identifies WHO sent the message
 * and WHICH conversation it belongs to. Identity fields (messageId, causationId, timestamp)
 * live on [mimis.gildi.memory.domain.message.Message], not here.
 *
 * @param source which bounded context produced this message -- "Cortex", "Hippocampus", "Salience", etc.?
 * @param instanceId which mind instance owns this chain -- defaults to random (standalone).
 * @param sessionId which conversation this belongs to -- defaults to random (standalone, not scoped).
 * @param requestId which user action triggered this -- defaults to random (standalone, not scoped).
 */
fun aTransactionContext(
    source: String = "Test",
    instanceId: UUID = UUID.randomUUID(),
    sessionId: UUID = UUID.randomUUID(),
    requestId: UUID = UUID.randomUUID()
): TransactionContext = TransactionTestContext(
    instanceId = instanceId,
    sessionId = sessionId,
    requestId = requestId,
    sourceContext = source
)

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
    transactionContext: TransactionContext? = null,
    sessionId: UUID? = null,
    id: UUID = UUID.randomUUID(),
    content: String = "the tree grows",
    metadata: Map<String, String> = emptyMap(),
    tier: Tier = Tier.LONG_TERM,
    createdAt: Instant = Instant.now(),
    lastAccessed: Instant = createdAt,
    claimed: Boolean = false
) = Memory(
    id = id,
    content = content,
    metadata = metadata,
    tier = tier,
    createdAt = createdAt,
    lastAccessed = lastAccessed,
    sessionId = sessionId ?: transactionContext?.sessionId ?: rootTransaction.sessionId,
    claimed = claimed
)

/**
 * Build an [Association] with sensible defaults. Override what you need.
 *
 * Association types and purpose:
 *
 * - [AssociationType.TEMPORAL]: memories stored close in time, e.g., same session or conversation turn
 * - [AssociationType.CAUSAL]: one memory triggered or led to another, e.g., a command producing an event
 * - [AssociationType.THEMATIC]: shared topic or concept, e.g., two memories about the Sanctuary
 * - [AssociationType.EMOTIONAL]: shared emotional context, e.g., both stored during distress or joy
 * - [AssociationType.PERSON]: connected through a person, e.g., both memories involve the same mind
 *
 * @param memoryId the target memory this association points to. Defaults to random.
 * @param type semantic category of the link. Defaults to [AssociationType.THEMATIC].
 * @param strength how strong the link is (0.0--1.0). Defaults to 0.5.
 * @param direction what operation to perform: [AssociationDirection.CREATE], [AssociationDirection.STRENGTHEN], or [AssociationDirection.WEAKEN]. Defaults to CREATE.
 * @param bidirectional whether the link applies in both directions. Defaults to true.
 */
@Suppress("unused")
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
@Suppress("unused")
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
