/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.outbound

/**
 * Outbound port for inter-instance relay via Agora.
 *
 * Future: when Agora exists, this port connects Total Recall
 * to peer MCP communication. Until then, no adapter implements this.
 *
 * Called by [mimis.gildi.memory.context.Cortex] when a mind requests
 * cross-instance communication.
 */
@Suppress("unused")
interface RelayPort {

    suspend fun relay(targetInstanceId: String, payload: Map<String, String>)
}
