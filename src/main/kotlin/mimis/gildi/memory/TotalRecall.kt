package mimis.gildi.memory

import io.github.oshai.kotlinlogging.KotlinLogging

val rootLog = KotlinLogging.logger {}

fun main() {
    configureLogging()

    rootLog.info { "Total Recall -- the tree grows." }
}
