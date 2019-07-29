package android.retrofit

import android.content.Context
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

fun createOkHttpClient(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool())
            .cookieJar(cookieManager)
            .build()
}
