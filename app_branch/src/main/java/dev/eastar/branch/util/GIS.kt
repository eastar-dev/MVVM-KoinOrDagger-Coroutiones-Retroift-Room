package dev.eastar.branch.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.log.Log
import java.io.IOException
import java.util.*

fun getAddress(context: Context, lat: Double, lon: Double): String {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        // Handle case where no address was found.
        Log.e(addresses)

        val address = addresses[0]
        with(address) {
            (0..maxAddressLineIndex).map {
                Log.e(getAddressLine(it))
            }
        }

        with(address) {
            (0..maxAddressLineIndex).map { getAddressLine(it) }
        }.joinToString(separator = "\n")
        Log.i(address)
        return address.toString()
    } catch (ioException: IOException) {
        ioException.printStackTrace()
    } catch (illegalArgumentException: IllegalArgumentException) {
        illegalArgumentException.printStackTrace()
    }
    return ""
}



