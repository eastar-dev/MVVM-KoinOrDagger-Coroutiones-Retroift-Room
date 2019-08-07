package dev.eastar.app_lib1

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class Lib1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib1)
    }

    fun onLib1App(v: View) {
        startActivity(Intent().setComponent(ComponentName(v.context, "dev.eastar.app.AppActivity")))
    }

    fun onLib1ToLib2(v: View) {
        startActivity(Intent("dev.eastar.intent.action.Lib2"))
    }
}
