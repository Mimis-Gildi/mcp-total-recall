/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import java.util.*

/**
 * Test-side [TransactionContext] that carries its own causation chain.
 *
 * Production code sees [TransactionContext] (the interface). Test code casts to this
 * class when it needs chain introspection -- [head], [tail], [size].
 *
 * ## How the chain works
 *
 * The [transactionChain] is a shared mutable list passed **by reference** through
 * data class [copy]. Every `copy()` call triggers the `init` block, which appends
 * the new context to the same list. All contexts produced by [next] or [nextOverride]
 * from a common ancestor share one list.
 *
 * ```
 * val root = aTransactionContext(CONTEXT_COMPONENT_CORTEX)   // chain: [root]
 * val hop1 = (root as TransactionTestContext)
 *     .next(CONTEXT_COMPONENT_HIPPOCAMPUS)                   // chain: [root, hop1]
 * val hop2 = (hop1 as TransactionTestContext)
 *     .next(CONTEXT_COMPONENT_SALIENCE)                      // chain: [root, hop1, hop2]
 *
 * // All three share the same list:
 * root.size() == 3   // true
 * root.head() === root
 * root.tail() === hop2
 * ```
 *
 * ## Fork and reset
 *
 * [nextOverride] creates a deviation: new identity fields, same chain.
 * [reset] breaks the chain: returns a new context with a **new list** (size 1),
 * while the old chain retains all its members.
 *
 * ## Design note: where you copy from matters
 *
 * `copy()` copies the list _reference_, not the contents. A context joins whichever
 * chain its source belongs to. Forking from [tail] vs [head] produces the same chain
 * growth, but the new context's identity fields come from whichever context you called
 * `next`/`nextOverride` on.
 *
 * @see TransactionContext production interface this implements
 * @see mimis.gildi.memory.testing.aTransactionContext factory function
 * @see mimis.gildi.memory.domain.TransactionContextTest exercising all chain operations
 */
data class TransactionTestContext(
    override val instanceId: UUID,
    override val sessionId: UUID,
    override val requestId: UUID,
    override val sourceContext: String,
    private val transactionChain: MutableList<TransactionContext> = mutableListOf()
) : TransactionContext {
    init {
        transactionChain.add(this)
    }

    /** First context in this chain. */
    fun head() = transactionChain.first()

    /** Most recent context in this chain. */
    fun tail() = transactionChain.last()

    /** Number of contexts in this chain. */
    fun size() = transactionChain.size

    /**
     * Advance the chain: same instance/session/request, new [sourceContext].
     *
     * The returned context joins the same shared chain. Identity fields are preserved
     * because `copy()` only overrides [sourceContext].
     */
    fun next(sourceContext: String): TransactionContext =
        copy(sourceContext = sourceContext)

    /**
     * Advance the chain with explicit field overrides.
     *
     * Use this to simulate forks and deviations -- e.g., a new request within the
     * same session, or a completely new identity entering the same chain.
     * The returned context still joins the same shared chain.
     *
     * @param sourceContext required -- every message originates from a specific bounded context.
     */
    fun nextOverride(
        instanceId: UUID = this.instanceId,
        sessionId: UUID = this.sessionId,
        requestId: UUID = this.requestId,
        sourceContext: String,
    ): TransactionContext = copy(
        instanceId = instanceId,
        sessionId = sessionId,
        requestId = requestId,
        sourceContext = sourceContext
    )

    /**
     * Break from this chain and start a new one.
     *
     * Returns a context with a **new** empty chain (size 1). The old chain is unaffected --
     * all existing members retain their shared list. Identity fields default to this context's
     * values (inherit from wherever you reset from, not from [head]).
     */
    fun reset(
        instanceId: UUID = this.instanceId,
        sessionId: UUID = this.sessionId,
        requestId: UUID = this.requestId,
        sourceContext: String = this.sourceContext
    ): TransactionContext = TransactionTestContext(
        instanceId = instanceId,
        sessionId = sessionId,
        requestId = requestId,
        sourceContext = sourceContext,
        transactionChain = mutableListOf()
    )
}
