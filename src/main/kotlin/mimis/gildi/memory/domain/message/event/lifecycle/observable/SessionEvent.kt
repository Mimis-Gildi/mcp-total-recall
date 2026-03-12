package mimis.gildi.memory.domain.message.event.lifecycle.observable

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.domain.message.event.Event
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.SessionEndCause
import java.time.Instant
import java.util.UUID

sealed interface SessionEvent : Event {
    val instanceId: String
    val mindType: String
}

/**
 * [mimis.gildi.memory.context.Cortex]. A mind disconnected. Triggers session audit via NotificationPort.
 *
 * @property tx chain of custody.
 * @property instanceId which mind disconnected.
 * @property reason why -- EXPLICIT (mind chose to leave), TIMEOUT (no heartbeat), CRASH (unexpected).
 */
data class SessionEnded(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Session properties
    override val instanceId: String,
    override val mindType: String,
    // Activity properties
    val causeOfTermination: SessionEndCause,
    val reasonForTermination: String?
) : SessionEvent


/**
 * [Cortex]. A mind connected. Loads identity and last session state.
 *
 * @property tx chain of custody.
 * @property instanceId unique identifier for this mind instance (e.g. "claude-1").
 * @property mindType what kind of mind -- "claude", "human", "other".
 * @property resumptionData optional context from the previous session for warm restart.
 */
data class SessionStarted(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Session properties
    override val instanceId: String,
    override val mindType: String,
    // Activity properties
    val resumptionData: Map<String, String> = emptyMap(),
) : SessionEvent
