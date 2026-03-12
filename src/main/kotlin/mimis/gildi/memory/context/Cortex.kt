/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.context

/**
 * The window clerk. Entry point and dispatcher for all inbound requests.
 *
 * Cortex receives from outside adapters (MCP transport, future UIs) and routes to internal bounded contexts.
 * It owns the session lifecycle and emits
 *
 * - [mimis.gildi.memory.domain.message.event.lifecycle.observable.SessionStarted],
 * - [mimis.gildi.memory.domain.message.event.lifecycle.observable.SessionEnded],
 * - [mimis.gildi.memory.domain.message.event.mode.StateTransitioned],
 * - [mimis.gildi.memory.domain.message.event.mode.ModeChanged],
 * - and [mimis.gildi.memory.domain.message.event.recall.TotalRecallAdvisoryRequested].
 *
 * @see <a href="https://mimis-gildi.github.io/mcp-total-recall/design/0009-cortex/">Design: Cortex</a>
 */
// FixMe: Created as a dependency for Event.kt KDoc links. Not implemented until we get here.
interface Cortex
