package opera.ext

import android.content.Context
import android.webkit.WebView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method
import java.util.*

class OperaXRequest {
    lateinit var json: String

    lateinit var clazz: Class<out OperaX>
    lateinit var method: Method
    var params: Array<Any?> = emptyArray()

    var requestcode: Int = 0
    var callback: String = DEFAULT_CALLBACK

    override fun toString(): String {
        return "${method.returnType.simpleName} ${method.declaringClass.simpleName}::${method.name} ${Arrays.toString(params)}"
    }

    fun invoke(context: Context): Any? {
        val extension = clazz.newInstance()
        extension.initialize(context, this)
        val method = method
        return method.invoke(extension, *params)
    }

    fun invoke(webView: WebView): Any? {
        val extension = clazz.newInstance()
        extension.initialize(webView, this)
        val method = method
        return method.invoke(extension, *params)
    }

    companion object {
        private val DEFALUT_EXTENSION_PACKAGE = OperaX::class.java.`package`!!.name

        private const val CALLBACK = "callback"
        private const val CLAZZ = "clazz"
        private const val METHOD = "method"
        private const val PARAMS = "params"
        private const val REQUESTCODE = "requestcode"
        private const val DEFAULT_CALLBACK: String = "console.log"

        fun errorInstance(json: String): OperaXRequest {
            return OperaXRequest().apply {
                this.json = json
                this.callback = try {
                    JSONObject(json).optString(CALLBACK, DEFAULT_CALLBACK)
                } catch (e: JSONException) {
                    DEFAULT_CALLBACK
                }
            }
        }

        @Throws(ClassNotFoundException::class, JSONException::class)
        fun newInstance(json: String): OperaXRequest {
            val req = OperaXRequest()
            req.json = json

            val jo = JSONObject(json)
            req.callback = jo.optString(CALLBACK, DEFAULT_CALLBACK)
            req.requestcode = jo.optInt(REQUESTCODE, -1)

            req.clazz = getClazz(jo.optString(CLAZZ))
            req.params = getParams(jo.optJSONArray(PARAMS))
            req.method = getMethod(req.clazz, jo.optString(METHOD), req.params)
            return req
        }

        @Suppress("UNCHECKED_CAST")
        private fun getClazz(clz: String): Class<out OperaX> {
            return try {
                Class.forName(clz) as Class<out OperaX>
            } catch (e: ClassNotFoundException) {
                Class.forName("$DEFALUT_EXTENSION_PACKAGE.$clz") as Class<out OperaX>
            }
        }

        @Throws(JSONException::class)
        private fun getParams(param: JSONArray?): Array<Any?> {
            return arrayOfNulls<Any>(param?.length() ?: 0)
                    .mapIndexed { index, _ -> param?.opt(index) }
                    .toTypedArray()
        }

        @Throws(NoSuchMethodException::class)
        private fun getMethod(clz: Class<*>, name: String, parameter: Array<*>): Method {
            for (method in clz.methods) {
                if (name != method.name)
                    continue
                if (method.parameterTypes.size == parameter.size)
                    return method
            }
            throw NoSuchMethodException("${clz.name}.$name( ${getParameterTypes(parameter)} )")
        }

        private fun getParameterTypes(args: Array<*>): List<Class<*>> = args.map { it?.javaClass ?: Any::class.java }

    }
}