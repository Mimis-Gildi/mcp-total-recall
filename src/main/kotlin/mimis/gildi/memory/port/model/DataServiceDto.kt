/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.model

import java.time.Instant
import java.util.UUID

/**
 * The data shape that crosses the data service port.
 *
 * Neither domain nor infrastructure. The port owns this contract.
 * The internal adapter (MemoryDataAdapter) translates domain objects to this shape.
 * The external adapter (SQLiteDriver) translates this shape to storage operations.
 *
 * Standard JVM types only -- no domain enums, no infrastructure types.
 * [tier] is a String, not a Tier enum. The port does not know tier semantics.
 *
 * @property id the memory's identity.
 * @property content the memory text.
 * @property metadata key-value pairs -- tags, source, context. Serialization is the adapter's concern.
 * @property tier tier name as a string. The port carries it, the domain interprets it.
 * @property createdAt when the memory was first stored.
 * @property lastAccessed when the memory was last read or touched.
 * @property sessionId which session stored this memory.
 * @property claimed whether the mind actively chose to hold onto this.
 */
data class DataServiceDto(
    val id: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: String,
    val createdAt: Instant,
    val lastAccessed: Instant,
    val sessionId: UUID,
    val claimed: Boolean
)
