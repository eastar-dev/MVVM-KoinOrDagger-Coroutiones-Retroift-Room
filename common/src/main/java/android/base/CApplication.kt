package android.base

abstract class CApplication : dagger.android.support.DaggerApplication() {
    init {
        CD.application = this
    }
}
//abstract class CApplication : android.app.Application()