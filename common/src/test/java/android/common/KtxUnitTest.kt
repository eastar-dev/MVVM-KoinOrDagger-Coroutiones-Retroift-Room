package android.common;

import android.log.Log
import android.net.Uri
import android.util.accctformat
import android.util.removeQuery
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class KtxUnitTest {
    @Test
    fun stringFailSplit() {
        val source = "111 222"
        val result = source.split("\\s")
        System.out.println(result[0])
        System.out.println(result[1])
    }

    @Test
    fun stringSuccessSplit() {
        val text: CharSequence? = null
        //text.masking do not work
    }



    @Test
    fun `accctformat test`() {
        Log.MODE = Log.eMODE.SYSTEMOUT
        Log.e("1234567890" accctformat "\\d{3}\\d{0,6}\\d*".toRegex())
        Log.e("1234567890" accctformat "\\d{3}\\d{0,6}".toRegex())
        Log.e("1234567890" accctformat "\\d{3}\\d{0,6}\\d".toRegex())
        Log.e("1234567890" accctformat "(\\d{3})(\\d{0,6})(\\d*)".toRegex())
        Log.e("12345678901234567890" accctformat "(\\d{3})(\\d{0,6})(\\d*)".toRegex())
    }
    @Test
    fun `removeQuery test`() {
        Log.MODE = Log.eMODE.SYSTEMOUT
        val uri:Uri? = Uri.parse("https://www.naver.com/path/path?a=1&b=2&c=3#hello")
        Log.e(uri?.removeQuery("a"))
    }
}