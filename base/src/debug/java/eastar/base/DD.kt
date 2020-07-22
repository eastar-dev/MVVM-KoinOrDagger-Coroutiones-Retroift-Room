package eastar.base

import android.app.NotificationManager
import android.base.CD
import android.content.Context
import android.log.Log
import android.retrofit.OkHttp3Logger
import android.webkit.WebView
import androidx.core.app.NotificationCompat
import java.io.File

class DD {
    companion object {
        @JvmStatic
        fun attachBaseContext() {
            //@formatter:off
            CD.PASS = true
            CD.LOG = true
            CD.DEVELOP = true
            CD.LIFELOG = true

            OkHttp3Logger.LOG = true
            OkHttp3Logger._OUT_1 = true
            OkHttp3Logger._OUT_2 = true
//            OkHttp3Logger._OUT_3 = true
//            OkHttp3Logger._OUT_H = true
//            OkHttp3Logger._OUT_C = true
            OkHttp3Logger._IN_1 = true
            OkHttp3Logger._IN_2 = true
            OkHttp3Logger._IN_LIMIT = 2500
//            OkHttp3Logger._IN_3 = true
//            OkHttp3Logger._IN_H = true
//            OkHttp3Logger._IN_C = true
            //@formatter:on
        }

        @JvmStatic
        fun onCreate(context: Context) {
            logInfo()
            displayInfo(context)
            setWebContentsDebuggingEnabled()
//            sacnFolder(context.filesDir.parentFile, "")
//            sacnFolder(File(context.getDatabasePath("0000").parent), "")
//            uncaughtExceptionHandler(context)
//            setNotification(context)
        }

        private fun logInfo() {
            //@formatter:off
            Log.e("PASS          ", CD.PASS)
            Log.e("DEVELOP       ", CD.DEVELOP)
            Log.e("LOG           ", CD.LOG)
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

            Log.e("OkHttp3Logger.LOG       ", OkHttp3Logger.LOG)
            Log.e("OkHttp3Logger._OUT_1        ", OkHttp3Logger._OUT_1)
            Log.e("OkHttp3Logger._OUT_2        ", OkHttp3Logger._OUT_2)
            Log.e("OkHttp3Logger._OUT_3        ", OkHttp3Logger._OUT_3)
            Log.e("OkHttp3Logger._OUT_H        ", OkHttp3Logger._OUT_H)
            Log.e("OkHttp3Logger._OUT_C        ", OkHttp3Logger._OUT_C)
            Log.e("OkHttp3Logger._IN_1         ", OkHttp3Logger._IN_1)
            Log.e("OkHttp3Logger._IN_2         ", OkHttp3Logger._IN_2)
            Log.e("OkHttp3Logger._IN_3         ", OkHttp3Logger._IN_3)
            Log.e("OkHttp3Logger._IN_H         ", OkHttp3Logger._IN_H)
            Log.e("OkHttp3Logger._IN_C         ", OkHttp3Logger._IN_C)

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
                Log.flog(context.packageName, throwable.stackTrace)
                dueHandler?.uncaughtException(thread, throwable)
            }
        }

        private fun sacnFolder(dir: File, prefix: String) {
            if (!dir.isDirectory) {
                Log.i(prefix, dir.absolutePath, dir.length(), dir.exists())
                return
            }

            Log.e(prefix, dir.absolutePath)

            dir.listFiles()?.forEach {
                if (!it.isDirectory) {
                    Log.i(prefix, it.absolutePath, it.length())
                }
            }
            dir.listFiles()?.forEach {
                if (it.isDirectory) {
                    sacnFolder(it, "$prefix▷")
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
            if (CD.LOG || CD.DEVELOP)
                WebView.setWebContentsDebuggingEnabled(true)
        }

    }
}
