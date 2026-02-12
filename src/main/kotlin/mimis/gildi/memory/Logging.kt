package mimis.gildi.memory

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import org.slf4j.LoggerFactory

fun configureLogging(level: Level = Level.INFO) {

    val context = LoggerFactory.getILoggerFactory() as LoggerContext
    context.reset()

    val encoder = PatternLayoutEncoder().apply {
        this.context = context
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
        start()
    }

    val appender = ConsoleAppender<ILoggingEvent>().apply {
        this.context = context
        name = "console"
        this.encoder = encoder
        start()
    }

    context.getLogger(Logger.ROOT_LOGGER_NAME).apply {
        this.level = level
        addAppender(appender)
    }
}
