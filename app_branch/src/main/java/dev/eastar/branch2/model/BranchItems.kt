package dev.eastar.branch2.model

class BranchItems {
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
        , var atm_install_place: String? = null
        , var atm_operating_hours: String? = null
        , var distance: String? = null
    )
}

