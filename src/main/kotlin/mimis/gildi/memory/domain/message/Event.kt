/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
@file:Suppress("unused")

package mimis.gildi.memory.domain.message

import mimis.gildi.memory.domain.model.Association
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

/**
 * Events are notifications that something happened. They carry facts.
 * An event can have multiple consumers.
 */
sealed interface Event

// -- Hippocampus events --

data class MemoryStored(
    val memoryId: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: Tier,
    val timestamp: Instant
) : Event

data class MemoryAccessed(
    val memoryId: UUID,
    val accessTimestamp: Instant
) : Event

data class MemoryClaimed(
    val memoryId: UUID,
    val claimTimestamp: Instant
) : Event

data class MemoryRetrieved(
    val memoryId: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: Tier
) : Event

data class TierChanged(
    val memoryId: UUID,
    val oldTier: Tier,
    val newTier: Tier,
    val reason: String
) : Event

data class MemoryReclassified(
    val memoryId: UUID,
    val oldTier: Tier,
    val newTier: Tier,
    val reason: String
) : Event

// -- Salience events --

data class TierPromoted(
    val memoryId: UUID,
    val newTier: Tier,
    val score: Double
) : Event

data class TierDemoted(
    val memoryId: UUID,
    val newTier: Tier,
    val score: Double
) : Event

data class SalienceScored(
    val memoryId: UUID,
    val score: Double,
    val lastAccessed: Instant,
    val decayRate: Double,
    val claimed: Boolean
) : Event

// -- Synapse events --

data class AssociationsFound(
    val sourceId: UUID,
    val associations: List<Association>
) : Event

// -- Lifecycle events --

data class SessionStart(
    val instanceId: String,
    val mindType: String,
    val resumptionData: Map<String, String> = emptyMap()
) : Event

data class SessionEnd(
    val instanceId: String,
    val reason: String
) : Event

data class StateTransition(
    val instanceId: String,
    val oldState: String,
    val newState: String,
    val context: Map<String, String> = emptyMap()
) : Event

data class ModeChanged(
    val instanceId: String,
    val oldMode: String,
    val newMode: String
) : Event

data class SessionState(
    val instanceId: String,
    val duration: Long,
    val activityLevel: String,
    val lastInteraction: Instant
) : Event
