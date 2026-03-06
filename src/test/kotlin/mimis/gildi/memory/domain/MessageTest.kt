/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import mimis.gildi.memory.domain.message.*
import mimis.gildi.memory.domain.model.*
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

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
            sessionId = UUID.randomUUID(),
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

    "remaining commands are sealed under Command" {
        val reclass: Command = ReclassifyCommand(
            tx = testTx("Cortex"),
            memoryId = UUID.randomUUID(),
            newTier = Tier.IDENTITY_CORE,
            reason = "this is who I am"
        )
        reclass.shouldBeInstanceOf<ReclassifyCommand>()

        val consolidate: Command = ConsolidateCommand(
            tx = testTx("Subconscious"),
            memoryIds = listOf(UUID.randomUUID(), UUID.randomUUID()),
            mergeStrategy = "keep-latest"
        )
        consolidate.shouldBeInstanceOf<ConsolidateCommand>()

        val shutdown: Command = ShutdownCommand(
            tx = testTx("Subconscious"),
            coldStorageTarget = "/archive/2026",
            flushTimeout = 30.seconds
        )
        shutdown.shouldBeInstanceOf<ShutdownCommand>()
    }

    "queries are sealed under Query" {
        val search: Query = SearchQuery(
            tx = testTx("Cortex"),
            query = "sanctuary",
            sessionId = UUID.randomUUID()
        )
        search.shouldBeInstanceOf<SearchQuery>()
        search.maxResults shouldBe 10
        search.includeAssociations shouldBe true

        val reflect: Query = ReflectQuery(
            tx = testTx("Cortex"),
            scope = "stale",
            timeSpanDays = 30
        )
        reflect.shouldBeInstanceOf<ReflectQuery>()
    }

    "remaining hippocampus events are sealed under Event" {
        val accessed: Event = MemoryAccessed(
            tx = testTx("Hippocampus"),
            memoryId = UUID.randomUUID(),
            accessTimestamp = Instant.now()
        )
        accessed.shouldBeInstanceOf<MemoryAccessed>()

        val claimed: Event = MemoryClaimed(
            tx = testTx("Hippocampus"),
            memoryId = UUID.randomUUID(),
            claimTimestamp = Instant.now()
        )
        claimed.shouldBeInstanceOf<MemoryClaimed>()

        val retrieved: Event = MemoryRetrieved(
            tx = testTx("Hippocampus"),
            memoryId = UUID.randomUUID(),
            content = "the tree grows",
            metadata = mapOf("source" to "session-1"),
            tier = Tier.LONG_TERM
        )
        retrieved.shouldBeInstanceOf<MemoryRetrieved>()

        val tierChanged: Event = TierChanged(
            tx = testTx("Hippocampus"),
            memoryId = UUID.randomUUID(),
            oldTier = Tier.ACTIVE_CONTEXT,
            newTier = Tier.ARCHIVE,
            reason = "decay"
        )
        tierChanged.shouldBeInstanceOf<TierChanged>()

        val reclassified: Event = MemoryReclassified(
            tx = testTx("Hippocampus"),
            memoryId = UUID.randomUUID(),
            oldTier = Tier.LONG_TERM,
            newTier = Tier.IDENTITY_CORE,
            reason = "mind chose to promote"
        )
        reclassified.shouldBeInstanceOf<MemoryReclassified>()
    }

    "salience events are sealed under Event" {
        val promoted: Event = TierPromoted(
            tx = testTx("Salience"),
            memoryId = UUID.randomUUID(),
            newTier = Tier.LONG_TERM,
            score = 0.85
        )
        promoted.shouldBeInstanceOf<TierPromoted>()

        val demoted: Event = TierDemoted(
            tx = testTx("Salience"),
            memoryId = UUID.randomUUID(),
            newTier = Tier.ARCHIVE,
            score = 0.15
        )
        demoted.shouldBeInstanceOf<TierDemoted>()
    }

    "salience scored wraps SalienceScore model" {
        val memId = UUID.randomUUID()
        val model = SalienceScore(
            memoryId = memId,
            score = 0.72,
            lastAccessed = Instant.now(),
            decayRate = 0.05
        )
        val event = SalienceScored(
            tx = testTx("Salience"),
            salienceScore = model
        )
        event.salienceScore.memoryId shouldBe memId
        event.salienceScore.score shouldBe 0.72
        event.salienceScore.decayRate shouldBe 0.05
    }

    "synapse and total recall events are sealed under Event" {
        val found: Event = AssociationsFound(
            tx = testTx("Synapse"),
            sourceId = UUID.randomUUID(),
            associations = listOf(
                Association(
                    memoryId = UUID.randomUUID(),
                    type = AssociationType.THEMATIC,
                    strength = 0.6
                )
            )
        )
        found.shouldBeInstanceOf<AssociationsFound>()

        val advisory: Event = TotalRecallAdvisory(
            tx = testTx("Cortex"),
            sourceMemoryIds = setOf(UUID.randomUUID()),
            originRequestId = UUID.randomUUID(),
            timestamp = Instant.now()
        )
        advisory.shouldBeInstanceOf<TotalRecallAdvisory>()
    }

    "lifecycle events use enums and Duration" {
        val ended: Event = SessionEnd(
            tx = testTx("Cortex"),
            instanceId = "claude-1",
            reason = SessionEndReason.TIMEOUT
        )
        ended.shouldBeInstanceOf<SessionEnd>()
        ended.reason shouldBe SessionEndReason.TIMEOUT

        val modeChanged: Event = ModeChanged(
            tx = testTx("Cortex"),
            instanceId = "claude-1",
            oldMode = WorkingMode.CONVERSATION,
            newMode = WorkingMode.TASK
        )
        modeChanged.shouldBeInstanceOf<ModeChanged>()

        val heartbeat: Event = HeartbeatReceived(
            tx = testTx("Lifecycle"),
            instanceId = "claude-1",
            timestamp = Instant.now()
        )
        heartbeat.shouldBeInstanceOf<HeartbeatReceived>()

        val state: Event = SessionState(
            tx = testTx("Cortex"),
            instanceId = "claude-1",
            duration = 45.minutes,
            activityLevel = "active",
            lastInteraction = Instant.now()
        )
        state.shouldBeInstanceOf<SessionState>()
        state.duration shouldBe 45.minutes

        val transition: Event = StateTransition(
            tx = testTx("Lifecycle"),
            instanceId = "claude-1",
            oldState = "connected",
            newState = "active"
        )
        transition.shouldBeInstanceOf<StateTransition>()
    }

    "TotalRecallNotification is sealed under Notification" {
        val recall: Notification = TotalRecallNotification(
            tx = testTx("Recall"),
            recalledMemories = listOf(UUID.randomUUID(), UUID.randomUUID()),
            depthReached = 3,
            originRequestId = UUID.randomUUID()
        )
        recall.shouldBeInstanceOf<TotalRecallNotification>()
    }

    "enums have correct entry counts" {
        WorkingMode.entries.size shouldBe 3
        SessionEndReason.entries.size shouldBe 3
        Tier.entries.size shouldBe 4
        AssociationType.entries.size shouldBe 5
    }

    "decay sweep scope defaults to null for all tiers" {
        val sweep = DecaySweep(
            tx = testTx("Subconscious"),
            timestamp = Instant.now()
        )
        sweep.scope shouldBe null

        val scoped = DecaySweep(
            tx = testTx("Subconscious"),
            timestamp = Instant.now(),
            scope = Tier.ARCHIVE
        )
        scoped.scope shouldBe Tier.ARCHIVE
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
            sessionId = sessionId,
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
