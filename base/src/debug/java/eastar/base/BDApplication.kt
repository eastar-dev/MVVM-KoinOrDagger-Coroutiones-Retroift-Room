package eastar.base

import android.base.CApplication
import android.content.Context

abstract class BDApplication : CApplication() {

    override fun attachBaseContext(base: Context) {
        DD.attachBaseContext()
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        DD.onCreate(applicationContext)
    }
}
