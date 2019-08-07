package opera.ext

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.log.Log
import android.os.Build
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

@Suppress("UNUSED_PARAMETER")
abstract class OperaX {
    companion object {
        private const val TAILFIX_CALLBACK = "C"
    }

    protected lateinit var mContext: Context//for extension
    protected lateinit var mActivity: AppCompatActivity//for extension
    protected lateinit var mWebview: WebView//for extension
    ///////////
    lateinit var mReq: OperaXRequest

    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class OperaXInternal

    open fun initialize(webView: WebView, req: OperaXRequest): OperaX {
        mWebview = webView
        initialize(webView.context, req)
        return this
    }

    open fun initialize(context: Context, req: OperaXRequest): OperaX {
        mContext = context
        if (context is AppCompatActivity)
            mActivity = context
        this.mReq = req
        return this
    }

    //////////////////////////////////////
    override fun toString(): String {
        return javaClass.simpleName
    }

    //////////////////////////////////////
    protected fun startActivityForResult(intent: Intent, requestCode: Int) {
        setRequestCode(requestCode)
        mActivity.startActivityForResult(intent, requestCode)
    }

    protected fun setRequestCode(requestCode: Int) {
        mReq.requestcode = requestCode
        OperaXManager.addRunningExtension(this)
    }

    ///////////////////////////////////////////////////////
    //Req
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callbackMethodName = mReq.method.name + TAILFIX_CALLBACK
//        Log.i(callbackMethodName, requestCode, resultCode, data)
        try {
            val method = javaClass.getDeclaredMethod(callbackMethodName, Int::class.java, Int::class.java, Intent::class.java)
            method.isAccessible = true
            method.invoke(this, requestCode, resultCode, data)
        } catch (e: NoSuchMethodException) {
            sendOnActivityResult(requestCode, resultCode, data)
        } catch (e: Exception) {
            sendException(e)
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected fun sendJavascript(script: String) {
        OperaXManager.sendJavascript(mWebview, script)
    }

    protected fun sendResult(result: Any) {
        Log.w("sendResult")
        val script = OperaXResponse.OK.getScript(mReq, result)
        OperaXManager.sendJavascript(mWebview, script)
    }

    protected fun sendException(e: Exception) {
        Log.w("sendException")
        OperaXManager.sendException(mWebview, mReq, e)
    }

    private fun sendOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.w("sendOnActivityResult")
        val res = if (Activity.RESULT_OK == resultCode) OperaXResponse.OK else OperaXResponse.FAIL
        val script = res.getScript(mReq, data)
        OperaXManager.sendJavascript(mWebview, script)

    }
}
