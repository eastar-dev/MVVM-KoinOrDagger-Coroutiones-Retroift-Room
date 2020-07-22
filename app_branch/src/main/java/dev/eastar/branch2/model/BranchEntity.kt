package dev.eastar.branch2.model

import android.location.Location
import android.log.Log
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.eastar.branch.R
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
) {
    @Ignore
    var info: String? = null

    @Ignore
    var distance: Int = -1

    @Ignore
    var distanceText: String? = null
}


val BranchEntity.icon: Int
    @DrawableRes
    get() = when (branch_type) {
        "지점" -> R.drawable.ic_hana
        "우체국", "브랜드ATM", "점외자동화" -> R.drawable.ic_atm
        else -> R.drawable.ic_atm
    }

val BranchEntity.poiIcon: Int
    @DrawableRes
    get() = when (branch_type) {
        "지점" -> R.drawable.icon_pin_branch_hana
        "우체국" -> R.drawable.icon_pin_branch_post
        "브랜드ATM", "점외자동화" -> R.drawable.poi_atm
        else -> R.drawable.icon_pin_branch_auto
    }

fun BranchEntity.getInfo(): String {
    if (info.isNullOrBlank()) {
        info = StringBuilder().apply {
            Log.e(name)
            if (!name.isBlank()) {
                append(name)
                when (branch_type) {
                    "지점", "우체국" -> append(" $branch_type")
                }
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
        }.toString()
    }
    return info!!
}


fun BranchEntity.getDistance(lat: Double, lon: Double): Int {
    if (distance < 0) {
        val results = FloatArray(3)
        Location.distanceBetween(lat, lon, this.lat, this.lon, results)
        distance = results[0].toInt()
    }
    return distance
}

val Int.wgs84: Double get() = this / 1_000_000.0

fun BranchEntity.getDistanceText(x: Double, y: Double): String {
    if (distanceText.isNullOrBlank()) {
        val distance = getDistance(x, y)
        distanceText = if (distance / 1000 > 1)
            String.format(Locale.getDefault(), "%.2fkm", distance.toFloat() / 1000f)
        else
            "" + distance + "m"
    }
    return distanceText!!
}

