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
 *
 * Notification routing paths:
 *
 * - [mimis.gildi.memory.domain.message.notification.wellness.BreakNotification]: [mimis.gildi.memory.context.Subconscious] → NotificationPort → mind
 * - [mimis.gildi.memory.domain.message.notification.audit.SessionAuditPrompt]: [mimis.gildi.memory.context.Cortex] → NotificationPort → mind
 * - [mimis.gildi.memory.domain.message.notification.recall.TotalRecallNotification]: [mimis.gildi.memory.context.Cortex] → NotificationPort → mind
 */
class NotificationTest : FeatureSpec({

    feature("!about -- notification tests live in their emitting context specs") {
        scenario("BreakNotification in SubconsciousTest, SessionAuditPrompt and TotalRecallNotification in CortexTest") {}
    }
})
