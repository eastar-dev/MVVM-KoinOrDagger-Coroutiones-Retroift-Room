package dev.eastar.branch.di.scope

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerContentProvider
import dagger.android.support.AndroidSupportInjectionModule
import dev.eastar.branch.di.module.*
import dev.eastar.branch.ui.BranchMap
import javax.inject.Singleton

@Component(modules = [AndroidSupportInjectionModule::class
    , RoomModule::class
    , RetrofitModule::class
    , RepositoryModule::class
    , BranchViewModule::class
    , BranchMapModule::class
])
@Singleton
interface BranchComponent : AndroidInjector<DaggerContentProvider> {
    fun inject(branchMap: BranchMap)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): BranchComponent
    }
}
