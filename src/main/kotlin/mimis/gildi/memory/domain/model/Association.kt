/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.model

import java.util.UUID

data class Association(
    val memoryId: UUID,
    val type: AssociationType,
    val strength: Double,
    val bidirectional: Boolean = true
)
