package dev.eastar.branch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dev.eastar.branch.presenter.AppViewModelFactory
import dev.eastar.branch.presenter.BranchViewModel
import dev.eastar.branch.ui.BranchMap
import javax.inject.Provider

@Module
abstract class BranchMapModule {
    //@ContributesAndroidInjector(modules = [BranchViewModel::class])
    //abstract fun bind(): BranchMap

    @Provides
    fun provideViewModelFactory(providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory =
            AppViewModelFactory(providers)

    @Module
    class InjectViewModel {
        @Provides
        fun provideAddNoteViewModel(factory: ViewModelProvider.Factory, target: BranchMap) =
                ViewModelProviders.of(target, factory).get(BranchViewModel::class.java)
    }

}