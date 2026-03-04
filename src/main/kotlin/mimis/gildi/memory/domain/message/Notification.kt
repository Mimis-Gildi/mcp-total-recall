/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import java.util.UUID
import kotlin.time.Duration

/**
 * Notifications flow outward through the Notification Port to reach
 * the connected mind. The port contract is universal. The adapter
 * decides delivery.
 */
sealed interface Notification

@Suppress("unused")
data class BreakNotification(
    val tx: TransactionContext,
    val timeInTaskMode: Duration,
    val suggestion: String
) : Notification

data class SessionAuditPrompt(
    val tx: TransactionContext,
    val sessionDuration: Duration,
    val memoriesStoredThisSession: Int,
    val prompt: String
) : Notification

@Suppress("unused")
data class TotalRecallNotification(
    val tx: TransactionContext,
    val recalledMemories: List<UUID>,
    val depthReached: Int,
    val originRequestId: UUID
) : Notification
