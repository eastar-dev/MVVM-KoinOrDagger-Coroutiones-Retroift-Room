package android.common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void tiemcheck() {

        System.out.println(System.currentTimeMillis() % (1000L * 60 * 60) / (1000L * 60 * 10L));

    }

    @Test
    public void stringSplit() {
        final String source = "111  222";
        String[] result = source.split("\\s");
        assertEquals("111", result[0]);
        assertEquals("", result[1]);
        assertEquals("222", result[2]);
    }

}