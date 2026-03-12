/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.query

import mimis.gildi.memory.domain.message.Message
import mimis.gildi.memory.domain.message.TransactionContext

/**
 * Queries are requests to read state without changing it.
 * A query has exactly one target and **must** be answered.
 *
 * @property tx chain of custody -- session, request, causation, source context.
 * @property metadata optional key-value pairs the issuer attaches for routing or auditing.
 *
 * Sub-hierarchies by target:
 *
 * - [mimis.gildi.memory.domain.message.query.recall.RecallQuery]:
 *   1. search and reflection ([mimis.gildi.memory.context.Recall]).
 */
interface Query : Message {
    val tx: TransactionContext
    val metadata: Map<String, String>?
}
