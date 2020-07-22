package dev.eastar.branch2.ui


import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.log.Log
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import dev.eastar.branch.databinding.BranchMapBinding
import dev.eastar.branch2.di.DaggerBranchComponent
import dev.eastar.branch2.model.BranchEntity
import dev.eastar.branch2.model.getInfo
import dev.eastar.branch2.model.poiIcon
import dev.eastar.ktx.alert
import dev.eastar.ktx.negativeButton
import dev.eastar.ktx.positiveButton
import eastar.base.BFragment
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.CalloutBalloonButtonType
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import javax.inject.Inject
import kotlin.coroutines.resume

class BranchMap : BFragment() {
    @Suppress("NonAsciiCharacters", "ObjectPropertyName")
    companion object {
        private const val ZOOM_WALK = 2
        private val startPoint = MapPoint.mapPointWithGeoCoord(37.56641387939453, 126.98188018798828)
    }

    val vm: BranchViewModel by activityViewModels()
    private lateinit var bb: BranchMapBinding

    override fun onDetach() {
        super.onDetach()
        bb.kakaoMap.surfaceDestroyed(null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        BranchMapBinding.inflate(inflater, container, false).let {
            bb = it
            it.vm = vm
            DaggerBranchComponent.builder()
                .application(inflater.context.applicationContext as Application)
//            .activity(requireActivity())
                .build()
                .inject(vm)

            it.root
        }

    override fun onLoadOnce() {
        bb.kakaoMap.setMapViewEventListener(onMapViewEventListener)
        bb.kakaoMap.setPOIItemEventListener(onPoiItemEventListener)
        bb.kakaoMap.setCurrentLocationEventListener(mOnCurrentLocationEventListener)
//        bb.kakaoMap.setCalloutBalloonAdapter(calloutBalloonAdapter)

        //livedata observer
        vm.branchInMap.observe(this@BranchMap) { updateMapPoi(it) }
        bb.currentPosition.setOnClickListener { moveCurrentPosition() }
    }

    override fun onClear() {
        bb.kakaoMap.removeAllPOIItems()
        bb.kakaoMap.removeAllPolylines()
        bb.kakaoMap.removeAllCircles()
        if (isLocationPermit)
            bb.kakaoMap.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    private fun showTelDialog(branch: BranchEntity?) {
        branch ?: return

        alert(branch.getInfo(), branch.name) {
            positiveButton("닫기")
            if (!branch.tel?.replace("\\D".toRegex(), "").isNullOrBlank())
                negativeButton("전화걸기") { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + branch.tel))) }
        }.setCanceledOnTouchOutside(true)

    }

    private fun updateMapPoi(branchs: List<BranchEntity>?) {
        branchs ?: return

        val mapView = bb.kakaoMap

        //원래 화면에 있던 핀
        val poiItems = mapView.poiItems.map {
            it.userObject as BranchEntity to it
        }.toMap()

        //새로 추가된 핀 추가
        branchs.subtract(poiItems.keys)
            .forEach {
                mapView.addPOIItem(it.poi)
            }

        //화면 밖에 있는 핀 재거
        poiItems.keys.subtract(branchs)
            .forEach {
                mapView.removePOIItem(poiItems[it])
            }
    }


    private val BranchEntity.poi: MapPOIItem
        get() = MapPOIItem().apply {
            itemName = name
            mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon)
            markerType = MapPOIItem.MarkerType.CustomImage
            showAnimationType = MapPOIItem.ShowAnimationType.NoAnimation
            isShowCalloutBalloonOnTouch = true
            isShowDisclosureButtonOnCalloutBalloon = true
            customImageResourceId = poiIcon
            isCustomImageAutoscale = false
            isDraggable = false
            userObject = this@poi
        }

    //현재위치 추적모드
    private fun moveCurrentPosition() {
        lifecycleScope.launch {
            Log.e("위치권한 확인")
            suspendCancellableCoroutine<Boolean> { co ->
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (!it)
                        co.cancel()
                    co.resume(it)
                }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            Log.e("현재위치이동")
            bb.kakaoMap.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        }
    }

    //map event ///////////////////////////////////////////////////////////////////////
    private var mOnCurrentLocationEventListener = object : MapView.CurrentLocationEventListener {
        override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, v: Float) {
            Log.e("현재위치이동 ok")
//            bb.currentPosition.isSelected = false
            mapView.setMapCenterPointAndZoomLevel(mapPoint, ZOOM_WALK, false)
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            vm.searchInMap(mapView)
        }

        override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView, v: Float) {}
        override fun onCurrentLocationUpdateFailed(mapView: MapView) {}
        override fun onCurrentLocationUpdateCancelled(mapView: MapView) {}
    }

    private val onMapViewEventListener = object : MapView.MapViewEventListener {
        var nomove: Boolean = false
        override fun onMapViewInitialized(mapView: MapView) {
            Log.e("초기화됨")
            mapView.setMapCenterPointAndZoomLevel(startPoint, ZOOM_WALK, false)
//            if (setCenterItem())
//                return
            Log.e("이동위치없음 초기화시에는 권한이 있을때만 이동")
            if (isLocationPermit) {
                moveCurrentPosition()
                return
            }
            vm.loadBranched.observe(this@BranchMap) {
                if (it)
                    vm.searchInMap(bb.kakaoMap)
            }
        }


        override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
            if (nomove) {
                nomove = false
                return
            }
            Log.e(mapView.zoomLevelFloat, ZOOM_WALK, "이동종료")
            vm.searchInMap(mapView)
        }

        override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {
            nomove = true
        }

        override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewZoomLevelChanged(mapView: MapView, i: Int) {}
        override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {}
    }

    private val onPoiItemEventListener = object : MapView.POIItemEventListener {
        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, mapPOIItem: MapPOIItem, calloutBalloonButtonType: CalloutBalloonButtonType) {}
        override fun onPOIItemSelected(mapView: MapView, mapPOIItem: MapPOIItem) {}
        override fun onDraggablePOIItemMoved(mapView: MapView, mapPOIItem: MapPOIItem, mapPoint: MapPoint) {}
        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, mapPOIItem: MapPOIItem) {
            showTelDialog(mapPOIItem.userObject as BranchEntity?)
        }
    }
}

//permit
val View.isLocationPermit: Boolean get() = context.isLocationPermit
val Fragment.isLocationPermit: Boolean get() = requireContext().isLocationPermit
val Context.isLocationPermit: Boolean get() = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


//    //특정위치 이동
//    fun setCenterItem(): Boolean {
//        if (mCenterItem == null)
//            return false
//
//        Log.e("이동위치있음", mCenterItem)
//        val mapView = bb.kakaoMap
//        val item = mCenterItem
//        val poi = item?.poi
//        mapView.addPOIItem(poi)
//
//        for (mapPOIItem in mapView.poiItems) {
//            //            Log.e(item, mapPOIItem.getUserObject());
//            if (item == mapPOIItem.userObject) {
//                if (mLastMapPOIItem != null)
//                    mapView.deselectPOIItem(mLastMapPOIItem)
//
//                mapView.setMapCenterPointAndZoomLevel(mapPOIItem.mapPoint, ZOOM_WARK, true)
//                mapView.selectPOIItem(mapPOIItem, true)
//                mLastMapPOIItem = mapPOIItem
//                break
//            }
//        }
//        return true
//    }

//private var calloutBalloonAdapter = object : CalloutBalloonAdapter {
//    override fun getCalloutBalloon(poiItem: MapPOIItem): View? {
//        val balloon = View.inflate(requireContext(), android.R.layout.simple_list_item_2, null)
//        balloon.findViewById<TextView>(android.R.id.text1).text = "getCalloutBalloon"
//        balloon.findViewById<TextView>(android.R.id.text2).text = "getCalloutBalloon"
//        return balloon
//    }
//
//    override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View? {
//        val balloon = View.inflate(requireContext(), android.R.layout.simple_list_item_2, null)
//        balloon.findViewById<TextView>(android.R.id.text1).text = "getPressedCalloutBalloon"
//        balloon.findViewById<TextView>(android.R.id.text2).text = "getPressedCalloutBalloon"
//        return balloon
//    }
//}

//debug
//    private fun drawXBox(mapView: MapView, s: MapPoint, e: MapPoint) {
//        val sx = s.mapPointScreenLocation.x
//        val sy = s.mapPointScreenLocation.y
//        val ex = e.mapPointScreenLocation.x
//        val ey = e.mapPointScreenLocation.y
//
//        val p1 = MapPolyline()
//        p1.lineColor = 0x55ff0000 // Polyline 컬러 지정.
//        p1.addPoint(MapPoint.mapPointWithScreenLocation(sx, sy))
//        p1.addPoint(MapPoint.mapPointWithScreenLocation(ex, sy))
//        p1.addPoint(MapPoint.mapPointWithScreenLocation(sx, ey))
//        p1.addPoint(MapPoint.mapPointWithScreenLocation(ex, ey))
//        mapView.addPolyline(p1)
//
//        val p2 = MapPolyline()
//        p2.lineColor = 0x55ff0000 // Polyline 컬러 지정.
//        p2.addPoint(MapPoint.mapPointWithScreenLocation(sx, ey))
//        p2.addPoint(MapPoint.mapPointWithScreenLocation(sx, sy))
//        p2.addPoint(MapPoint.mapPointWithScreenLocation(ex, ey))
//        p2.addPoint(MapPoint.mapPointWithScreenLocation(ex, sy))
//        mapView.addPolyline(p2)
//    }
//
//    private fun drawCircle(mapView: MapView, center: MapPoint, radius: Int) {
//        val circle = MapCircle(center, radius, 0x55ff0000, 0x55ff0000)
//        mapView.addCircle(circle)
//    }