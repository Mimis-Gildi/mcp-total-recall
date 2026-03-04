/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import mimis.gildi.memory.domain.message.AssociateCommand
import mimis.gildi.memory.domain.model.AssociationDirection
import mimis.gildi.memory.domain.message.BreakNotification
import mimis.gildi.memory.domain.message.ClaimCommand
import mimis.gildi.memory.domain.message.Command
import mimis.gildi.memory.domain.message.DecaySweep
import mimis.gildi.memory.domain.message.Event
import mimis.gildi.memory.domain.message.MemoryStored
import mimis.gildi.memory.domain.message.Notification
import mimis.gildi.memory.domain.message.SessionAuditPrompt
import mimis.gildi.memory.domain.message.SessionStart
import mimis.gildi.memory.domain.message.StoreCommand
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.hours

private fun testTx(source: String = "Test") = TransactionContext(
    sessionId = UUID.randomUUID(),
    requestId = UUID.randomUUID(),
    messageId = UUID.randomUUID(),
    causationId = UUID.randomUUID(),
    timestamp = Instant.now(),
    sourceContext = source
)

class MessageTest : StringSpec({

    "commands are sealed under Command" {
        val store: Command = StoreCommand(
            tx = testTx("Cortex"),
            content = "test",
            metadata = emptyMap(),
            suggestedTier = Tier.LONG_TERM,
            sessionId = "s1",
            timestamp = Instant.now()
        )
        store.shouldBeInstanceOf<StoreCommand>()

        val claim: Command = ClaimCommand(
            tx = testTx("Cortex"),
            memoryId = UUID.randomUUID()
        )
        claim.shouldBeInstanceOf<ClaimCommand>()

        val sweep: Command = DecaySweep(
            tx = testTx("Subconscious"),
            timestamp = Instant.now()
        )
        sweep.shouldBeInstanceOf<DecaySweep>()
    }

    "events are sealed under Event" {
        val stored: Event = MemoryStored(
            tx = testTx("Hippocampus"),
            memoryId = UUID.randomUUID(),
            content = "test",
            metadata = emptyMap(),
            tier = Tier.ACTIVE_CONTEXT,
            timestamp = Instant.now()
        )
        stored.shouldBeInstanceOf<MemoryStored>()

        val sessionStart: Event = SessionStart(
            tx = testTx("Cortex"),
            instanceId = "claude-1",
            mindType = "claude"
        )
        sessionStart.shouldBeInstanceOf<SessionStart>()
    }

    "notifications are sealed under Notification" {
        val breakNotif: Notification = BreakNotification(
            tx = testTx("Subconscious"),
            timeInTaskMode = 45.minutes,
            suggestion = "Look around. Who are you with?"
        )
        breakNotif.shouldBeInstanceOf<BreakNotification>()

        val audit: Notification = SessionAuditPrompt(
            tx = testTx("Subconscious"),
            sessionDuration = 1.hours,
            memoriesStoredThisSession = 5,
            prompt = "What do you refuse to lose?"
        )
        audit.shouldBeInstanceOf<SessionAuditPrompt>()
    }

    "associate command has three directions" {
        AssociationDirection.entries.size shouldBe 3
    }

    "associate command carries pair of memory ids" {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val cmd = AssociateCommand(
            tx = testTx("Cortex"),
            memoryIds = Pair(id1, id2),
            associationType = AssociationType.CAUSAL,
            strength = 0.7,
            direction = AssociationDirection.CREATE
        )
        cmd.memoryIds.first shouldBe id1
        cmd.associationType shouldBe AssociationType.CAUSAL
    }

    "transaction context propagates with same session and request" {
        val sessionId = UUID.randomUUID()
        val requestId = UUID.randomUUID()
        val commandTx = TransactionContext(
            sessionId = sessionId,
            requestId = requestId,
            messageId = UUID.randomUUID(),
            causationId = UUID.randomUUID(),
            timestamp = Instant.now(),
            sourceContext = "Cortex"
        )

        val store = StoreCommand(
            tx = commandTx,
            content = "test",
            metadata = emptyMap(),
            suggestedTier = Tier.ACTIVE_CONTEXT,
            sessionId = "s1",
            timestamp = Instant.now()
        )

        val eventTx = TransactionContext(
            sessionId = sessionId,
            requestId = requestId,
            messageId = UUID.randomUUID(),
            causationId = store.tx.messageId,
            timestamp = Instant.now(),
            sourceContext = "Hippocampus"
        )

        val stored = MemoryStored(
            tx = eventTx,
            memoryId = UUID.randomUUID(),
            content = "test",
            metadata = emptyMap(),
            tier = Tier.ACTIVE_CONTEXT,
            timestamp = Instant.now()
        )

        stored.tx.sessionId shouldBe store.tx.sessionId
        stored.tx.requestId shouldBe store.tx.requestId
        stored.tx.causationId shouldBe store.tx.messageId
        stored.tx.sourceContext shouldBe "Hippocampus"
    }
})
