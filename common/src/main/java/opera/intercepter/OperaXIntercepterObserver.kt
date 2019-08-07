package opera.intercepter

import android.app.Activity
import java.util.*

object OperaXIntercepterObserver : Observable() {

    fun notifyObservers(type: TYPE) {
        setChanged()
        super.notifyObservers(type)
    }

    enum class TYPE {
        ON_CREATE, ON_DESTROY, ON_START, ON_STOP;

        lateinit var activity: Activity

        fun with(activity: Activity): TYPE {
            this.activity = activity
            return this
        }
    }
}
