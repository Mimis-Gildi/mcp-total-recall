/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import mimis.gildi.memory.domain.model.Association
import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.SalienceScore
import mimis.gildi.memory.domain.model.Memory
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

class ModelTest : StringSpec({

    "tiers have four levels" {
        Tier.entries.size shouldBe 4
    }

    "association types cover all five categories" {
        AssociationType.entries.map { it.name } shouldBe listOf(
            "TEMPORAL", "CAUSAL", "THEMATIC", "EMOTIONAL", "PERSON"
        )
    }

    "memory holds its shape" {
        val now = Instant.now()
        val id = UUID.randomUUID()
        val memory = Memory(
            id = id,
            content = "the tree grows",
            metadata = mapOf("source" to "test"),
            tier = Tier.LONG_TERM,
            createdAt = now,
            lastAccessed = now,
            sessionId = UUID.randomUUID()
        )
        memory.id shouldBe id
        memory.claimed shouldBe false
        memory.tier shouldBe Tier.LONG_TERM
    }

    "associations are bidirectional by default" {
        val assoc = Association(
            memoryId = UUID.randomUUID(),
            type = AssociationType.THEMATIC,
            strength = 0.8
        )
        assoc.bidirectional shouldBe true
    }

    "salience score carries decay info" {
        val score = SalienceScore(
            memoryId = UUID.randomUUID(),
            score = 0.95,
            lastAccessed = Instant.now(),
            decayRate = 0.01,
            claimed = true
        )
        score.claimed shouldBe true
        score.score shouldBe 0.95
    }
})
