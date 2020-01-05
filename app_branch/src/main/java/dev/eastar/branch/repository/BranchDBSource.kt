package dev.eastar.branch.repository

import androidx.room.*
import dev.eastar.branch.model.BranchEntity

@Database(entities = [BranchEntity::class], version = 1)
abstract class BranchDatabase : RoomDatabase() {
    abstract fun branchDao(): BranchDao
}

@Dao
interface BranchDao {
    @Query("SELECT * FROM branch ")
    suspend fun getBranch(): List<BranchEntity>

    @Query("SELECT * FROM branch WHERE address LIKE :keyword")
    suspend fun getBranchByKeyword(keyword: String): List<BranchEntity>

    @Query("SELECT * FROM branch WHERE (lon between :l and :r) and (lat between :b and :t)")
    suspend fun getBranchByRect(l: Double, t: Double, r: Double, b: Double): List<BranchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBranch(vararg branch: BranchEntity)
}
