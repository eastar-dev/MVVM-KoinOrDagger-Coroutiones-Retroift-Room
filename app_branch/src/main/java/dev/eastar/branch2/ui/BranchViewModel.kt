package dev.eastar.branch2.ui

import android.app.Application
import android.log.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.eastar.branch2.di.DaggerBranchComponent
import dev.eastar.branch2.model.BranchEntity
import dev.eastar.branch2.repository.BranchRepository
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapView
import javax.inject.Inject


class BranchViewModel(application: Application) : AndroidViewModel(application) {
    init {
        DaggerBranchComponent.builder()
            .application(application)
            .build()
            .inject(this)
    }
    @Inject
    lateinit var repository: BranchRepository

    val loadBranched: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val branchInMap by lazy { MutableLiveData<List<BranchEntity>>() }

    init {
        loadBranch()
    }

    private fun loadBranch() {
        viewModelScope.launch {
            Log.w("loadBranch", "초기로딩0")
            kotlin.runCatching {
                repository.loadBranch()
            }.onFailure {
                Log.w(it)
            }
            loadBranched.value = true
            Log.w("loadBranch", "초기로딩99")
        }
    }

    fun searchInMap(mapView: MapView) {
        Log.w("범위검색")
        viewModelScope.launch {
            val branch = repository.getBranchByRect(
                mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.longitude
                , mapView.mapPointBounds.topRight.mapPointGeoCoord.latitude
                , mapView.mapPointBounds.topRight.mapPointGeoCoord.longitude
                , mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.latitude
            )
            branchInMap.value = branch
        }
    }
}

