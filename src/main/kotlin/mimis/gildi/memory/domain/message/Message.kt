package mimis.gildi.memory.domain.message

import java.time.Instant
import java.util.UUID

interface Message {
    val messageId: UUID
    val causationId: UUID
    val timestamp: Instant
    val content: String?
}
