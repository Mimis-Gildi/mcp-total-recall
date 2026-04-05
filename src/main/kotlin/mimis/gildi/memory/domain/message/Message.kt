/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import java.time.Instant
import java.util.UUID

/**
 * Every internal message has identity, causation, and chain of custody.
 *
 * @property messageId unique identity of this message.
 * @property causationId the messageId of the message that caused this one -- self-referencing for root causes.
 * @property timestamp when this message was created.
 * @property content optional human-readable payload.
 * @property tx chain of custody -- instance, session, request, source context.
 * @property metadata optional key-value pairs attached by the emitter for routing or auditing.
 */
interface Message {
    val messageId: UUID
    val causationId: UUID
    val timestamp: Instant
    val content: String?
    val tx: TransactionContext
    val metadata: Map<String, String>?
}
