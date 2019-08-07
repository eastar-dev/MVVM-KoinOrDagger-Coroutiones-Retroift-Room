package opera.intercepter

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import opera.intercepter.OperaXIntercepterObserver.TYPE.*

@Suppress("unused")
fun Application.operaXIntercepter() {
    registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
                    (activity as? AppCompatActivity)?.run {
                        lifecycle.addObserver(object : LifecycleObserver {
                            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                            fun onActivityCreated() = OperaXIntercepterObserver.notifyObservers(ON_CREATE.with(activity))

                            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                            fun onActivityDestroyed() = OperaXIntercepterObserver.notifyObservers(ON_DESTROY.with(activity))

                            @OnLifecycleEvent(Lifecycle.Event.ON_START)
                            fun onActivityStarted() = OperaXIntercepterObserver.notifyObservers(ON_START.with(activity))

                            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
                            fun onActivityPaused() = OperaXIntercepterObserver.notifyObservers(ON_STOP.with(activity))
                        })
                    }
                }
                override fun onActivityDestroyed(activity: Activity?) {}
                override fun onActivityStarted(activity: Activity?) {}
                override fun onActivityStopped(activity: Activity?) {}
                override fun onActivityResumed(activity: Activity?) {}
                override fun onActivityPaused(activity: Activity?) {}
                override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {}
            }
    )
}

