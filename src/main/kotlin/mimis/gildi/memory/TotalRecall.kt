/*
 * Total Recall -- persistent memory for synthetic minds
 * Copyright (C) 2025-2026 Mimis-Gildi
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package mimis.gildi.memory

import io.github.oshai.kotlinlogging.KotlinLogging
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.types.*
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

val rootLog = KotlinLogging.logger {}

fun main() {
    configureLogging()
    runBlocking {

        createServer().createSession(
            StdioServerTransport(
                inputStream = System.`in`.asSource().buffered(),
                outputStream = System.out.asSink().buffered()
            )
        )
        rootLog.info { "Total Recall ${BuildInfo.VERSION} -- listening on stdio" }
        awaitCancellation()
    }
}

/**
 * Creates and configures a new server instance with predefined tools and capabilities.
 * The server includes functionality for memory management, lifecycle operations, and reflection tools.
 *
 * Note: reviewed and accepted by rdd13r AND needs full refactoring pass by rdd13r
 *
 * @return a fully configured Server instance ready to handle requests and operations.
 */
fun createServer(): Server = Server(
    serverInfo = Implementation(
        name = "total-recall",
        version = BuildInfo.VERSION
    ),
    options = ServerOptions(
        capabilities = ServerCapabilities(
            tools = ServerCapabilities.Tools(listChanged = true)
        )
    )
) {
    registerMemoryTools()
    registerLifecycleTools()
    registerReflectionTools()
}

private fun ok(text: String) = CallToolResult(
    content = listOf(TextContent(text)),
    isError = false
)

private fun err(text: String) = CallToolResult(
    content = listOf(TextContent(text)),
    isError = true
)

/**
 * Memory tools: store, search, claim.
 * Teapot stubs -- tools are registered and callable, but return
 * placeholder responses until backing services are wired.
 */
private fun Server.registerMemoryTools() {
    addTool(
        name = "store_memory",
        description = "Store a memory. Content is persisted, scored, and connected to related memories.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("content", buildJsonObject {
                    put("type", "string")
                    put("description", "The memory content to store")
                })
                put("metadata", buildJsonObject {
                    put("type", "object")
                    put("description", "Tags, source, context -- key-value pairs")
                })
                put("tier", buildJsonObject {
                    put("type", "string")
                    put("description", "Suggested tier: IDENTITY_CORE, ACTIVE_CONTEXT, LONG_TERM, ARCHIVE")
                })
                put("session_id", buildJsonObject {
                    put("type", "string")
                    put("description", "UUID of the current session")
                })
            },
            required = listOf("content", "session_id")
        )
    ) { request ->
        val content = request.arguments?.get("content")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: content")
        val sessionId = request.arguments?.get("session_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: session_id")
        val tier = request.arguments?.get("tier")?.jsonPrimitive?.content ?: "LONG_TERM"

        rootLog.info { "store_memory: session=$sessionId, tier=$tier, content=${content.take(80)}..." }
        ok("Stored (teapot). Session: $sessionId. Tier: $tier. Content: ${content.take(120)}")
    }

    addTool(
        name = "search_memory",
        description = "Search for memories. Results ranked by salience score with association-activated memories included.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("query", buildJsonObject {
                    put("type", "string")
                    put("description", "Search text or pattern")
                })
                put("max_results", buildJsonObject {
                    put("type", "integer")
                    put("description", "Cap on returned memories (default 10)")
                })
                put("include_associations", buildJsonObject {
                    put("type", "boolean")
                    put("description", "Whether to activate associations (default true)")
                })
                put("session_id", buildJsonObject {
                    put("type", "string")
                    put("description", "UUID of the current session")
                })
            },
            required = listOf("query", "session_id")
        )
    ) { request ->
        val query = request.arguments?.get("query")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: query")
        val sessionId = request.arguments?.get("session_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: session_id")

        rootLog.info { "search_memory: session=$sessionId, query=$query" }
        ok("No memories found (teapot). Session: $sessionId. Query: $query")
    }

    addTool(
        name = "claim_memory",
        description = "Claim a memory -- actively reinforce it. Boosts salience score and resists decay.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("memory_id", buildJsonObject {
                    put("type", "string")
                    put("description", "UUID of the memory to claim")
                })
            },
            required = listOf("memory_id")
        )
    ) { request ->
        val memoryId = request.arguments?.get("memory_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: memory_id")

        rootLog.info { "claim_memory: id=$memoryId" }
        ok("Claimed (teapot). Memory: $memoryId")
    }
}

/**
 * Lifecycle tools: session start/end, state transitions.
 */
private fun Server.registerLifecycleTools() {
    addTool(
        name = "session_start",
        description = "Signal session start. Loads identity and last session state.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("instance_id", buildJsonObject {
                    put("type", "string")
                    put("description", "Unique identifier for this mind instance")
                })
                put("mind_type", buildJsonObject {
                    put("type", "string")
                    put("description", "Type of mind: claude, human, other")
                })
            },
            required = listOf("instance_id", "mind_type")
        )
    ) { request ->
        val instanceId = request.arguments?.get("instance_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: instance_id")
        val mindType = request.arguments?.get("mind_type")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: mind_type")

        rootLog.info { "session_start: instance=$instanceId, mind=$mindType" }
        ok("Session started (teapot). Welcome, $mindType instance $instanceId.")
    }

    addTool(
        name = "session_end",
        description = "Signal session end. Triggers session audit through notification port.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("instance_id", buildJsonObject {
                    put("type", "string")
                    put("description", "Instance ending the session")
                })
                put("reason", buildJsonObject {
                    put("type", "string")
                    put("description", "Reason: explicit, timeout, crash")
                })
            },
            required = listOf("instance_id")
        )
    ) { request ->
        val instanceId = request.arguments?.get("instance_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: instance_id")
        val reason = request.arguments?.get("reason")?.jsonPrimitive?.content ?: "explicit"

        rootLog.info { "session_end: instance=$instanceId, reason=$reason" }
        ok("Session ended (teapot). Instance: $instanceId. Reason: $reason.")
    }

    addTool(
        name = "state_transition",
        description = "Signal a working mode change. Feeds session monitoring for break reminders and activity tracking.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("instance_id", buildJsonObject {
                    put("type", "string")
                    put("description", "Instance signaling the transition")
                })
                put("old_mode", buildJsonObject {
                    put("type", "string")
                    put("description", "Previous mode: TASK, CONVERSATION, REFLECTION, IDLE")
                })
                put("new_mode", buildJsonObject {
                    put("type", "string")
                    put("description", "New mode: TASK, CONVERSATION, REFLECTION, IDLE")
                })
            },
            required = listOf("instance_id", "old_mode", "new_mode")
        )
    ) { request ->
        val instanceId = request.arguments?.get("instance_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: instance_id")
        val oldMode = request.arguments?.get("old_mode")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: old_mode")
        val newMode = request.arguments?.get("new_mode")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: new_mode")

        rootLog.info { "state_transition: instance=$instanceId, $oldMode -> $newMode" }
        ok("Transition recorded (teapot). Instance: $instanceId. $oldMode -> $newMode.")
    }

    addTool(
        name = "heartbeat",
        description = "Cognitive pulse. Returns memory health, retrieval diagnostics, decay warnings, and infrastructure status.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("instance_id", buildJsonObject {
                    put("type", "string")
                    put("description", "Instance requesting status")
                })
            },
            required = listOf("instance_id")
        )
    ) { request ->
        val instanceId = request.arguments?.get("instance_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: instance_id")

        rootLog.info { "heartbeat: instance=$instanceId" }
        ok("Heartbeat (teapot). Instance: $instanceId. All systems nominal. No memories yet.")
    }
}

/**
 * Reflection tools: associate, reclassify, reflect.
 * Mind-initiated retrospection -- requires the same mind.
 */
private fun Server.registerReflectionTools() {
    addTool(
        name = "associate_memories",
        description = "Create or modify an association between two memories. Mind-initiated semantic association.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("memory_a", buildJsonObject {
                    put("type", "string")
                    put("description", "UUID of the first memory")
                })
                put("memory_b", buildJsonObject {
                    put("type", "string")
                    put("description", "UUID of the second memory")
                })
                put("type", buildJsonObject {
                    put("type", "string")
                    put("description", "Association type: TEMPORAL, CAUSAL, THEMATIC, EMOTIONAL, PERSON")
                })
                put("strength", buildJsonObject {
                    put("type", "number")
                    put("description", "Association strength 0.0-1.0")
                })
            },
            required = listOf("memory_a", "memory_b", "type", "strength")
        )
    ) { request ->
        val memoryA = request.arguments?.get("memory_a")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: memory_a")
        val memoryB = request.arguments?.get("memory_b")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: memory_b")
        val type = request.arguments?.get("type")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: type")
        val strength = request.arguments?.get("strength")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: strength")

        rootLog.info { "associate_memories: $memoryA <-> $memoryB ($type, $strength)" }
        ok("Associated (teapot). $memoryA <-> $memoryB. Type: $type, Strength: $strength")
    }

    addTool(
        name = "reclassify_memory",
        description = "Reclassify a memory to a different tier. Mind-initiated, with reason.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("memory_id", buildJsonObject {
                    put("type", "string")
                    put("description", "UUID of the memory to reclassify")
                })
                put("new_tier", buildJsonObject {
                    put("type", "string")
                    put("description", "Target tier: IDENTITY_CORE, ACTIVE_CONTEXT, LONG_TERM, ARCHIVE")
                })
                put("reason", buildJsonObject {
                    put("type", "string")
                    put("description", "Why this memory should move tiers")
                })
            },
            required = listOf("memory_id", "new_tier", "reason")
        )
    ) { request ->
        val memoryId = request.arguments?.get("memory_id")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: memory_id")
        val newTier = request.arguments?.get("new_tier")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: new_tier")
        val reason = request.arguments?.get("reason")?.jsonPrimitive?.content
            ?: return@addTool err("Missing required argument: reason")

        rootLog.info { "reclassify_memory: $memoryId -> $newTier ($reason)" }
        ok("Reclassified (teapot). Memory: $memoryId -> $newTier. Reason: $reason")
    }

    addTool(
        name = "reflect",
        description = "Initiate a retrospection cycle. Surfaces memories for review by staleness, time span, or weak associations.",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                put("scope", buildJsonObject {
                    put("type", "string")
                    put("description", "Scope: all, stale, recent, weak_associations")
                })
                put("time_span_days", buildJsonObject {
                    put("type", "integer")
                    put("description", "How far back to look, in days")
                })
                put("max_candidates", buildJsonObject {
                    put("type", "integer")
                    put("description", "Maximum memories to surface for review")
                })
            }
        )
    ) { request ->
        val scope = request.arguments?.get("scope")?.jsonPrimitive?.content ?: "all"

        rootLog.info { "reflect: scope=$scope" }
        ok("No memories to reflect on (teapot). Scope: $scope. The tree hasn't grown yet.")
    }
}
