/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.DescribeSpec

/**
 * DescribeSpec: event contracts organized by bounded context.
 *
 * Nested describe blocks mirror the domain structure --
 * each bounded context gets its own section. Events are facts
 * about what happened; describe/it reads like a report.
 *
 * Event emission paths (emitter → consumers):
 *
 * [mimis.gildi.memory.context.Hippocampus] emits:
 * - [mimis.gildi.memory.domain.message.MemoryStored] → [mimis.gildi.memory.context.Salience], [mimis.gildi.memory.context.Synapse]
 * - [mimis.gildi.memory.domain.message.MemoryAccessed] → [mimis.gildi.memory.context.Salience]
 * - [mimis.gildi.memory.domain.message.MemoryClaimed] → [mimis.gildi.memory.context.Salience]
 * - [mimis.gildi.memory.domain.message.MemoryRetrieved] → [mimis.gildi.memory.context.Recall]
 * - [mimis.gildi.memory.domain.message.TierChanged] → internal bookkeeping
 * - [mimis.gildi.memory.domain.message.MemoryReclassified] → [mimis.gildi.memory.context.Synapse]
 *
 * [mimis.gildi.memory.context.Salience] emits:
 * - [mimis.gildi.memory.domain.message.TierPromoted] → [mimis.gildi.memory.context.Hippocampus]
 * - [mimis.gildi.memory.domain.message.TierDemoted] → [mimis.gildi.memory.context.Hippocampus]
 * - [mimis.gildi.memory.domain.message.SalienceScored] → [mimis.gildi.memory.context.Hippocampus]
 *
 * [mimis.gildi.memory.context.Synapse] emits:
 * - [mimis.gildi.memory.domain.message.AssociationsFound] → [mimis.gildi.memory.context.Recall]
 *
 * [mimis.gildi.memory.context.Cortex] emits:
 * - [mimis.gildi.memory.domain.message.TotalRecallAdvisory] → [mimis.gildi.memory.context.Subconscious]
 * - [mimis.gildi.memory.domain.message.SessionStart] → all contexts
 * - [mimis.gildi.memory.domain.message.SessionEnd] → all contexts
 * - [mimis.gildi.memory.domain.message.StateTransition] → [mimis.gildi.memory.context.Subconscious]
 * - [mimis.gildi.memory.domain.message.ModeChanged] → [mimis.gildi.memory.context.Subconscious]
 */
class EventTest : DescribeSpec({

    describe("!about -- event tests live in bounded context specs") {
        it("Hippocampus emitting MemoryStored belongs in HippocampusTest, not here") {}
    }
})
