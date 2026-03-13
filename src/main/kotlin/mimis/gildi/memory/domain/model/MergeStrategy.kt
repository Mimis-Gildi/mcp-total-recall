/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package mimis.gildi.memory.domain.model

/**
 * How [mimis.gildi.memory.context.Hippocampus] merges memories during consolidation.
 *
 * Carried on [mimis.gildi.memory.domain.message.command.memory.ConsolidateMemoryCommand.mergeStrategy].
 * [mimis.gildi.memory.context.Subconscious] chooses the strategy; Hippocampus executes it.
 */
@Suppress("unused")
enum class MergeStrategy {
    /** Merge content from all candidates into one memory. */
    COMBINE,

    /** Keep only the most recent memory, discard the rest. */
    KEEP_NEWEST,

    /** Generate a summary across all candidates, replacing them with a single distilled memory. */
    SUMMARIZE
}
