/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.context

/**
 * Background processes. Break detection, session health monitoring,
 * and autonomous maintenance that runs without mind interaction.
 *
 * "Subconscious" consumes
 *
 * - [mimis.gildi.memory.domain.message.ModeChanged]
 *
 * to detect sustained task mode and trigger
 *
 * - [mimis.gildi.memory.domain.message.notification.BreakNotification].
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/design/0010-subconscious/">Design: Subconscious</a>
 */
// FixMe: Created as a dependency for Event.kt KDoc links. Not implemented until we get here.
interface Subconscious
