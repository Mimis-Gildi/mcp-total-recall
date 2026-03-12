package mimis.gildi.memory.domain.message.event.memory

import mimis.gildi.memory.domain.message.event.Event
import java.util.UUID

sealed interface MemoryEvent: Event {
    val memoryId: UUID
}
