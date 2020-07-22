package dev.eastar.branch2.di

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner
import dev.eastar.branch2.di.RepositoryModule
import dev.eastar.branch2.di.RetrofitModule
import dev.eastar.branch2.di.RoomModule
import dev.eastar.branch2.ui.BranchList
import dev.eastar.branch2.ui.BranchMap
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerContentProvider
import dev.eastar.branch2.ui.BranchViewModel
import javax.inject.Singleton

@Component(modules = [RoomModule::class, RetrofitModule::class, RepositoryModule::class])
@Singleton
interface BranchComponent {
    fun inject(branchMap: BranchMap)
    fun inject(branchMap: BranchViewModel)
    fun inject(branchList: BranchList)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

//        @BindsInstance
//        fun activity(owner: ViewModelStoreOwner): Builder

        fun build(): BranchComponent
    }
}
