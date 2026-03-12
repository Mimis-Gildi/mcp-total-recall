package mimis.gildi.memory.domain.message.event.lifecycle.observable

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.context.Subconscious
import mimis.gildi.memory.domain.message.event.Event
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.SessionEndCause
import java.time.Instant
import java.util.UUID

/**
 * Session lifecycle events. Observable by all bounded contexts.
 *
 * [Cortex] emits these when a mind connects or disconnects.
 * Every context receives them -- [mimis.gildi.memory.context.Hippocampus] flushes,
 * [mimis.gildi.memory.context.Salience] snapshots decay curves,
 * [Subconscious] starts or stops background monitoring.
 *
 * @property instanceId which mind instance (e.g. "claude-1").
 * @property mindType what kind of mind -- "claude", "human", "other".
 */
sealed interface SessionEvent : Event {
    val instanceId: String
    val mindType: String
}

/**
 * [Cortex]. A mind connected. Loads identity and last session state.
 *
 * @property tx chain of custody.
 * @property instanceId which mind connected.
 * @property mindType what kind of mind.
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

/**
 * [Cortex]. A mind disconnected. Triggers session audit via NotificationPort.
 *
 * @property tx chain of custody.
 * @property instanceId which mind disconnected.
 * @property causeOfTermination why -- [SessionEndCause.EXPLICIT] (mind chose to leave),
 *   [SessionEndCause.TIMEOUT] (no activity), [SessionEndCause.CRASH] (unexpected).
 * @property reasonForTermination optional human-readable explanation.
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
