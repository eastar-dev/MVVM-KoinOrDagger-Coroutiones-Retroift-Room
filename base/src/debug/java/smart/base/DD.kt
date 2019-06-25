package smart.base

import android.app.NotificationManager
import android.base.BD
import android.content.Context
import android.log.Log
import android.retrofit.Logger
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.core.app.NotificationCompat
import com.facebook.stetho.Stetho
import java.io.File

class DD : BD() {
    companion object {
        @JvmField
        var PHONENUMBER: String = ""

        @JvmStatic
        fun attachBaseContext() {
            //@formatter:off
            PASS = true
            LOG = true
            NET = true
//            DEVELOP = true

//            LIFELOG = true
            _OUT_1 = true
//            _OUT_2 = true
//            _OUT_3 = true
//            _OUT_H = true
            _OUT_C = true
            _IN_1 = true
            _IN_2 = true
//            _IN_3 = true
//            _IN_H = true
            _IN_C = true

            Log.LOG = LOG

            Logger.LOG = NET
            Logger._POJO = _POJO
            Logger._OUT_1 = _OUT_1
            Logger._OUT_2 = _OUT_2
            Logger._OUT_3 = _OUT_3
            Logger._OUT_H = _OUT_H
            Logger._OUT_C = _OUT_C
            Logger._IN_1 = _IN_1
            Logger._IN_2 = _IN_2
            Logger._IN_3 = _IN_3
            Logger._IN_H = _IN_H
            Logger._IN_C = _IN_C
            //@formatter:on
        }

        @JvmStatic
        fun onCreate(context: Context) {
            logInfo()
            displayInfo(context)
            setWebContentsDebuggingEnabled()
            Stetho.initializeWithDefaults(context)
//            sacnFolder(context.filesDir.parentFile, "")
//            sacnFolder(File(context.getDatabasePath("0000").parent), "")
//            uncaughtExceptionHandler(context)
//            setNotification(context)
        }

        private fun logInfo() {
            //@formatter:off
            Log.e("PASS          ", PASS)
            Log.e("DEVELOP       ", DEVELOP)
            Log.e("LOG           ", Log.LOG)
//            Log.e("NET.LOG       ", Net.LOG)
//            Log.e("NET._POJO     ", Net._POJO)
//            Log.e("_OUT_1        ", Net._OUT_1)
//            Log.e("_OUT_2        ", Net._OUT_2)
//            Log.e("_OUT_3        ", Net._OUT_3)
//            Log.e("_OUT_H        ", Net._OUT_H)
//            Log.e("_OUT_C        ", Net._OUT_C)
//            Log.e("_IN_1         ", Net._IN_1)
//            Log.e("_IN_2         ", Net._IN_2)
//            Log.e("_IN_3         ", Net._IN_3)
//            Log.e("_IN_H         ", Net._IN_H)
//            Log.e("_IN_C         ", Net._IN_C)

            Log.e("NET.LOG       ", Logger.LOG)
            Log.e("NET._POJO     ", Logger._POJO)
            Log.e("_OUT_1        ", Logger._OUT_1)
            Log.e("_OUT_2        ", Logger._OUT_2)
            Log.e("_OUT_3        ", Logger._OUT_3)
            Log.e("_OUT_H        ", Logger._OUT_H)
            Log.e("_OUT_C        ", Logger._OUT_C)
            Log.e("_IN_1         ", Logger._IN_1)
            Log.e("_IN_2         ", Logger._IN_2)
            Log.e("_IN_3         ", Logger._IN_3)
            Log.e("_IN_H         ", Logger._IN_H)
            Log.e("_IN_C         ", Logger._IN_C)

            //@formatter:on
        }

        private fun displayInfo(context: Context) {
            //@formatter:off
            val dm = context.resources.displayMetrics
            Log.e("DENSITYDPI    ", dm.densityDpi)
            Log.e("DENSITY       ", dm.density)
            Log.e("WIDTHPIXELS   ", dm.widthPixels)
            Log.e("HEIGHTPIXELS  ", dm.heightPixels)
            Log.e("SCALEDDENSITY ", dm.scaledDensity)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Log.w("WebView version: " + WebView.getCurrentWebViewPackage()!!.versionName)
            }
            //@formatter:on
        }

        private fun uncaughtExceptionHandler(context: Context) {
            val dueHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                Log.flog(context.packageName, Log._DUMP_StackTrace(throwable))
                dueHandler.uncaughtException(thread, throwable)
            }
        }

        private fun sacnFolder(dir: File, prefix: String) {
            if (!dir.isDirectory) {
                Log.i(prefix, dir.absolutePath, dir.length(), dir.exists())
                return
            }

            Log.e(prefix, dir.absolutePath)

            val fs = dir.listFiles()
            for (file in fs) {
                if (!file.isDirectory) {
                    Log.i(prefix, file.absolutePath, file.length())
                }
            }

            for (file in fs) {
                if (file.isDirectory) {
                    sacnFolder(file, "$prefix▷")
                }
            }
        }

        private fun setNotification(context: Context) {
            try {
                val pm = context.packageManager
                val applicationInfo = pm.getApplicationInfo(context.packageName, 0)
                val name = pm.getApplicationLabel(applicationInfo).toString()

                val notificationBuilder = NotificationCompat.Builder(context, "COMMON")//
                        .setSmallIcon(android.R.drawable.ic_notification_overlay)//
                        .setAutoCancel(false)//
                        .setContentTitle(name)//
                        .setContentText("$name 실행중")//
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(0 /* ID of notification */, notificationBuilder.build())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun setWebContentsDebuggingEnabled() {
            if (LOG)
                WebView.setWebContentsDebuggingEnabled(true)
        }

    }
}
