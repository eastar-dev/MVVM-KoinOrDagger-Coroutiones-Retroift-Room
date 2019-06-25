package android.log

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Application.logActivity() =
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                Log.onCreate(activity?.javaClass, savedInstanceState)
                if (activity is AppCompatActivity)
                    logFragment(activity)
            }

        //@formatter:off
        override fun onActivitySaveInstanceState (activity: Activity?, outState: Bundle?){ Log.w        (activity?.javaClass) }
        override fun onActivityStarted           (activity: Activity?                   ){ Log.onStart  (activity?.javaClass) }
        override fun onActivityResumed           (activity: Activity?                   ){ Log.onResume (activity?.javaClass) }
        override fun onActivityPaused            (activity: Activity?                   ){ Log.onPause  (activity?.javaClass) }
        override fun onActivityStopped           (activity: Activity?                   ){ Log.onStop   (activity?.javaClass) }
        override fun onActivityDestroyed         (activity: Activity?                   ){ Log.onDestroy(activity?.javaClass) }
        //@formatter:on
        })

private fun logFragment(activity: AppCompatActivity) {
    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
        //@formatter:off
        override fun onFragmentPreAttached      (fm: FragmentManager, f: Fragment, context: Context                    ) = super.onFragmentPreAttached      (fm, f, context              )
        override fun onFragmentAttached         (fm: FragmentManager, f: Fragment, context: Context                    ) = super.onFragmentAttached         (fm, f, context              )
        override fun onFragmentCreated          (fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?         ) = Log.onCreate(f.javaClass, savedInstanceState                  )
        override fun onFragmentActivityCreated  (fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?         ) = super.onFragmentActivityCreated  (fm, f, savedInstanceState   )
        override fun onFragmentViewCreated      (fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) = super.onFragmentViewCreated      (fm, f, v, savedInstanceState)
        override fun onFragmentStarted          (fm: FragmentManager, f: Fragment                                      ) = super.onFragmentStarted          (fm, f                       )
        override fun onFragmentResumed          (fm: FragmentManager, f: Fragment                                      ) = super.onFragmentResumed          (fm, f                       )
        override fun onFragmentPaused           (fm: FragmentManager, f: Fragment                                      ) = super.onFragmentPaused           (fm, f                       )
        override fun onFragmentStopped          (fm: FragmentManager, f: Fragment                                      ) = super.onFragmentStopped          (fm, f                       )
        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle                    ) = super.onFragmentSaveInstanceState(fm, f, outState             )
        override fun onFragmentViewDestroyed    (fm: FragmentManager, f: Fragment                                      ) = super.onFragmentViewDestroyed    (fm, f                       )
        override fun onFragmentDestroyed        (fm: FragmentManager, f: Fragment                                      ) = Log.onDestroy(f.javaClass                                     )
        override fun onFragmentDetached         (fm: FragmentManager, f: Fragment                                      ) = super.onFragmentDetached         (fm, f                       )
        //@formatter:on
    }, true)
}

