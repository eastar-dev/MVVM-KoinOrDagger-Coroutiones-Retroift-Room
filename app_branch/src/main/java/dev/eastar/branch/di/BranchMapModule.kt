package dev.eastar.branch.di

import dagger.Module
import dagger.Provides
import dev.eastar.branch.di.annotation.FragmentScope
import dev.eastar.branch.repository.BranchRepository
import dev.eastar.branch.presenter.BranchViewModel

@Module
class BranchMapModule {
    @Provides
    @FragmentScope
    fun provideBranchViewModel(repository: BranchRepository): BranchViewModel {
        return BranchViewModel(repository)
    }
}

