/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.outbound

import mimis.gildi.memory.domain.message.Notification

/**
 * Outbound port for mind-directed notifications. The adapter decides
 * delivery: MCP server notification for Claude, push notification
 * for a UI, etc.
 */
interface NotificationPort {

    @Suppress("unused")
    suspend fun send(notification: Notification)
}
