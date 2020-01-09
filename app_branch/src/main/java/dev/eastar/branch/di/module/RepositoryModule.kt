package dev.eastar.branch.di.module

import dagger.Module
import dagger.Provides
import dev.eastar.branch.repository.BranchDao
import dev.eastar.branch.repository.BranchRepository
import dev.eastar.branch.repository.BranchRepositoryImpl
import dev.eastar.branch.repository.BranchService

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(branchService: BranchService, branchDao: BranchDao): BranchRepository {
        return BranchRepositoryImpl(branchService, branchDao)
    }
}
