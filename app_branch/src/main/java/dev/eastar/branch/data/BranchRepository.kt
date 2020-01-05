package dev.eastar.branch.data

import eastar.base.PP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface BranchRepository {
    suspend fun getBranch(): List<BranchEntity>
    suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity>
    suspend fun getBranchByKeyword(keyword: String): List<BranchEntity>
}

class BranchRepositoryImpl @Inject constructor(
        private val branchNetSource: BranchService,
        private val branchDBSource: BranchDao
) : BranchRepository {
    init {
        initBranch()
    }

    private fun initBranch() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isUpdateBranch()) return@launch
            val branchFromNet = branchNetSource.getBranchAsync().await()
            if (!branchFromNet.isSuccessful) return@launch
            val items = branchFromNet.body()?.list
            if (items.isNullOrEmpty()) return@launch

            val address1 = mutableSetOf<String?>()
            val address2 = mutableSetOf<String?>()

            val branchForDb = items.map {
                BranchEntity(it.id
                        , it.search_type
                        , it.company_name
                        , it.name
                        , it.address
                        , it.address?.split(' ')?.get(0)
                        , it.address?.split(' ')?.get(1)
                        , it.driving_directions
                        , it.tel
                        , it.fax
                        , it.services
                        , it.branch_code
                        , it.business_hours
                        , it.branch_type
                        , it.mgr_name
                        , it.mgr_tel
                        , toWGS84(it.position_y)
                        , toWGS84(it.position_x))
            }.toTypedArray()
                    .also {
                        it.forEach {
                            it.lat
                            it.lon
                        }
                    }

//            address1.forEach { Log.e(it) }
//            address2.forEach { Log.e(it) }
            branchDBSource.insertBranch(*branchForDb)

            PP.LAST_BRANCH_SYNC.set(System.currentTimeMillis())
        }

    }

    private suspend fun isUpdateBranch(): Boolean {
        return true
//        Log.e(System.currentTimeMillis(), PP.LAST_BRANCH_SYNC.getLong(), System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong(), if (System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong() > TimeUnit.MINUTES.toMillis(10)) "Need Sync" else "Synced")
//        return System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong() > TimeUnit.MINUTES.toMillis(10)
    }

    override suspend fun getBranch(): List<BranchEntity> {
        return branchDBSource.getBranch()
    }

    override suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity> {
        return branchDBSource.getBranchByRect(l, t, r, b)
    }

    override suspend fun getBranchByKeyword(keyword: String): List<BranchEntity> {
        return branchDBSource.getBranchByKeyword(keyword)
    }
}

val BranchEntity.icon: String
    get() = "https://openhanafn.ttmap.co.kr/iframe/images/map_icn_hana.png"
/*
"https://openhanafn.ttmap.co.kr/iframe/images/map_icn_gb365.png"
*/
