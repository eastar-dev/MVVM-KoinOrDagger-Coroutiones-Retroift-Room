package dev.eastar.branch.di

import android.retrofit.createOkHttpClient
import android.retrofit.createService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.eastar.branch.repository.BranchService

@Module
@Suppress("unused")
class RetrofitModule {
    @Provides
    @Reusable
    internal fun provideService(): BranchService {
        return createService(createOkHttpClient(), BranchService::class.java)
    }
}

