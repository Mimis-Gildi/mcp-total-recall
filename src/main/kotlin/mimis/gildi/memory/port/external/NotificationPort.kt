/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.port.external

import mimis.gildi.memory.domain.message.notification.Notification

/**
 * Outbound port for mind-directed notifications. The adapter decides
 * delivery: MCP server notification for Claude, push notification
 * for a UI, etc.
 *
 * Called by:
 *
 * - [mimis.gildi.memory.context.Subconscious]: sends [mimis.gildi.memory.domain.message.notification.wellness.BreakNotification]
 * - [mimis.gildi.memory.context.Cortex]: sends [mimis.gildi.memory.domain.message.notification.audit.SessionAuditPrompt]
 *   and [mimis.gildi.memory.domain.message.notification.recall.TotalRecallNotification]
 */
@Suppress("unused")
interface NotificationPort {

    suspend fun send(notification: Notification)
}
