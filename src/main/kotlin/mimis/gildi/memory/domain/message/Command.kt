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

// -- Tiered Memory commands --

data class StoreCommand(
    val content: String,
    val metadata: Map<String, String>,
    val suggestedTier: Tier,
    val sessionId: String,
    val timestamp: Instant
) : Command

data class ClaimCommand(
    val memoryId: UUID
) : Command

data class ReclassifyCommand(
    val memoryId: UUID,
    val newTier: Tier,
    val newMetadata: Map<String, String> = emptyMap(),
    val reason: String
) : Command

data class ConsolidateCommand(
    val memoryIds: List<UUID>,
    val mergeStrategy: String
) : Command

data class ShutdownCommand(
    val coldStorageTarget: String,
    val flushTimeout: Long
) : Command

// -- Association Graph commands --

data class AssociateCommand(
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

// -- Attention commands --

data class DecaySweep(
    val timestamp: Instant,
    val scope: String = "all"
) : Command
