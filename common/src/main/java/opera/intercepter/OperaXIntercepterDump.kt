@file:Suppress("DEPRECATION")

package opera.intercepter

import android.app.Application
import android.log.Log
import dalvik.system.DexFile
import java.util.concurrent.Executors

@Suppress("FunctionName")
fun Application.operaXIntercepterDump() {
    Executors.newSingleThreadExecutor().execute {
        try {
            val parentClass = OperaXIntercepter::class.java
            val packageName = parentClass.`package`?.name!!
            val thisClass = parentClass.name
            DexFile(packageCodePath).entries().toList()
                    .filter { it.startsWith(packageName) }
                    .filterNot { it.startsWith(thisClass) }
                    .filterNot { it.contains('$') }
                    .map { Class.forName(it) }
                    .filter { parentClass.isAssignableFrom(it) }
                    .forEach { Log.w(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}