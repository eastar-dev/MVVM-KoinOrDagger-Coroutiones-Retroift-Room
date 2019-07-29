@file:Suppress("unused")

package android.log

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
class LifeCycleLog

fun Application.logActivity() = registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) = logFragment(activity?.also { Log.e(activity.javaClass) })
    override fun onActivityDestroyed(activity: Activity?) = Log.w(activity?.javaClass).let { Unit }
    override fun onActivityStarted(activity: Activity?) {}
    override fun onActivityStopped(activity: Activity?) {}
    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
})

private fun logFragment(activity: Activity?) {
    activity.takeIf { activity is AppCompatActivity } ?: return
    (activity as AppCompatActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = Log.onCreate(f.javaClass, savedInstanceState)
        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) = Log.onDestroy(f.javaClass)
    }, true)
}

