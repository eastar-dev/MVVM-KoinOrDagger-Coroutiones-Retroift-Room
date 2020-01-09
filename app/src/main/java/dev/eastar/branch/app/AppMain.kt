package dev.eastar.branch.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
//import dev.eastar.app_lib1.Lib1Activity
//import dev.eastar.app_lib2.Lib2Activity

import dev.eastar.branch.ui.BranchMain
import javax.inject.Inject

class AppMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, BranchMain())
                    .commitNow()
        }
    }
}
