/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.datatest.withContexts
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeInstanceOf
import io.kotest.matchers.types.shouldNotBeTypeOf
import mimis.gildi.memory.domain.message.DefaultTransactionContext
import mimis.gildi.memory.domain.message.TransactionContext
import mimis.gildi.memory.domain.message.TransactionTestContext
import mimis.gildi.memory.testing.*
import java.util.UUID.randomUUID

/**
 * ExpectSpec: [TransactionContext] propagation and causation chains.
 *
 * "Expect" fits contract verification -- I expect this envelope
 * to flow from command to event, preserving session and request
 * while chaining messageId → causationId.
 *
 * Each mind interaction is a saga -- a causation chain flowing through bounded contexts:
 *
 * 1. **Store memory:** Mind → [mimis.gildi.memory.context.Cortex] → [mimis.gildi.memory.context.Hippocampus] + [mimis.gildi.memory.context.Salience] + [mimis.gildi.memory.context.Synapse]
 * 2. **Search memory:** Mind → Cortex → [mimis.gildi.memory.context.Recall] (reads Hippocampus + Salience + Synapse)
 * 3. **Claim memory:** Mind → Cortex → Hippocampus + Salience
 * 4. **Decay sweep:** [mimis.gildi.memory.context.Subconscious] timer → Salience → Hippocampus
 * 5. **Session lifecycle:** Mind → Cortex → all contexts
 * 6. **Reflect:** Mind → Recall → Mind → Synapse/Hippocampus
 * 7. **Total Recall:** Search → Subconscious → Recall deep traversal → NotificationPort
 *
 * @see TransactionTestContext test introspection context
 */
class TransactionContextTest : ExpectSpec({

    // Spec-level fixture: independent chain, lazy-initialized on first access.
    val baseTx by lazy { aTransactionContext(CONTEXT_COMPONENT_SEARCH) }

    context("identity: tx fixture maintains sequence automatically") {
        // Container-scoped fixture: shared by all leaves below, sequential execution.
        val tx by lazy { aTransactionContext(CONTEXT_COMPONENT_SUBCONSCIOUS) }

        // ── Type guarantees ────────────────────────────────────────────────
        // All fixture scopes (local, container, global) produce TransactionTestContext,
        // never DefaultTransactionContext. This ensures test introspection works everywhere.
        withContexts(
            nameFn = { "Type-test ${it.first}" },
            "scope local" to tx,
            "scope container" to baseTx,
            "scope global" to rootTransaction
        ) { (_, context) ->
            context.shouldBeInstanceOf<TransactionContext>()

            context.shouldBeInstanceOf<TransactionTestContext>()
            context.shouldNotBeInstanceOf<DefaultTransactionContext>()

            context.shouldBeTypeOf<TransactionTestContext>()
            context.shouldNotBeTypeOf<DefaultTransactionContext>()

            context.shouldNotBeTypeOf<TransactionContext>()
        }

        // ── Root cause: a fresh fixture is a chain of one ──────────────────
        expect("When I ask for memory I get a root cause transaction object automatically and this object is introspectable") {
            tx.instanceId shouldNotBe null
            tx.sessionId shouldNotBe null
            tx.requestId shouldNotBe null
            tx.sourceContext shouldBe CONTEXT_COMPONENT_SUBCONSCIOUS

            val txZ = aTransactionContext()
            txZ.sourceContext shouldBe CONTEXT_COMPONENT_DEFAULT

            val txT = tx as TransactionTestContext
            txT.size() shouldBe 1
            txT.head() shouldBe tx
            txT.tail() shouldBe tx
        }

        // ── Chain increment: next() preserves identity, advances a chain ─────
        // Tests all three scope levels: global (rootTransaction), spec (baseTx), container (tx).
        // After next(), chain grows to 2. Identity fields (session, request, instance) are
        // preserved. sourceContext changes to the new bounded context.
        withContexts(
            nameFn = { "Increment-test  <${it.first.sourceContext}> to <${it.second.sourceContext}>" },
            rootTransaction to (rootTransaction as TransactionTestContext).next(CONTEXT_COMPONENT_CORTEX) as TransactionTestContext,
            baseTx to (baseTx as TransactionTestContext).next(CONTEXT_COMPONENT_CORTEX) as TransactionTestContext,
            tx to (tx as TransactionTestContext).next(CONTEXT_COMPONENT_CORTEX) as TransactionTestContext
        ) { (previous, next) ->
            next.size() shouldBe 2
            (previous as TransactionTestContext).size() shouldBe 2
            next.tail() shouldBe next
            next.head() shouldBe previous

            next.sourceContext shouldBe CONTEXT_COMPONENT_CORTEX

            next.sessionId shouldBe previous.sessionId
            next.requestId shouldBe previous.requestId
            next.instanceId shouldBe previous.instanceId
        }

        // ── Tail bump: chain grows linearly with each next() ───────────────
        expect("If I ask for another increment I receive a tail bump") {
            val txT = (tx as TransactionTestContext).next(CONTEXT_COMPONENT_RECALL) as TransactionTestContext
            txT.size() shouldBe 3
            txT.tail() shouldBe txT
            txT.head() shouldBe tx
        }

        // ── Fork, deviation, reset ─────────────────────────────────────────
        // Exercises the full lifecycle:
        //   1. nextOverride with all-new UUIDs → deviation on the same chain (size 4)
        //   2. nextOverride with partial override (requestId only) → second branch (size 5)
        //   3. next from a branch → terminal node inherits branch identity (size 6)
        //   4. reset → new chain (size 1), old chain unaffected (still 6)
        //   5. "where you copy from matters": tail().sessionId reflects the last
        //      nextOverride source, not the original root (line 167)
        expect("If I fork with a deviation then my linear history follows each deviation") {
            val txFirst = tx as TransactionTestContext
            val txFork = txFirst.tail() as TransactionTestContext

            txFork.size() shouldBe 3

            // Branch 1: full identity override → completely new instance/session/request
            val txFirstBranch = txFirst.nextOverride(
                randomUUID(),
                randomUUID(),
                randomUUID(),
                CONTEXT_COMPONENT_HIPPOCAMPUS
            ) as TransactionTestContext
            txFirst.size() shouldBe 4
            txFork.tail() shouldBe txFirstBranch
            txFirstBranch.instanceId shouldNotBe txFirst.instanceId
            txFirstBranch.sessionId shouldNotBe txFirst.sessionId
            txFirstBranch.requestId shouldNotBe txFirst.requestId

            // Branch 2: partial override (new requestId only) from a mid-chain position
            val txSecondBranch =
                txFork.nextOverride(requestId = randomUUID(), sourceContext = CONTEXT_COMPONENT_SALIENCE)
            txFirst.size() shouldBe 5
            txSecondBranch.requestId shouldNotBe txFirst.requestId
            txSecondBranch.requestId shouldNotBe txFirstBranch.requestId

            // Terminal: next from branch 1 → inherits branch 1's identity
            val txTerminal = txFirstBranch.next(CONTEXT_COMPONENT_NOTIFICATION)
            txFirst.size() shouldBe 6
            txTerminal.sessionId shouldBe txFirstBranch.sessionId
            txTerminal.requestId shouldBe txFirstBranch.requestId
            txTerminal.instanceId shouldBe txFirstBranch.instanceId

            // Reset: break from chain, start fresh. Identity defaults to source (txTerminal).
            val txReset = (txTerminal as TransactionTestContext).reset()

            txReset.sessionId shouldBe txFirstBranch.sessionId
            txReset.requestId shouldBe txFirstBranch.requestId
            txReset.instanceId shouldBe txFirstBranch.instanceId
            txReset.sourceContext shouldBe CONTEXT_COMPONENT_NOTIFICATION

            // Reset chain is independent -- size 1, self-referencing
            with(txReset as TransactionTestContext) {
                size() shouldBe 1
                head() shouldBe this
                tail() shouldBe this
            }

            // Old chain is unaffected -- all members still see size 6
            txFirst shouldBe tx
            txFirst.size() shouldBe 6
            txFork.size() shouldBe 6
            txFirstBranch.size() shouldBe 6
            (txSecondBranch as TransactionTestContext).size() shouldBe 6
            txTerminal.size() shouldBe 6

            // Reset inherited identity from txTerminal (which got it from txFirstBranch),
            // NOT from the original root (tx). Different sessions.
            tx.sessionId shouldNotBe txReset.sessionId

            txReset.sessionId shouldBe txFirstBranch.sessionId
            txReset.requestId shouldBe txFirstBranch.requestId
            txReset.instanceId shouldBe txFirstBranch.instanceId
            txReset.sourceContext shouldNotBe txFirstBranch.sourceContext
            txReset.sourceContext shouldBe CONTEXT_COMPONENT_NOTIFICATION

            // Design quirk: tail's sessionId comes from wherever the last nextOverride
            // copied from -- NOT necessarily from head. Where you copy from matters.
            txFirst.tail().sessionId shouldBe txReset.sessionId
        }

        // ── Orphan generation: next() from any position grows the same chain ─
        // Generating from tail vs head produces different identity fields
        // (each inherits from its source) but both append to the shared list.
        expect("Orphaned transaction context remains allocated until reference count garbage collected") {
            val txFirst = tx as TransactionTestContext
            txFirst.size() shouldBe 6
            val txTailGen = (txFirst.tail() as TransactionTestContext).next(sourceContext = CONTEXT_COMPONENT_SYNAPSE) as TransactionTestContext
            val txHeadGen = txFirst.next(CONTEXT_COMPONENT_MIND) as TransactionTestContext

            txFirst.size() shouldBe 8
            txHeadGen.sessionId shouldNotBe txTailGen.sessionId
        }
    }
})
