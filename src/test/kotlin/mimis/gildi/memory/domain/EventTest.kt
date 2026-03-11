/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import mimis.gildi.memory.domain.message.*
import mimis.gildi.memory.domain.model.*
import mimis.gildi.memory.testing.anAssociation
import mimis.gildi.memory.testing.aSalienceScore
import mimis.gildi.memory.testing.aTx
import java.time.Instant
import java.util.UUID

/**
 * DescribeSpec: event contracts organized by bounded context.
 *
 * Nested describe blocks mirror the domain structure --
 * each bounded context gets its own section. Events are facts
 * about what happened; describe/it reads like a report.
 */
class EventTest : DescribeSpec({

    describe("Hippocampus events") {

        it("MemoryStored carries content and tier") {
            val event = MemoryStored(
                tx = aTx(source = "Hippocampus", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                content = "the tree grows",
                metadata = emptyMap(),
                tier = Tier.ACTIVE_CONTEXT,
                timestamp = Instant.now()
            )
            event.shouldBeInstanceOf<Event>()
            event.tier shouldBe Tier.ACTIVE_CONTEXT
        }

        it("MemoryAccessed records access timestamp") {
            val now = Instant.now()
            val event = MemoryAccessed(
                tx = aTx(source = "Hippocampus", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                accessTimestamp = now
            )
            event.accessTimestamp shouldBe now
        }

        it("MemoryClaimed records claim timestamp") {
            val event = MemoryClaimed(
                tx = aTx(source = "Hippocampus", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                claimTimestamp = Instant.now()
            )
            event.shouldBeInstanceOf<Event>()
        }

        it("MemoryRetrieved carries full content and metadata") {
            val event = MemoryRetrieved(
                tx = aTx(source = "Hippocampus", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                content = "the tree grows",
                metadata = mapOf("source" to "session-1"),
                tier = Tier.LONG_TERM
            )
            event.metadata["source"] shouldBe "session-1"
        }

        it("TierChanged records old and new tier with reason") {
            val event = TierChanged(
                tx = aTx(source = "Hippocampus", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                oldTier = Tier.ACTIVE_CONTEXT,
                newTier = Tier.ARCHIVE,
                reason = "decay"
            )
            event.oldTier shouldBe Tier.ACTIVE_CONTEXT
            event.newTier shouldBe Tier.ARCHIVE
        }

        it("MemoryReclassified is mind-initiated, distinct from TierChanged") {
            val event = MemoryReclassified(
                tx = aTx(source = "Hippocampus", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                oldTier = Tier.LONG_TERM,
                newTier = Tier.IDENTITY_CORE,
                reason = "mind chose to promote"
            )
            event.shouldBeInstanceOf<Event>()
            event.reason shouldBe "mind chose to promote"
        }
    }

    describe("Salience events") {

        it("TierPromoted carries new tier and score") {
            val event = TierPromoted(
                tx = aTx(source = "Salience", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                newTier = Tier.LONG_TERM,
                score = 0.85
            )
            event.score shouldBe 0.85
        }

        it("TierDemoted carries new tier and score") {
            val event = TierDemoted(
                tx = aTx(source = "Salience", causationId = UUID.randomUUID()),
                memoryId = UUID.randomUUID(),
                newTier = Tier.ARCHIVE,
                score = 0.15
            )
            event.newTier shouldBe Tier.ARCHIVE
        }

        it("SalienceScored wraps the SalienceScore model") {
            val score = aSalienceScore(score = 0.72, decayRate = 0.05)
            val event = SalienceScored(tx = aTx(source = "Salience", causationId = UUID.randomUUID()), salienceScore = score)
            event.salienceScore.score shouldBe 0.72
            event.salienceScore.decayRate shouldBe 0.05
        }
    }

    describe("Synapse events") {

        it("AssociationsFound carries a list of associations") {
            val event = AssociationsFound(
                tx = aTx(source = "Synapse", causationId = UUID.randomUUID()),
                sourceId = UUID.randomUUID(),
                associations = listOf(
                    anAssociation(type = AssociationType.THEMATIC, strength = 0.6)
                )
            )
            event.associations.size shouldBe 1
            event.associations.first().type shouldBe AssociationType.THEMATIC
        }
    }

    describe("Total Recall events") {

        it("TotalRecallAdvisory carries source memory IDs and origin request") {
            val originId = UUID.randomUUID()
            val event = TotalRecallAdvisory(
                tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
                sourceMemoryIds = setOf(UUID.randomUUID()),
                originRequestId = originId,
                timestamp = Instant.now()
            )
            event.originRequestId shouldBe originId
        }
    }
})
