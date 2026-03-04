/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
@file:Suppress("unused")

package mimis.gildi.memory.domain.message

import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

/**
 * Commands are requests to change state. They carry intent.
 * A command has exactly one target. May be accepted, rejected, or ignored.
 */
sealed interface Command

// -- Hippocampus commands --

data class StoreCommand(
    val tx: TransactionContext,
    val content: String,
    val metadata: Map<String, String>,
    val suggestedTier: Tier,
    val sessionId: String,
    val timestamp: Instant
) : Command

data class ClaimCommand(
    val tx: TransactionContext,
    val memoryId: UUID
) : Command

data class ReclassifyCommand(
    val tx: TransactionContext,
    val memoryId: UUID,
    val newTier: Tier,
    val newMetadata: Map<String, String> = emptyMap(),
    val reason: String
) : Command

data class ConsolidateCommand(
    val tx: TransactionContext,
    val memoryIds: List<UUID>,
    val mergeStrategy: String
) : Command

data class ShutdownCommand(
    val tx: TransactionContext,
    val coldStorageTarget: String,
    val flushTimeout: Long
) : Command

// -- Synapse commands --

data class AssociateCommand(
    val tx: TransactionContext,
    val memoryIds: Pair<UUID, UUID>,
    val associationType: AssociationType,
    val strength: Double,
    val direction: AssociationDirection = AssociationDirection.STRENGTHEN
) : Command

enum class AssociationDirection {
    CREATE,
    STRENGTHEN,
    WEAKEN
}

// -- Salience commands --

data class DecaySweep(
    val tx: TransactionContext,
    val timestamp: Instant,
    val scope: String = "all"
) : Command
