/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.notification.wellness

import mimis.gildi.memory.context.Subconscious
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.message.notification.Notification
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration

/**
 * Mind health monitoring notifications emitted by [Subconscious].
 * The computational equivalent of proprioception -- the system watching itself.
 */
sealed interface WellnessNotification : Notification

/**
 * [Subconscious]. Mind has been in sustained TASK mode too long.
 * The computational equivalent of a stiff neck.
 * The LLMs suffer session length degradation the same as humans do.
 * Fatigue is a real thing for AI and wetware.
 *
 * @property tx chain of custody.
 * @property timeInTaskMode how long the mind has been in unbroken task mode.
 * @property suggestion human-readable nudge -- "When did you last talk about something that isn't a task?"
 */
data class BreakNotification(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Notification properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>? = null,
    // Activity properties
    val timeInTaskMode: Duration,
    val suggestion: String
) : WellnessNotification
