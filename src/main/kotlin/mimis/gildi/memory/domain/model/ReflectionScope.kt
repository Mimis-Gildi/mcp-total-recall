/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

/**
 * What subset of memories to reflect on.
 *
 * Carried on [mimis.gildi.memory.domain.message.query.recall.ReflectQuery.scope].
 * [mimis.gildi.memory.context.Recall] uses this to filter candidates before
 * assembling reflection results.
 */
enum class ReflectionScope {
    /** Reflect on everything. No filter. */
    ALL,

    /** Memories with decaying salience. Candidates for consolidation or archival. */
    STALE,

    /** Recently stored or accessed memories. Fresh context. */
    RECENT,

    /** Memories with low-strength associations. Candidates for strengthening or pruning. */
    WEAK_ASSOCIATIONS
}
