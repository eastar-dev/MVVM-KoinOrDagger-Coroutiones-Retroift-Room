package dev.eastar.branch.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.eastar.mvvm.R

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }
}
