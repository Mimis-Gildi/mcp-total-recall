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
 * - [mimis.gildi.memory.domain.model.Tier]: IDENTITY_CORE, ACTIVE_CONTEXT, LONG_TERM, ARCHIVE
 * - [mimis.gildi.memory.domain.model.AssociationType]: TEMPORAL, CAUSAL, THEMATIC, EMOTIONAL, PERSON
 * - [mimis.gildi.memory.domain.model.AssociationDirection]: CREATE, STRENGTHEN, WEAKEN
 * - [mimis.gildi.memory.domain.model.WorkingMode]: TASK, CONVERSATION, IDLE
 * - [mimis.gildi.memory.domain.model.SessionEndCause]: EXPLICIT, TIMEOUT, CRASH
 * - [mimis.gildi.memory.domain.model.MergeStrategy]: COMBINE, KEEP_NEWEST, SUMMARIZE
 * - [mimis.gildi.memory.domain.model.ReflectionScope]: ALL, STALE, RECENT, WEAK_ASSOCIATIONS
 */
class ModelTest : StringSpec({

    "!about -- model tests verify domain invariants once behavior exists" {
        // Value objects are data classes today. When they gain validation
        // (e.g., score 0.0..1.0, strength bounds, tier transition rules),
        // those constraints belong here.
    }
})
