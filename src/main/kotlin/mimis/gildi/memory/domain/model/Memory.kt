/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

import java.time.Instant
import java.util.UUID

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
