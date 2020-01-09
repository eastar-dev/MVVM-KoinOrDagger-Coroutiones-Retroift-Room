package dev.eastar.branch.app

import android.os.Bundle
import android.text.TextUtils
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        val tv = appCompatButton4
        tv.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                run()
            }
        })
    }

    fun run() {
        CoroutineScope(Dispatchers.Main).launch {
            repeat(20) {
                val tv = appCompatButton4
                val name = "1234567890".take(it % 10)
                val age = " , 20"
                val avail: Float = tv.width.toFloat() - tv.paint.measureText(age)
                val result = TextUtils.ellipsize(name, tv.paint, avail, TextUtils.TruncateAt.END)
                tv.text = "$result$age"
                orgText.text = "$name$age"
                delay(1000)
            }
        }
    }
}
