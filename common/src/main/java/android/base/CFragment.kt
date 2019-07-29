package android.base

import android.log.Log
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import java.util.*

@Suppress("unused")
abstract class CFragment : Fragment(), Observer {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventObserver.addObserver(this)
    }

    override fun update(o: Observable?, arg: Any?) {
        Log.e(o, arg)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventObserver.deleteObserver(this)
    }

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

    private var mIsLoading = false
    protected fun load() {
        if (mIsLoading) {
            Log.w("mIsLoading=", mIsLoading)
            return
        }
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

    open val intent get() = requireActivity().intent

}