/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.outbound

/**
 * Outbound port for inter-instance relay via Agora.
 * Future: when Agora exists, this port connects Total Recall
 * to peer MCP communication.
 */
interface RelayPort {

    @Suppress("unused")
    suspend fun relay(targetInstanceId: String, payload: Map<String, String>)
}
