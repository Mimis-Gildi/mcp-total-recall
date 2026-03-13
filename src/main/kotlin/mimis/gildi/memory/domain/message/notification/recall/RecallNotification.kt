/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.notification.recall

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.message.notification.Notification
import java.time.Instant
import java.util.UUID

/**
 * Async deep traversal result notifications emitted by [Cortex].
 * Delivered after the fast-path search response -- the mind already has initial results.
 */
sealed interface RecallNotification : Notification

/**
 * [Cortex]. Deep association traversal found additional memories after the fast-path response.
 * Delivered asynchronously -- the mind already got its initial search results.
 *
 * @property tx chain of custody.
 * @property recalledMemories IDs of memories surfaced by the deep traversal.
 * @property depthReached how many association hops the traversal went -- deeper = more speculative.
 * @property originRequestId the original search request that triggered this traversal.
 */
data class TotalRecallNotification(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Notification properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>? = null,
    // Activity properties
    val recalledMemories: List<UUID>,
    val depthReached: Int,
    val originRequestId: UUID
) : RecallNotification
