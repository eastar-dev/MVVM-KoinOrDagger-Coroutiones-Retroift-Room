package dev.eastar.branch.repository

import android.log.Log
import dev.eastar.branch.model.BranchEntity
import dev.eastar.branch.model.toWGS84
import eastar.base.PP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BranchRepositoryImpl @Inject constructor(val branchNetSource: BranchService, val branchDao: BranchDao) : BranchRepository {

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
            branchDao.insertBranch(*branchForDb)

            PP.LAST_BRANCH_SYNC.set(System.currentTimeMillis())
        }

    }

    private fun isUpdateBranch(): Boolean {
        return true
        Log.e(System.currentTimeMillis(), PP.LAST_BRANCH_SYNC.getLong(), System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong(), if (System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong() > TimeUnit.MINUTES.toMillis(10)) "Need Sync" else "Synced")
        return System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong() > TimeUnit.MINUTES.toMillis(10)
    }

    override suspend fun getBranch(): List<BranchEntity> = branchDao.getBranch()
    override suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity> = branchDao.getBranchByRect(l, t, r, b)
    override suspend fun getBranchByKeyword(keyword: String): List<BranchEntity> = branchDao.getBranchByKeyword(keyword)
}