/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

import java.time.Instant
import java.util.UUID

/**
 * Decay tracking for a single [Memory].
 *
 * Owned by [mimis.gildi.memory.context.Salience]. Updated on every
 * [mimis.gildi.memory.domain.message.event.memory.MemoryAccessed] event.
 * When the score crosses promotion/demotion thresholds, Salience emits
 * [mimis.gildi.memory.domain.message.event.memory.AttentionTierPromotionRequested] or
 * [mimis.gildi.memory.domain.message.event.memory.AttentionTierDemotionRequested].
 *
 * @property memoryId the memory this score belongs to.
 * @property score current salience (0.0--1.0). Decays over time relative to [lastAccessed].
 * @property lastAccessed when the memory was last read. Decay restarts from here.
 * @property decayRate how fast the score drops per time unit. Higher = faster forgetting.
 */
data class SalienceScore(
    val memoryId: UUID,
    val score: Double,
    val lastAccessed: Instant,
    val decayRate: Double
)
