@file:Suppress("SpellCheckingInspection")

package android.retrofit.smart.base

import android.base.CActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.log.Log
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView

class AllActivity : CActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sc = ScrollView(this)
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.VERTICAL
        sc.addView(ll)
        setContentView(sc)

        val activities = getActivities()
        for (activity in activities) {
            val btn = Button(this)
            btn.text = activity.substring(activity.lastIndexOf('.') + 1)
            btn.setOnClickListener { startActivity(Intent().setClassName(this, activity)) }
            ll.addView(btn, -1, -2)
            Log.w(activity)
        }
    }

    fun getActivities(): List<String> {
        val clzs = arrayListOf<String>()
        val list = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities
        for (activityInfo in list) {
//            Log.d(activityInfo.name)
            if (activityInfo.name.startsWith(activityInfo.packageName))
                clzs += activityInfo.name
        }
//        Log.e(clzs)
        return clzs
    }

}
