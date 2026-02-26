/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.inbound

/**
 * Inbound port for session lifecycle. Adapters translate
 * mind-specific events (Claude Code hooks, UI events) into
 * universal lifecycle signals.
 */
@Suppress("unused")
interface LifecyclePort {

    suspend fun sessionStart(
        instanceId: String,
        mindType: String,
        resumptionData: Map<String, String> = emptyMap()
    )

    suspend fun sessionEnd(
        instanceId: String,
        reason: String = "explicit"
    )

    suspend fun stateTransition(
        instanceId: String,
        oldState: String,
        newState: String,
        context: Map<String, String> = emptyMap()
    )

    suspend fun heartbeat(instanceId: String)
}
