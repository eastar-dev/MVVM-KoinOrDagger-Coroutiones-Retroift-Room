package dev.eastar.branch.presenter

import android.log.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.eastar.branch.model.BranchEntity
import dev.eastar.branch.repository.BranchRepository
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import javax.inject.Inject

class BranchViewModel @Inject constructor(val repository: BranchRepository) : ViewModel() {

    val progress by lazy { MutableLiveData<Int>() }
    val branchInMap by lazy { MutableLiveData<List<BranchEntity>>() }
    val branchTel by lazy { MutableLiveData<BranchEntity>() }

    fun getBranch() {
        viewModelScope.launch {
            runCatching {
                repository.getBranch()
            }
        }
    }

    fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
        searchInMap(mapView)
    }

    fun searchInMap(mapView: MapView) {
        viewModelScope.launch {
            val branch = repository.getBranchByRect(mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.longitude
                    , mapView.mapPointBounds.topRight.mapPointGeoCoord.latitude
                    , mapView.mapPointBounds.topRight.mapPointGeoCoord.longitude
                    , mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.latitude
            )
            branchInMap.value = branch
            Log.e("queryed branchsAll.size", branch.size)
        }
    }

    fun touchedPoiItem(branch: BranchEntity) {
        if (branch.tel?.replace("\\D".toRegex(), "").isNullOrBlank())
            return
        branchTel.value = branch
    }
}
