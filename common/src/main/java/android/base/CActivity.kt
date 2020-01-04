package android.base

import android.log.Log
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle

abstract class CActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseExtra()
        loadOnce()
        reload()
        updateUI()
    }

    protected fun parseExtra() = try {
        onParseExtra()
    } catch (e: Exception) {
        Log.printStackTrace(e)
    }

    protected fun loadOnce() = onLoadOnce()

    fun reload() {
        clear()
        load()
    }

    protected fun clear() = try {
        onClear()
    } catch (e: Exception) {
        Log.printStackTrace(e)
    }

    protected fun load() {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Log.w("Lifecycle destroyed")
            return
        }
        try {
            onLoad()
        } catch (e: Exception) {
            Log.printStackTrace(e)
        }

    }

    protected fun updateUI() {
        try {
            onUpdateUI()
        } catch (e: Exception) {
            Log.printStackTrace(e)
        }
    }

    open fun onParseExtra() {}
    open fun onLoadOnce() {}
    open fun onClear() {}
    open fun onLoad() {}
    open fun onUpdateUI() {}
}