package android.base

import android.content.Context
import android.log.Log
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import java.util.*
import kotlin.properties.Delegates

open class CActivity : AppCompatActivity(), Observer {

    lateinit var mActivity: CActivity
    lateinit var mContext: Context
    private var destroied: Boolean  by Delegates.observable(false) { property, oldValue, newValue -> Log.e(property.name, "$oldValue -> $newValue") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        mContext = this
        EventObserver.addObserver(this)
        parseExtra()
        loadOnce()
        reload()
        updateUI()
    }

    override fun update(o: Observable?, arg: Any?) {
        Log.e(o, arg)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroied = true
        EventObserver.deleteObserver(this)
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

    protected var mIsLoading = false
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

    val progress by lazy { createProgress() }
}