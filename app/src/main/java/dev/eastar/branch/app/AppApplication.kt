package dev.eastar.branch.app

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.eastar.branch.di.RepositoryModule
import dev.eastar.branch.di.RetrofitModule
import dev.eastar.branch.di.RoomModule
import eastar.base.BApplication
import javax.inject.Singleton

class AppApplication : BApplication() {
    override fun applicationInjector(): AndroidInjector<out dagger.android.DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this).apply { inject(this@AppApplication) }
    }
}

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class
    , RetrofitModule::class
    , RepositoryModule::class
    , RoomModule::class
])

interface ApplicationComponent : AndroidInjector<AppApplication> {
    //abstract class Builder : AndroidInjector.Factory<AppApplication>
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<AppApplication>()
}