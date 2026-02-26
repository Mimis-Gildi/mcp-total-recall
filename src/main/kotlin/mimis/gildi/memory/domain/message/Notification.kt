/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

/**
 * Notifications flow outward through the Notification Port to reach
 * the connected mind. The port contract is universal. The adapter
 * decides delivery.
 */
sealed interface Notification

data class BreakNotification(
    val minutesInTaskMode: Long,
    val suggestion: String
) : Notification

data class SessionAuditPrompt(
    val sessionDuration: Long,
    val memoriesStoredThisSession: Int,
    val prompt: String
) : Notification
