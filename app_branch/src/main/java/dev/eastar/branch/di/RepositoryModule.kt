package dev.eastar.branch.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.eastar.branch.repository.BranchDao
import dev.eastar.branch.repository.BranchRepository
import dev.eastar.branch.repository.BranchRepositoryImpl
import dev.eastar.branch.repository.BranchService

@Module
@Suppress("unused")
class RepositoryModule {
    @Provides
    @Reusable
    fun provideRepository(branchNetSource: BranchService, branchDBSource: BranchDao): BranchRepository {
        return BranchRepositoryImpl(branchNetSource, branchDBSource)
    }
}
