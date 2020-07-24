package dev.eastar.branch2.di

import dagger.Module
import dagger.Provides
import dev.eastar.branch2.repository.BranchDao
import dev.eastar.branch2.repository.BranchRepository
import dev.eastar.branch2.repository.BranchRepositoryImpl
import dev.eastar.branch2.repository.BranchService

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(branchService: BranchService, branchDao: BranchDao): BranchRepository {
        return BranchRepositoryImpl(branchService, branchDao)
    }
}

