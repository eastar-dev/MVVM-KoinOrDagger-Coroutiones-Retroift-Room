package eastar.base

import smart.base.BDApplication

abstract class BApplication : BDApplication() {
    override fun onCreate() {
        super.onCreate()
        PP.CREATE(applicationContext)
    }
}