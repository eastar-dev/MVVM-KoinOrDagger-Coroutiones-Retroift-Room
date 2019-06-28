package android.util

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.base.BC
import android.content.ClipData
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.log.Log
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.view.children
import java.io.InputStream
import java.nio.charset.Charset
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.regex.Pattern
import javax.net.ssl.*

object BU {

//    fun isFirstTask(context: Context): Boolean {
//        try {
//
//            val packageManager = context.packageManager as PackageManager
//            var activity: PackageInfo? = null
//            try {
//                activity = packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
//            } catch (e: PackageManager.NameNotFoundException) {
//                e.printStackTrace()
//            }
//
//            val activityNames = ArrayList<String>()
//            for (activityInfo in activity!!.activities) {
//                activityNames.add(activityInfo.name)
//            }
//
//            return activityNames.contains(getCurrentActivity(context).topActivity.className)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return false
//    }

//    fun getCurrentActivity(context: Context): ActivityManager.RunningTaskInfo {
//        val am = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
//        val taskInfo = am.getRunningTasks(1)
//        return taskInfo[0]
//    }

    @SuppressLint("MissingPermission")
    fun getLine1Number(context: Context): String {
        var line1Number = ""
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(READ_SMS) == PERMISSION_GRANTED || context.checkSelfPermission(READ_PHONE_NUMBERS) == PERMISSION_GRANTED || context.checkSelfPermission(READ_PHONE_STATE) == PERMISSION_GRANTED) {
                line1Number = tm.line1Number
            } else {
                Log.w("PERMISSION_GRANTED")
            }
        } else {
            line1Number = tm.line1Number
        }
        line1Number = line1Number.replace("+82", "0").replace("\\D".toRegex(), "")
        return line1Number
    }

    @Throws(Exception::class)
    fun trustHttpsCertificates() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        })

        val sc = SSLContext.getInstance("TLS")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { _: String, _: SSLSession -> true }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findChildsByTag(parent: ViewGroup, tag: String, result: MutableList<T>) {
        for (child in parent.children) {
            if (child is ViewGroup)
                findChildsByTag(child, tag, result)

            if (tag == child.tag)
                result += (child as T)
        }
    }

}

class LimitByteTextWatcher @JvmOverloads constructor(val bytelength: Int, val charset: Charset, val what: Runnable? = null) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(s: Editable) {
        val text = s.toString()
        val count = text byteSize charset
        if (count > bytelength) {
            val utfcount = String(text.toByteArray(charset), 0, bytelength, charset).length
            s.delete(utfcount, s.length)
        }
        what?.run()
    }
}

fun String.limitByte(length: Int, charset: Charset): String? {
    val count = this byteSize charset
    if (count > length)
        return substring(0, String(toByteArray(charset), 0, length, charset).length)
    return this
}

infix fun String.byteSize(charset: Charset) = toByteArray(charset).size

fun Context.getResourceId(@AttrRes resid: Int): Int {
    val out = TypedValue()
    theme.resolveAttribute(resid, out, true)
    return out.resourceId
}

infix fun CharSequence.starmark(length: Int) = CharArray(length).fill('●').toString()
val CharSequence.starmark get() = if (isEmpty()) "" else this starmark length

val InputStream.text get() = bufferedReader().use { it.readText() }

//fun phoneNumberFormater(phoneNo: String): String? {
//    try {
//        return phoneNo.replace(BC.regularExpressionPhoneNo.toRegex(), "$1-$2-$3")
//    } catch (e: Exception) {
//        return phoneNo
//    }
//}

fun String.phoneNumberFormater(): String? {
    return "^(0(?:505|70|10|11|16|17|18|19))(\\d{3}|\\d{0,4})(\\d*)$".toRegex().matchEntire(onlyNumber)?.run {
        groupValues.drop(1).reduce { l, r -> "$l-$r" }
    }
}

infix fun Uri.removeQuery(removeQuery: String) =
        buildUpon().clearQuery()
                .query(query.split('&')
                        .filterNot { it.startsWith("$removeQuery=") }
                        .reduce { l, r -> "$l&$r" })
                .build()

val CharSequence.onlyNumber get() = replace("\\D".toRegex(), "")

/**"(\\d{3})(\\d{0,6})(\\d*)"*/
infix fun CharSequence.accctformat(deviderRegex: Regex) = deviderRegex.matchEntire(onlyNumber)?.run { groupValues.drop(1).reduce { l, r -> "$l-$r" } }

infix fun CharSequence.accctformat(devider: String) = accctformat(devider.toRegex())
//"KSC5601"

val SDK_INT = Build.VERSION.SDK_INT

infix fun Context.copy(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    clipboard.primaryClip = ClipData.newPlainText(text, text)
    toast("복사 하였습니다.")
}


