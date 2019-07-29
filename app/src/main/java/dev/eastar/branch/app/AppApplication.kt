package dev.eastar.branch.app

import android.content.Context
import android.log.Log
import dev.eastar.branch.base.startBranchKoin
import eastar.base.BApplication

class AppApplication : BApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        Log.e("시작됨>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    }

    override fun onCreate() {
        super.onCreate()
        startBranchKoin(this)
    }
}
