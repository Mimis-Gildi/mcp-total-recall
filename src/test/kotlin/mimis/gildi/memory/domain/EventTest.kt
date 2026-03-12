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
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryStored] → [mimis.gildi.memory.context.Salience], [mimis.gildi.memory.context.Synapse]
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryAccessed] → [mimis.gildi.memory.context.Salience]
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryClaimed] → [mimis.gildi.memory.context.Salience]
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryRetrieved] → [mimis.gildi.memory.context.Recall]
 * - [mimis.gildi.memory.domain.message.event.memory.TierChanged] → internal bookkeeping
 * - [mimis.gildi.memory.domain.message.event.memory.MemoryReclassified] → [mimis.gildi.memory.context.Synapse]
 *
 * [mimis.gildi.memory.context.Salience] emits recommendations:
 * - [mimis.gildi.memory.domain.message.event.memory.AttentionTierPromotionRequested] → [mimis.gildi.memory.context.Hippocampus]
 * - [mimis.gildi.memory.domain.message.event.memory.AttentionTierDemotionRequested] → [mimis.gildi.memory.context.Hippocampus]
 * - [mimis.gildi.memory.domain.message.event.memory.AttentionScoreChanged] → [mimis.gildi.memory.context.Hippocampus]
 *
 * [mimis.gildi.memory.context.Synapse] emits:
 * - [mimis.gildi.memory.domain.message.event.memory.AssociationsFound] → [mimis.gildi.memory.context.Recall]
 *
 * [mimis.gildi.memory.context.Cortex] emits:
 * - [mimis.gildi.memory.domain.message.event.recall.TotalRecallAdvisoryRequested] → [mimis.gildi.memory.context.Subconscious]
 * - [mimis.gildi.memory.domain.message.event.observable.SessionStarted] → all contexts
 * - [mimis.gildi.memory.domain.message.event.observable.SessionEnded] → all contexts
 * - [mimis.gildi.memory.domain.message.event.mode.StateTransitioned] → [mimis.gildi.memory.context.Subconscious]
 * - [mimis.gildi.memory.domain.message.event.mode.ModeChanged] → [mimis.gildi.memory.context.Subconscious]
 */
class EventTest : DescribeSpec({

    describe("!about -- event tests live in bounded context specs") {
        it("Hippocampus emitting MemoryStored belongs in HippocampusTest, not here") {}
    }
})
