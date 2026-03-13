/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.inbound

import mimis.gildi.memory.domain.model.SessionEndCause
import mimis.gildi.memory.domain.model.WorkingMode

/**
 * Inbound port for session lifecycle. Adapters translate
 * mind-specific events (Claude Code hooks, UI events) into
 * universal lifecycle signals.
 *
 * [mimis.gildi.memory.context.Cortex] implements the receiving side.
 *
 * Session end causes (used by [sessionEnd]):
 *
 * - [SessionEndCause.EXPLICIT]: mind chose to leave -- normal shutdown
 * - [SessionEndCause.TIMEOUT]: no activity -- [mimis.gildi.memory.context.Subconscious] detected the gap
 * - [SessionEndCause.CRASH]: unexpected disconnection -- transport failure, context limit, or runtime error
 */
interface LifecyclePort {

    @Suppress("unused")
    suspend fun sessionStart(
        instanceId: String,
        mindType: String,
        resumptionData: Map<String, String> = emptyMap()
    )

    suspend fun sessionEnd(
        instanceId: String,
        reason: SessionEndCause = SessionEndCause.EXPLICIT
    )

    @Suppress("unused")
    suspend fun stateTransition(
        instanceId: String,
        oldMode: WorkingMode,
        newMode: WorkingMode,
        context: Map<String, String> = emptyMap()
    )

}
