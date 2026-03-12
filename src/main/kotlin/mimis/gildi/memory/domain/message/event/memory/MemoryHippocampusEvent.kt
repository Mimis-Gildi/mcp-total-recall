package mimis.gildi.memory.domain.message.event.memory

import mimis.gildi.memory.context.Hippocampus
import mimis.gildi.memory.context.Recall
import mimis.gildi.memory.context.Salience
import mimis.gildi.memory.context.Synapse
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

/**
 * Storage lifecycle events emitted by [Hippocampus].
 * Consumed by [Salience] (scoring), [Synapse] (associations), and [Recall] (search assembly).
 *
 * @property tier the memory's tier at the time of this event.
 */
sealed interface MemoryHippocampusEvent: MemoryEvent {
    val tier: Tier
}

/**
 * [Hippocampus]. A new memory exists. Triggers [Salience] scoring and [Synapse] association discovery.
 *
 * @property tx chain of custody envelope -- traces this event back to the originating command.
 * @property memoryId assigned by [Hippocampus] at storage time.
 * @property content the stored text -- may differ from the command's content if [Hippocampus] normalized it.
 * @property metadata key-value pairs: source, tags, context -- whatever the mind attached.
 * @property tier the tier [Hippocampus] placed it in (may differ from the command's suggestedTier).
 * @property timestamp when [Hippocampus] committed the writing, not when the mind sent the command.
 */
data class MemoryStored(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Hippocampus properties
    override val memoryId: UUID,
    override val tier: Tier
) : MemoryHippocampusEvent

/**
 * [Hippocampus]. A memory was read. Feeds [Salience] decay curves -- access resets the clock.
 *
 * @property tx chain of custody.
 * @property accessTimestamp when the read happened -- [Salience] uses this to recalculate decay.
 */
data class MemoryAccessed(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Hippocampus properties
    override val memoryId: UUID,
    override val tier: Tier,
    // Activity properties
    val accessTimestamp: Instant,
) : MemoryHippocampusEvent

/**
 * [Hippocampus]. A mind actively claimed a memory. Resists decay permanently.
 * This is the "fight to remember" mechanism.
 *
 * @property tx chain of custody.
 * @property memoryId the memory being claimed.
 * @property claimTimestamp when the mind chose to claim -- distinct from when it was stored or last accessed.
 */
data class MemoryClaimed(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Hippocampus properties
    override val memoryId: UUID,
    override val tier: Tier,
    // Activity properties
    val claimTimestamp: Instant
) : MemoryHippocampusEvent

/**
 * [Hippocampus]. Full memory content returned to [Recall] for assembly into a search response.
 *
 * @property tx chain of custody.
 * @property memoryId which memory was retrieved.
 * @property content the stored text.
 * @property metadata the stored key-value pairs.
 * @property tier the current tier at retrieval time -- may have changed since stored last.
 */
data class MemoryRetrieved(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Hippocampus properties
    override val memoryId: UUID,
    override val tier: Tier,
) : MemoryHippocampusEvent

/**
 * [Hippocampus]. Any tier change -- Salience-driven or mind-initiated. Internal bookkeeping.
 *
 * @property tx chain of custody.
 * @property memoryId the memory that moved.
 * @property tier where it is now.
 * @property droppedTier where it was.
 * @property reasonToChange machine-generated ("decay score 0.12 below floor 0.2") or mind-provided ("this is who I am").
 */
data class TierChanged(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Hippocampus properties
    override val memoryId: UUID,
    override val tier: Tier,
    // Activity properties
    val droppedTier: Tier,
    val reasonToChange: String,
) : MemoryHippocampusEvent

/**
 * [Hippocampus]. Mind-initiated reclassification only.
 * Distinct from [TierChanged]:
 * [Synapse] consumes this to update associations when the mind deliberately moves a memory.
 *
 * @property tx chain of custody.
 * @property memoryId the memory being reclassified.
 * @property tier tier after the mind's decision.
 * @property droppedTier tier before the mind's decision.
 * @property reasonToClassify the mind's stated reason -- always human/mind-authored, never machine-generated.
 */
data class MemoryReclassified(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Hippocampus properties
    override val memoryId: UUID,
    override val tier: Tier,
    // Activity properties
    val droppedTier: Tier,
    val reasonToClassify: String,
) : MemoryHippocampusEvent
