package dev.eastar.branch2.repository

import dev.eastar.branch2.model.BranchItems
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BranchService {
    @POST("https://openhanafn.ttmap.co.kr/hanafn_json.jsp?company_code=01,21")
    suspend fun branch(): BranchItems


    @POST("https://openhanafn.ttmap.co.kr/hanafn_json.jsp")
    @FormUrlEncoded
    suspend fun radiusBranch(
        @Field("position_x") position_x: String,
        @Field("position_y") position_y: String,
        @Field("radius") radius: String = "500",
        @Field("company_code") company_code: String = "01,21"
    ): BranchItems

    @POST("https://openhanafn.ttmap.co.kr/hanafn_json.jsp")
    @FormUrlEncoded
    suspend fun queryBranch(
        @Field("position_x") position_x: String = "126985168",
        @Field("position_y") position_y: String = "37565837",
        @Field("query") radius: String = "종로",
        @Field("company_code") company_code: String = "01,21"
    ): BranchItems
}
