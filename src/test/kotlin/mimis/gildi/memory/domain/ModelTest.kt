/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain

import io.kotest.core.spec.style.StringSpec

/**
 * StringSpec: domain model value objects.
 *
 * Flat assertions on data classes. No setup, no nesting, no narrative.
 * If a data class holds its shape, one line says so.
 *
 * Value objects in [mimis.gildi.memory.domain.model]:
 *
 * - [mimis.gildi.memory.domain.model.Memory]: the stored unit -- content, metadata, tier, session, claimed flag
 * - [mimis.gildi.memory.domain.model.Association]: link between memories -- type, strength, direction, bidirectional
 * - [mimis.gildi.memory.domain.model.SalienceScore]: decay tracking -- score, lastAccessed, decayRate
 *
 * Enums:
 *
 * - [mimis.gildi.memory.domain.model.Tier]: WORKING, SHORT_TERM, LONG_TERM, IDENTITY_CORE
 * - [mimis.gildi.memory.domain.model.AssociationType]: TEMPORAL, CAUSAL, THEMATIC, EMOTIONAL, PERSON
 * - [mimis.gildi.memory.domain.model.AssociationDirection]: CREATE, STRENGTHEN, WEAKEN
 * - [mimis.gildi.memory.domain.model.WorkingMode]: CONVERSATION, TASK, REFLECTION, IDLE
 * - [mimis.gildi.memory.domain.model.SessionEndCause]: NORMAL, TIMEOUT, ERROR, CONTEXT_LIMIT
 * - [mimis.gildi.memory.domain.model.MergeStrategy]: REPLACE, APPEND, WEIGHTED
 * - [mimis.gildi.memory.domain.model.ReflectionScope]: ALL, STALE, RECENT, UNCLAIMED
 */
class ModelTest : StringSpec({

    "!about -- model tests verify domain invariants once behavior exists" {
        // Value objects are data classes today. When they gain validation
        // (e.g., score 0.0..1.0, strength bounds, tier transition rules),
        // those constraints belong here.
    }
})
