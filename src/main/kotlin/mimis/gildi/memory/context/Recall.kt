/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.context

/**
 * The CQRS read side. No state, no storage -- assembles search responses
 * from [Hippocampus], [Salience], and [Synapse] at query time.
 *
 * Recall consumes
 *
 * - [mimis.gildi.memory.domain.message.MemoryRetrieved] and
 * - [mimis.gildi.memory.domain.message.AssociationsFound]
 *
 * to build complete search results for the mind.
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/design/0008-recall/">Design: Recall</a>
 */
// FixMe: Created as a dependency for Event.kt KDoc links. Not implemented until we get here.
interface Recall
