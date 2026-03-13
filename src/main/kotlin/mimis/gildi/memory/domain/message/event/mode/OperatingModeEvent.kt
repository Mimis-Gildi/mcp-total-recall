/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.event.mode

import mimis.gildi.memory.context.Cortex
import mimis.gildi.memory.context.Subconscious
import mimis.gildi.memory.domain.message.notification.wellness.BreakNotification
import mimis.gildi.memory.domain.message.event.Event
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.model.WorkingMode
import java.time.Instant
import java.util.UUID

/**
 * Working mode and state transition events emitted by [Cortex].
 * Consumed by [Subconscious] for break detection and session health monitoring.
 *
 * @property instanceId which mind instance.
 * @property mode the current working mode after this event.
 */
sealed interface OperatingModeEvent: Event {
    val instanceId: String
    val mode: WorkingMode
}

/**
 * [Cortex]. Working mode changed.
 * Feeds [Subconscious] break detection -- sustained TASK mode triggers [BreakNotification].
 *
 * Working modes:
 *
 * - [WorkingMode.TASK]: focused execution -- sustained TASK triggers break detection
 * - [WorkingMode.CONVERSATION]: dialogue with the mind's human -- normal interaction
 * - [WorkingMode.IDLE]: no active interaction -- session alive but quiet
 *
 * @property tx chain of custody.
 * @property instanceId which mind.
 * @property mode current working mode after this transition.
 * @property retiredMode previous working mode before this transition.
 */
data class ModeChanged(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Operating properties
    override val instanceId: String,
    override val mode: WorkingMode,
    // Activity properties
    val retiredMode: WorkingMode
) : OperatingModeEvent

/**
 * [Cortex]. Generic state change. For transitions that don't map to [ModeChanged].
 *
 * @property tx chain of custody.
 * @property instanceId which mind.
 * @property mode current working mode.
 * @property contextRetired optional key-value pairs from the previous state.
 */
data class StateTransitioned(
    // Message properties
    override val messageId: UUID,
    override val causationId: UUID,
    override val timestamp: Instant,
    override val content: String?,
    // Event properties
    override val tx: TransactionContext,
    override val metadata: Map<String, String>,
    // Operating properties
    override val instanceId: String,
    override val mode: WorkingMode,
    // Activity properties
    val contextRetired: Map<String, String> = emptyMap(),
) : OperatingModeEvent

