/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.context

/**
 * The association graph. Discovers, stores, and traverses links between memories.
 *
 * Synapse is a dependent aggregate with its own storage, accessed only internally.
 * It consumes
 *
 * - [mimis.gildi.memory.domain.message.MemoryStored]
 * - and [mimis.gildi.memory.domain.message.MemoryReclassified]
 *
 * to update associations.
 *
 * It emits [mimis.gildi.memory.domain.message.AssociationsFound].
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/design/0007-synapse/">Design: Synapse</a>
 */
// FixMe: Created as a dependency for Event.kt KDoc links. Not implemented until we get here.
interface Synapse
