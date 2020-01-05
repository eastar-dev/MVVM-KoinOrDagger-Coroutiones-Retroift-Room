package dev.eastar.branch.model

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.util.*

@Entity(tableName = "branch")
data class BranchEntity(
        @PrimaryKey var id: String = ""
        , var search_type: String? = null
        , var company_name: String? = null
        , var name: String = ""
        , var address: String? = null
        , var address1: String? = null
        , var address2: String? = null
        , var driving_directions: String? = null
        , var tel: String? = null
        , var fax: String? = null
        , var services: String? = null
        , var branch_code: String? = null
        , var business_hours: String? = null
        , var branch_type: String? = null
        , var mgr_name: String? = null
        , var mgr_tel: String? = null
        , var lat: Double = 0.0
        , var lon: Double = 0.0
) : ClusterItem by BranchClusterItem(LatLng(lat, lon), name, name) {
    @Ignore
    var info: String = ""
    @Ignore
    var distance: Int = 0
    @Ignore
    var distanceText: String = ""
}

const val ATM = "ATM"

fun BranchEntity.getInfo(): String {
    if (!info.isBlank()) return info

    val info = StringBuilder().apply {
        if (!name.isBlank()) {
            append(name)
            if (ATM != branch_type)
                append(" $branch_type")
            append("\n\n")
        }

        if (!address.isNullOrBlank()) {
            append("-주소")
            append("\n : $address")
            append("\n\n")
        }

        if (!driving_directions.isNullOrBlank()) {
            append("-오시는길")
            append("\n: $driving_directions")
            append("\n\n")
        }
        if (!tel.isNullOrBlank()) {
            append("-연락처")
            if (!tel.isNullOrBlank())
                append("\n : $tel(전화)")
            if (!fax.isNullOrBlank())
                append("\n/ $fax(FAX)")

            append("\n\n")
        }

        if (!business_hours.isNullOrBlank() && business_hours != "없음") {
            append("-운영시간")
            append("\n : $business_hours")
            append("\n")
        }
    }

    this.info = info.toString()
    return info.toString()
}

fun BranchEntity.getDistance(lat: Double, lon: Double): Int {
    if (distance > 0) return distance
    val results = FloatArray(3)
    Location.distanceBetween(lat, lon, this.lat, this.lon, results)
    return results[0].toInt().also { distance = it }
}

fun BranchEntity.getDistanceText(x: Double, y: Double): String {
    if (!distanceText.isBlank())
        return distanceText
    val distance = getDistance(x, y)
    return if (distance / 1000 > 1)
        String.format(Locale.getDefault(), "%.2fkm", distance.toFloat() / 1000f)
    else
        "" + distance + "m"
}

val BranchEntity.intent: Intent get() = Intent().putExtras(bundle)
val BranchEntity.bundle
    get() = bundleOf(
            "id" to id,
            "search_type" to search_type,
            "company_name " to company_name,
            "name " to name,
            "address" to address,
            "driving_directions " to driving_directions,
            "tel" to tel,
            "fax" to fax,
            "services " to services,
            "branch_code" to branch_code,
            "business_hours " to business_hours,
            "branch_type" to branch_type,
            "mgr_name " to mgr_name,
            "mgr_tel" to mgr_tel,
            "lon" to lon,
            "lat" to lat
    )

fun fromIntent(intent: Intent): BranchEntity = fromBundle(intent.extras ?: Bundle())
fun fromBundle(bundle: Bundle): BranchEntity {
    return BranchEntity().apply {
        id = bundle.getString("id", "")
        search_type = bundle.getString("search_type ")
        company_name = bundle.getString("company_name")
        name = bundle.getString("name", "")
        address = bundle.getString("address ")
        driving_directions = bundle.getString("driving_directions")
        tel = bundle.getString("tel ")
        fax = bundle.getString("fax ")
        services = bundle.getString("services")
        branch_code = bundle.getString("branch_code ")
        business_hours = bundle.getString("business_hours")
        branch_type = bundle.getString("branch_type ")
        mgr_name = bundle.getString("mgr_name")
        mgr_tel = bundle.getString("mgr_tel ")
        lon = bundle.getDouble("lon")
        lat = bundle.getDouble("lat")
    }
}

val BranchEntity.icon: String
    get() = "https://openhanafn.ttmap.co.kr/iframe/images/map_icn_hana.png"