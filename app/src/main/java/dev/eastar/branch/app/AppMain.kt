package dev.eastar.branch.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.eastar.branch.ui.BranchMain

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
