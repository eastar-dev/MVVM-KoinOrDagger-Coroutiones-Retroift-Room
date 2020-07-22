package android.common;

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class FlowTest {

    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    @Test
    fun main() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis() // remember the start time
        (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
            .flatMapConcat { requestFlow(it) }
            .collect { value -> // collect and print
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }
}