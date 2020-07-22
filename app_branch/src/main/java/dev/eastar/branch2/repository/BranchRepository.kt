package dev.eastar.branch2.repository

import dev.eastar.branch2.model.BranchEntity

interface BranchRepository {
    suspend fun loadBranch()
//    suspend fun getBranch(): List<BranchEntity>
    suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity>
    suspend fun getBranchByKeyword(keyword: String): List<BranchEntity>
}