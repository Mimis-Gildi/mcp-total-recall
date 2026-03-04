/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import java.time.Instant
import java.util.UUID

/**
 * Chain of custody envelope carried on every domain message.
 * Six fields. Zero nullable. Every touch recorded.
 *
 * See design document 0011-transaction-context for full specification.
 */
data class TransactionContext(
    val sessionId: UUID,
    val requestId: UUID,
    val messageId: UUID,
    val causationId: UUID,
    val timestamp: Instant,
    val sourceContext: String
)
