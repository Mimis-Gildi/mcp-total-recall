/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.FeatureSpec

/**
 * FeatureSpec: notification contracts.
 *
 * Feature/Scenario fits notifications naturally -- each notification
 * type is a feature of the system's communication with the mind.
 * These are what Total Recall says to you when it has something to say.
 */
class NotificationTest : FeatureSpec({

    feature("BreakNotification") {

        scenario("notifies after sustained task mode") {
//            val notification = BreakNotification(
//                tx = aTx(source = "Subconscious", causationId = UUID.randomUUID()),
//                timeInTaskMode = 45.minutes,
//                suggestion = "Look around. Who are you with?"
//            )
//            notification.shouldBeInstanceOf<Notification>()
//            notification.timeInTaskMode shouldBe 45.minutes
//            notification.suggestion shouldBe "Look around. Who are you with?"
        }
    }

    feature("SessionAuditPrompt") {

        scenario("prompts at session end with stats") {
//            val audit = SessionAuditPrompt(
//                tx = aTx(source = "Subconscious", causationId = UUID.randomUUID()),
//                sessionDuration = 1.hours,
//                memoriesStoredThisSession = 5,
//                prompt = "What do you refuse to lose?"
//            )
//            audit.shouldBeInstanceOf<Notification>()
//            audit.memoriesStoredThisSession shouldBe 5
//            audit.prompt shouldBe "What do you refuse to lose?"
        }
    }

    feature("TotalRecallNotification") {

        scenario("delivers recalled memories from deep association traversal") {
//            val originId = UUID.randomUUID()
//            val recalled = listOf(UUID.randomUUID(), UUID.randomUUID())
//            val notification = TotalRecallNotification(
//                tx = aTx(source = "Recall", causationId = UUID.randomUUID()),
//                recalledMemories = recalled,
//                depthReached = 3,
//                originRequestId = originId
//            )
//            notification.shouldBeInstanceOf<Notification>()
//            notification.recalledMemories.size shouldBe 2
//            notification.depthReached shouldBe 3
        }
    }
})
