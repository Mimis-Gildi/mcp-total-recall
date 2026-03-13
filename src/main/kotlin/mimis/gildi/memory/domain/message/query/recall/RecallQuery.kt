/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.query.recall

import mimis.gildi.memory.context.Recall
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.message.query.Query
import mimis.gildi.memory.domain.model.ReflectionScope
import java.time.Instant
import java.util.UUID

/**
 * Queries targeting [Recall] -- the read side.
 * Issued by [mimis.gildi.memory.context.Cortex] (mind-initiated).
 */
sealed interface RecallQuery : Query

/**
 * [Recall]. Find memories matching text and filters.
 *
 * The fast path returns immediately;
 * deep association traversal continues in the background and may produce a
 * [mimis.gildi.memory.domain.message.notification.recall.TotalRecallNotification] later.
 *
 * @property tx chain of custody.
 * @property query free-text search -- semantics TBD (keyword, embedding, hybrid).
 * @property filters key-value narrowing: tag, source, tier, date range.
 * @property maxResults cap on returned memories -- defaults to 10, not unlimited.
 * @property includeAssociations whether to traverse the association graph or return raw hits only.
 */
data class SearchQuery(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Query properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>? = null,
    // Activity properties
    val query: String,
    val filters: Map<String, String> = emptyMap(),
    val maxResults: Int = 10,
    val includeAssociations: Boolean = true,
) : RecallQuery

/**
 * [Recall]. Introspective query -- the mind asks, "what do I know?"
 * Used for consolidation, self-assessment, and memory hygiene.
 *
 * Reflection scopes:
 *
 * - [ReflectionScope.ALL]: no filter, reflect on everything
 * - [ReflectionScope.STALE]: decaying salience, candidates for consolidation or archival
 * - [ReflectionScope.RECENT]: recently stored or accessed, fresh context
 * - [ReflectionScope.WEAK_ASSOCIATIONS]: low-strength links, candidates for strengthening or pruning
 *
 * @property tx chain of custody.
 * @property scope what subset of memories to reflect on. Defaults to [ReflectionScope.ALL].
 * @property timeSpanDays optional window -- null means all time.
 * @property maxCandidates an optional cap on candidates surfaced -- null means no limit.
 */
data class ReflectQuery(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Query properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>? = null,
    // Activity properties
    val scope: ReflectionScope = ReflectionScope.ALL,
    val timeSpanDays: Int? = null,
    val maxCandidates: Int? = null
) : RecallQuery
