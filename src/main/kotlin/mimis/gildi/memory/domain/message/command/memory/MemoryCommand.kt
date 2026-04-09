/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.command.memory

import mimis.gildi.memory.context.Hippocampus
import mimis.gildi.memory.context.Salience
import mimis.gildi.memory.context.Subconscious
import mimis.gildi.memory.context.Synapse
import mimis.gildi.memory.domain.message.command.Command
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.message.event.memory.MemoryReclassified
import mimis.gildi.memory.domain.message.event.memory.TierChanged
import mimis.gildi.memory.domain.model.MergeStrategy
import mimis.gildi.memory.domain.model.Tier
import mimis.gildi.memory.port.internal.MemoryDraft
import java.time.Instant
import java.util.UUID

/**
 * Commands targeting [Hippocampus] -- the memory keeper.
 * Issued by [mimis.gildi.memory.context.Cortex] (mind-initiated)
 * or [Subconscious] (autonomous maintenance).
 */
sealed interface MemoryCommand : Command

/**
 * [Hippocampus]. Mind wants to persist a memory.
 * The tier is ONLY a suggestion -- [Hippocampus] makes the final placement decision.
 *
 * @property tx chain of custody.
 * @property memory the mind's draft -- identity, content, and metadata.
 * @property suggestedTier where the mind thinks this belongs -- [Hippocampus] may override.
 */
data class StoreMemoryCommand(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Command properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>?,
    override val responses: Set<UUID>? = null,
    // Activity properties
    val memory: MemoryDraft,
    val suggestedTier: Tier,
) : MemoryCommand

/**
 * [Hippocampus]. Mind actively claims a memory -- "this is mine, resist decay."
 * The "fight to remember" mechanism. Claimed memories survive [Salience] demotion.
 *
 * @property tx chain of custody.
 * @property memoryId the memory being claimed.
 */
data class ClaimMemoryCommand(
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
    val memoryId: UUID
) : MemoryCommand

/**
 * [Hippocampus]. Mind deliberately moves a memory to a different tier.
 * Distinct from [Salience]-driven tier changes -- this is intentional, with a stated reason.
 * Emits [MemoryReclassified] (consumed by [Synapse]) in addition to [TierChanged].
 *
 * @property tx chain of custody.
 * @property memoryId the memory being reclassified.
 * @property newTier where the mind wants it.
 * @property newMetadata optional metadata updates applied alongside the reclassification.
 * @property reasonToClassify the mind's stated reason -- always mind-authored, never machine-generated.
 */
data class ReclassifyMemoryCommand(
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
    val memoryId: UUID,
    val newTier: Tier,
    val newMetadata: Map<String, String> = emptyMap(),
    val reasonToClassify: String
) : MemoryCommand

/**
 * [Hippocampus]. Merge multiple memories into one.
 * Used during reflection to reduce redundancy and strengthen important patterns.
 *
 * @property tx chain of custody.
 * @property memoryIds the memories to consolidate -- order may matter depending on strategy.
 * @property mergeStrategy how to combine: APPEND, SUMMARIZE, or REPLACE.
 */
data class ConsolidateMemoryCommand(
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
    val memoryIds: List<UUID>,
    val mergeStrategy: MergeStrategy
) : MemoryCommand
