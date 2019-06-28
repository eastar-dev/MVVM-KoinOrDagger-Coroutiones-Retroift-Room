package dev.eastar.branch.data

import kotlinx.coroutines.Deferred
import org.koin.dsl.module
import retrofit2.Response
import retrofit2.http.POST
import android.retrofit.createOkHttpClient
import android.retrofit.createService

interface BranchService {
    //    @POST("/hanafn_json.jsp?company_code=01,21&radius=300&position_x=126920000&position_y=37554002")

    @POST("/hanafn_json.jsp?company_code=01,21")
    fun getBranchsAsync(): Deferred<Response<BranchList>>
}

val netModule = module {
    single { createOkHttpClient(get()) }
    single { createService(get(), "https://openhanafn.ttmap.co.kr", BranchService::class.java) }
}
