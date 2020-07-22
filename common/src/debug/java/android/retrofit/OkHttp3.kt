package android.retrofit

import android.base.CD
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(10L, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool())
        .addInterceptor(OkHttp3Logger())
        .addInterceptor(ChuckInterceptor(CD.application))
        .cookieJar(cookieJar)
        .build()




