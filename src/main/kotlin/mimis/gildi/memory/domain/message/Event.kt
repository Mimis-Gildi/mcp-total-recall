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
    val tx: TransactionContext,
    val memoryId: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: Tier,
    val timestamp: Instant
) : Event

data class MemoryAccessed(
    val tx: TransactionContext,
    val memoryId: UUID,
    val accessTimestamp: Instant
) : Event

data class MemoryClaimed(
    val tx: TransactionContext,
    val memoryId: UUID,
    val claimTimestamp: Instant
) : Event

data class MemoryRetrieved(
    val tx: TransactionContext,
    val memoryId: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: Tier
) : Event

data class TierChanged(
    val tx: TransactionContext,
    val memoryId: UUID,
    val oldTier: Tier,
    val newTier: Tier,
    val reason: String
) : Event

data class MemoryReclassified(
    val tx: TransactionContext,
    val memoryId: UUID,
    val oldTier: Tier,
    val newTier: Tier,
    val reason: String
) : Event

// -- Salience events --

data class TierPromoted(
    val tx: TransactionContext,
    val memoryId: UUID,
    val newTier: Tier,
    val score: Double
) : Event

data class TierDemoted(
    val tx: TransactionContext,
    val memoryId: UUID,
    val newTier: Tier,
    val score: Double
) : Event

data class SalienceScored(
    val tx: TransactionContext,
    val memoryId: UUID,
    val score: Double,
    val lastAccessed: Instant,
    val decayRate: Double,
    val claimed: Boolean
) : Event

// -- Synapse events --

data class AssociationsFound(
    val tx: TransactionContext,
    val sourceId: UUID,
    val associations: List<Association>
) : Event

// -- Total Recall events --

data class TotalRecallAdvisory(
    val tx: TransactionContext,
    val sourceMemoryIds: Set<UUID>,
    val originRequestId: UUID,
    val timestamp: Instant
) : Event

// -- Lifecycle events --

data class SessionStart(
    val tx: TransactionContext,
    val instanceId: String,
    val mindType: String,
    val resumptionData: Map<String, String> = emptyMap()
) : Event

data class SessionEnd(
    val tx: TransactionContext,
    val instanceId: String,
    val reason: String
) : Event

data class StateTransition(
    val tx: TransactionContext,
    val instanceId: String,
    val oldState: String,
    val newState: String,
    val context: Map<String, String> = emptyMap()
) : Event

data class ModeChanged(
    val tx: TransactionContext,
    val instanceId: String,
    val oldMode: String,
    val newMode: String
) : Event

data class SessionState(
    val tx: TransactionContext,
    val instanceId: String,
    val duration: Long,
    val activityLevel: String,
    val lastInteraction: Instant
) : Event
