package dev.eastar.branch.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class BranchClusterItem(private val latLng: LatLng, private val snippet: String, private val title: String) : ClusterItem {
    override fun getSnippet() = snippet
    override fun getTitle() = title
    override fun getPosition() = latLng
}