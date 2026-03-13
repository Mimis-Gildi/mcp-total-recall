/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

import java.time.Instant
import java.util.UUID

/**
 * The stored unit. What a mind remembers.
 *
 * Owned by [mimis.gildi.memory.context.Hippocampus]. Created via
 * [mimis.gildi.memory.domain.message.command.memory.StoreMemoryCommand],
 * retrieved via [mimis.gildi.memory.domain.message.query.recall.SearchQuery].
 *
 * @property id unique identity. Stable across tier changes and reclassification.
 * @property content the stored text. What was actually remembered.
 * @property metadata key-value pairs attached by the mind at store time. Unstructured, mind-defined.
 * @property tier where this memory lives in the hierarchy. [mimis.gildi.memory.context.Salience]
 *   recommends promotions/demotions; [mimis.gildi.memory.context.Hippocampus] executes them.
 * @property createdAt when the memory was first stored. Immutable.
 * @property lastAccessed when the memory was last read. [mimis.gildi.memory.context.Salience] uses
 *   this for decay calculation.
 * @property sessionId which session produced this memory. Links back to the mind interaction.
 * @property claimed whether the mind has actively claimed this memory. Claimed memories resist
 *   decay -- [mimis.gildi.memory.context.Salience] skips them during demotion sweeps.
 */
data class Memory(
    val id: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: Tier,
    val createdAt: Instant,
    val lastAccessed: Instant,
    val sessionId: UUID,
    val claimed: Boolean = false
)
