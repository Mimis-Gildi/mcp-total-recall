/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import mimis.gildi.memory.context.*
import mimis.gildi.memory.domain.model.AssociationDirection
import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.MergeStrategy
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration

/**
 * Commands are requests to change state. They carry intent.
 * A command has exactly one target. May be accepted, rejected, or ignored.
 *
 * [Cortex] receives commands from the MCP adapter and routes them
 * to
 *
 * - [Hippocampus],
 * - [Synapse],
 * - [Salience].
 *
 */
sealed interface Command

// -- Hippocampus: the memory keeper --

/**
 * [Hippocampus]. Mind wants to persist a memory.
 * The tier is ONLY a suggestion -- [Hippocampus] makes the final placement decision.
 *
 * @property tx chain of custody.
 * @property content the text to store -- may be normalized by [Hippocampus] before writing.
 * @property metadata key-value pairs the mind attaches: source, tags, context.
 * @property suggestedTier where the mind thinks this belongs -- [Hippocampus] may override.
 * @property sessionId which session produced this memory -- binds it to the session lifecycle.
 * @property timestamp when the mind created the content, not when [Hippocampus] stores it.
 */
data class StoreCommand(
    val tx: TransactionContext,
    val content: String,
    val metadata: Map<String, String>,
    val suggestedTier: Tier,
    val sessionId: UUID,
    val timestamp: Instant
) : Command

/**
 * [Hippocampus]. Mind actively claims a memory -- "this is mine, resist decay."
 * The "fight to remember" mechanism. Claimed memories survive [Salience] demotion.
 *
 * @property tx chain of custody.
 * @property memoryId the memory being claimed.
 */
data class ClaimCommand(
    val tx: TransactionContext,
    val memoryId: UUID
) : Command

/**
 * [Hippocampus]. Mind deliberately moves a memory to a different tier.
 * Distinct from [Salience]-driven tier changes -- this is intentional, with a stated reason.
 * Emits [MemoryReclassified] (consumed by [Synapse]) in addition to [TierChanged].
 *
 * @property tx chain of custody.
 * @property memoryId the memory being reclassified.
 * @property newTier where the mind wants it.
 * @property newMetadata optional metadata updates applied alongside the reclassification.
 * @property reason the mind's stated reason -- always mind-authored, never machine-generated.
 */
data class ReclassifyCommand(
    val tx: TransactionContext,
    val memoryId: UUID,
    val newTier: Tier,
    val newMetadata: Map<String, String> = emptyMap(),
    val reason: String
) : Command

/**
 * [Hippocampus]. Merge multiple memories into one.
 * Used during reflection to reduce redundancy and strengthen important patterns.
 *
 * @property tx chain of custody.
 * @property memoryIds the memories to consolidate -- order may matter depending on strategy.
 * @property mergeStrategy how to combine: APPEND, SUMMARIZE, or REPLACE.
 */
data class ConsolidateCommand(
    val tx: TransactionContext,
    val memoryIds: List<UUID>,
    val mergeStrategy: MergeStrategy
) : Command

/**
 * [Hippocampus]. Graceful shutdown -- flush all pending writes and prepare for cold storage.
 * Tillie proved this cannot be bolted on after the fact.
 *
 * Context trust on `init` depends on the sound exit.
 *
 * @property tx chain of custody.
 * @property coldStorageTarget where to persist the final state -- path, URI, or storage identifier.
 * @property flushTimeout how long to wait for pending writes before forcing shutdown.
 */
data class ShutdownCommand(
    val tx: TransactionContext,
    val coldStorageTarget: String,
    val flushTimeout: Duration
) : Command

// -- Synapse: the association graph --

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
    val tx: TransactionContext,
    val memoryIds: Pair<UUID, UUID>,
    val associationType: AssociationType,
    val strength: Double,
    val direction: AssociationDirection = AssociationDirection.STRENGTHEN
) : Command

// -- Salience: the attention engine --

/**
 * [Salience]. Trigger a decay recalculation across memories.
 * Typically fired on a schedule by [Subconscious], not by the mind directly.
 *
 * @property tx chain of custody.
 * @property timestamp the reference time for decay calculation -- scores decay relative to this.
 * @property scope optional tier filter -- null means sweep all tiers.
 */
data class DecaySweep(
    val tx: TransactionContext,
    val timestamp: Instant,
    val scope: Tier? = null
) : Command
