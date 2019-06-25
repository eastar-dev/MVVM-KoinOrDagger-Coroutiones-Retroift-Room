package android.volley;

import android.log.Log;

import androidx.annotation.WorkerThread;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.RequestFuture;

import java.util.concurrent.ExecutionException;

/**
 * <pre>
 * &lt;uses-permission android:name="android.permission.INTERNET" />
 * </pre>
 */
@SuppressWarnings({"unused", "WeakerAccess", "CatchMayIgnoreException", "ConstantConditions", "RedundantThrows"})
public class Net {
    public static final String EUCKR = "euc-kr";
    public static final String UTF8 = "UTF-8";
    public static final String COOKIE = "Cookie";
    public static final String SET_COOKIE = "Set-Cookie";

    public static final String SESSIONID = "JSESSIONID";
    public static final String USER_AGENT = "User-Agent";

    public static boolean LOG = false;
    public static boolean _POJO = false;
    public static boolean _OUT_1 = false;
    public static boolean _OUT_2 = false;
    public static boolean _OUT_3 = false;
    public static boolean _OUT_H = false;
    public static boolean _OUT_C = false;
    public static boolean _IN_1 = false;
    public static boolean _IN_2 = false;
    public static boolean _IN_3 = false;
    public static boolean _IN_H = false;
    public static boolean _IN_C = false;

    private static final RequestQueue sRequestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));

    static {
        sRequestQueue.start();
    }

    /** 동기기다림 */
    @WorkerThread
    public static <T extends NetEnty> T block(T enty) throws InterruptedException, ExecutionException, VolleyError {
        return sync(enty).get();
    }

    /** 동기 */
    @WorkerThread
    public static <T extends NetEnty> RequestFuture<T> sync(T enty) {
        RequestFuture<T> future = RequestFuture.newFuture();
        NetRequest<T> req = new NetRequest<>(enty, future);
        NetLog._NETLOG_OUT(enty);
        sRequestQueue.add(req);
        return future;
    }

    /** 비동기 */
    public static <T extends NetEnty> void sent(T enty) {
        async(enty, null, null);
    }

    /** 비동기 */
    public static <T extends NetEnty> void async(T enty, OnSuccess<T> onSuccess, OnError onError) {
        if (enty.isSuccess() && onSuccess != null) {
            Log.p(enty.isSuccess() ? Log.INFO : Log.WARN, enty.getClass(), " may be is dummy codeing? deliver to " + (enty.isSuccess() ? "successed" : "errored"));
            onSuccess.onResponse(enty);
            return;
        }

        if (enty.isFail()) {
            onError.onErrorResponse(new VolleyError(enty.getNetworkResponse()));
            return;
        }

        if (enty.isDummy()) {
            Log.p(enty.isSuccess() ? Log.INFO : Log.WARN, enty.getClass(), " is dummy codeing? deliver to " + (enty.isSuccess() ? "successed" : "errored"));
            if (enty.isSuccess() && onSuccess != null) {
                onSuccess.onResponse(enty);
            }
            if (!enty.isSuccess() && onError != null) {
                onError.onErrorResponse(new VolleyError("dummy codeing error"));
            }
            return;
        }

        NetRequest<T> req = new NetRequest<>(enty, onSuccess, onError);
        NetLog._NETLOG_OUT(enty);
        sRequestQueue.add(req);
    }

    //    public static abstract class OnDeliverResponse<T extends NetEnty> implements Response.Listener<T>, Response.ErrorListener {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//
//        }
//        @Override
//        public void onResponse(T response) {
//
//        }
//    }
//    public interface OnDeliverResponse<T extends NetEnty> extends Response.Listener<T> {}

//    /** 비동기 */
//    public static <T extends NetEnty> void async(T enty, Response.Listener<T> onResponse) {
//        NetLog._NETLOG_OUT(enty);
////            req.setRetryPolicy(mRetryPolicy);
//        NetRequest<T> req = new NetRequest<T>(enty, onResponse, onResponse);
//        NetLog._NETLOG_OUT(enty);
//        sRequestQueue.add(req);
//    }

    //
    public static <T> void request(Request<T> request) {
        sRequestQueue.add(request);
    }

    public interface OnSuccess<T extends NetEnty> extends Response.Listener<T> {}

    public interface OnError extends Response.ErrorListener {}

    public static void errorParse(VolleyError error) {
        try {
            Log.e("ErrorResponse", error.networkResponse.statusCode, new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, Net.UTF8)));
        } catch (Exception e) {
        }

        try {
            if (error instanceof NoConnectionError) {
                Log.e("NoConnectionError", error);
            } else if (error instanceof NetworkError) {
                Log.e("NetworkError", error);
            } else if (error instanceof TimeoutError) {
                Log.e("TimeoutError", error);
            } else if (error instanceof VolleyError) {
                Log.e("VolleyError", error.getMessage());
            } else {
                Log.e("EtcError");
            }
        } catch (Exception e) {
            Log.e("Exception");
        }
    }

//    /**
//     * 비동기 success fail
//     */
//    @Deprecated
//    public static <T extends NetEnty> void async(T enty, Response.Listener<T> onResponse, Response.ErrorListener onError) {
//        if (enty.isDummy() && onResponse != null) {
//            Log.w(enty.getClass(), " is dummy codeing? deliver to successed");
//            onResponse.onResponse(enty);
//            return;
//        }
//        sRequestQueue.add(new NetRequest<T>(enty, onResponse, onError));
//    }
//
//    /**
//     * 비동기 Callback
//     */
//    @Deprecated
//    public static <T extends NetEnty> void async(T enty, OnNetResponse<T> onResponse) {
//        if (enty.isDummy() && onResponse != null) {
//            Log.w(enty.getClass(), " is dummy codeing? deliver to successed");
//            onResponse.onResponse(enty);
//            return;
//        }
//        sRequestQueue.add(new NetRequest<T>(enty, onResponse));
//    }

//    @Deprecated
//    public interface OnNetResponse<T> extends Response.Listener<T>, Response.ErrorListener {
//        void onErrorResponse(VolleyError error);
//
//        void onResponse(T response);
//    }

//    @Deprecated
//    public static class OnSimpleNetResponse<T> implements OnNetResponse<T> {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            try {
//                Log.e("ErrorResponse", error.networkResponse.statusCode, new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, Net.UTF8)));
//            } catch (Exception e) {
//            }
//
//            try {
//                if (error instanceof NoConnectionError) {
//                    Log.e("NoConnectionError", error);
//                } else if (error instanceof NetworkError) {
//                    Log.e("NetworkError", error);
//                } else if (error instanceof TimeoutError) {
//                    Log.e("TimeoutError", error);
//                } else if (error instanceof VolleyError) {
//                    Log.e("VolleyError", error.getMessage());
//                } else {
//                    Log.e("EtcError");
//                }
//            } catch (Exception e) {
//                Log.e("Exception");
//            }
//        }
//
//        @Override
//        public void onResponse(T response) {
//            Log.pn(Log.INFO, 1, response);
//        }
//    }

//    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
//    public static void setCookieHandler() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
//            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
//    }

//    public static void setImageSource(NetworkImageView niv, String url, ImageCache imageCache) {
//        if (url.toLowerCase(Locale.getDefault()).matches("http(|s)://.*")) {
//            niv.setImageUrl(url, new ImageLoader(sRequestQueue, imageCache));
//        }
//    }

//    public static <T extends NetEnty> void async(Request<T> request) {
//        sRequestQueue.add(request);
//    }

}
