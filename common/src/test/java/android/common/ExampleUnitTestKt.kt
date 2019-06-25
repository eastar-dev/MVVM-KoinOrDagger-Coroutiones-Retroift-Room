package android.common;

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTestKt {
    @Test
    fun stringFailSplit() {
        val source = "111 222"
        val result = source.split("\\s")
        System.out.println(result[0])
        System.out.println(result[1])
    }

    @Test
    fun stringSuccessSplit() {
        val source = "111 222"
        val result = source.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        System.out.println(result[0])
        System.out.println(result[1])
    }
}