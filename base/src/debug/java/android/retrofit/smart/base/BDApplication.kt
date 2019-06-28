package android.retrofit.smart.base

import android.base.CApplication
import android.content.Context

open class BDApplication : CApplication() {

    override fun attachBaseContext(base: Context) {
        DD.attachBaseContext()
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        DD.onCreate(applicationContext)
    }
}
