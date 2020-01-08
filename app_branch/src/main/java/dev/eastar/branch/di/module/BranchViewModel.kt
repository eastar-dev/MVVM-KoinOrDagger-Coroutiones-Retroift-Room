package dev.eastar.branch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dev.eastar.branch.di.scope.ViewModelKey
import dev.eastar.branch.presenter.AppViewModelFactory
import dev.eastar.branch.presenter.BranchViewModel
import dev.eastar.branch.repository.BranchRepository
import dev.eastar.branch.ui.BranchMap
import javax.inject.Provider

//@Module(includes = [BranchRepository::class])
@Module
class BranchViewModule {
    @Provides
    fun provideViewModelFactory(providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory =
            AppViewModelFactory(providers)

    @Module
    class ProvideViewModule {
        @Provides
        @IntoMap
        @ViewModelKey(BranchViewModel::class)
        fun provideBranchViewModel(branchRepository: BranchRepository): ViewModel =
                BranchViewModel(branchRepository)
    }

    @Module
    class InjectViewModule {
        @Provides
        fun provideBranchViewModel(factory: ViewModelProvider.Factory, target: BranchMap) =
                ViewModelProviders.of(target, factory).get(BranchViewModel::class.java)
    }
}
