package dev.eastar.branch.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.eastar.branch.repository.BranchDao
import dev.eastar.branch.repository.BranchDatabase
import javax.inject.Singleton

@Module
@Suppress("unused")
class RoomModule {
    @Provides
    @Singleton
    internal fun provideBranchDatabase(application: Application): BranchDatabase {
        return Room.databaseBuilder(application, BranchDatabase::class.java, "Branch.db").build()
    }

    @Provides
    @Reusable
    internal fun provideBranchDao(branchDatabase: BranchDatabase): BranchDao {
        return branchDatabase.branchDao()
    }
}
