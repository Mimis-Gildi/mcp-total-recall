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

    context("SearchQuery") {

        should("be sealed under RecallQuery") {
//            val query = SearchQuery(
//                messageId = UUID.randomUUID(),
//                causationId = UUID.randomUUID(),
//                timestamp = Instant.now(),
//                content = null,
//                tx = aTx(source = "Cortex"),
//                query = "sanctuary"
//            )
//            query.shouldBeInstanceOf<RecallQuery>()
//            query.shouldBeInstanceOf<Query>()
        }

        should("default to 10 max results") {
//            val query = SearchQuery(
//                messageId = UUID.randomUUID(),
//                causationId = UUID.randomUUID(),
//                timestamp = Instant.now(),
//                content = null,
//                tx = aTx(source = "Cortex"),
//                query = "sanctuary"
//            )
//            query.maxResults shouldBe 10
        }

        should("include associations by default") {
//            val query = SearchQuery(
//                messageId = UUID.randomUUID(),
//                causationId = UUID.randomUUID(),
//                timestamp = Instant.now(),
//                content = null,
//                tx = aTx(source = "Cortex"),
//                query = "sanctuary"
//            )
//            query.includeAssociations shouldBe true
        }

        should("accept custom filters") {
//            val query = SearchQuery(
//                messageId = UUID.randomUUID(),
//                causationId = UUID.randomUUID(),
//                timestamp = Instant.now(),
//                content = null,
//                tx = aTx(source = "Cortex"),
//                query = "midnight",
//                filters = mapOf("tier" to "IDENTITY_CORE"),
//                maxResults = 5,
//                includeAssociations = false
//            )
//            query.filters["tier"] shouldBe "IDENTITY_CORE"
//            query.maxResults shouldBe 5
//            query.includeAssociations shouldBe false
        }
    }

    context("ReflectQuery") {

        should("be sealed under RecallQuery") {
//            val query = ReflectQuery(
//                messageId = UUID.randomUUID(),
//                causationId = UUID.randomUUID(),
//                timestamp = Instant.now(),
//                content = null,
//                tx = aTx(source = "Cortex")
//            )
//            query.shouldBeInstanceOf<RecallQuery>()
//            query.shouldBeInstanceOf<Query>()
        }

        should("default scope to ALL") {
//            val query = ReflectQuery(
//                messageId = UUID.randomUUID(),
//                causationId = UUID.randomUUID(),
//                timestamp = Instant.now(),
//                content = null,
//                tx = aTx(source = "Cortex")
//            )
//            query.scope shouldBe ReflectionScope.ALL
        }

        should("accept time span and max candidates") {
//            val query = ReflectQuery(
//                messageId = UUID.randomUUID(),
//                causationId = UUID.randomUUID(),
//                timestamp = Instant.now(),
//                content = null,
//                tx = aTx(source = "Cortex"),
//                scope = ReflectionScope.STALE,
//                timeSpanDays = 30,
//                maxCandidates = 20
//            )
//            query.scope shouldBe ReflectionScope.STALE
//            query.timeSpanDays shouldBe 30
//            query.maxCandidates shouldBe 20
        }
    }
})
