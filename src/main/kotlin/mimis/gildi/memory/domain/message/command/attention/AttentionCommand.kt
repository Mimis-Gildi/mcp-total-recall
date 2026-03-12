package mimis.gildi.memory.domain.message.command.attention

import mimis.gildi.memory.context.Salience
import mimis.gildi.memory.context.Subconscious
import mimis.gildi.memory.domain.message.command.Command
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

/**
 * Commands targeting [Salience] -- the attention engine.
 * Issued by [Subconscious] (autonomous scheduling).
 */
sealed interface AttentionCommand : Command

/**
 * [Salience]. Trigger a decay recalculation across memories.
 * Typically fired on a schedule by [Subconscious], not by the mind directly.
 *
 * @property tx chain of custody.
 * @property timestamp the reference time for decay calculation -- scores decay relative to this.
 * @property scope optional tier filter -- null means sweep all tiers.
 */
data class DecaySweep(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Command properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>? = null,
    override val responses: Set<UUID>? = null,
    // Activity properties
    val scope: Tier? = null
) : AttentionCommand
