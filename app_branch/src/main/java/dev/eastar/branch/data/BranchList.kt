package dev.eastar.branch.data

class BranchList {
    var list: Array<BranchNetEntity>? = null

    data class BranchNetEntity(
            var id: String
            , var search_type: String? = null
            , var company_name: String? = null
            , var name: String
            , var address: String? = null
            , var driving_directions: String? = null
            , var tel: String? = null
            , var fax: String? = null
            , var services: String? = null
            , var branch_code: String? = null
            , var business_hours: String? = null
            , var branch_type: String? = null
            , var mgr_name: String? = null
            , var mgr_tel: String? = null
            , var position_x: Int = 0
            , var position_y: Int = 0
    )
}

fun toWGS84(location: Int): Double = location / 1_000_000.0


data class BranchH(
        var isExtend: String? = "",
        var resultList: List<Result?>? = listOf(),
        var resultListFn: List<ResultFn?>? = listOf()
)

data class ResultFn(
        var address: String? = "",
        var address_new: String? = "",
        var biz_type: String? = "",
        var e_address: String? = "",
        var e_address_new: String? = "",
        var e_name: String? = "",
        var e_search_road: String? = "",
        var e_upmu: String? = "",
        var fax: String? = "",
        var map_x: String? = "",
        var map_y: String? = "",
        var name: String? = "",
        var search_road: String? = "",
        var seq_no: String? = "",
        var sido_name: String? = "",
        var sigungu_name: String? = "",
        var tel: String? = "",
        var upmu: String? = "",
        var zip_code: String? = ""
)

data class Result(
        var address_detail: String? = "",
        var address_new: String? = "",
        var all_night_yn: String? = "",
        var atm_install_cnt: String? = "",
        var atm_install_place: String? = "",
        var atm_operating_hour: String? = "",
        var atm_yn: String? = "",
        var bank_type: String? = "",
        var bankbook_yn: String? = "",
        var best_branch_url: String? = "",
        var best_yn: String? = "",
        var branch_code: String? = "",
        var branch_name: String? = "",
        var business_hour: String? = "",
        var deposit_withdraw_yn: String? = "",
        var deposit_yn: String? = "",
        var directions: String? = "",
        var en_address: String? = "",
        var en_address_new: String? = "",
        var en_atm_install_place: String? = "",
        var en_branch_name: String? = "",
        var en_directions: String? = "",
        var en_manager_branch: String? = "",
        var en_subway_info: String? = "",
        var fax: String? = "",
        var fdi_yn: String? = "",
        var fifty_thousand_deposit_withdraw_yn: String? = "",
        var foreign_vip_yn: String? = "",
        var global_atm_yn: String? = "",
        var global_desk_yn: String? = "",
        var manager_branch: String? = "",
        var manager_branch_code: String? = "",
        var map_x: String? = "",
        var map_y: String? = "",
        var mobile_branch_url: String? = "",
        var night_banking_yn: String? = "",
        var night_deposit_yn: String? = "",
        var overseas_center_yn: String? = "",
        var overseas_club_yn: String? = "",
        var pb_branch_yn: String? = "",
        var seq_no: String? = "",
        var sido_name: String? = "",
        var sigungu_name: String? = "",
        var smart_branch_url: String? = "",
        var subway_info: String? = "",
        var sunday_banking_yn: String? = "",
        var svc_type: String? = "",
        var tel: String? = "",
        var withdraw_yn: String? = ""
)