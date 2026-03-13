/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.outbound

import mimis.gildi.memory.domain.model.Memory
import java.util.UUID

/**
 * Outbound port for persistence. The passive structure -- defines the shape of what crosses,
 * not what anyone does with it.
 *
 * SQLite is the primary adapter (ADR-0008). Redis enters at Agora.
 * Multiple adapters can run simultaneously (ADR-0004).
 *
 * Called by:
 *
 * - [mimis.gildi.memory.context.Hippocampus]: [save], [findById], [update], [delete] for memory CRUD
 * - [mimis.gildi.memory.context.Recall]: [search], [findById] for query assembly
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/architecture/0008-sqlite-primary/">ADR-0008: SQLite Primary</a>
 */
@Suppress("unused")
interface BackingServicePort {

    suspend fun save(memory: Memory)

    suspend fun findById(id: UUID): Memory?

    suspend fun search(query: String, filters: Map<String, String>, limit: Int): List<Memory>

    suspend fun delete(id: UUID)

    suspend fun update(memory: Memory)
}
