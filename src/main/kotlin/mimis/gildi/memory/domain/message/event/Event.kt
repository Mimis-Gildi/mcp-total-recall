package mimis.gildi.memory.domain.message.event

import mimis.gildi.memory.domain.message.Message
import mimis.gildi.memory.domain.message.TransactionContext

/**
 * Events are facts. Something happened. Zero, one, or many consumers.
 *
 * Grouped by bounded context:
 *
 * - [mimis.gildi.memory.context.Hippocampus] (storage),
 * - [mimis.gildi.memory.context.Salience] (scoring),
 * - [mimis.gildi.memory.context.Synapse] (associations),
 * - [mimis.gildi.memory.context.Cortex] (lifecycle),
 * - and [mimis.gildi.memory.context.Subconscious] (background).
 */
interface Event: Message {
    val tx: TransactionContext
    val metadata: Map<String, String>
}
