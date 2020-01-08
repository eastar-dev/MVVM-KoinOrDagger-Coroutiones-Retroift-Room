@file:Suppress("NonAsciiCharacters", "FunctionName", "NAME_SHADOWING", "LocalVariableName", "unused", "SpellCheckingInspection")

package android.util

import android.app.PendingIntent
import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.core.content.pm.PackageInfoCompat
import androidx.preference.PreferenceManager
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URISyntaxException
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class Ktx

val Number.dpf: Float get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics)
val Number.spf: Float get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, toFloat(), Resources.getSystem().displayMetrics)
val Number.dp: Int get() = dpf.roundToInt()
val Number.sp: Int get() = dpf.roundToInt()

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

fun Context.rawText(@RawRes resid: Int) = resources.openRawResource(resid).text
val Context.deviceid: String
    get() = PreferenceManager.getDefaultSharedPreferences(this).run {
        getString("__deviceid", null) ?: UUID.randomUUID().toString().also { edit().putString("__deviceid", it).apply() }
    }

fun Context.getAppName(packageName: String) = packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 0)).toString()

fun Context.getVersionCode(packageName: String) = PackageInfoCompat.getLongVersionCode(packageManager.getPackageInfo(packageName, 0))

fun Context.getVersionName(packageName: String) = packageManager.getPackageInfo(packageName, 0).versionName

fun Context.isInstall(packageName: String) = try {
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

fun Bitmap.toJpegStream(quality: Int = 100): InputStream {
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, bos)
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

val DAY1 = 86400000L
val Long.stripTime: Long get() = ((this + TimeZone.getDefault().rawOffset) / DAY1 * DAY1) - TimeZone.getDefault().rawOffset

val Number.comma: String get() = String.format(Locale.getDefault(), "%,25d", this)
private var keep = 0L
fun nano() = (System.nanoTime() - keep).comma.also { keep = System.nanoTime() }

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

infix fun CharSequence.masking(length: Int) = CharArray(length).fill('●').toString()
val CharSequence.masking get() = this masking length

val InputStream.text get() = bufferedReader().use { it.readText() }

fun String.phoneNumberFormater(): String? {
    return "^(0(?:505|70|10|11|16|17|18|19))(\\d{3}|\\d{0,4})(\\d*)$".toRegex()
            .matchEntire(onlyNumber)
            ?.run {
                groupValues
                        .drop(1)
                        .takeIf { it.isNotEmpty() }
                        ?.reduce { l, r -> "$l-$r" }
            } ?: this@phoneNumberFormater
}

infix fun Uri.removeQuery(removeQuery: String) =
        buildUpon().clearQuery()
                .query(query?.run {
                    split('&')
                            .filterNot { it.startsWith("$removeQuery=") }
                            .takeIf { it.isNotEmpty() }
                            ?.reduce { l, r -> "$l&$r" }
                }).build()

val CharSequence.onlyNumber get() = replace("\\D".toRegex(), "")

/**"1234567890" accctformat "(\\d{3})(\\d{0,6})(\\d*)"  -> 123-456789-0*/
infix fun CharSequence.accctformat(deviderRegex: Regex) = deviderRegex.matchEntire(onlyNumber)
        ?.run {
            groupValues
                    .drop(1)
                    .takeIf { it.isNotEmpty() }
                    ?.reduce { l, r -> "$l-$r" }
        } ?: this@accctformat

val SDK_INT = Build.VERSION.SDK_INT

infix fun Context.copy(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(text, text))
    toast("복사 하였습니다.")
}

fun Context.startMain() =
        Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            `package` = packageName
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }.also {
            startActivity(it)
        }

