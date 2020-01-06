package dev.eastar.branch.di.module

import android.base.CD
import android.retrofit.OkHttp3Logger
import android.retrofit.cookieJar
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dev.eastar.branch.repository.BranchService
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {
    @Singleton
    @Provides
    fun provideBranchService(): BranchService {
        return Retrofit.Builder()
                .baseUrl("http://123.123.123.123:1234")
                .client(OkHttpClient.Builder()
                        .connectTimeout(10L, TimeUnit.SECONDS)
                        .readTimeout(60L, TimeUnit.SECONDS)
                        .writeTimeout(10L, TimeUnit.SECONDS)
                        .connectionPool(ConnectionPool())
                        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
                        .addInterceptor(OkHttp3Logger())
                        .addInterceptor(ChuckInterceptor(CD.application))
                        .cookieJar(cookieJar)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(BranchService::class.java)
    }

}

