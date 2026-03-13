/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

/**
 * What operation to perform on an [Association] link.
 *
 * Carried on [mimis.gildi.memory.domain.message.command.association.AssociateCommand].
 * [mimis.gildi.memory.context.Synapse] interprets this to decide whether to insert,
 * reinforce, or decay a graph edge.
 */
enum class AssociationDirection {
    /** Insert a new link. Fails silently if the link already exists. */
    CREATE,

    /** Increase an existing link's strength. No-op if the link doesn't exist. */
    STRENGTHEN,

    /** Decrease an existing link's strength. May trigger removal at zero. */
    WEAKEN
}
