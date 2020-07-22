package dev.eastar.branch2.repository

import android.app.Application
import android.log.Log
import dev.eastar.branch2.model.BranchEntity
import dev.eastar.branch2.model.wgs84
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BranchRepositoryImpl @Inject constructor(val application: Application
                                               , val branchNetSource: BranchService
                                               , val branchDao: BranchDao)
    : BranchRepository {

    override suspend fun loadBranch() {
        Log.w("loadBranch", "초기로딩1")
        if (!isUpdateBranch())
            return
        val branchList = withContext(Dispatchers.IO) {
            kotlin.runCatching { branchNetSource.branch() }
                .onFailure { Log.w(it) }
                .getOrNull()
        }
//            val geocoder = Geocoder(application, Locale.KOREA)
        Log.w("초기지점 로딩2")
        branchList?.list?.map { it ->
            BranchEntity(
                it.id
                , it.search_type
                , it.company_name
                , it.name
                , it.address
                , it.address
                , it.address
                , it.driving_directions
                , it.tel
                , it.fax
                , it.services
                , it.branch_code
                , it.business_hours
                , it.branch_type
                , it.mgr_name
                , it.mgr_tel
                , it.position_y.wgs84
                , it.position_x.wgs84
            ).apply {
//                    val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
//                    addresses.getOrNull(0)?.let {
//                        address1 = it.locality ?: it.adminArea
//                        address2 = it.thoroughfare
//                    }
            }
        }?.toTypedArray()
            ?.let {
                Log.w("초기지점 로딩3")
                branchDao.insertBranch(*it)
                Log.w("초기지점 로딩4")
            }
        Log.w("초기지점 로딩5")

        //TODO
//            PP.LAST_BRANCH_SYNC.set(System.currentTimeMillis())


    }

    private fun isUpdateBranch(): Boolean {
        return true
        //TODO
//        Log.e(System.currentTimeMillis(), PP.LAST_BRANCH_SYNC.getLong(), System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong(), if (System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong() > TimeUnit.MINUTES.toMillis(10)) "Need Sync" else "Synced")
//        return System.currentTimeMillis() - PP.LAST_BRANCH_SYNC.getLong() > TimeUnit.MINUTES.toMillis(10)
//        return true
    }

    //    override suspend fun getBranch(): List<BranchEntity> = branchDao.getBranch()
    override suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity> = branchDao.getBranchByRect(l, t, r, b)
    override suspend fun getBranchByKeyword(keyword: String): List<BranchEntity> = branchDao.getBranchByKeyword(keyword)
}