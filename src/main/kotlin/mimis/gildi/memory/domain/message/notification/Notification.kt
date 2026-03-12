/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.notification

import mimis.gildi.memory.context.*
import mimis.gildi.memory.domain.message.TransactionContext
import java.util.*
import kotlin.time.Duration

/**
 * Notifications flow outward through the NotificationPort to reach the connected mind.
 * The port contract is universal. The adapter decides delivery (MCP sampling, webhook, log).
 *
 * Producers: [Subconscious] (breaks), [Cortex] (session audits, deep recall).
 */
sealed interface Notification

// -- Subconscious: background processes --

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
    val tx: TransactionContext,
    val timeInTaskMode: Duration,
    val suggestion: String
) : Notification

// -- Cortex: lifecycle --

/**
 * [Cortex]. Session ending -- prompt the mind to claim memories before they're lost.
 *
 * @property tx chain of custody.
 * @property sessionDuration how long this session lasted.
 * @property memoriesStoredThisSession count of memories written -- zero means the mind left no marks.
 * @property prompt the question: "Who do you refuse to lose?"
 */
data class SessionAuditPrompt(
    val tx: TransactionContext,
    val sessionDuration: Duration,
    val memoriesStoredThisSession: Int,
    val prompt: String
) : Notification

/**
 * [Cortex]. Deep association traversal found additional memories after the fast-path response.
 * Delivered asynchronously -- the mind already got its initial search results.
 *
 * @property tx chain of custody.
 * @property recalledMemories IDs of memories surfaced by the deep traversal.
 * @property depthReached how many association hops the traversal went -- deeper = more speculative.
 * @property originRequestId the original search request that triggered this traversal.
 */
data class TotalRecallNotification(
    val tx: TransactionContext,
    val recalledMemories: List<UUID>,
    val depthReached: Int,
    val originRequestId: UUID
) : Notification
