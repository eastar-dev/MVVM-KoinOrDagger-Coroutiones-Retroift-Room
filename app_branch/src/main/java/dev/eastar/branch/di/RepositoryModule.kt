package dev.eastar.branch.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.eastar.branch.data.BranchDao
import dev.eastar.branch.data.BranchRepository
import dev.eastar.branch.data.BranchRepositoryImpl
import dev.eastar.branch.data.BranchService

@Module
@Suppress("unused")
class RepositoryModule {
    @Provides
    @Reusable
    fun provideRepository(branchNetSource: BranchService, branchDBSource: BranchDao): BranchRepository {
        return BranchRepositoryImpl(branchNetSource, branchDBSource)
    }
}
