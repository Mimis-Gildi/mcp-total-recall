package mimis.gildi.memory.domain.message.event.memory

import mimis.gildi.memory.context.Recall
import mimis.gildi.memory.context.Synapse
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.Association
import java.time.Instant
import java.util.UUID

/**
 * Association discovery events emitted by [Synapse].
 * Consumed by [Recall] for inclusion in search results.
 *
 * @property associations the discovered links -- each carries type, strength, and direction.
 */
sealed interface MemorySynapseEvent: MemoryEvent {
    val associations: List<Association>
}

/**
 * [Synapse]. Association traversal complete. Returned to [Recall] for inclusion in search results.
 *
 * @property tx chain of custody.
 * @property sourceId the memory whose associations were traversed.
 * @property associations the discovered links -- each carries type, strength, and direction.
 */
data class AssociationsFound(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Synapse properties
    override val memoryId: UUID,
    override val associations: List<Association>,
    // Activity properties
    val sourceId: UUID,
) : MemorySynapseEvent

