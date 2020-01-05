package dev.eastar.branch.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.eastar.branch.model.BranchEntity

@Database(entities = [BranchEntity::class], version = 1)
abstract class BranchDatabase : RoomDatabase() {
    abstract fun branchDao(): BranchDao
}