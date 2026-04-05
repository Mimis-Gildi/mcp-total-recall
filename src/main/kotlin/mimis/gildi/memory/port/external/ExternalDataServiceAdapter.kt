/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.external

import mimis.gildi.memory.port.model.DataServiceDto
import java.util.UUID

/**
 * Data service port. The passive structure -- defines the DTO shape of what crosses,
 * not what anyone does with it.
 *
 * SQLite is the primary external adapter (ADR-0008). Redis enters at Agora.
 * Multiple external adapters can run simultaneously (ADR-0004).
 *
 * Internal adapter (MemoryDataAdapter) translates domain objects to [DataServiceDto] and places them here.
 * External adapter (SQLiteDriver) picks up [DataServiceDto] and translates to storage operations.
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/decisions/0008-sqlite-as-primary-backing-service/">ADR-0008: SQLite Primary</a>
 */
@Suppress("unused")
interface ExternalDataServiceAdapter {

    suspend fun save(dto: DataServiceDto)

    suspend fun findById(id: UUID): DataServiceDto?

    suspend fun search(query: String, filters: Map<String, String>, limit: Int): List<DataServiceDto>

    suspend fun delete(id: UUID)

    suspend fun update(dto: DataServiceDto)
}
