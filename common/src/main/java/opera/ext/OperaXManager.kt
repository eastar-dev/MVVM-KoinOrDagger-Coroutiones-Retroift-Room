package opera.ext

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.base.CD
import android.content.Context
import android.content.Intent
import android.log.Log
import android.os.Build
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONException
import java.util.*

/**
 * Opera Manager
 */
object OperaXManager {
    //RUNNING_EXTENSION/////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("UseSparseArrays")
    private val RUNNING_EXTENSION = HashMap<Int, OperaX>()

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <R> execute(context: Context, json: String): R? {
        return try {
            val req = OperaXRequest.newInstance(json)
            if (CD.LOG) {
//                Log.e("OPERA>>", json)
                Log.flog("OPERA>>TOSS", json)
                Log.e("OPERA>>TOSS", req)
                Log.flog("OPERA>>TOSS", req)
            }

            val result = req.invoke(context)
            if (CD.LOG) {
                Log.i("TOSS<<OPERA", result)
                Log.flog("TOSS<<OPERA", result)
            }
            result as R?
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun execute(webView: WebView, json: String): Any? {
        try {
            val req = OperaXRequest.newInstance(json)
            if (CD.LOG) {
//                Log.e("OPERA>>", json)
                Log.flog("OPERA>>", json)
                Log.e("OPERA>>", req)
                Log.flog("OPERA>>", req)
            }

            val result = req.invoke(webView)
            if (req.method.returnType === Void.TYPE)
                return null

            var res = OperaXResponse.OK
            if (result == null)
                res = OperaXResponse.FAIL
            val script = res.getScript(req, result)
            sendJavascript(webView, script)

            return result
        } catch (e: Exception) {
            sendException(webView, OperaXRequest.errorInstance(json), e)
            return e.javaClass.simpleName + " : " + e.message
        }
    }

    fun sendException(webview: WebView, req: OperaXRequest, e: Exception) {
        e.printStackTrace()
        var th: Throwable = e
        while (th.cause != null) {
            th = th.cause!!
        }
        val resClassName = th.javaClass.simpleName
        val message = resClassName + " " + th.message
        val res = OperaXResponse.find(resClassName)
        val script = res.getScript(req, message)
        sendJavascript(webview, script)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun sendJavascript(webview: WebView, script: String) {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                webview.post { sendJavascript(webview, script) }
                return
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //            view.evaluateJavascript(script, value -> Log.i(value));//log
                webview.evaluateJavascript(script, null)
            } else {
                webview.loadUrl("javascript:$script")
            }

            //log
            if (CD.LOG) {
                try {
                    val regex = Regex("\\((.*)\\)\\((.*)\\);")
                    regex.matchEntire(script)?.run {
                        //                        Log.i("<<OPERA", groupValues[1], groupValues[2])
                        Log.flog("<<OPERA", groupValues[1], groupValues[2])
                        Log.i("<<OPERA", script)
                        Log.flog("<<OPERA", script)
                    } ?: run {
                        Log.i("<<OPERA", script)
                        Log.flog("<<OPERA", script)
                    }
                } catch (e: Exception) {
                    Log.w(e)
                }
            }

        } catch (e: Exception) {
            Log.w(e)
        }

    }

    //RUNNING_EXTENSION/////////////////////////////////////////////////////////////////////////////////
    @JvmStatic
    fun addOperaXJavascriptInterface(webview: WebView, name: String) {
        Log.e("addOperaXJavascriptInterface", name)
        webview.addJavascriptInterface(object : Any() {
            @JavascriptInterface
            fun execute(json: String): Any? {
                return execute(webview, json)
                //                webview.post(() -> OperaXManager.execute(webview, json));
                //                return "";
            }
        }, name)
    }

    @JvmStatic
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val ext = removeRunningExtension(requestCode) ?: return false

        ext.onActivityResult(requestCode, resultCode, data)
        return true
    }

    internal fun addRunningExtension(operaXItem: OperaX) {
        RUNNING_EXTENSION[operaXItem.mReq.requestcode] = operaXItem
    }

    private fun removeRunningExtension(requestCode: Int): OperaX? {
        return RUNNING_EXTENSION.remove(requestCode)
    }

    /**
     * JSONxxxxx element -> primitive type
     */
    @Throws(JSONException::class)
    fun dewarp(element: Any?): Any? {
        if (element == null) {
            return null
        } else if (element is Number) {
            return element
        } else if (element is String) {
            val string = element.toString()
            if (string.matches("\\d+|\\d*\\.\\d+|\\d+\\.\\d*".toRegex()))
                Log.w("!JSON element : $element may be Number?")
            return string
        } else {
            return element
        }
    }

}