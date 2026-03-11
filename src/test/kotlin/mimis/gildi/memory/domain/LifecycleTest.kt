/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import mimis.gildi.memory.domain.message.*
import mimis.gildi.memory.domain.model.ActivityLevel
import mimis.gildi.memory.domain.model.SessionEndReason
import mimis.gildi.memory.domain.model.WorkingMode
import mimis.gildi.memory.testing.aTx
import java.util.UUID
import java.time.Instant
import kotlin.time.Duration.Companion.minutes

/**
 * WordSpec: lifecycle state machines.
 *
 * "When ... should" nesting fits transitions between states.
 * Sessions start, modes change, heartbeats arrive, sessions end.
 * WordSpec reads like a state machine specification.
 */
class LifecycleTest : WordSpec({

    "SessionStart" When {

        "created with instance and mind type" should {
            val event = SessionStart(
                tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                mindType = "claude"
            )

            "be sealed under Event" {
                event.shouldBeInstanceOf<Event>()
            }

            "default resumptionData to empty" {
                event.resumptionData shouldBe emptyMap()
            }
        }

        "created with resumption data" should {
            val event = SessionStart(
                tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                mindType = "claude",
                resumptionData = mapOf("lastBranch" to "72-architecture")
            )

            "carry the resumption context" {
                event.resumptionData["lastBranch"] shouldBe "72-architecture"
            }
        }
    }

    "SessionEnd" When {

        "ending explicitly" should {
            val event = SessionEnd(
                tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                reason = SessionEndReason.EXPLICIT
            )

            "carry the reason" {
                event.reason shouldBe SessionEndReason.EXPLICIT
            }
        }

        "ending by timeout" should {
            val event = SessionEnd(
                tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                reason = SessionEndReason.TIMEOUT
            )

            "carry TIMEOUT reason" {
                event.reason shouldBe SessionEndReason.TIMEOUT
            }
        }
    }

    "ModeChanged" When {

        "transitioning from CONVERSATION to TASK" should {
            val event = ModeChanged(
                tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                oldMode = WorkingMode.CONVERSATION,
                newMode = WorkingMode.TASK
            )

            "record both modes" {
                event.oldMode shouldBe WorkingMode.CONVERSATION
                event.newMode shouldBe WorkingMode.TASK
            }
        }
    }

    "HeartbeatReceived" When {

        "received from an instance" should {
            val now = Instant.now()
            val event = HeartbeatReceived(
                tx = aTx(source = "Lifecycle", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                timestamp = now
            )

            "record the timestamp" {
                event.timestamp shouldBe now
            }
        }
    }

    "SessionState" When {

        "reporting active status" should {
            val event = SessionState(
                tx = aTx(source = "Cortex", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                duration = 45.minutes,
                activityLevel = ActivityLevel.ACTIVE,
                lastInteraction = Instant.now()
            )

            "carry duration as Duration type" {
                event.duration shouldBe 45.minutes
            }

            "carry activity level" {
                event.activityLevel shouldBe ActivityLevel.ACTIVE
            }
        }
    }

    "StateTransition" When {

        "recording a state change" should {
            val event = StateTransition(
                tx = aTx(source = "Lifecycle", causationId = UUID.randomUUID()),
                instanceId = "claude-1",
                oldState = "connected",
                newState = "active"
            )

            "default context to empty" {
                event.context shouldBe emptyMap()
            }
        }
    }

    "Enums" When {

        "counting entries" should {

            "WorkingMode has 3 modes" {
                WorkingMode.entries.size shouldBe 3
            }

            "SessionEndReason has 3 reasons" {
                SessionEndReason.entries.size shouldBe 3
            }

            "AssociationDirection has 3 directions" {
                mimis.gildi.memory.domain.model.AssociationDirection.entries.size shouldBe 3
            }
        }
    }
})
