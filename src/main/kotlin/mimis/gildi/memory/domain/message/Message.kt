package mimis.gildi.memory.domain.message

import java.time.Instant
import java.util.UUID

/**
 * Every internal message has identity and causation.
 *
 * @property messageId unique identity of this message.
 * @property causationId the messageId of the message that caused this one -- self-referencing for root causes.
 * @property timestamp when this message was created.
 * @property content optional human-readable payload.
 */
interface Message {
    val messageId: UUID
    val causationId: UUID
    val timestamp: Instant
    val content: String?
}
