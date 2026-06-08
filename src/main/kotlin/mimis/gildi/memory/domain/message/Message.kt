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
 * The message is the Domain Event in DDD. It survives the test of "I changed the state of things"
 * without imposing any constraints on the 'state of things' or intent.
 * It only enforces the abstraction that "I am a FACT" with no additional assumptions.
 * Because the mind is a massively parallel distributed system, this "fact" exists within another innate abstraction:
 * [TransactionContext] which can be viewed as an envelope if the message is a letter in 1922.
 * As an atomic unit of fact, the [TransactionContext] is an integral abstract part of it serving a whole family of realworld
 * crosscut concerns omnipresent and not of any concern to the actors; much like the mailman is of no concern
 * to the corresponding parties in 1922, nevertheless necessary and dependent on the existence of the letter.
 *
 * In practical terms the message is further specialized in classification by at least:
 *
 * - [mimis.gildi.memory.domain.message.event.Event] - a fact container overriding only the environmental metadata as a necessity;
 * - [mimis.gildi.memory.domain.message.command.Command] - a DDD Command construct hoping for responses;
 * - [mimis.gildi.memory.domain.message.query.Query] - a DDD Query construct demanding agency (yet to be fully implemented);
 * - [mimis.gildi.memory.domain.message.notification.Notification] - an EDA Notification construct with no expectation of consumers.
 *
 * Additional classifications may be added in the future to accommodate the currently unknown emergent nature of mind.
 *
 * @property messageId unique identity of this message.
 * @property causationId the messageId of the message that caused this one -- self-referencing for root causes.
 * @property timestamp when this message was created.
 * @property content optional human-readable payload.
 * @property tx the envelope slip for the message.
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
