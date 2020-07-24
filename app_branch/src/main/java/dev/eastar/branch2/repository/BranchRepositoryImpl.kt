package dev.eastar.branch2.repository

import android.log.Log
import dev.eastar.branch2.model.BranchEntity
import dev.eastar.branch2.model.wgs84
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BranchRepositoryImpl @Inject constructor(
    val branchNetSource: BranchService,
    val branchDao: BranchDao
) : BranchRepository {

    override suspend fun loadBranch() {

        val branchList = withContext(Dispatchers.IO) {
            kotlin.runCatching { branchNetSource.branch() }
                .onFailure { Log.w(it) }
                .getOrNull()
        }

        val branchs = branchList?.list?.map { it ->
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
            )
        }?.toTypedArray() ?: emptyArray()
        branchDao.insertBranch(*branchs)
    }

    override suspend fun getBranchByRect(
        l: Double,
        t: Double,
        r: Double,
        b: Double
    ): List<BranchEntity> =
        branchDao.getBranchByRect(l, t, r, b)

    override suspend fun getBranchByKeyword(keyword: String): List<BranchEntity> =
        branchDao.getBranchByKeyword(keyword)
}