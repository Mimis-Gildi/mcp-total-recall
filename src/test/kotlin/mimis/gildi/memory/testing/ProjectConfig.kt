/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory.testing

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.engine.concurrency.SpecExecutionMode
import io.kotest.engine.concurrency.TestExecutionMode
import kotlin.time.Duration.Companion.seconds

/**
 * Kotest project-level configuration.
 *
 * Discovered automatically by Kotest on the classpath.
 * One per project. Affects every spec.
 */
object ProjectConfig : AbstractProjectConfig() {

    override val specExecutionMode = SpecExecutionMode.Concurrent

    override val testExecutionMode = TestExecutionMode.Concurrent

    override val timeout = 30.seconds

    override val invocationTimeout = 10.seconds

    override val testNameAppendTags = true
}
