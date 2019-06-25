package android.volley;

import android.log.Log;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "unused"})
public class NetEnty {
    public boolean _NO_LOG = false;
    public boolean _NO_OUT_H = false;
    public boolean _NO_OUT_C = false;
    public boolean _NO_OUT_1 = false;
    public boolean _NO_OUT_2 = false;
    public boolean _NO_OUT_3 = false;
    public boolean _NO_IN_H = false;
    public boolean _NO_IN_C = false;
    public boolean _NO_IN_1 = false;
    public boolean _NO_IN_2 = false;
    public boolean _NO_IN_3 = false;
    public boolean _NO_POJO = false;
    public boolean _OUT_H = false;
    public boolean _OUT_C = false;
    public boolean _OUT_1 = false;
    public boolean _OUT_2 = false;
    public boolean _OUT_3 = false;
    public boolean _IN_H = false;
    public boolean _IN_C = false;
    public boolean _IN_1 = false;
    public boolean _IN_2 = false;
    public boolean _IN_3 = false;
    public boolean _POJO = false;

    public StackTraceElement getStack() {
        return stack;
    }
    @NonNull
    final StackTraceElement stack;
    public NetEnty(int method) {
        this.method = method;
        stack = NetLog.getStack();
    }

    public boolean isDummy() {
        return false;
    }
    public boolean isFail() { return false; }

    protected void setUrl(String url) {
        this.url = url;
    }

    protected void setHeaders(String... key_value) {
        headers.clear();
        addHeaders(key_value);
    }
    protected void addHeaders(String... key_value) {
        if (key_value.length % 2 == 1)
            throw new IllegalArgumentException("!!key value must pair");

        int N = (key_value.length) / 2;
        for (int i = 0; i < N; i++) {
            final String key = (String) key_value[i * 2];
            final String value = key_value[i * 2 + 1];
            headers.put(key, value);
        }
    }

    protected void setParams(Object... key_value) {
        params.clear();
        addParams(key_value);
    }
    protected void addParams(Object... key_value) {
        if (key_value.length % 2 == 1)
            throw new IllegalArgumentException("!!key value must pair");

        int N = (key_value.length) / 2;
        for (int i = 0; i < N; i++) {
            final String key = (String) key_value[i * 2];
            final Object value = key_value[i * 2 + 1];
//			Log.e(key, value);
            if (value == null) {
                params.put(key, null);
            } else if (value.getClass().isArray()) {
                final Object[] values = (Object[]) value;
                for (int j = 0; j < values.length; j++)
                    params.put(key + "[" + j + "]", values[j]);
            } else if (value.getClass().isAssignableFrom(List.class)) {
                final List<?> values = (List<?>) value;
                for (int j = 0; j < values.size(); j++)
                    params.put(key + "[" + j + "]", values.get(j));
            } else if (value instanceof Boolean) {
                params.put(key, (((Boolean) value) ? "Y" : "N"));
            } else {
                params.put(key, value);
            }
        }
    }
    protected void setJson(String json) {
        this.json = json;
    }

    //--------------------------------------------------------------------------------------
    protected int getMethod() {
        return method;
    }

    protected String getUrl() {
        if (method == Method.GET)
            return encodeParametersUrl();
        return url;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    Map<String, Object> getParams() {
        return params;
    }
    String getJson() {
        return json;
    }

    private boolean isStringParams() {
        for (Object obj : params.values()) {
            if (obj instanceof File)
                return false;
        }
        return true;
    }

    byte[] getBody() {
        if (body == null)
            setBody();
        return body;
    }
    private void setBody() {
        if (!isEmpty(json)) {
            body = json.getBytes();
            contentType = "application/json; charset=" + Net.UTF8;
        } else if (isStringParams()) {
            setParametersBody(params);
        } else {
            setMultipartBody(params);
        }
    }

    String getBodyContentType() {
        return contentType;
    }
    //--------------------------------------------------------------------------------------

    //req
    private final int method;
    protected String url;
    //req-header
    private Map<String, String> headers = new HashMap<>();
    //req-post
    private String contentType = "text/plain; charset=" + Net.UTF8;
    private byte[] body;
    protected Map<String, Object> params = new HashMap<>();
    private String json;

    //res
    protected NetworkResponse networkResponse;
    protected String stringBody;
    //res-parsed data
    protected boolean success;
    public String errorCode;
    public String errorMessage;
    protected Gson gson;

    protected void parseNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
        Log.e(getClass().getSimpleName(), networkResponse.statusCode, networkResponse.data.length, networkResponse.networkTimeMs, networkResponse.notModified/*, response.allHeaders*/);
    }
    protected void parseStatusCode(int statusCode) {
        success = (statusCode == HttpURLConnection.HTTP_OK);
    }
    protected void parseHeaders(Map<String, String> headers) { }
    protected void parseHeaders(List<Header> allHeaders) { }
    public void parseHeaders(NetworkResponse response) { }
    protected void parseBytes(byte[] bytes) { }
    protected void parseString(String stringBody) {
        this.stringBody = stringBody;
        gson = gsonCreate();
        try {
            final Field field = getDataField(getClass());
            final Class<?> type = getDataType(getClass());
//            Log.e(field.getType().getName(), type.getName());
            field.setAccessible(true);
            field.set(this, gson.fromJson(stringBody, type));
            success = true;
            errorCode = "0";
            errorMessage = "success";
        } catch (Exception e) {
            Log.e(e);
        }
    }

    protected void parseNetworkError(VolleyError volleyError) {
        success = false;
        errorCode = "-1";
        errorMessage = "error";
    }

    private static Field getDataField(Class clz) throws NoSuchFieldException {
        if (!NetEnty.class.isAssignableFrom(clz))
            throw new NoSuchFieldException();
        try {
            return clz.getDeclaredField("data");
        } catch (NoSuchFieldException e) {
            return getDataField(clz.getSuperclass());
        }
    }

    private static Class<?> getDataType(Class clz) throws NoSuchFieldException {
        if (!NetEnty.class.isAssignableFrom(clz))
            throw new NoSuchFieldException();
        try {
            return Class.forName(clz.getName() + "$Data");
        } catch (ClassNotFoundException e) {
            return getDataType(clz.getSuperclass());
        }
    }

    protected Gson gsonCreate() {
        return new GsonBuilder().create();
    }

    protected Object getQueryParameter(String key) {
        if (method == Method.GET)
            return Uri.parse(url).getQueryParameter(key);
        else
            return params.get(key);
    }

    public NetworkResponse getNetworkResponse() {
        return networkResponse;
    }

//    protected Map<String, String> getResponseHeader() {
//        return responseHeader;
//    }
//    protected List<Header> getResponseAllHeader() {
//        return responseAllHeaders;
//    }

    public boolean isSuccess() {
        return success;
    }

    public void onDeliverResponse() {
//        Log.w("onDeliverResponse");
    }

    public void onDeliverError() {
//        Log.w("onDeliverError");
    }

    //get
    private String encodeParametersUrl() {
        if (isEmpty(url))
            return url;

        final Uri uri = Uri.parse(url);

        final String scheme = uri.getScheme();
        final String authority = uri.getEncodedAuthority();
        final String path = uri.getEncodedPath();
        final String query = uri.getQuery();
        final String fragment = uri.getFragment();

        String param = getEncodedParams(query);

        String url = scheme + "://" + authority;
        if (!isEmpty(path))
            url += path;
        if (!isEmpty(param))
            url += "?" + param;
        if (!isEmpty(fragment))
            url += "#" + fragment;
        return url;
    }

    private String getEncodedParams(String query) {
        if (isEmpty(query))
            return "";

        final Map<String, Object> paramsMap = new HashMap<>();
        final String[] params = query.split("&");
        for (int i = 0; i < params.length; i++) {
            final String keyvalue = params[i];

            int po = keyvalue.indexOf("=");
            if (-1 == po)
                continue;

            final String key = keyvalue.substring(0, po);
            final String value = keyvalue.substring(po + 1);

            paramsMap.put(key, value);
        }

        final Set<Entry<String, Object>> set = paramsMap.entrySet();

        StringBuilder encodedParams = new StringBuilder();
        for (Entry<String, Object> entry : set) {
            if (!(entry.getValue() instanceof CharSequence)) {
                Log.w("! unsupported value ", entry.getKey(), entry.getValue().toString());
                continue;
            }

            try {
                encodedParams.append(URLEncoder.encode(entry.getKey(), Net.UTF8));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue().toString(), Net.UTF8));
                encodedParams.append('&');
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding not supported: " + Net.UTF8, e);
            }
        }
        return encodedParams.toString();
    }

    private void setParametersBody(Map<String, Object> params) {

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Entry<String, ?> entry : params.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                if (value == null)
                    continue;

                encodedParams.append(URLEncoder.encode(key, Net.UTF8));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(value.toString(), Net.UTF8));
                encodedParams.append('&');
            }
            this.body = encodedParams.toString().getBytes(Net.UTF8);
            this.contentType = "application/x-www-form-urlencoded; charset=" + Net.UTF8;
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + Net.UTF8, uee);
        }
    }

    protected void setMultipartBody(Map<String, Object> params) {
        try {
            MultipartUtil builder = new MultipartUtil();
            final Set<Entry<String, Object>> entrys = params.entrySet();
            //text
            for (Entry<String, Object> entry : entrys) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                if (value == null)
                    continue;

                if (value instanceof File) {
                    ;
                } else {
                    builder.addText(key, value.toString());
                }
            }
            //file
            for (Entry<String, Object> entry : entrys) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                if (value == null)
                    continue;

                if (value instanceof File) {
                    builder.addFile(key, (File) value);
                } else {

                }
            }
            builder.build();

            this.body = builder.toByteArray();
            this.contentType = builder.getContentType();
//			headers.put("Content-Length", Long.toString(body.length));
        } catch (IOException e) {
            throw new RuntimeException("multipartEntity not supported: ", e);
        }
    }

    //etc
    @Override
    public String toString() {
        return getClass().getSimpleName() + "<" + (getMethod() == Method.POST ? "POST" : "GET") + ">" + getUrl() + "?" + (getMethod() == Method.POST ? (!isEmpty(json) ? json.substring(0, Math.min(2000, json.length())) : params.toString()) : "");
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    private class MultipartUtil {
        public static final String TWO_HYPHENS = "--";
        public static final String BOUNDARY = "==WebKitFormBoundaryIT3nXTYf3AEmugh4==";
        public static final String CRLF = "\r\n";
        public static final int MAX_BUFFER_SIZE = 1024 * 1024;

        public final String getContentType() {
            return "multipart/form-data; boundary=" + BOUNDARY;
//			return "multipart/form-data; boundary=" + BOUNDARY + "; charset=EUC-KR";
        }

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
        private DataOutputStream os = new DataOutputStream(baos);

        public byte[] toByteArray() {
            return baos.toByteArray();
        }

        public void build() throws IOException {
            os.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + CRLF);
            os.close();
        }

        public void addText(String key, String value) throws IOException {
            os.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
            os.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + CRLF);
//			os.writeBytes("Content-Disposition: form-data; name=\"+key+\", Content-Type: text/plain; charset=EUC-KR, Content-Transfer-Encoding: 8bit" + CRLF);
//			os.writeBytes("Content-Type: text/plain; charset=EUC-KR" + CRLF);

            os.writeBytes(CRLF);
            os.writeBytes(value);
            os.writeBytes(CRLF);
        }

        public void addFile(String key, File file) throws IOException {
            String fileName = file.getName();
            os.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
            os.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"" + CRLF);
//			os.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(fileName) + CRLF);
            os.writeBytes("Content-Transfer-Encoding: binary" + CRLF);
            os.writeBytes(CRLF);

            FileInputStream is = new FileInputStream(file);
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            int bytesRead = is.read(buffer, 0, MAX_BUFFER_SIZE);
            while (bytesRead > 0) {
                os.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer, 0, MAX_BUFFER_SIZE);
            }
            is.close();
            os.writeBytes(CRLF);
        }

    }

}