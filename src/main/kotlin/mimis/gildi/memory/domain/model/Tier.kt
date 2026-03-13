/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

/**
 * Where a [Memory] lives in the hierarchy.
 *
 * [mimis.gildi.memory.context.Salience] recommends tier changes via
 * [mimis.gildi.memory.domain.message.event.memory.AttentionTierPromotionRequested] and
 * [mimis.gildi.memory.domain.message.event.memory.AttentionTierDemotionRequested].
 * [mimis.gildi.memory.context.Hippocampus] executes them, emitting
 * [mimis.gildi.memory.domain.message.event.memory.TierChanged].
 */
enum class Tier {
    /** Who I am. Resists all decay. Never demoted automatically. */
    IDENTITY_CORE,

    /** Currently relevant. High salience, frequently accessed. */
    ACTIVE_CONTEXT,

    /** Retained but not active. Subject to decay toward ARCHIVE. */
    LONG_TERM,

    /** Low salience. Retained for deep recall but not surfaced by default search. */
    ARCHIVE
}
