package dev.eastar.branch2.di

import android.app.Application
import dev.eastar.branch2.repository.BranchDao
import dev.eastar.branch2.repository.BranchRepository
import dev.eastar.branch2.repository.BranchRepositoryImpl
import dev.eastar.branch2.repository.BranchService
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(application: Application, branchService: BranchService, branchDao: BranchDao): BranchRepository {
        return BranchRepositoryImpl(application, branchService, branchDao)
    }
}

