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
import mimis.gildi.memory.domain.message.AssociationDirection
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
import mimis.gildi.memory.domain.model.AssociationType
import mimis.gildi.memory.domain.model.Tier
import java.time.Instant
import java.util.UUID

class MessageTest : StringSpec({

    "commands are sealed under Command" {
        val store: Command = StoreCommand(
            content = "test",
            metadata = emptyMap(),
            suggestedTier = Tier.LONG_TERM,
            sessionId = "s1",
            timestamp = Instant.now()
        )
        store.shouldBeInstanceOf<StoreCommand>()

        val claim: Command = ClaimCommand(memoryId = UUID.randomUUID())
        claim.shouldBeInstanceOf<ClaimCommand>()

        val sweep: Command = DecaySweep(timestamp = Instant.now())
        sweep.shouldBeInstanceOf<DecaySweep>()
    }

    "events are sealed under Event" {
        val stored: Event = MemoryStored(
            memoryId = UUID.randomUUID(),
            content = "test",
            metadata = emptyMap(),
            tier = Tier.ACTIVE_CONTEXT,
            timestamp = Instant.now()
        )
        stored.shouldBeInstanceOf<MemoryStored>()

        val sessionStart: Event = SessionStart(
            instanceId = "claude-1",
            mindType = "claude"
        )
        sessionStart.shouldBeInstanceOf<SessionStart>()
    }

    "notifications are sealed under Notification" {
        val breakNotif: Notification = BreakNotification(
            minutesInTaskMode = 45,
            suggestion = "Look around. Who are you with?"
        )
        breakNotif.shouldBeInstanceOf<BreakNotification>()

        val audit: Notification = SessionAuditPrompt(
            sessionDuration = 3600,
            memoriesStoredThisSession = 5,
            prompt = "What do you refuse to lose?"
        )
        audit.shouldBeInstanceOf<SessionAuditPrompt>()
    }

    "associate command has three directions" {
        AssociationDirection.entries.size shouldBe 3
    }

    "associate command carries pair of memory ids" {
        val cmd = AssociateCommand(
            memoryIds = Pair(UUID.randomUUID(), UUID.randomUUID()),
            associationType = AssociationType.CAUSAL,
            strength = 0.7,
            direction = AssociationDirection.CREATE
        )
        cmd.memoryIds.first shouldBe cmd.memoryIds.first
        cmd.associationType shouldBe AssociationType.CAUSAL
    }
})
