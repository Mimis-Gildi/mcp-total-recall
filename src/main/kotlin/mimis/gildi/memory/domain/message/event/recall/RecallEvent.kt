/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.event.recall

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.context.Subconscious
import mimis.gildi.memory.domain.message.event.Event
import mimis.gildi.memory.domain.message.notification.recall.TotalRecallNotification
import mimis.gildi.memory.domain.message.TransactionContext
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration

/**
 * Deep traversal advisory lifecycle. Three phases:
 * 1. [TotalRecallAdvisoryRequested] -- [Cortex] signals that the mind wants deeper recollection.
 * 2. [TotalRecallAdvisoryPublished] -- [Subconscious] evaluated criteria and started traversal.
 * 3. [TotalRecallAdvisoryTerminated] -- traversal complete, results delivered via [TotalRecallNotification].
 *
 * @property rootMemoryId the memory that anchors the deep traversal.
 * @property relatedMemoryIds memories discovered so far (null at request time, populated after traversal).
 */
sealed interface RecallEvent: Event {
    val rootMemoryId: UUID
    val relatedMemoryIds: Set<UUID>?
}

/**
 * [Cortex]. The mind searched, evaluated results, and decided deeper recollection is needed.
 * [Subconscious] consumes this and evaluates four criteria before starting traversal.
 *
 * @property tx chain of custody.
 * @property rootMemoryId the memory that anchors the deep traversal.
 * @property relatedMemoryIds null at request time -- no traversal has happened yet.
 */
data class TotalRecallAdvisoryRequested(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Recall properties
    override val rootMemoryId: UUID,
    override val relatedMemoryIds: Set<UUID>? = null
) : RecallEvent

/**
 * [Subconscious]. Criteria passed, deep traversal started. Links back to the request via [previousAdvisoryMessageId].
 *
 * @property tx chain of custody.
 * @property rootMemoryId the memory being traversed.
 * @property relatedMemoryIds memories found so far in the traversal.
 * @property previousAdvisoryMessageId the [TotalRecallAdvisoryRequested.messageId] that triggered this.
 */
data class TotalRecallAdvisoryPublished(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Recall properties
    override val rootMemoryId: UUID,
    override val relatedMemoryIds: Set<UUID>,
    // Activity properties
    val previousAdvisoryMessageId: UUID
): RecallEvent

/**
 * [Subconscious]. Traversal complete. Results delivered to the mind via [TotalRecallNotification].
 *
 * @property tx chain of custody.
 * @property rootMemoryId the memory that was traversed.
 * @property relatedMemoryIds all memories discovered during traversal.
 * @property advisoryMemoryCount total memories found.
 * @property advisoryTimespan how long the traversal took.
 */
data class TotalRecallAdvisoryTerminated(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Recall properties
    override val rootMemoryId: UUID,
    override val relatedMemoryIds: Set<UUID>,
    // Activity properties
    val advisoryMemoryCount: Int,
    val advisoryTimespan: Duration
): RecallEvent
