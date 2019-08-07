package opera.eventer

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

fun Application.operaXEventer() {
    registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
                    (activity as? AppCompatActivity)?.run {
                        supportFragmentManager.registerFragmentLifecycleCallbacks(object : androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
                            override fun onFragmentCreated(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
                                (f as? OperaXEventObserver)?.let { OperaXEventObservable.addObserver(it) }
                            }

                            override fun onFragmentDestroyed(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
                                (f as? OperaXEventObserver)?.let { OperaXEventObservable.deleteObserver(it) }
                            }
                        }, true)
                    }
                    (activity as? OperaXEventObserver)?.let { OperaXEventObservable.addObserver(it) }
                }
                override fun onActivityDestroyed(activity: Activity?) {
                    (activity as? OperaXEventObserver)?.let { OperaXEventObservable.deleteObserver(it) }
                }

                override fun onActivityStarted(activity: Activity?) {}
                override fun onActivityStopped(activity: Activity?) {}
                override fun onActivityResumed(activity: Activity?) {}
                override fun onActivityPaused(activity: Activity?) {}
                override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {}
            }
    )
}





