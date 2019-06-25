package smart.retrofit

import android.content.Context
import android.log.Log
import android.retrofit.Logger
import android.webkit.CookieManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//val EUCKR = "euc-kr"
//val UTF8 = "UTF-8"
//val COOKIE = "Cookie"
//val SET_COOKIE = "Set-Cookie"
//
//val SESSIONID = "JSESSIONID"
//val USER_AGENT = "User-Agent"
//
//var LOG = false
//var _POJO = false
//var _OUT_1 = false
//var _OUT_2 = false
//var _OUT_3 = false
//var _OUT_H = false
//var _OUT_C = false
//var _IN_1 = false
//var _IN_2 = false
//var _IN_3 = false
//var _IN_H = false
//var _IN_C = false

fun createOkHttpClient(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool())
            .addNetworkInterceptor(StethoInterceptor())
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
            .addInterceptor(Logger())
            .addInterceptor(ChuckInterceptor(context))
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    for (cookie in cookies) {
                        CookieManager.getInstance().setCookie(url.toString(), cookie.toString())
                        Log.w(url.toString(), cookie.toString())
                    }
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cm = CookieManager.getInstance()
                    val cookies: ArrayList<Cookie> = ArrayList()
                    if (cm.getCookie(url.toString()) != null) {
                        val splitCookies = cm.getCookie(url.toString()).split("[,;]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (i in splitCookies.indices) {
                            cookies.add(Cookie.parse(url, splitCookies[i].trim { it <= ' ' })!!)
                            Log.e(Cookie.parse(url, splitCookies[i].trim { it <= ' ' })!!)
                        }
                    }
                    return cookies
                }
            })
            .build()
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



