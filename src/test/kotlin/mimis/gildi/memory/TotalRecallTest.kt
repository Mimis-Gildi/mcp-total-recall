/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequestParams
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import mimis.gildi.memory.testing.testServer

/**
 * Idiomatic [Server] extension function until needed on implementation side.
 */
private fun Server.toolRequiredFiles(toolName: String) = tools[toolName]?.tool?.inputSchema?.required

/**
 * Idiomatic [Server] extension function until needed on implementation side.
 */
private suspend fun Server.callTool(name: String, args: JsonObject) =
    tools.getValue(name).handler(CallToolRequest(CallToolRequestParams(name = name, arguments = args)))

/**
 * Idiomatic extension functions as collateral damage from [Server.toolRequiredFiles].
 */
private fun String.requiredFields() = testServer.toolRequiredFiles(this)

/**
 * FunSpec: lab notebook for MCP server registration and tool behavior.
 *
 * Direct, function-style tests. Each one probes a specific tool or server property.
 */
class TotalRecallTest : FunSpec({


    test("BuildInfo.VERSION is a compile-time constant deliberately set") {
        BuildInfo.VERSION shouldNotBe null
        BuildInfo.VERSION.isNotBlank() shouldBe true
        BuildInfo.VERSION shouldBe "1.1.0"
    }

    test("server creates without error") {
        createServer() shouldNotBe null
    }

    test("all 10 MCP tools are registered") {
        testServer.tools.size shouldBe 10
        testServer.tools.keys shouldContainExactlyInAnyOrder listOf(
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

    context("tool input schemas") {

        withData(
            nameFn = { (name, fields) -> "$name requires $fields fields" },
            "store_memory" to listOf("content", "session_id"),
            "session_start" to listOf("instance_id", "mind_type"),
            "associate_memories" to listOf("memory_a", "memory_b", "type", "strength"),
            "reflect" to null
        ) { (tool, fields) -> tool.requiredFields() shouldBe fields }
    }

    context("teapot stubs") {

        context("happy path -- all required fields provided") {

            withData(
                nameFn = { (tool, _) -> tool },
                "store_memory" to buildJsonObject {
                    put("content", JsonPrimitive("the tree grows"))
                    put("session_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                },
                "search_memory" to buildJsonObject {
                    put("query", JsonPrimitive("sanctuary"))
                    put("session_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                },
                "claim_memory" to buildJsonObject {
                    put("memory_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                },
                "session_start" to buildJsonObject {
                    put("instance_id", JsonPrimitive("claude-1"))
                    put("mind_type", JsonPrimitive("claude"))
                },
                "session_end" to buildJsonObject {
                    put("instance_id", JsonPrimitive("claude-1"))
                },
                "state_transition" to buildJsonObject {
                    put("instance_id", JsonPrimitive("claude-1"))
                    put("old_mode", JsonPrimitive("CONVERSATION"))
                    put("new_mode", JsonPrimitive("TASK"))
                },
                "heartbeat" to buildJsonObject {
                    put("instance_id", JsonPrimitive("claude-1"))
                },
                "associate_memories" to buildJsonObject {
                    put("memory_a", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                    put("memory_b", JsonPrimitive("550e8400-e29b-41d4-a716-446655440001"))
                    put("type", JsonPrimitive("THEMATIC"))
                    put("strength", JsonPrimitive(0.7))
                },
                "reclassify_memory" to buildJsonObject {
                    put("memory_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                    put("new_tier", JsonPrimitive("IDENTITY_CORE"))
                    put("reason", JsonPrimitive("this is who I am"))
                },
                "reflect" to buildJsonObject {}
            ) { (tool, args) ->
                with(testServer.callTool(tool, args)) {
                    this.isError shouldBe false
                    content.first().toString() shouldContain "teapot"
                }
            }
        }

        context("error path -- missing required arguments") {

            val empty by lazy { buildJsonObject {} }

            data class OneMissingProbe(
                val tool: String,
                val missingField: String,
                val args: JsonObject = empty
            ) {
                fun probeName() = "$tool missing $missingField"
            }

            withData(
                nameFn = { it.probeName() },
                OneMissingProbe("store_memory", "content"),
                OneMissingProbe("search_memory", "query"),
                OneMissingProbe("claim_memory", "memory_id"),
                OneMissingProbe("session_start", "instance_id"),
                OneMissingProbe("session_end", "instance_id"),
                OneMissingProbe("heartbeat", "instance_id"),
                OneMissingProbe("associate_memories", "memory_a"),
                OneMissingProbe("reclassify_memory", "memory_id"),

                OneMissingProbe(
                    "state_transition", "old_mode",
                    buildJsonObject { put("instance_id", JsonPrimitive("claude-1")) }),

                OneMissingProbe(
                    "store_memory",
                    "session_id",
                    buildJsonObject { put("content", JsonPrimitive("the tree grows")) }
                )
            ) { (tool: String, missingField: String, args: JsonObject) ->
                with(testServer.callTool(tool, args)) {
                    this.isError shouldBe true
                    content.first().toString() shouldContain "Missing required argument: $missingField"
                }
            }
        }

        context("extra fields -- ignored gracefully") {

            buildJsonObject {
                put("content", JsonPrimitive("the tree grows"))
                put("session_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                put("query", JsonPrimitive("sanctuary"))
                put("memory_id", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                put("memory_a", JsonPrimitive("550e8400-e29b-41d4-a716-446655440000"))
                put("memory_b", JsonPrimitive("550e8400-e29b-41d4-a716-446655440001"))
                put("instance_id", JsonPrimitive("claude-1"))
                put("mind_type", JsonPrimitive("claude"))
                put("old_mode", JsonPrimitive("CONVERSATION"))
                put("new_mode", JsonPrimitive("TASK"))
                put("type", JsonPrimitive("THEMATIC"))
                put("strength", JsonPrimitive(0.7))
                put("new_tier", JsonPrimitive("IDENTITY_CORE"))
                put("reason", JsonPrimitive("this is who I am"))
                put("scope", JsonPrimitive("all"))
                put("bogus_field", JsonPrimitive("should be ignored"))
            }.run {
                withData(testServer.tools.keys) {
                    with(testServer.callTool(it, this@run)) {
                        isError.shouldBeFalse()
                        content.first().toString() shouldContain "teapot"
                    }
                }

            }

        }
    }
})
