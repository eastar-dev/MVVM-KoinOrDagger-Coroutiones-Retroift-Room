package opera.intercepter

import android.app.Activity
import android.content.Context
import android.log.Log
import java.util.*

@Suppress("UNUSED_PARAMETER")
abstract class OperaXIntercepter(var context: Context) : Observer {
    interface IMain

    init {
        Log.e("OperaXIntercepter", javaClass.name)
        OperaXIntercepterObserver.addObserver(this)
    }

    override fun update(observable: Observable, data: Any) {
//        Log.e((data as OperaXIntercepterObserver.TYPE).name, data.activity.javaClass.simpleName)
        when (val type = data as OperaXIntercepterObserver.TYPE) {
            OperaXIntercepterObserver.TYPE.ON_CREATE -> onCreate(type.activity)
            OperaXIntercepterObserver.TYPE.ON_DESTROY -> onDestroy(type.activity)
            OperaXIntercepterObserver.TYPE.ON_START -> onStart(type.activity)
            OperaXIntercepterObserver.TYPE.ON_STOP -> onStop(type.activity)
            else -> Log.e("!undefined message")
        }
    }

    protected open fun onCreate(activity: Activity) {}
    protected open fun onDestroy(activity: Activity) {}
    protected open fun onStart(activity: Activity) {}
    protected open fun onStop(activity: Activity) {}
}

