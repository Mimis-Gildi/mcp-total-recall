/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.command.lifecycle

import mimis.gildi.memory.context.Subconscious
import mimis.gildi.memory.domain.message.command.Command
import mimis.gildi.memory.domain.message.TransactionContext
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration

/**
 * Commands targeting all bounded contexts -- system lifecycle.
 * Issued by [Subconscious] (autonomous maintenance).
 */
sealed interface LifecycleCommand : Command

/**
 * Graceful shutdown -- flush all pending writes and prepare for cold storage.
 * Tillie proved this cannot be bolted on after the fact.
 *
 * Context trust on `init` depends on the sound exit.
 *
 * @property tx chain of custody.
 * @property coldStorageTarget where to persist the final state -- path, URI, or storage identifier.
 * @property flushTimeout how long to wait for pending writes before forcing shutdown.
 */
data class ShutdownCommand(
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
    val coldStorageTarget: String,
    val flushTimeout: Duration
) : LifecycleCommand
