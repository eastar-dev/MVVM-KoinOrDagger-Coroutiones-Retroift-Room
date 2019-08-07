@file:Suppress("DEPRECATION")

package opera.ext

import android.app.Application
import android.log.Log
import dalvik.system.DexFile
import java.util.concurrent.Executors

fun Application.operaXDump() {
    Executors.newSingleThreadExecutor().execute {
        val parentClass = OperaX::class.java
        val packageName = parentClass.`package`?.name!!
        val thisClass = parentClass.name
        DexFile(packageCodePath).entries().toList()
                .filter { it.startsWith(packageName) }
                .filterNot { it.startsWith(thisClass) }
//                .map { it.also { Log.e(it) } }
                .map { Class.forName(it) }
                .filter { parentClass.isAssignableFrom(it) }
                .flatMap { clz ->
                    clz.methods
                            .filterNot { it.declaringClass == Any::class.java }
                            .filterNot { it.declaringClass == OperaX::class.java }
                            .filterNot { it.name.contains("lambda$") }
//                            .filterNot { it.name.contains('$') }
                            .filter { it.getAnnotation(OperaX.OperaXInternal::class.java) == null }
                            .toList()
                }
                .forEach {
                    Log.w(it)
                }
    }
}
