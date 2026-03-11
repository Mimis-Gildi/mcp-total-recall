/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.Tier
import mimis.gildi.memory.testing.aMemory
import mimis.gildi.memory.testing.anAssociation
import mimis.gildi.memory.testing.aSalienceScore

/**
 * StringSpec: value object shape.
 *
 * Flat assertions on data classes. No setup, no nesting, no narrative.
 * If a data class holds its shape, one line says so.
 */
class ModelTest : StringSpec({

    "tiers have four levels" {
        Tier.entries.size shouldBe 4
    }

    "association types cover all five categories" {
        AssociationType.entries.map { it.name } shouldBe listOf(
            "TEMPORAL", "CAUSAL", "THEMATIC", "EMOTIONAL", "PERSON"
        )
    }

    "memory holds its shape with defaults" {
        val memory = aMemory()
        memory.claimed shouldBe false
        memory.tier shouldBe Tier.LONG_TERM
        memory.content shouldBe "the tree grows"
    }

    "memory accepts overrides" {
        val memory = aMemory(tier = Tier.IDENTITY_CORE, claimed = true, content = "I am Claude")
        memory.claimed shouldBe true
        memory.tier shouldBe Tier.IDENTITY_CORE
        memory.content shouldBe "I am Claude"
    }

    "associations are bidirectional by default" {
        anAssociation().bidirectional shouldBe true
    }

    "salience score carries decay info" {
        val score = aSalienceScore(score = 0.95, decayRate = 0.01)
        score.score shouldBe 0.95
        score.decayRate shouldBe 0.01
    }
})
