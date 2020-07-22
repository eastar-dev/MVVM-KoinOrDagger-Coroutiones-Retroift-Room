package dev.eastar.branch2.di

import android.retrofit.OkHttp3Logger
import dev.eastar.branch2.repository.BranchService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RetrofitModule {
    @Provides
    fun provideOkhttp3Client() = OkHttpClient.Builder()
        .connectTimeout(10L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(10L, TimeUnit.SECONDS)
        .addNetworkInterceptor(OkHttp3Logger())
        .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://localhost/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideService(retrofit: Retrofit): BranchService = retrofit.create(BranchService::class.java)
}

