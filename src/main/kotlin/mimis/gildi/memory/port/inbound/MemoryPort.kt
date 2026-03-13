/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
@file:Suppress("unused")

package mimis.gildi.memory.port.inbound

import mimis.gildi.memory.domain.model.Association
import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.Memory
import mimis.gildi.memory.domain.model.ReflectionScope
import mimis.gildi.memory.domain.model.Tier
import java.util.UUID

/**
 * The mind's proposal of a memory before [mimis.gildi.memory.context.Hippocampus] accepts and enriches it.
 * Identity is assigned at creation -- the mind names its own memories.
 *
 * @property id the memory's identity from birth.
 * @property content the text to store.
 * @property metadata key-value pairs the mind attaches: source, tags, context.
 */
data class MemoryDraft(
    val id: UUID,
    val content: String,
    val metadata: Map<String, String>
)

/**
 * Inbound port for memory operations. Any mind connects through this.
 *
 * Conscience-universal: the contract is the same whether the connected
 * mind is Claude, a human with a UI, or any future conscious system.
 *
 * Memory tiers (used by [storeMemory] and [reclassifyMemory]):
 *
 * - [Tier.IDENTITY_CORE]: who I am -- resists all decay, never demoted automatically
 * - [Tier.ACTIVE_CONTEXT]: currently relevant -- high salience, frequently accessed
 * - [Tier.LONG_TERM]: retained but not active -- subject to decay toward ARCHIVE
 * - [Tier.ARCHIVE]: low salience -- retained for deep recall, not surfaced by default search
 */
@Suppress("unused")
interface MemoryPort {

    suspend fun storeMemory(
        memory: MemoryDraft,
        suggestedTier: Tier = Tier.LONG_TERM
    ): Memory

    suspend fun searchMemory(
        query: String,
        filters: Map<String, String> = emptyMap(),
        maxResults: Int = 10,
        includeAssociations: Boolean = true,
        sessionId: UUID
    ): List<Memory>

    suspend fun claimMemory(memoryId: UUID): Memory

    suspend fun associateMemories(
        memoryA: UUID,
        memoryB: UUID,
        type: AssociationType,
        strength: Double
    ): List<Association>

    suspend fun reclassifyMemory(
        memoryId: UUID,
        newTier: Tier,
        reason: String
    ): Memory

    suspend fun reflect(
        scope: ReflectionScope = ReflectionScope.ALL,
        timeSpanDays: Int? = null,
        maxCandidates: Int? = null
    ): List<Memory>
}
