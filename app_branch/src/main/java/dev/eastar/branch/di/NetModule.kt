package dev.eastar.branch.di

import android.retrofit.createOkHttpClient
import android.retrofit.createService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.eastar.branch.data.BranchService
import okhttp3.OkHttpClient

@Module
@Suppress("unused")
class NetModule {
    @Provides
    @Reusable
    internal fun provideOkHttpClient(): OkHttpClient {
        return createOkHttpClient()
    }

    @Provides
    @Reusable
    internal fun provideService(okHttpClient: OkHttpClient): BranchService {
        return createService(okHttpClient, BranchService::class.java)
    }
}

