package dev.eastar.branch.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.eastar.branch.repository.BranchDao
import dev.eastar.branch.repository.BranchDatabase

@Module
class RoomModule {
    @Provides
    fun provideBranchDatabase(context: Context): BranchDatabase {
        return Room.databaseBuilder(context, BranchDatabase::class.java, "Branch.db").build()
    }

    @Provides
    fun provideBranchDao(branchDatabase: BranchDatabase): BranchDao {
        return branchDatabase.branchDao()
    }
}
