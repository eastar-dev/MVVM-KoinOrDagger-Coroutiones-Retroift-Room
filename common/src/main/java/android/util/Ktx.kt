@file:Suppress("NonAsciiCharacters", "FunctionName", "NAME_SHADOWING", "LocalVariableName", "unused", "SpellCheckingInspection")

package android.util

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.preference.PreferenceManager
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.core.content.pm.PackageInfoCompat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URISyntaxException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

typealias Ktx = Any

val Number.dp: Int get() = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics))
val Number.sp: Int get() = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, toFloat(), Resources.getSystem().displayMetrics))
val densityDpi = Resources.getSystem().displayMetrics.densityDpi
val density = Resources.getSystem().displayMetrics.density
val widthPixels = Resources.getSystem().displayMetrics.widthPixels
val heightPixels = Resources.getSystem().displayMetrics.heightPixels
val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics

fun Context.getResid(name: String, defType: String? = null, defPackage: String = packageName) = resources.getIdentifier(name, defType, defPackage)
fun Context.getDrawable(name: String) = getResid(name, "drawable")
fun Context.getUriForDrawable(name: String) = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/drawable/" + name)!!
fun Context.toast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
fun Context.toast(@StringRes text: Int) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
fun Context.toastLong(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
fun Context.toastLong(@StringRes text: Int) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.getLaunchIntentForPackage(packageName: String) = Intent(packageManager.getLaunchIntentForPackage(packageName))
fun Context.getLaunchIntentForPackage() = Intent(packageManager.getLaunchIntentForPackage(packageName))
fun Context.startActivity(intent_text: String) = startActivity(Intent.parseUri(intent_text, Intent.URI_INTENT_SCHEME))

val Context.versionCode: Long get() = getVersionCode(packageName)
val Context.versionName: String get() = getVersionName(packageName)
val Context.appName: String get() = getAppName(packageName)
val Context.networkOperator: String get() = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperator

fun Context.rawText(@RawRes resid: Int) =
        try {
            val `in` = resources.openRawResource(resid)
            val b = ByteArray(`in`.available())
            `in`.read(b)
            `in`.close()
            String(b)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

val Context.deviceid: String
    get() = PreferenceManager.getDefaultSharedPreferences(this).run {
        getString("deviceid", null)
                ?: java.util.UUID.randomUUID().toString().also { edit().putString("deviceid", it).apply() }
    }

fun Context.getAppName(packageName: String) =
        try {
            packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 0)).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }

fun Context.getVersionCode(packageName: String) =
        try {
            PackageInfoCompat.getLongVersionCode(packageManager.getPackageInfo(packageName, 0))
        } catch (e: PackageManager.NameNotFoundException) {
            -1L
        }

fun Context.getVersionName(packageName: String) =
        try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "0.0.0"
        }

fun Context.isInstall(packageName: String) =
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

val CharSequence.sha256: () -> String
    get() = {
        val bytes = toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        digest.fold("") { str, it -> str + "%02x".format(it) }
    }

fun CharSequence.이가(이: Char, 가: Char): CharSequence {
    var 이 = 이
    var 가 = 가
    val JT = 28
    val M = 21
    if ((이 - '가') / JT / M != 11/*ㅇ*/) {
        val t = 이
        이 = 가
        가 = t
    }

    val lastName = last()
    return when {
        (lastName < '가' || lastName > '힣') -> this
        (lastName - '가') % JT > 0 -> "$this$이"
        else -> "$this$가"
    }
}

val String.intent: Intent
    get() = Intent.parseUri(this, Intent.URI_INTENT_SCHEME)

fun CharSequence.pendingIntent(context: Context): PendingIntent? =
        try {
            PendingIntent.getActivity(context, 0, Intent.parseUri(toString(), Intent.URI_INTENT_SCHEME), PendingIntent.FLAG_UPDATE_CURRENT)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }

fun TextView.check(): Boolean {
    if (text.isNullOrBlank()) {
        Toast.makeText(context, " ${hint.이가('이', '가')} 없습니다.", Toast.LENGTH_SHORT).show()
        requestFocus()
        return false
    }
    return true
}

fun Bitmap.toStream(quality: Int): InputStream {
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, bos)
    val bytes = bos.toByteArray()
    return ByteArrayInputStream(bytes)
}

val Bitmap.jpegstream: InputStream
    get() {
        val bos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bytes = bos.toByteArray()
        return ByteArrayInputStream(bytes)
    }

val Long.toTimeText: String get() = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(this)
fun Long.tlog() = android.log.Log.pn(Log.ERROR, 1, toTimeText)

fun Long.stripTime() = Calendar.getInstance().apply {
    timeInMillis = this@stripTime
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

private const val DAY1 = 86400000L
val Long.stripTime: Long get() = ((this + TimeZone.getDefault().rawOffset) / DAY1 * DAY1) - TimeZone.getDefault().rawOffset

var keep = 0L
private val Long.r: String get() = String.format(Locale.getDefault(), "%,25d", this)
fun nano() = (if (keep == 0L) 0L else System.nanoTime() - keep).r.also { keep = System.nanoTime() }
fun sano() = 0L.r.also { keep = System.nanoTime() }
