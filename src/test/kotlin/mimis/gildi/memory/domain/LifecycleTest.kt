/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.WordSpec

/**
 * WordSpec: lifecycle state machines.
 *
 * "When ... should" nesting fits transitions between states.
 * Sessions start, modes change, sessions end.
 * WordSpec reads like a state machine specification.
 *
 * Lifecycle is owned by [mimis.gildi.memory.context.Cortex]. All lifecycle events originate there:
 *
 * - [mimis.gildi.memory.domain.message.SessionStart]: mind connects → load identity, resume state
 * - [mimis.gildi.memory.domain.message.SessionEnd]: mind disconnects → audit, flush, notify
 * - [mimis.gildi.memory.domain.message.ModeChanged]: working mode shift → [mimis.gildi.memory.context.Subconscious] break detection
 * - [mimis.gildi.memory.domain.message.StateTransition]: generic state change → [mimis.gildi.memory.context.Subconscious]
 */
class LifecycleTest : WordSpec({

    "!about -- lifecycle tests live in Cortex and Subconscious specs" When {
        "Cortex owns session state machines" should {
            "Subconscious owns break detection and timeout monitoring" {}
        }
    }
})
