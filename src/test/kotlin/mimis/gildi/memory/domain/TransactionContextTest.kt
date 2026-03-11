/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import mimis.gildi.memory.domain.message.*
import mimis.gildi.memory.domain.model.Tier
import mimis.gildi.memory.testing.TxChain
import mimis.gildi.memory.testing.aTx
import mimis.gildi.memory.testing.rootCauseTx
import java.time.Instant
import java.util.UUID

/**
 * ExpectSpec: TransactionContext propagation and causation chains.
 *
 * "expect" fits contract verification -- I expect this envelope
 * to flow from command to event, preserving session and request
 * while chaining messageId → causationId.
 */
class TransactionContextTest : ExpectSpec({

    context("causation chain") {

        expect("event causationId matches command messageId") {
            val chain = TxChain(source = "Cortex")
            val commandTx = chain.next()

            val store = StoreCommand(
                tx = commandTx,
                content = "test",
                metadata = emptyMap(),
                suggestedTier = Tier.ACTIVE_CONTEXT,
                sessionId = chain.sessionId,
                timestamp = Instant.now()
            )

            val eventTx = aTx(
                sessionId = chain.sessionId,
                causationId = store.tx.messageId,
                source = "Hippocampus"
            )

            val stored = MemoryStored(
                tx = eventTx,
                memoryId = UUID.randomUUID(),
                content = "test",
                metadata = emptyMap(),
                tier = Tier.ACTIVE_CONTEXT,
                timestamp = Instant.now()
            )

            stored.tx.sessionId shouldBe store.tx.sessionId
            stored.tx.causationId shouldBe store.tx.messageId
        }

        expect("TxChain auto-chains causation across calls") {
            val chain = TxChain(source = "Hippocampus")
            val first = chain.next()
            val second = chain.next()

            first.causationId shouldBe first.messageId
            second.causationId shouldBe first.messageId
        }

        expect("rootCauseTx self-references causationId to messageId") {
            val tx = rootCauseTx(source = "Cortex")
            tx.causationId shouldBe tx.messageId
        }
    }

    context("source context routing") {

        expect("each bounded context identifies itself") {
            val cortex = rootCauseTx(source = "Cortex")
            val hippo = rootCauseTx(source = "Hippocampus")
            val salience = rootCauseTx(source = "Salience")

            cortex.sourceContext shouldBe "Cortex"
            hippo.sourceContext shouldBe "Hippocampus"
            salience.sourceContext shouldBe "Salience"
        }
    }

    context("identity") {

        expect("every message gets a unique messageId") {
            val tx1 = rootCauseTx()
            val tx2 = rootCauseTx()
            tx1.messageId shouldNotBe tx2.messageId
        }

        expect("TxChain preserves sessionId across the chain") {
            val chain = TxChain(source = "Cortex")
            val tx1 = chain.next()
            val tx2 = chain.next()
            tx1.sessionId shouldBe tx2.sessionId
        }

        expect("different TxChains have different sessions") {
            val chain1 = TxChain(source = "Cortex")
            val chain2 = TxChain(source = "Hippocampus")
            chain1.sessionId shouldNotBe chain2.sessionId
        }
    }
})
