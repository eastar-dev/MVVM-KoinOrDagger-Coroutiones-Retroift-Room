package dev.eastar.branch.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.eastar.branch.di.module.BranchMapModule
import dev.eastar.branch.di.scope.ActivityScope

@Module
abstract class AppMainModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [BranchMapModule::class])
    abstract fun provideAppMain(): AppMain
}


