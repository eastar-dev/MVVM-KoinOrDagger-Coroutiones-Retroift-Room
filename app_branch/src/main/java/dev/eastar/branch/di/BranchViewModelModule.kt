package dev.eastar.branch.di

import dagger.Module
import dagger.Provides
import dev.eastar.branch.data.BranchRepository
import dev.eastar.branch.presentation.BranchViewModel

@Module
class BranchViewModelModule {
    @Provides
    fun provideMainViewModel(repository: BranchRepository): BranchViewModel {
        return BranchViewModel(repository)
    }
}
