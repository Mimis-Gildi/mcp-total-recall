/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.inbound

import mimis.gildi.memory.domain.model.Association
import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.Memory
import mimis.gildi.memory.domain.model.ReflectionScope
import mimis.gildi.memory.domain.model.Tier
import java.util.UUID

/**
 * Inbound port for memory operations. Any mind connects through this.
 *
 * Conscience-universal: the contract is the same whether the connected
 * mind is Claude, a human with a UI, or any future conscious system.
 */
interface MemoryPort {

    @Suppress("unused")
    suspend fun storeMemory(
        content: String,
        metadata: Map<String, String> = emptyMap(),
        suggestedTier: Tier = Tier.LONG_TERM,
        sessionId: UUID
    ): Memory

    @Suppress("unused")
    suspend fun searchMemory(
        query: String,
        filters: Map<String, String> = emptyMap(),
        maxResults: Int = 10,
        includeAssociations: Boolean = true,
        sessionId: UUID
    ): List<Memory>

    @Suppress("unused")
    suspend fun claimMemory(memoryId: UUID): Memory

    @Suppress("unused")
    suspend fun associateMemories(
        memoryA: UUID,
        memoryB: UUID,
        type: AssociationType,
        strength: Double
    ): List<Association>

    @Suppress("unused")
    suspend fun reclassifyMemory(
        memoryId: UUID,
        newTier: Tier,
        reason: String
    ): Memory

    @Suppress("unused")
    suspend fun reflect(
        scope: ReflectionScope = ReflectionScope.ALL,
        timeSpanDays: Int? = null,
        maxCandidates: Int? = null
    ): List<Memory>
}
