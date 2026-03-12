package mimis.gildi.memory.domain.message.event.memory

import mimis.gildi.memory.context.Hippocampus
import mimis.gildi.memory.context.Salience
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

sealed interface MemorySalienceEvent: MemoryEvent {
    val tier: Tier
    val score: Double
}

/**
 * [Salience]. Promoted a memory to a higher tier. Score crossed the promotion threshold.
 *
 * @property tx chain of custody.
 * @property memoryId the memory that was promoted.
 * @property newTier the tier it was promoted to.
 * @property score the salience score that triggered promotion (0.0--1.0).
 */
data class AttentionTierPromotionRequested(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Salience properties
    override val memoryId: UUID,
    override val tier: Tier,
    override val score: Double,
    // Activity properties
    val droppedTier: Tier,
    val reasonToPromote: String
) : MemorySalienceEvent

/**
 * [Salience]. Demoted a memory. Score decayed below the tier floor.
 *
 * @property tx chain of custody.
 * @property memoryId the memory that was demoted.
 * @property newTier the tier it fell to.
 * @property score the salience score at demotion time (0.0--1.0).
 */
data class AttentionTierDemotionRequested(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Salience properties
    override val memoryId: UUID,
    override val tier: Tier,
    override val score: Double,
    // Activity properties
    val droppedTier: Tier,
    val reasonToDemote: String

) : MemorySalienceEvent

/**
 * [Salience]. Raw salience calculation result. Consumed by [Hippocampus] to decide tier transitions.
 *
 * @property tx chain of custody.
 * @property salienceScore the full score object -- memoryId, score, lastAccessed, decayRate.
 */
data class AttentionScoreChanged(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Salience properties
    override val memoryId: UUID,
    override val tier: Tier,
    override val score: Double,
) : MemorySalienceEvent

