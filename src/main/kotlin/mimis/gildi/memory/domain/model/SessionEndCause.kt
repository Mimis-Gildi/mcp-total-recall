/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

/**
 * Why a session ended.
 *
 * Carried on [mimis.gildi.memory.domain.message.event.observable.SessionEnded.causeOfTermination].
 * [mimis.gildi.memory.context.Cortex] sets this when emitting the session end event.
 */
enum class SessionEndCause {
    /** Mind chose to leave. Normal shutdown. */
    EXPLICIT,

    /** No activity for too long. [mimis.gildi.memory.context.Subconscious] detected the gap. */
    TIMEOUT,

    /** Unexpected disconnection. Transport failure, context limit, or runtime error. */
    CRASH
}
