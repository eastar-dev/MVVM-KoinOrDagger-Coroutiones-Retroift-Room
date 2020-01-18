package android.base

import android.app.Application

class CD {
    companion object {
        var LOG = false
        var DEVELOP = false
        var LIFELOG = false
        var PASS = false
        lateinit var application : Application
    }
}
