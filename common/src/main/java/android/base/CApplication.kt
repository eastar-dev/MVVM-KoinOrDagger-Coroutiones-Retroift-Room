package android.base

import android.app.Application

abstract class CApplication : Application() {
    init {
        CD.application = this
    }
}