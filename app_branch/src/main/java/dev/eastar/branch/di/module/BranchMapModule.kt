package dev.eastar.branch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dev.eastar.branch.presenter.AppViewModelFactory
import dev.eastar.branch.presenter.BranchViewModel
import dev.eastar.branch.ui.BranchMap
import javax.inject.Provider

//@Module(includes = [BranchViewModel::class])
@Module
abstract class BranchMapModule {
    //@ContributesAndroidInjector(modules = [BranchViewModel::class])
    @ContributesAndroidInjector
    abstract fun bind(): BranchViewModel
}