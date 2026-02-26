/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

import java.time.Instant

data class SearchFilter(
    val tier: Tier? = null,
    val timeRangeStart: Instant? = null,
    val timeRangeEnd: Instant? = null,
    val associationType: AssociationType? = null,
    val tags: Set<String> = emptySet()
)
