package dev.eastar.branch.repository

import dev.eastar.branch.model.BranchEntity

interface BranchRepository {
    suspend fun getBranch(): List<BranchEntity>
    suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity>
    suspend fun getBranchByKeyword(keyword: String): List<BranchEntity>
}