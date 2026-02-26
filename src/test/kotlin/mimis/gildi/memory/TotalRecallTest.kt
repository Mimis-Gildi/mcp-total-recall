/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe

class TotalRecallTest : StringSpec({

    "server creates without error" {
        val server = createServer()
        server shouldNotBe null
    }
})
