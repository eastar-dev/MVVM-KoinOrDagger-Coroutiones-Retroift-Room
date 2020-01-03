@file:Suppress("unused")

package android.log

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LifeCycleLog

fun Application.logActivity() = registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        logFragment(activity)
        Log.println(Log.ERROR, "onActivityCreated", "(${activity?.javaClass?.simpleName}.java:0)", activity?.javaClass?.simpleName)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.println(Log.WARN, "onActivityDestroyed", "(${activity?.javaClass?.simpleName}.java:0)", activity?.javaClass?.simpleName)
    }

    override fun onActivityStarted(activity: Activity?) {}
    override fun onActivityStopped(activity: Activity?) {}
    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
})

private fun logFragment(activity: Activity?) {
    (activity as? AppCompatActivity)?.run {
        supportFragmentManager.registerFragmentLifecycleCallbacks(object : androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
                f.javaClass.simpleName.takeUnless { "SupportRequestManagerFragment" == it }?.let { Log.println(Log.ERROR, "onFragmentCreated", "($it.java:0)", it) }
            }

            override fun onFragmentDestroyed(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
                f.javaClass.simpleName.takeUnless { "SupportRequestManagerFragment" == it }?.let { Log.println(Log.WARN, "onFragmentDestroyed", "($it.java:0)", it) }
            }
        }, true)
    }
}

