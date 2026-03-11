/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
@file:Suppress("unused")

package mimis.gildi.memory.domain.model

enum class MergeStrategy {
    @Suppress("unused")
    COMBINE,
    KEEP_NEWEST,
    SUMMARIZE
}
