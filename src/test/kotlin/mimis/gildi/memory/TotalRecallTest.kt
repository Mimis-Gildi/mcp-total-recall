/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withContexts
import io.kotest.datatest.withTests
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

/** Reach into a registered tool's JSON Schema to get its `required` field list. */
private fun Server.toolRequiredFiles(toolName: String) = tools[toolName]?.tool?.inputSchema?.required

/** Call a tool handler directly, bypassing transport. Returns the raw [io.modelcontextprotocol.kotlin.sdk.types.CallToolResult]. */
private suspend fun Server.callTool(name: String, args: JsonObject) =
    tools.getValue(name).handler(CallToolRequest(CallToolRequestParams(name = name, arguments = args)))

/** Shorthand: `"store_memory".requiredFields()` reads like a sentence. */
private fun String.requiredFields() = testServer.toolRequiredFiles(this)

/**
 * FunSpec: MCP server smoke tests -- registration, tool schemas, and teapot stubs.
 *
 * This spec validates the server contract visible to MCP clients:
 * - All 9 tools are registered with correct names
 * - Each tool's JSON Schema declares the right `required` fields
 * - Teapot stubs respond correctly (happy path, missing args, extra args)
 *
 * Uses [testServer] -- a shared lazy [Server] instance from `mimis.gildi.memory.testing.Fixtures`.
 * No backing services, no persistence. Pure protocol-level verification.
 *
 * @see mimis.gildi.memory.createServer the factory under test
 * @see mimis.gildi.memory.testing.testServer shared server instance
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

    test("all 9 MCP tools are registered") {
        testServer.tools.size shouldBe 9
        testServer.tools.keys shouldContainExactlyInAnyOrder listOf(
            "store_memory",
            "search_memory",
            "claim_memory",
            "session_start",
            "session_end",
            "state_transition",
            "associate_memories",
            "reclassify_memory",
            "reflect"
        )
    }

    context("tool input schemas") {

        withTests(
            nameFn = { (name, fields) -> "$name requires $fields" },
            "store_memory" to listOf("content", "session_id"),
            "search_memory" to listOf("query", "session_id"),
            "claim_memory" to listOf("memory_id"),
            "session_start" to listOf("instance_id", "mind_type"),
            "session_end" to listOf("instance_id"),
            "state_transition" to listOf("instance_id", "old_mode", "new_mode"),
            "associate_memories" to listOf("memory_a", "memory_b", "type", "strength"),
            "reclassify_memory" to listOf("memory_id", "new_tier", "reason"),
            "reflect" to null
        ) { (tool, fields) -> tool.requiredFields() shouldBe fields }
    }

    context("teapot stubs") {

        context("happy path -- all required fields provided") {

            withTests(
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

            withTests(
                nameFn = { it.probeName() },
                OneMissingProbe("store_memory", "content"),
                OneMissingProbe("search_memory", "query"),
                OneMissingProbe("claim_memory", "memory_id"),
                OneMissingProbe("session_start", "instance_id"),
                OneMissingProbe("session_end", "instance_id"),
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
                withContexts(testServer.tools.keys) {
                    with(testServer.callTool(it, this@run)) {
                        isError.shouldBeFalse()
                        content.first().toString() shouldContain "teapot"
                    }
                }

            }

        }
    }
})
