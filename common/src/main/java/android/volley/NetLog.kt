package android.volley

import android.log.Log
import com.android.volley.NetworkResponse
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NetLog {
    companion object {
        val SPACE = "          "
        val LOG_STACK = "^android\\.volley\\..+|^smart\\.net\\..+".toRegex()
        @JvmStatic
        fun getStack(): StackTraceElement {
            val stackTraceElements = Exception().stackTrace
            var i = 0
            val N = stackTraceElements.size
            var stackTraceElement = stackTraceElements[i]
            while (i < N) {
                stackTraceElement = stackTraceElements[i]
                val className = stackTraceElement.className
                if (!className.matches(LOG_STACK)) {
                    break
                }
                ++i
            }
            while (i >= 0) {
                stackTraceElement = stackTraceElements[i]
                val lineNumber = stackTraceElement.lineNumber
                if (lineNumber >= 0) {
                    break
                }
                --i
            }
            return stackTraceElement
        }

        @JvmStatic
        fun _NETLOG_OUT(enty: NetEnty) {
            if (!Net.LOG) return
            if (enty._NO_LOG) return
            try {
                if (!enty._NO_OUT_H && (enty._OUT_H || Net._OUT_H)) {
                    enty.headers.entries
                            .filter { it.key != Net.COOKIE }
                            .forEach { Log.ps(Log.ERROR, enty.stack, "나해:", enty.javaClass.simpleName, it) }
                }
                if (!enty._NO_OUT_C && (enty._OUT_C || Net._OUT_C)) {
                    enty.headers.entries
                            .filter { it.key == Net.COOKIE }
                            .forEach { Log.ps(Log.ERROR, enty.stack, "나쿠:", it, enty.javaClass.simpleName) }
                }
                if (!enty._NO_OUT_1 && (enty._OUT_1 || enty._OUT_2 || enty._OUT_3 || Net._OUT_1 || Net._OUT_2 || Net._OUT_3)) {
                    Log.ps(Log.ERROR, enty.stack, "나가:", enty.toString())
                }
                if (!enty._NO_OUT_2 && (enty._OUT_2 || enty._OUT_3 || Net._OUT_2 || Net._OUT_3)) {
                    enty.params.entries
                            .map { Log.ps(Log.ERROR, enty.stack, it.key + SPACE.substring(Math.min(SPACE.length, it.key.length)) + " = " + it.value) }
                }
                if (!enty._NO_OUT_3 && (enty._OUT_3 || Net._OUT_3)) {
                    Log.ps(Log.ERROR, enty.stack, String(enty.body))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @JvmStatic
        fun _NETLOG_IN(enty: NetEnty, response: NetworkResponse) {
            if (!Net.LOG) return
            if (enty._NO_LOG) return
            try {
                if (!enty._NO_IN_1 && (enty._IN_1 || Net._IN_1 || enty._IN_2 || Net._IN_2)) {
                    Log.ps(Log.INFO, enty.stack, "들와:", response.statusCode, enty)
                }
                if (!enty._NO_IN_H && (enty._IN_H || Net._IN_H)) {
                    response.allHeaders
                            .filter { !it.name.equals(Net.SET_COOKIE, true) }
                            .forEach { Log.ps(Log.INFO, enty.stack, "들해:", enty.javaClass.simpleName, it) }
                }
                if (!enty._NO_IN_C && (enty._IN_C || Net._IN_C)) {
                    response.allHeaders
                            .filter { it.name.equals(Net.SET_COOKIE, true) }
                            .forEach { Log.ps(Log.INFO, enty.stack, "들쿠:", it, enty.javaClass.simpleName) }
                }
                if (!enty._NO_IN_2 && (enty._IN_2 || Net._IN_2)) {
                    val charset = charset(HttpHeaderParser.parseCharset(response.headers, Net.UTF8))
                    Log.ps(Log.INFO, enty.stack, response.statusCode, String(response.data, charset))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @JvmStatic
        fun _NETLOG_IN(enty: NetEnty, volleyError: VolleyError) {
            if (!Net.LOG) return
            if (enty._NO_LOG) return
            try {
                if (!enty._NO_IN_1 && (enty._IN_1 || Net._IN_1 || enty._IN_2 || Net._IN_2 || enty._IN_3 || Net._IN_3))
                    Log.ps(Log.WARN, enty.stack, "!들와:", enty, volleyError.networkResponse?.statusCode ?: volleyError.message)
                if (!enty._NO_IN_H && (enty._IN_H || Net._IN_H))
                    Log.ps(Log.WARN, enty.stack, "들해:", enty.javaClass.simpleName, volleyError.networkResponse?.allHeaders)
                if (!enty._NO_IN_2 && (enty._IN_2 || Net._IN_2 || enty._IN_3 || Net._IN_3)) {
                    Log.ps(Log.WARN, enty.stack, "!들와:", volleyError.javaClass.simpleName, volleyError.message)
                    val byteArray = volleyError.networkResponse?.data ?: ByteArray(0)
                    val charset = charset(HttpHeaderParser.parseCharset(volleyError.networkResponse?.headers, Net.UTF8))
                    Log.ps(Log.WARN, enty.stack, "!들와:", prettyJson(String(byteArray, charset)))
                }
                if (!enty._NO_IN_3 && (enty._IN_3 || Net._IN_3))
                    volleyError.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @Throws(JSONException::class)
        private fun prettyJson(json: String): String {
            val j = json.trim { it <= ' ' }
            if (j.isNotEmpty()) {
                if (j[0] == '{') return JSONObject(j).toString(4)
                if (j[0] == '[') return JSONArray(j).toString(4)
            }
            return json
        }

        @JvmStatic
        fun pojo(enty: NetEnty, json: String) {
            try {
                if (enty._NO_LOG) return
                if (enty._NO_POJO) return
                if (!Net.LOG) return
                if (enty._POJO || Net._POJO) {
                    Pojo.create(enty.javaClass.simpleName, json).toLog()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
