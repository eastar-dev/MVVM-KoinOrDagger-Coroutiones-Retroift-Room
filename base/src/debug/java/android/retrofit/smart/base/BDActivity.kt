@file:Suppress("LocalVariableName")

package android.retrofit.smart.base

import android.app.Activity
import android.base.CActivity
import android.log.Log
import android.util.versionCode
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.*

open class BDActivity : CActivity() {
    override fun onStart() {
        super.onStart()
        try {
            egg()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @Suppress("SpellCheckingInspection")
        val eastarEggs by lazy {
            val funcs = ArrayList<String>()
            val eastarEgg = Class.forName("android.etc.EastarEgg")
            for (method in eastarEgg.methods) {
                //            Log.e(method.getName());
                if (method.declaringClass != eastarEgg)
                    continue
                if (method.returnType != Void.TYPE)
                    continue
                if (method.name.contains("$"))
                    continue
                funcs.add(method.name)
                Log.e(method.name)
            }
            funcs.toTypedArray()
        }
    }

    @Suppress("SpellCheckingInspection")
    @Throws(Exception::class)
    fun egg() {
        val SHOWMETHEMONEY = "showmethemoney"
        val parent = findViewById<ViewGroup>(android.R.id.content)
        if (parent!!.findViewWithTag<View>(SHOWMETHEMONEY) != null)
            return

        val ver = TextView(this)
        val ver_string = "[v$versionCode]"
        ver.text = ver_string
        ver.textSize = 9f
        ver.setTextColor(0x55ff0000)
        ver.tag = SHOWMETHEMONEY
        parent.addView(ver, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ver.setOnClickListener {
            AlertDialog.Builder(this).setItems(eastarEggs) { dialog, which ->
                try {
                    val eastarEgg = Class.forName("android.etc.EastarEgg")
                    val constructor = eastarEgg.getConstructor(Activity::class.java)
                    val receiver = constructor.newInstance(this@BDActivity)
                    val funcname = (dialog as AlertDialog).listView.getItemAtPosition(which) as String
                    val method = eastarEgg.getMethod(funcname)
                    method.invoke(receiver)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.show()
        }
    }
}
