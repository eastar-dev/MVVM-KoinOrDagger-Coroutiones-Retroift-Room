package dev.eastar.branch.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.eastar.branch.di.scope.FragmentScope
import dev.eastar.branch.ui.BranchMap

@Module
abstract class BranchMapModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideBranchMap(): BranchMap
}


