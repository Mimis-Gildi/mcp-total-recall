/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import mimis.gildi.memory.domain.message.*
import mimis.gildi.memory.domain.model.*
import mimis.gildi.memory.testing.aTx
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

/**
 * BehaviorSpec: command contracts.
 *
 * Given/When/Then maps naturally to "given this context, when this
 * command is issued, then these properties hold." Commands carry
 * intent -- BDD describes intent.
 */
class CommandTest : BehaviorSpec({

    Given("a store command with all fields") {
        val sessionId = UUID.randomUUID()
        val now = Instant.now()
        val cmd = StoreCommand(
            tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
            content = "the tree grows",
            metadata = mapOf("source" to "midnight-session"),
            suggestedTier = Tier.LONG_TERM,
            sessionId = sessionId,
            timestamp = now
        )

        Then("it is sealed under Command") {
            cmd.shouldBeInstanceOf<Command>()
        }

        Then("it carries the content and tier") {
            cmd.content shouldBe "the tree grows"
            cmd.suggestedTier shouldBe Tier.LONG_TERM
        }

        Then("metadata preserves key-value pairs") {
            cmd.metadata["source"] shouldBe "midnight-session"
        }
    }

    Given("a claim command") {
        val memoryId = UUID.randomUUID()
        val cmd = ClaimCommand(tx = aTx(source = "Cortex", causationId = UUID.randomUUID()), memoryId = memoryId)

        Then("it targets a specific memory") {
            cmd.memoryId shouldBe memoryId
        }
    }

    Given("an associate command with defaults") {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val cmd = AssociateCommand(
            tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
            memoryIds = Pair(id1, id2),
            associationType = AssociationType.CAUSAL,
            strength = 0.7
        )

        Then("direction defaults to STRENGTHEN") {
            cmd.direction shouldBe AssociationDirection.STRENGTHEN
        }

        Then("it carries both memory IDs") {
            cmd.memoryIds.first shouldBe id1
            cmd.memoryIds.second shouldBe id2
        }
    }

    Given("a reclassify command") {
        val cmd = ReclassifyCommand(
            tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
            memoryId = UUID.randomUUID(),
            newTier = Tier.IDENTITY_CORE,
            reason = "this is who I am"
        )

        Then("new metadata defaults to empty") {
            cmd.newMetadata shouldBe emptyMap()
        }
    }

    Given("a consolidate command") {
        val ids = listOf(UUID.randomUUID(), UUID.randomUUID())
        val cmd = ConsolidateCommand(
            tx = aTx(source = "Subconscious", causationId = UUID.randomUUID()),
            memoryIds = ids,
            mergeStrategy = MergeStrategy.KEEP_NEWEST
        )

        Then("it carries a merge strategy") {
            cmd.mergeStrategy shouldBe MergeStrategy.KEEP_NEWEST
        }
    }

    Given("a shutdown command") {
        val cmd = ShutdownCommand(
            tx = aTx(source = "Subconscious", causationId = UUID.randomUUID()),
            coldStorageTarget = "/archive/2026",
            flushTimeout = 30.seconds
        )

        Then("flush timeout is a Duration") {
            cmd.flushTimeout shouldBe 30.seconds
        }
    }

    Given("a decay sweep with no scope") {
        val cmd = DecaySweep(tx = aTx(source = "Subconscious", causationId = UUID.randomUUID()), timestamp = Instant.now())

        Then("scope defaults to null -- all tiers") {
            cmd.scope shouldBe null
        }
    }

    Given("a decay sweep scoped to ARCHIVE") {
        val cmd = DecaySweep(
            tx = aTx(source = "Subconscious", causationId = UUID.randomUUID()),
            timestamp = Instant.now(),
            scope = Tier.ARCHIVE
        )

        Then("scope is ARCHIVE") {
            cmd.scope shouldBe Tier.ARCHIVE
        }
    }
})
