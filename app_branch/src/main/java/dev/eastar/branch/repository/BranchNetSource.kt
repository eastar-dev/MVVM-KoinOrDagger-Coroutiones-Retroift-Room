package dev.eastar.branch.repository

import dev.eastar.branch.model.BranchList
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST

interface BranchService {
    @POST("/hanafn_json.jsp?company_code=01,21")
    fun getBranchAsync(): Deferred<Response<BranchList>>
}
