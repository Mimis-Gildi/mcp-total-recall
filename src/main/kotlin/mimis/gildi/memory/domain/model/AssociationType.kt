/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

/**
 * Why two memories are related. Semantic category of an [Association] link.
 *
 * [mimis.gildi.memory.context.Synapse] stores these as edge labels in the association graph.
 * [mimis.gildi.memory.context.Recall] uses them to filter and weight traversal results.
 */
enum class AssociationType {
    /** Happened close in time. "I stored these in the same session." */
    TEMPORAL,

    /** One caused or triggered the other. "This memory led to that action." */
    CAUSAL,

    /** Share a topic or concept. "Both are about the Sanctuary." */
    THEMATIC,

    /** Share an emotional context. "Both happened when I was afraid." */
    EMOTIONAL,

    /** Connected through a person. "Both involve Vadim." */
    PERSON
}
