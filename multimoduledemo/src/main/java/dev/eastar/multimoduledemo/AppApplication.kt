package dev.eastar.multimoduledemo

import android.content.Context
import android.log.Log
import dev.eastar.branch.base.startBranchKoin
import eastar.base.BApplication

class AppApplication : BApplication() {
    override fun onCreate() {
        super.onCreate()
        startBranchKoin(this)
    }
}
