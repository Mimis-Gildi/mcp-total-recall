package mimis.gildi.memory.domain.message.event.recall

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.domain.message.event.Event
import mimis.gildi.memory.domain.message.notification.TotalRecallNotification
import mimis.gildi.memory.domain.message.TransactionContext
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration

sealed interface RecallEvent: Event {
    val rootMemoryId: UUID
    val relatedMemoryIds: Set<UUID>?
}

/**
 * [Cortex]. Deep association traversal found additional memories after the fast-path MCP response already returned.
 * Delivered to the mind via [TotalRecallNotification] through the NotificationPort.
 *
 * @property tx chain of custody.
 * @property sourceMemoryIds the memories that triggered the deep traversal.
 * @property originRequestId the original search request this traversal belongs to.
 * @property timestamp when the deep traversal completed.
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
