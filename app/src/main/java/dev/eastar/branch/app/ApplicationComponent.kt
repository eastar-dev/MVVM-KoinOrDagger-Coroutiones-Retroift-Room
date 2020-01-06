package dev.eastar.branch.app

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import dev.eastar.branch.di.module.RepositoryModule
import dev.eastar.branch.di.module.RetrofitModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class
    , RetrofitModule::class
    , RepositoryModule::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: AppApplication): Builder

        fun build(): ApplicationComponent
    }

    fun inject(mainApp: AppApplication)
}