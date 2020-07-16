package android.base

import android.content.Intent
import android.log.Log
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

@Suppress("unused")
abstract class CFragment : Fragment() {
    open val intent: Intent get() = requireActivity().intent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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