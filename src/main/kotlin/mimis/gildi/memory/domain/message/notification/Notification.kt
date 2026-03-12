/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.domain.message.notification

import mimis.gildi.memory.domain.message.Message

/**
 * Notifications flow outward through the NotificationPort to reach the connected mind.
 * The port contract is universal. The adapter decides delivery (MCP sampling, webhook, log).
 *
 * Sub-hierarchies by domain concern:
 *
 * - [mimis.gildi.memory.domain.message.notification.wellness.WellnessNotification]:
 *   1. mind health monitoring ([mimis.gildi.memory.context.Subconscious]).
 *
 * - [mimis.gildi.memory.domain.message.notification.audit.AuditNotification]:
 *   1. session end-of-life prompts ([mimis.gildi.memory.context.Cortex]).
 *
 * - [mimis.gildi.memory.domain.message.notification.recall.RecallNotification]:
 *   1. async deep traversal results ([mimis.gildi.memory.context.Cortex]).
 */
interface Notification : Message
