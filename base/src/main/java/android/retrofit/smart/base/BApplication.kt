package android.retrofit.smart.base

open class BApplication : BDApplication() {

    override fun onCreate() {
        super.onCreate()
        PP.CREATE(applicationContext)
    }
}