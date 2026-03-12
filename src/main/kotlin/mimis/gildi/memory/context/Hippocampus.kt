/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.context

/**
 * The memory keeper. Stores, retrieves, and manages the lifecycle of memories.
 *
 * Hippocampus owns all persistence decisions: what tier a memory lands in,
 * when it moves, and how it responds to claims.
 * It emits
 *
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryStored],
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryAccessed],
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryClaimed],
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryRetrieved],
 * - [mimis.gildi.memory.domain.message.event.memory.TierChanged],
 * - and [mimis.gildi.memory.domain.message.event.memory.MemoryReclassified].
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/design/0005-tiered-memory/">Design: Hippocampus</a>
 */
// FixMe: Created as a dependency for Event.kt KDoc links. Not implemented until we get here.
interface Hippocampus
