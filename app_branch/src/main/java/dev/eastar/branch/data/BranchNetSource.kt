package dev.eastar.branch.data

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST

interface BranchService {
    @POST("/hanafn_json.jsp?company_code=01,21")
    fun getBranchAsync(): Deferred<Response<BranchList>>
}
