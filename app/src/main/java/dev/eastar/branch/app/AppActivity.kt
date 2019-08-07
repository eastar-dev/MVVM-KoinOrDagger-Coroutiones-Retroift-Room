package dev.eastar.branch.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
//import dev.eastar.app_lib1.Lib1Activity
//import dev.eastar.app_lib2.Lib2Activity

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }

//    fun onAppToLib1(v: View) {
//        startActivity(Intent(v.context, Lib1Activity::class.java))
//    }
//
//    fun onAppToLib2(v: View) {
//        startActivity(Intent(v.context, Lib2Activity::class.java))
//    }
}
