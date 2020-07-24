package dev.eastar.branch2.di

import android.app.Application
import dev.eastar.branch2.ui.BranchMap
import dagger.BindsInstance
import dagger.Component
import dev.eastar.branch2.ui.BranchViewModel
import javax.inject.Singleton

@Component(modules = [RoomModule::class, RetrofitModule::class, RepositoryModule::class])
@Singleton
interface BranchComponent {
    fun inject(branchViewModel: BranchViewModel)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

//        @BindsInstance
//        fun activity(owner: ViewModelStoreOwner): Builder

        fun build(): BranchComponent
    }
}
