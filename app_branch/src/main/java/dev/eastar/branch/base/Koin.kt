package dev.eastar.branch.base

import android.app.Application
import dev.eastar.branch.data.netModule
import dev.eastar.branch.data.repositoryModule
import dev.eastar.branch.data.roomModule
import dev.eastar.branch.presentation.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

typealias Koin = Any

fun startBranchKoin(application: Application) {
    startKoin {
        androidLogger()
        androidContext(application)
        modules(viewmodelModule + repositoryModule + netModule + roomModule)
    }
}
