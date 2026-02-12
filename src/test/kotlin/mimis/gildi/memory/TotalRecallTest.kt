package mimis.gildi.memory

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TotalRecallTest : StringSpec({

    "the tree grows" {
        val originalOut = System.out
        ByteArrayOutputStream().use { captured ->
            System.setOut(PrintStream(captured))
            main()
            System.setOut(originalOut)
            captured.toString() shouldContain "the tree grows"
        }
        rootLog.info { "Test completed: tree growth verified" }
    }

    "the tree has not grown yet" {
        val originalOut = System.out
        ByteArrayOutputStream().use { captured ->
            System.setOut(PrintStream(captured))
            main()
            System.setOut(originalOut)
            captured.toString() shouldNotContain "the tree grew"
        }
        rootLog.info { "Test completed: tree hasn't grown yet" }
    }
})
