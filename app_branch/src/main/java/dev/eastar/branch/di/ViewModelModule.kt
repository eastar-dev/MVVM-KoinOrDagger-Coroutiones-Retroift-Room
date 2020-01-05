package dev.eastar.branch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.eastar.branch.data.ViewModelFactory
import dev.eastar.branch.presentation.BranchViewModel

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BranchViewModel::class)
    internal abstract fun branchViewModel(viewModel: BranchViewModel): ViewModel

    //Add more ViewModels here
}