package android.base

import android.app.Application

class CD {
    companion object {
        var LOG = true
        var DEVELOP = true
        var PASS = true
        var LIFELOG = true
        lateinit var application : Application
    }
}
