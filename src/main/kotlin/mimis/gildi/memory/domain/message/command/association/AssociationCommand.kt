package mimis.gildi.memory.domain.message.command.association

import mimis.gildi.memory.context.Synapse
import mimis.gildi.memory.domain.message.command.Command
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.AssociationDirection
import mimis.gildi.memory.domain.model.AssociationType
import java.time.Instant
import java.util.UUID

/**
 * Commands targeting [Synapse] -- the association graph.
 * Issued by [mimis.gildi.memory.context.Cortex] (mind-initiated).
 */
sealed interface AssociationCommand : Command

/**
 * [Synapse]. Create or modify an association between two memories.
 * Direction determines whether this strengthens, weakens, or creates the link.
 *
 * @property tx chain of custody.
 * @property memoryIds the pair of memories to associate -- order is (source, target).
 * @property associationType semantic category: THEMATIC, TEMPORAL, CAUSAL, CONTRADICTORY.
 * @property strength how strong the link is (0.0--1.0).
 * @property direction what to do: CREATE, STRENGTHEN, WEAKEN, or DISSOLVE.
 */
data class AssociateCommand(
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
    val memoryIds: Pair<UUID, UUID>,
    val associationType: AssociationType,
    val strength: Double,
    val direction: AssociationDirection = AssociationDirection.STRENGTHEN
) : AssociationCommand
