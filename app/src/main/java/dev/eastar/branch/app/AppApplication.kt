package dev.eastar.branch.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import eastar.base.BApplication

class AppApplication : BApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }
}
