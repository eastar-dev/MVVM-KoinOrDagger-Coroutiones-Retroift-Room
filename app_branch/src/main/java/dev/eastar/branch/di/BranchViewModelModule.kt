package dev.eastar.branch.di

import dagger.Module
import dagger.Provides
import dev.eastar.branch.repository.BranchRepository
import dev.eastar.branch.presenter.BranchViewModel

@Module
class BranchViewModelModule {
    @Provides
    fun provideMainViewModel(repository: BranchRepository): BranchViewModel {
        return BranchViewModel(repository)
    }
}
