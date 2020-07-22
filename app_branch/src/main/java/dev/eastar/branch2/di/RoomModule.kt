package dev.eastar.branch2.di

import android.app.Application
import androidx.room.Room
import dev.eastar.branch2.repository.BranchDao
import dev.eastar.branch2.repository.BranchDatabase
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
    @Provides
    fun provideBranchDatabase(application: Application): BranchDatabase {
        return Room.databaseBuilder(application, BranchDatabase::class.java, "Branch.db").build()
    }

    @Provides
    fun provideBranchDao(branchDatabase: BranchDatabase): BranchDao {
        return branchDatabase.branchDao()
    }
}
