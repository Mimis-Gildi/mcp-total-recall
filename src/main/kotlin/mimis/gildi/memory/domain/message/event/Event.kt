package mimis.gildi.memory.domain.message.event

import mimis.gildi.memory.domain.message.Message

/**
 * Events are facts. Something happened. Zero, one, or many consumers.
 *
 * Events narrow [metadata] to non-nullable -- every event must carry context.
 *
 * Sub-hierarchies:
 *
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryEvent]:
 * 1. storage ([mimis.gildi.memory.context.Hippocampus]),
 * 2. scoring ([mimis.gildi.memory.context.Salience]),
 * 3. associations ([mimis.gildi.memory.context.Synapse]).
 *
 * - [mimis.gildi.memory.domain.message.event.recall.RecallEvent]:
 * 1. deep traversal advisory lifecycle ([mimis.gildi.memory.context.Recall]).
 *
 * - [mimis.gildi.memory.domain.message.event.observable.SessionEvent]:
 * 1. session lifecycle, observable by all contexts ([mimis.gildi.memory.context.Cortex]).
 *
 * - [mimis.gildi.memory.domain.message.event.mode.OperatingModeEvent]:
 * 1. working mode and state transitions ([mimis.gildi.memory.context.Cortex]),
 * 2. consumed by [mimis.gildi.memory.context.Subconscious] for break detection.
 */
interface Event: Message {
    override val metadata: Map<String, String>
}
