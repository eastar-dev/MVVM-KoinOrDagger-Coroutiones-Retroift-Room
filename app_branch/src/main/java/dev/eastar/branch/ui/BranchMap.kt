package dev.eastar.branch.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.log.Log
import android.net.Uri
import android.os.Bundle
import android.util.toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import dev.eastar.branch.R
import dev.eastar.branch.data.BranchEntity
import dev.eastar.branch.databinding.BranchMapBinding
import dev.eastar.branch.presentation.BranchViewModel
import dev.eastar.permission.PermissionRequest
import net.daum.mf.map.api.*
import org.koin.android.viewmodel.ext.android.getSharedViewModel
import eastar.base.BFragment

class BranchMap : BFragment() {
    @Suppress("NonAsciiCharacters", "ObjectPropertyName")
    companion object {
        private const val ZOOM_도보이동가능반경 = 2
        private val 하나은행본사 = MapPoint.mapPointWithGeoCoord(37.56641387939453, 126.98188018798828)
        private const val MAPAPIKEY = "1de0dd82efcb553ad7647a52cb4845fdf701be6b"
    }

    private var mLastMapPOIItem: MapPOIItem? = null
    private var mCenterItem: BranchEntity? = null
    private lateinit var bb: BranchMapBinding
    private lateinit var vm: BranchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bb = BranchMapBinding.inflate(inflater, container, false)
        vm = getSharedViewModel()
        bb.vm = vm
        return bb.root
    }

    override fun onLoadOnce() {
        bb.kakaoMap.setMapViewEventListener(onMapViewEventListener)
        bb.kakaoMap.setPOIItemEventListener(onPoiItemEventListener)
        bb.kakaoMap.setCurrentLocationEventListener(mOnCurrentLocationEventListener)

        //livedata observer
        vm.branchsInMap.observe(this@BranchMap, Observer<List<BranchEntity>> { updateMapPoi(it) })
        vm.branchsTeling.observe(this@BranchMap, Observer<BranchEntity> { showTelDialog(it) })

        bb.showList.setOnClickListener { fragmentManager?.beginTransaction()?.add(R.id.branch_container, BranchList())?.addToBackStack(null)?.commitAllowingStateLoss() }
        bb.showSearch.setOnClickListener { fragmentManager?.beginTransaction()?.add(R.id.branch_container, Search())?.addToBackStack(null)?.commitAllowingStateLoss() }
        bb.currentPosition.setOnClickListener { moveCurrentPosition() }
    }

    private fun showTelDialog(branch: BranchEntity?) {
        branch ?: return

        val builder = AlertDialog.Builder(requireContext())
                .setMessage(branch.info)
                .setPositiveButton("닫기", null)
        if (!branch.tel?.replace("\\D".toRegex(), "").isNullOrBlank())
            builder.setNegativeButton("전화걸기") { _, _ -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + branch.tel))) }

        builder.show().setCanceledOnTouchOutside(true)
    }

    override fun onLoad() {

    }

    private fun showList() {
        if (bb.kakaoMap.poiItems.isEmpty()) {
            bb.kakaoMap.context.toast("영업점/ATM이 없습니다.")
            return
        }
    }

    override fun onClear() {
        bb.kakaoMap.removeAllPOIItems()
        bb.kakaoMap.removeAllPolylines()
        bb.kakaoMap.removeAllCircles()
        if (isLocationPermit(requireContext()))
            bb.kakaoMap.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    ////////////////////////////////////////////////////////////////////////////
    //map///////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    private var mOnCurrentLocationEventListener = object : MapView.CurrentLocationEventListener {
        override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, v: Float) {
            Log.e("현재위치이동 ok")
            bb.currentPosition.isSelected = false
            mapView.setMapCenterPointAndZoomLevel(mapPoint, ZOOM_도보이동가능반경, false)
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            vm.searchInMap(mapView)
        }

        override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView, v: Float) {}
        override fun onCurrentLocationUpdateFailed(mapView: MapView) {}
        override fun onCurrentLocationUpdateCancelled(mapView: MapView) {}
    }

    private val onMapViewEventListener = object : MapView.MapViewEventListener {

        override fun onMapViewInitialized(mapView: MapView) {
            Log.e("초기화됨")
            mapView.setMapCenterPointAndZoomLevel(하나은행본사, ZOOM_도보이동가능반경, false)
            if (setCenterItem())
                return
            Log.e("이동위치없음 초기화시에는 권한이 있을때만 이동")
            if (isLocationPermit(mapView.context)) {
                moveCurrentPosition()
                return
            }
        }

        override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
            Log.e(mapView.zoomLevelFloat, ZOOM_도보이동가능반경, "자동검색")
            vm.onMapViewMoveFinished(mapView, mapPoint)
        }

        override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewZoomLevelChanged(mapView: MapView, i: Int) {}
        override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {}
        override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {}
    }

    private val onPoiItemEventListener = object : MapView.POIItemEventListener {
        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, mapPOIItem: MapPOIItem, calloutBalloonButtonType: MapPOIItem.CalloutBalloonButtonType) {
            val branch = mapPOIItem.userObject as BranchEntity
            Log.e(mapView, mapPOIItem, calloutBalloonButtonType, branch)
            vm.touchedPoiItem(branch)
        }

        override fun onPOIItemSelected(mapView: MapView, mapPOIItem: MapPOIItem) {}
        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, mapPOIItem: MapPOIItem) {}
        override fun onDraggablePOIItemMoved(mapView: MapView, mapPOIItem: MapPOIItem, mapPoint: MapPoint) {}
    }

    private fun updateMapPoi(branchs: List<BranchEntity>?) {
        branchs ?: return

        val mapView = bb.kakaoMap
        val poiItems = mapView.poiItems.flatMap { listOf(it.userObject as BranchEntity to it) }.toMap()
        branchs
                .subtract(poiItems.keys)
                .forEach { mapView.addPOIItem(getPOI(it)) }

        poiItems.keys
                .subtract(branchs)
                .forEach { mapView.removePOIItem(poiItems[it]) }

        //256이상의 MapPOIItem를 포함하면 죽는다 JNI ERROR (app bug): local reference table overflow (max=512)
    }

    private fun getPOI(branch: BranchEntity): MapPOIItem {
//        Log.i(branch)
        return MapPOIItem().apply {
            itemName = branch.name
            mapPoint = MapPoint.mapPointWithGeoCoord(branch.lat, branch.lon)
            markerType = MapPOIItem.MarkerType.CustomImage
            showAnimationType = MapPOIItem.ShowAnimationType.NoAnimation
            isShowCalloutBalloonOnTouch = true
            isShowDisclosureButtonOnCalloutBalloon = true
            isDraggable = false
            userObject = branch
            customImageResourceId = R.drawable.hnw_map_pin_branch
            customImageAnchorPointOffset = MapPOIItem.ImageOffset(31, 0)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun selectedItem(item: BranchEntity) {
        val mapView = bb.kakaoMap
        for (mapPOIItem in mapView.poiItems) {
            //            Log.e(item, mapPOIItem.getUserObject());
            if (item == mapPOIItem.userObject) {
                if (mLastMapPOIItem != null)
                    mapView.deselectPOIItem(mLastMapPOIItem)

                mapView.setMapCenterPointAndZoomLevel(mapPOIItem.mapPoint, ZOOM_도보이동가능반경, true)
                mapView.selectPOIItem(mapPOIItem, true)
                mLastMapPOIItem = mapPOIItem
                break
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun moveCurrentPosition() {
        val context = requireContext()
        val mapView = bb.kakaoMap
        Log.e("위치권한 확인")

        if (!isLocationPermit(context)) {
            locationPermit(context)
            return
        }
        Log.e("현재위치이동")
        bb.currentPosition.isSelected = true
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    ////////////////////////////////////////////////////////////////////////////
//list//////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
    fun setCenterItem(): Boolean {
        if (mCenterItem == null)
            return false

        Log.e("이동위치있음", mCenterItem)
        val item = mCenterItem
        val poi = getPOI(item!!)
        bb.kakaoMap.addPOIItem(poi)
        selectedItem(item)
        return true
    }

//    internal fun loadOnce_list() {
//        mAdapter = DataAdapter().apply {
//            //            setOnItemClickListener()
//            setOnItemClickListener(object : BindingAdapter.OnItemClickListener<BranchModel.Item> {
//                override fun onItemClick(parent: RecyclerView, view: View, position: Int, data: BranchModel.Item?) {
//                    parent.visibility = View.GONE
//                    data?.let { selectedItem(it) }
//                }
//            })
//        }.also {
//            bb.list.adapter = it
//        }
//
//    }

    ////////////////////////////////////////////////////////////////////////////
//permit////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
    private fun isLocationPermit(context: Context) = PermissionRequest.isPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)

    private fun locationPermit(context: Context) = PermissionRequest.builder(context, Manifest.permission.ACCESS_FINE_LOCATION).run()

//    private fun locationPermit(context: Context) {
//        val text = ("앱 접근권한 안내\n"//
//                + "\n"//
//                + "\n"//
//                + "앱에서 사용하는 접근 권한에 대하여 아래와 같이 안내해 드립니다.\n"//
//                + "\n"//
//                + "선택적 접근권한의 경우 동의 하지 않으실 수 있으나. 해당 접근권한이 필요한 일부 기능 사용에 제한이 있을 수 있습니다.\n"//
//                + "\n"//
//                + "\n"//
//                + "◼︎ 선택적 접근 권한\n"//
//                + "  위치 : 내위치 주변 지점 ATM검색을 위하여 필요합니다.\n" //
//                + "\n")//
//        val requestMessage = SpannableString(text)
//        requestMessage.setSpan(StyleSpan(Typeface.BOLD), 0, 9, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//
//        val denyMessage = ("동의하지 않으시면 기능사용에 제한이 있을 수 있습니다.\n"//
//                + "설정 버튼을 이용하여 동의여부를 직접 수정 할 수 있습니다.\n"//
//                + "\n"//
//                + "\n"//
//                + "  ※ 언제든 동의여부를 바꿀 수 있습니다.\n"//
//                + "    안드로이드설정>애플리케이션 관리>애플리케이션 관리자>" + context.appName + ">권한 에서 가능합니다.\n"//
//                + "  ※ 안드로이드 6.0미만의 경우 필수,선택 구분 없이 설치시에 모든 권한 동의를 하셔야 정상적인 이용이 가능합니다.\n")//
//
//        PermissionRequest.builder(context, Manifest.permission.ACCESS_FINE_LOCATION)//
//                .setRequestMessage(requestMessage)//
//                .setDenyMessage(denyMessage)//
//                .run()
//    }

    private fun drawXBox(mapView: MapView, s: MapPoint, e: MapPoint) {
        val sx = s.mapPointScreenLocation.x
        val sy = s.mapPointScreenLocation.y
        val ex = e.mapPointScreenLocation.x
        val ey = e.mapPointScreenLocation.y

        val p1 = MapPolyline()
        p1.lineColor = 0x55ff0000 // Polyline 컬러 지정.
        p1.addPoint(MapPoint.mapPointWithScreenLocation(sx, sy))
        p1.addPoint(MapPoint.mapPointWithScreenLocation(ex, sy))
        p1.addPoint(MapPoint.mapPointWithScreenLocation(sx, ey))
        p1.addPoint(MapPoint.mapPointWithScreenLocation(ex, ey))
        mapView.addPolyline(p1)

        val p2 = MapPolyline()
        p2.lineColor = 0x55ff0000 // Polyline 컬러 지정.
        p2.addPoint(MapPoint.mapPointWithScreenLocation(sx, ey))
        p2.addPoint(MapPoint.mapPointWithScreenLocation(sx, sy))
        p2.addPoint(MapPoint.mapPointWithScreenLocation(ex, ey))
        p2.addPoint(MapPoint.mapPointWithScreenLocation(ex, sy))
        mapView.addPolyline(p2)
    }

    private fun drawCircle(mapView: MapView, center: MapPoint, radius: Int) {
        val circle = MapCircle(center, radius, 0x55ff0000, 0x55ff0000)
        mapView.addCircle(circle)
    }
}
