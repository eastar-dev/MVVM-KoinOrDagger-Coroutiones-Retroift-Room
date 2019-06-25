/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.volley;

import android.log.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;

import java.util.Map;

public class NetRequest<T extends NetEnty> extends Request<T> {
    private T mEnty;
    T getEnty() {
        return mEnty;
    }

    public NetRequest(T enty, Response.Listener<T> onResponse, Response.ErrorListener onError) {
        super(enty.getMethod(), enty.getUrl(), onError);//Error Listener
        mEnty = enty;
        this.listener = onResponse;
        setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1f));
    }

    public NetRequest(T enty, RequestFuture<T> future) {
        super(enty.getMethod(), enty.getUrl(), future);
        mEnty = enty;
        this.listener = future;
        setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1f));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mEnty.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mEnty.getBody();
//		final byte[] body = mEnty.getBody();
//		if (body != null && body.length > 0)
//			return mEnty.getBody();
//		return super.getBody();
    }

    public String getBodyContentType() {
        return mEnty.getBodyContentType();
    }

    // res//////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        NetLog._NETLOG_IN(mEnty, response);

        try {
            mEnty.parseNetworkResponse(response);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

        try {
            mEnty.parseStatusCode(response.statusCode);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

        try {
            mEnty.parseHeaders(response.headers);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        try {
            mEnty.parseHeaders(response.allHeaders);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        try {
            mEnty.parseHeaders(response);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

        try {
            mEnty.parseBytes(response.data);
        } catch (Exception e) {
//            Log.printStackTrace(e);
            return Response.error(new ParseError(response));
        }

        try {
            final String stringBody = new String(response.data, HttpHeaderParser.parseCharset(response.headers, Net.UTF8));
            NetLog.pojo(mEnty, stringBody);
            mEnty.parseString(stringBody);
        } catch (Exception e) {
//            Log.printStackTrace(e);
            return Response.error(parseNetworkError(new ParseError(response)));
        }

        if (mEnty.isSuccess())
            return Response.success(mEnty, HttpHeaderParser.parseCacheHeaders(response));
        else
            return Response.error(parseNetworkError(new ParseError(response)));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        NetLog._NETLOG_IN(mEnty, volleyError);// log
        mEnty.parseNetworkError(volleyError);
        return super.parseNetworkError(volleyError);
    }

    //callback//////////////////////////////////////////////////////////////////////////////////////////////
    private Response.Listener<T> listener;

    @Override
    protected void deliverResponse(T response) {
        mEnty.onDeliverResponse();
        if (listener != null)
            listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mEnty.onDeliverError();
        super.deliverError(error);
    }
}
