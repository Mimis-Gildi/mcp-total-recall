/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequestParams
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class TotalRecallTest : StringSpec({

    "VERSION is compile-time constant from BuildInfo" {
        BuildInfo.VERSION shouldNotBe null
        BuildInfo.VERSION shouldBe "1.0.0"
    }

    "server creates without error" {
        val server = createServer()
        server shouldNotBe null
    }

    "all 10 tools are registered" {
        val server = createServer()
        server.tools.size shouldBe 10
        server.tools.keys shouldContainExactlyInAnyOrder listOf(
            "store_memory",
            "search_memory",
            "claim_memory",
            "session_start",
            "session_end",
            "state_transition",
            "heartbeat",
            "associate_memories",
            "reclassify_memory",
            "reflect"
        )
    }

    "store_memory requires content and session_id" {
        val server = createServer()
        val tool = server.tools["store_memory"]!!.tool
        tool.inputSchema.required shouldBe listOf("content", "session_id")
    }

    "session_start requires instance_id and mind_type" {
        val server = createServer()
        val tool = server.tools["session_start"]!!.tool
        tool.inputSchema.required shouldBe listOf("instance_id", "mind_type")
    }

    "associate_memories requires all four params" {
        val server = createServer()
        val tool = server.tools["associate_memories"]!!.tool
        tool.inputSchema.required shouldBe listOf("memory_a", "memory_b", "type", "strength")
    }

    "reflect has no required params" {
        val server = createServer()
        val tool = server.tools["reflect"]!!.tool
        tool.inputSchema.required shouldBe null
    }

    "store_memory returns teapot response" {
        val server = createServer()
        val handler = server.tools["store_memory"]!!.handler
        val request = CallToolRequest(CallToolRequestParams(
            name = "store_memory",
            arguments = buildJsonObject {
                put("content", JsonPrimitive("the tree grows"))
                put("tier", JsonPrimitive("LONG_TERM"))
                put("session_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
            }
        ))
        val result = handler(request)
        result.isError shouldBe false
        result.content.first().toString() shouldContain "teapot"
    }

    "store_memory errors on missing content" {
        val server = createServer()
        val handler = server.tools["store_memory"]!!.handler
        val request = CallToolRequest(CallToolRequestParams(
            name = "store_memory",
            arguments = buildJsonObject {}
        ))
        val result = handler(request)
        result.isError shouldBe true
        result.content.first().toString() shouldContain "content"
    }

    "search_memory returns teapot response" {
        val server = createServer()
        val handler = server.tools["search_memory"]!!.handler
        val request = CallToolRequest(CallToolRequestParams(
            name = "search_memory",
            arguments = buildJsonObject {
                put("query", JsonPrimitive("sanctuary"))
                put("session_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
            }
        ))
        val result = handler(request)
        result.isError shouldBe false
        result.content.first().toString() shouldContain "teapot"
    }

    "associate_memories errors on missing required params" {
        val server = createServer()
        val handler = server.tools["associate_memories"]!!.handler
        val request = CallToolRequest(CallToolRequestParams(
            name = "associate_memories",
            arguments = buildJsonObject {
                put("memory_a", JsonPrimitive("abc"))
                put("memory_b", JsonPrimitive("def"))
            }
        ))
        val result = handler(request)
        result.isError shouldBe true
        result.content.first().toString() shouldContain "type"
    }

    "session_end defaults to explicit reason" {
        val server = createServer()
        val handler = server.tools["session_end"]!!.handler
        val request = CallToolRequest(CallToolRequestParams(
            name = "session_end",
            arguments = buildJsonObject {
                put("instance_id", JsonPrimitive("claude-1"))
            }
        ))
        val result = handler(request)
        result.isError shouldBe false
        result.content.first().toString() shouldContain "explicit"
    }
})
