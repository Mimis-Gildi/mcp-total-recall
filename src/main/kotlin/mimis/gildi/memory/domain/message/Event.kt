/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message

import mimis.gildi.memory.context.*
import mimis.gildi.memory.domain.model.*
import java.time.Instant
import java.util.*
import kotlin.time.Duration

/**
 * Events are facts. Something happened. Zero, one, or many consumers.
 *
 * Grouped by bounded context:
 *
 * - [Hippocampus] (storage),
 * - [Salience] (scoring),
 * - [Synapse] (associations),
 * - [Cortex] (lifecycle),
 * - and [Subconscious] (background).
 */
sealed interface Event

// -- Hippocampus: the memory keeper --

/**
 * [Hippocampus]. A new memory exists. Triggers [Salience] scoring and [Synapse] association discovery.
 *
 * @property tx chain of custody envelope -- traces this event back to the originating command.
 * @property memoryId assigned by [Hippocampus] at storage time.
 * @property content the stored text -- may differ from the command's content if [Hippocampus] normalized it.
 * @property metadata key-value pairs: source, tags, context -- whatever the mind attached.
 * @property tier the tier [Hippocampus] placed it in (may differ from the command's suggestedTier).
 * @property timestamp when [Hippocampus] committed the writing, not when the mind sent the command.
 */
data class MemoryStored(
    val tx: TransactionContext,
    val memoryId: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: Tier,
    val timestamp: Instant
) : Event

/**
 * [Hippocampus]. A memory was read. Feeds [Salience] decay curves -- access resets the clock.
 *
 * @property tx chain of custody.
 * @property memoryId the memory that was accessed.
 * @property accessTimestamp when the read happened -- [Salience] uses this to recalculate decay.
 */
data class MemoryAccessed(
    val tx: TransactionContext,
    val memoryId: UUID,
    val accessTimestamp: Instant
) : Event

/**
 * [Hippocampus]. A mind actively claimed a memory. Resists decay permanently.
 * This is the "fight to remember" mechanism.
 *
 * @property tx chain of custody.
 * @property memoryId the memory being claimed.
 * @property claimTimestamp when the mind chose to claim -- distinct from when it was stored or last accessed.
 */
data class MemoryClaimed(
    val tx: TransactionContext,
    val memoryId: UUID,
    val claimTimestamp: Instant
) : Event

/**
 * [Hippocampus]. Full memory content returned to [Recall] for assembly into a search response.
 *
 * @property tx chain of custody.
 * @property memoryId which memory was retrieved?
 * @property content the stored text.
 * @property metadata the stored key-value pairs.
 * @property tier the current tier at retrieval time -- may have changed since stored last.
 */
data class MemoryRetrieved(
    val tx: TransactionContext,
    val memoryId: UUID,
    val content: String,
    val metadata: Map<String, String>,
    val tier: Tier
) : Event

/**
 * [Hippocampus]. Any tier change -- Salience-driven or mind-initiated. Internal bookkeeping.
 *
 * @property tx chain of custody.
 * @property memoryId the memory that moved.
 * @property oldTier where it was.
 * @property newTier where it is now.
 * @property reason machine-generated ("decay score 0.12 below floor 0.2") or mind-provided ("this is who I am").
 */
data class TierChanged(
    val tx: TransactionContext,
    val memoryId: UUID,
    val oldTier: Tier,
    val newTier: Tier,
    val reason: String
) : Event

/**
 * [Hippocampus]. Mind-initiated reclassification only.
 * Distinct from [TierChanged]:
 * [Synapse] consumes this to update associations when the mind deliberately moves a memory.
 *
 * @property tx chain of custody.
 * @property memoryId the memory being reclassified.
 * @property oldTier tier before the mind's decision.
 * @property newTier tier after the mind's decision.
 * @property reason the mind's stated reason -- always human/mind-authored, never machine-generated.
 */
data class MemoryReclassified(
    val tx: TransactionContext,
    val memoryId: UUID,
    val oldTier: Tier,
    val newTier: Tier,
    val reason: String
) : Event

// -- Salience: the attention engine --

/**
 * [Salience]. Promoted a memory to a higher tier. Score crossed the promotion threshold.
 *
 * @property tx chain of custody.
 * @property memoryId the memory that was promoted.
 * @property newTier the tier it was promoted to.
 * @property score the salience score that triggered promotion (0.0--1.0).
 */
data class TierPromoted(
    val tx: TransactionContext,
    val memoryId: UUID,
    val newTier: Tier,
    val score: Double
) : Event

/**
 * [Salience]. Demoted a memory. Score decayed below the tier floor.
 *
 * @property tx chain of custody.
 * @property memoryId the memory that was demoted.
 * @property newTier the tier it fell to.
 * @property score the salience score at demotion time (0.0--1.0).
 */
data class TierDemoted(
    val tx: TransactionContext,
    val memoryId: UUID,
    val newTier: Tier,
    val score: Double
) : Event

/**
 * [Salience]. Raw salience calculation result. Consumed by [Hippocampus] to decide tier transitions.
 *
 * @property tx chain of custody.
 * @property salienceScore the full score object -- memoryId, score, lastAccessed, decayRate.
 */
data class SalienceScored(
    val tx: TransactionContext,
    val salienceScore: SalienceScore
) : Event

// -- Synapse: the association graph --

/**
 * [Synapse]. Association traversal complete. Returned to [Recall] for inclusion in search results.
 *
 * @property tx chain of custody.
 * @property sourceId the memory whose associations were traversed.
 * @property associations the discovered links -- each carries type, strength, and direction.
 */
data class AssociationsFound(
    val tx: TransactionContext,
    val sourceId: UUID,
    val associations: List<Association>
) : Event

// -- Total Recall: deep async recall --

/**
 * [Cortex]. Deep association traversal found additional memories after the fast-path MCP response already returned.
 * Delivered to the mind via [TotalRecallNotification] through the NotificationPort.
 *
 * @property tx chain of custody.
 * @property sourceMemoryIds the memories that triggered the deep traversal.
 * @property originRequestId the original search request this traversal belongs to.
 * @property timestamp when the deep traversal completed.
 */
data class TotalRecallAdvisory(
    val tx: TransactionContext,
    val sourceMemoryIds: Set<UUID>,
    val originRequestId: UUID,
    val timestamp: Instant
) : Event

// -- Lifecycle: session and state --

/**
 * [Cortex]. A mind connected. Loads identity and last session state.
 *
 * @property tx chain of custody.
 * @property instanceId unique identifier for this mind instance (e.g. "claude-1").
 * @property mindType what kind of mind -- "claude", "human", "other".
 * @property resumptionData optional context from the previous session for warm restart.
 */
data class SessionStart(
    val tx: TransactionContext,
    val instanceId: String,
    val mindType: String,
    val resumptionData: Map<String, String> = emptyMap()
) : Event

/**
 * [Cortex]. A mind disconnected. Triggers session audit via NotificationPort.
 *
 * @property tx chain of custody.
 * @property instanceId which mind disconnected.
 * @property reason why -- EXPLICIT (mind chose to leave), TIMEOUT (no heartbeat), CRASH (unexpected).
 */
data class SessionEnd(
    val tx: TransactionContext,
    val instanceId: String,
    val reason: SessionEndReason
) : Event

/**
 * [Cortex]. Generic state change. For transitions that don't map to [ModeChanged].
 *
 * @property tx chain of custody.
 * @property instanceId which mind.
 * @property oldState free-form previous state (e.g. "connected", "active").
 * @property newState free-form new state.
 * @property context optional key-value pairs explaining the transition.
 */
data class StateTransition(
    val tx: TransactionContext,
    val instanceId: String,
    val oldState: String,
    val newState: String,
    val context: Map<String, String> = emptyMap()
) : Event

/**
 * [Cortex]. Working mode changed.
 * Feeds [Subconscious] break detection -- sustained TASK mode triggers [BreakNotification].
 *
 * @property tx chain of custody.
 * @property instanceId which mind.
 * @property oldMode previous working mode.
 * @property newMode current working mode.
 */
data class ModeChanged(
    val tx: TransactionContext,
    val instanceId: String,
    val oldMode: WorkingMode,
    val newMode: WorkingMode
) : Event

/**
 * [Cortex]. Cognitive pulse. Absence triggers timeout detection.
 *
 * @property tx chain of custody.
 * @property instanceId which mind sent the pulse?
 * @property timestamp when the heartbeat arrived -- the gap between heartbeats is how we detect timeout.
 */
data class HeartbeatReceived(
    val tx: TransactionContext,
    val instanceId: String,
    val timestamp: Instant
) : Event

/**
 * [Cortex]. Snapshot of session health. Duration, activity level, last interaction.
 *
 * @property tx chain of custody.
 * @property instanceId which mind.
 * @property duration how long this session has been alive?
 * @property activityLevel current engagement -- ACTIVE, IDLE, or SUSTAINED_TASK.
 * @property lastInteraction when the mind last did something -- the gap from now indicates drift.
 */
data class SessionState(
    val tx: TransactionContext,
    val instanceId: String,
    val duration: Duration,
    val activityLevel: ActivityLevel,
    val lastInteraction: Instant
) : Event
