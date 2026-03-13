/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.ShouldSpec

/**
 * ShouldSpec: query contracts and default behavior.
 *
 * "should" reads naturally for expectations about read-only operations.
 * Queries don't change state -- they describe what you expect to see.
 *
 * Query routing paths:
 *
 * - [mimis.gildi.memory.domain.message.query.recall.SearchQuery]: [mimis.gildi.memory.context.Cortex] → [mimis.gildi.memory.context.Recall]
 * - [mimis.gildi.memory.domain.message.query.recall.ReflectQuery]: [mimis.gildi.memory.context.Cortex] → [mimis.gildi.memory.context.Recall]
 */
class QueryTest : ShouldSpec({

    should("!about -- query tests live in Recall specs") {
        // SearchQuery defaults (maxResults, includeAssociations, filters)
        // and ReflectQuery defaults (scope, timeSpanDays, maxCandidates)
        // are tested where they're consumed: Recall context.
    }
})
