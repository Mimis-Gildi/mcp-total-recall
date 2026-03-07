/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
@file:Suppress("unused")

package mimis.gildi.memory.domain.message

import mimis.gildi.memory.domain.model.ActivityLevel
import mimis.gildi.memory.domain.model.Association
import mimis.gildi.memory.domain.model.SalienceScore
import mimis.gildi.memory.domain.model.SessionEndReason
import mimis.gildi.memory.domain.model.Tier
import mimis.gildi.memory.domain.model.WorkingMode
import kotlin.time.Duration
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

/** Internal record of any tier change (Salience-driven or mind-initiated). */
data class TierChanged(
    val tx: TransactionContext,
    val memoryId: UUID,
    val oldTier: Tier,
    val newTier: Tier,
    val reason: String
) : Event

/** Mind-initiated reclassification only. Consumed by Synapse to update associations. */
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
    val salienceScore: SalienceScore
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
    val reason: SessionEndReason
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
    val oldMode: WorkingMode,
    val newMode: WorkingMode
) : Event

data class HeartbeatReceived(
    val tx: TransactionContext,
    val instanceId: String,
    val timestamp: Instant
) : Event

data class SessionState(
    val tx: TransactionContext,
    val instanceId: String,
    val duration: Duration,
    val activityLevel: ActivityLevel,
    val lastInteraction: Instant
) : Event
