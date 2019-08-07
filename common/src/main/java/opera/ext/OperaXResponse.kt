package opera.ext

//import android.log.Log
import android.content.Intent
import org.json.JSONException
import org.json.JSONObject

@Suppress("unused")
enum class OperaXResponse {
    OK,//정상적으로 호출되고 결과도 정상임
    FAIL,//정상적으로 호출되고 결과는 실패임
    IllegalAccessException,//extension method를 잘못입력한경우
    IllegalArgumentException,//Argument 가 잘못된경우
    InvocationTargetException,//함수 실행중 오류가 난경우
    NoSuchMethodException,//지정한 method가 없는경우
    ExtensionNotFoundException,//지정한 extension이 없는경우
    InstantiationException,//함수로부터 확장팩을 못찾는경우
    UnsupportedException,//호출 규격자체가 잘못된경우
    AnotherException;//기타 예외처리

    fun getScript(req: OperaXRequest, result: Any?): String {
        //			Log.e(mReq, result);


        val jo = JSONObject()
        try {
            var response = result
            if (result is Intent)
                response = intent2json(result)

            jo.put("resultCode", ordinal)

            if (this == OK)
                jo.put("result", response)
            else
                jo.put("message", response)

            try {
                jo.put("request", JSONObject(req.json))
            } catch (e: Exception) {
                jo.put("request", req.toString())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return "(${req.callback})($jo);"
    }

    private fun intent2json(data: Intent?): JSONObject {
        val result = JSONObject()
        if (data != null && data.extras != null) {
            val bundle = data.extras
            val keys = bundle!!.keySet()
            for (key in keys) {
                try {
                    val o = bundle.get(key)
                    result.put(key, o)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }
        return result
    }

    companion object {
        fun find(name: String): OperaXResponse {
            return try {
                valueOf(name)
            } catch (e: Exception) {
                AnotherException
            }
        }
    }

    /*
            web:
            var callback_func = function(param) {
                alert(param);
            };
            ------------------------------------------------------
            asis:
            callback_func

            tobe:
            callback_func("hello x")

            or
            tobe:
            (callback_func)("hello x")
            -------------------------------------------------------
            asis:
            function(param) {
                alert(param);
            }

            tobe:
            var __opera_temp_callback_func = function(param) {
                alert(param);
            };
            __opera_temp_callback_func("hello x")

            or
            tobe:
            javascript:(
                function(param) {
                    alert(param);
                }
            )("hello x")

            or
            tobe:
            (
                function(param) {
                    alert(param);
                }
            )("hello x")
*/
}