/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import java.util.UUID

/**
 * Chain of custody envelope carried on every domain message.
 * Four fields. Zero nullable. Every touch is recorded.
 *
 * @property instanceId which mind instance owns this chain.
 * @property sessionId the session this message belongs to.
 * @property requestId groups related messages within a session into one request/response cycle.
 * @property sourceContext which bounded context emitted this message.
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/design/0011-transaction-context/">Design Spec: TransactionContext</a>
 */
data class TransactionContext(
    val instanceId: UUID,
    val sessionId: UUID,
    val requestId: UUID,
    val sourceContext: String
)
