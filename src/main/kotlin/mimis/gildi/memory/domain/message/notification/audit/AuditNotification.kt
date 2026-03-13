/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.notification.audit

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.message.notification.Notification
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration

/**
 * Session end-of-life notifications emitted by [Cortex].
 * Prompts the mind to claim memories before they're lost.
 */
sealed interface AuditNotification : Notification

/**
 * [Cortex]. Session ending -- prompt the mind to claim memories before they're lost.
 *
 * @property tx chain of custody.
 * @property sessionDuration how long this session lasted.
 * @property memoriesStoredThisSession count of memories written -- zero means the mind left no marks.
 * @property prompt the question: "Who do you refuse to lose?"
 */
data class SessionAuditPrompt(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Notification properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>? = null,
    // Activity properties
    val sessionDuration: Duration,
    val memoriesStoredThisSession: Int,
    val prompt: String
) : AuditNotification
