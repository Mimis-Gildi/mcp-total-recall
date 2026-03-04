/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
@file:Suppress("unused")

package mimis.gildi.memory.domain.message

/**
 * Queries are requests to read state without changing it.
 * A query has exactly one target and must be answered.
 */
sealed interface Query

data class SearchQuery(
    val tx: TransactionContext,
    val query: String,
    val filters: Map<String, String> = emptyMap(),
    val maxResults: Int = 10,
    val includeAssociations: Boolean = true,
    val sessionId: String
) : Query

data class ReflectQuery(
    val tx: TransactionContext,
    val criteria: Map<String, String>,
    val scope: String
) : Query
