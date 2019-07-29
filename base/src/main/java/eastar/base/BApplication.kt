package eastar.base

abstract class BApplication : BDApplication() {
    override fun onCreate() {
        super.onCreate()
        PP.CREATE(applicationContext)
    }
}