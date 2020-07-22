package android.retrofit

import android.webkit.CookieManager

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val cookieJar = object : CookieJar {
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { CookieManager.getInstance().setCookie(url.toString(), it.toString()) }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return CookieManager.getInstance().getCookie(url.toString())
            ?.split("[,;]".toRegex())
            ?.filterNot { it.isBlank() }
            ?.mapNotNull { Cookie.parse(url, it.trim()) }
            ?.toList()
            ?: emptyList()
    }
}

fun <T> createService(okHttpClient: OkHttpClient, baseUrl: String, service: Class<T>): T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(service)
}

fun <T> createService(okHttpClient: OkHttpClient, service: Class<T>): T {
    return Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(service)
}


