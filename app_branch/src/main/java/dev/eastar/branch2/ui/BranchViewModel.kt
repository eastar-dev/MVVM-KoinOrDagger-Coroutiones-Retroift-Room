package dev.eastar.branch2.ui

import android.log.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.eastar.branch2.di.DaggerBranchComponent
import dev.eastar.branch2.model.BranchEntity
import dev.eastar.branch2.repository.BranchRepository
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapView
import javax.inject.Inject

//class BranchViewModel @Inject constructor(val repository: BranchRepository) : ViewModel() {
class BranchViewModel : ViewModel() {
    init {
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
            }.onFailure { Log.w(it) }
            Log.w("loadBranch", "초기로딩6")
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
//            Log.e("queryed branchsAll.size", branch.size)
//            branch.forEach { Log.e(it.branch_type, it.name, it.address) }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

