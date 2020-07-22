package dev.eastar.branch

import android.util.accctformat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun accctformattext() {
        println("010-225-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("10-225-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("0-225-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("-225-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("225-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("25-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("5-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("-225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("225698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("5698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("698" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("8" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
        println("" accctformat "(\\d{3})(\\d{0,6})(\\d*)")
    }

}






