/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

import java.util.UUID

/**
 * A typed link between two memories in the [mimis.gildi.memory.context.Synapse] graph.
 *
 * Associations are carried on [mimis.gildi.memory.domain.message.command.association.AssociateCommand]
 * and stored by Synapse. Recall traverses them during search to find related memories.
 *
 * @property memoryId the target memory this link points to.
 * @property type semantic category: why are these memories related?
 * @property strength link weight (0.0--1.0). Higher = stronger association.
 * @property direction what to do with this link: [AssociationDirection.CREATE] a new one,
 *   [AssociationDirection.STRENGTHEN] an existing one, or [AssociationDirection.WEAKEN] it.
 * @property bidirectional whether the link applies in both directions. Defaults to true.
 */
data class Association(
    val memoryId: UUID,
    val type: AssociationType,
    val strength: Double,
    val direction: AssociationDirection = AssociationDirection.CREATE,
    val bidirectional: Boolean = true
)
