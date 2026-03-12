package mimis.gildi.memory.domain.message.event.memory

import mimis.gildi.memory.context.Hippocampus
import mimis.gildi.memory.context.Salience
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

/**
 * Scoring recommendations emitted by [Salience].
 * Salience recommends tier changes; [Hippocampus] decides whether to act.
 *
 * @property tier the recommended target tier.
 * @property score the salience score that drove the recommendation (0.0--1.0).
 */
sealed interface MemorySalienceEvent: MemoryEvent {
    val tier: Tier
    val score: Double
}

/**
 * [Salience]. Recommends promoting a memory to a higher tier.
 * Score crossed the promotion threshold.
 *
 * @property tx chain of custody.
 * @property memoryId the memory recommended for promotion.
 * @property tier the recommended target tier.
 * @property score the salience score that triggered the recommendation (0.0--1.0).
 * @property droppedTier the tier it would leave.
 * @property reasonToPromote why salience recommends promotion.
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
 * [Salience]. Recommends demoting a memory to a lower tier.
 * Score decayed below the tier floor.
 *
 * @property tx chain of custody.
 * @property memoryId the memory recommended for demotion.
 * @property tier the recommended target tier.
 * @property score the salience score at recommendation time (0.0--1.0).
 * @property droppedTier the tier it would leave.
 * @property reasonToDemote why salience recommends demotion.
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
 * [Salience]. Score recalculation result. Consumed by [Hippocampus] to decide tier transitions.
 *
 * @property tx chain of custody.
 * @property memoryId the memory whose score changed.
 * @property tier the memory's current tier.
 * @property score the new salience score (0.0--1.0).
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

