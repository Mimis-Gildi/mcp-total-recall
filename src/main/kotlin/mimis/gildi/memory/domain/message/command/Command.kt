/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.command

import mimis.gildi.memory.domain.message.Message
import mimis.gildi.memory.domain.message.TransactionContext
import java.util.UUID

/**
 * Commands are requests to change state. They carry intent.
 * A command has exactly one target. May be accepted, rejected, or ignored.
 *
 * @property tx chain of custody -- session, request, causation, source context.
 * @property metadata optional key-value pairs the issuer attaches for routing or auditing.
 * @property responses optional set of messageIds this command is responding to.
 *
 * Sub-hierarchies by target:
 *
 * - [mimis.gildi.memory.domain.message.command.memory.MemoryCommand]:
 *   1. storage operations ([mimis.gildi.memory.context.Hippocampus]).
 *
 * - [mimis.gildi.memory.domain.message.command.association.AssociationCommand]:
 *   1. graph operations ([mimis.gildi.memory.context.Synapse]).
 *
 * - [mimis.gildi.memory.domain.message.command.attention.AttentionCommand]:
 *   1. scoring operations ([mimis.gildi.memory.context.Salience]).
 *
 * - [mimis.gildi.memory.domain.message.command.lifecycle.LifecycleCommand]:
 *   1. system lifecycle (all bounded contexts).
 */
interface Command : Message {
    val tx: TransactionContext
    val metadata: Map<String, String>?
    val responses: Set<UUID>?
}
