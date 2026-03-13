/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

/**
 * What the mind is doing right now.
 *
 * Carried on [mimis.gildi.memory.domain.message.event.mode.ModeChanged] and
 * [mimis.gildi.memory.domain.message.event.mode.StateTransitioned].
 * [mimis.gildi.memory.context.Subconscious] watches mode changes to detect
 * sustained task mode and trigger break notifications.
 */
enum class WorkingMode {
    /** Focused execution. Sustained TASK mode triggers break detection. */
    TASK,

    /** Dialogue with the mind's human. Normal interaction. */
    CONVERSATION,

    /** No active interaction. Session alive but quiet. */
    IDLE
}
