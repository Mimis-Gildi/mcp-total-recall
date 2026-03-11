/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.context.Recall
import mimis.gildi.memory.domain.model.ReflectionScope
import java.util.UUID

/**
 * Queries are requests to read state without changing it.
 * A query has exactly one target and **must** be answered.
 *
 * [Cortex] receives queries from the MCP adapter and routes them to [Recall].
 */
sealed interface Query

// -- Recall: the read side --

/**
 * [Recall]. Find memories matching text and filters.
 *
 * The fast path returns immediately;
 * deep association traversal continues in the background and may produce a [TotalRecallNotification] later.
 *
 * @property tx chain of custody.
 * @property query free-text search -- semantics TBD (keyword, embedding, hybrid).
 * @property filters key-value narrowing: tag, source, tier, date range.
 * @property maxResults cap on returned memories -- defaults to 10, not unlimited.
 * @property includeAssociations whether to traverse the association graph or return raw hits only.
 * @property sessionId which session is asking -- scopes results and feeds access tracking.
 */
data class SearchQuery(
    val tx: TransactionContext,
    val query: String,
    val filters: Map<String, String> = emptyMap(),
    val maxResults: Int = 10,
    val includeAssociations: Boolean = true,
    val sessionId: UUID
) : Query

/**
 * [Recall]. Introspective query -- the mind asks, "what do I know?"
 * Used for consolidation, self-assessment, and memory hygiene.
 *
 * @property tx chain of custody.
 * @property scope what to reflect on -- ALL, RECENT, DECAYING, or CLAIMED.
 * @property timeSpanDays optional window -- null means all time.
 * @property maxCandidates an optional cap on candidates surfaced -- null means no limit.
 */
data class ReflectQuery(
    val tx: TransactionContext,
    val scope: ReflectionScope = ReflectionScope.ALL,
    val timeSpanDays: Int? = null,
    val maxCandidates: Int? = null
) : Query
