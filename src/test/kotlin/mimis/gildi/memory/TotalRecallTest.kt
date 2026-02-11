package mimis.gildi.memory

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TotalRecallTest : StringSpec({

    "the tree grows" {
        val originalOut = System.out
        val captured = ByteArrayOutputStream()
        System.setOut(PrintStream(captured))
        try {
            main()
        } finally {
            System.setOut(originalOut)
        }
        captured.toString() shouldContain "the tree grows"
    }
})
