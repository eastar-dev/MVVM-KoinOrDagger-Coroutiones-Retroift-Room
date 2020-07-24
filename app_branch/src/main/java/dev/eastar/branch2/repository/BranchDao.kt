package dev.eastar.branch2.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.eastar.branch2.model.BranchEntity

@Dao
interface BranchDao {
    @Query("SELECT * FROM branch WHERE address LIKE :keyword")
    suspend fun getBranchByKeyword(keyword: String): List<BranchEntity>

    @Query("SELECT * FROM branch WHERE (lon between :l and :r) and (lat between :b and :t)")
    suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBranch(vararg branch: BranchEntity)
}
