/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.context

/**
 * The attention engine. Scores memories by relevance, recency, and access patterns.
 *
 * Salience consumes
 *
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryStored] and
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryAccessed]
 *
 * to calculate decay curves.
 *
 * It emits recommendations:
 *
 * - [mimis.gildi.memory.domain.message.event.memory.AttentionScoreChanged],
 * - [mimis.gildi.memory.domain.message.event.memory.AttentionTierPromotionRequested],
 * - and [mimis.gildi.memory.domain.message.event.memory.AttentionTierDemotionRequested].
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/design/0006-salience/">Design: Salience</a>
 */
// FixMe: Created as a dependency for Event.kt KDoc links. Not implemented until we get here.
interface Salience
