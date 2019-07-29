package android.retrofit

import android.webkit.CookieManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val cookieManager = object : CookieJar {
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { CookieManager.getInstance().setCookie(url.toString(), it.toString()) }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return CookieManager.getInstance().getCookie(url.toString())?.let { cookies ->
            cookies.split("[,;]".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .map { Cookie.parse(url, it.trim())!! }
                    .toList()
        } ?: emptyList()
    }
}

fun <T> createService(okHttpClient: OkHttpClient, baseUrl: String, service: Class<T>): T {
    return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(service)
}




//interface NetEnty {
//    @POST
//    fun <T : NetEnty> async(@Url url: String, @QueryMap query: Map<String, String>): T
//
//    fun service() = Retrofit.Builder()
//            .client(createOkHttpClient())
//            .addConverterFactory(GsonConverterFactory.create())
//
//}
//
//abstract class SmartEnty : NetEnty {
//    data class Data(val firebase_token: String?)
//
//    val observable by lazy {
//        service()
//                .baseUrl("")
//                .build()
//                .create(this::class.java)
//                .async<SmartEnty>("", mapOf())
//    }
//}



