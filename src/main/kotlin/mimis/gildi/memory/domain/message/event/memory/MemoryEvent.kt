package mimis.gildi.memory.domain.message.event.memory

import mimis.gildi.memory.domain.message.event.Event
import java.util.UUID

/**
 * Events about a specific memory. Every sub-hierarchy identifies the
 * memory via [memoryId].
 *
 * Sub-hierarchies:
 * - [MemoryHippocampusEvent] -- storage lifecycle (stored, accessed, claimed, retrieved, tier changed, reclassified).
 * - [MemorySalienceEvent] -- scoring recommendations (promotion, demotion, score change).
 * - [MemorySynapseEvent] -- association discovery results.
 *
 * @property memoryId the memory this event is about.
 */
sealed interface MemoryEvent: Event {
    val memoryId: UUID
}
