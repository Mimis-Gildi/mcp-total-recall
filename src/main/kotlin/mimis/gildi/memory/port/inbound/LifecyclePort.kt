/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.inbound

import mimis.gildi.memory.domain.model.SessionEndReason
import mimis.gildi.memory.domain.model.WorkingMode

/**
 * Inbound port for session lifecycle. Adapters translate
 * mind-specific events (Claude Code hooks, UI events) into
 * universal lifecycle signals.
 */
interface LifecyclePort {

    @Suppress("unused")
    suspend fun sessionStart(
        instanceId: String,
        mindType: String,
        resumptionData: Map<String, String> = emptyMap()
    )

    @Suppress("unused")
    suspend fun sessionEnd(
        instanceId: String,
        reason: SessionEndReason = SessionEndReason.EXPLICIT
    )

    @Suppress("unused")
    suspend fun stateTransition(
        instanceId: String,
        oldMode: WorkingMode,
        newMode: WorkingMode,
        context: Map<String, String> = emptyMap()
    )

    @Suppress("unused")
    suspend fun heartbeat(instanceId: String)
}
