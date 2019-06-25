package android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.base.BC;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.log.Log;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class BU {

    public static final String BK = "●";
    static TypedValue out = new TypedValue();

    public static boolean isFirstTask(Context context) {
        try {

            PackageManager packageManager = (PackageManager) context.getPackageManager();
            PackageInfo activity = null;
            try {
                activity = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            List<String> activityNames = new ArrayList<String>();
            for (ActivityInfo activityInfo : activity.activities) {
                activityNames.add(activityInfo.name);
            }

            return activityNames.contains(getCurrentActivity(context).topActivity.getClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static ActivityManager.RunningTaskInfo getCurrentActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        return taskInfo.get(0);
    }
    public static String STARMARK(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
            sb.append(BK);
        return sb.toString();
    }
    public static String STARMARK(String text) {
        return (text == null) ? "" : STARMARK(text.length());
    }

    @NotNull
    public static String getLine1Number(@NotNull Context context) {
        String line1Number = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(READ_SMS) == PERMISSION_GRANTED || context.checkSelfPermission(READ_PHONE_NUMBERS) == PERMISSION_GRANTED || context.checkSelfPermission(READ_PHONE_STATE) == PERMISSION_GRANTED) {
                line1Number = tm.getLine1Number();
            } else {
                Log.w("PERMISSION_GRANTED");
            }
        } else {
            line1Number = tm.getLine1Number();
        }
        line1Number = line1Number.replace("+82", "0").replace(BC.regularExpressionNotdecimal, "");
        return line1Number;
    }
    /**
     * 유효 비밀번호 확인
     *
     * @return
     */
    public static boolean validPass(String password) {
        return Pattern.compile("[a-zA-Z]").matcher(password).find();
    }
    /**
     * 하나은행 사용 전화번호 포멧
     * 0505 010 011 016 017 018 019 번호에 한하여 문자를 보낼수 있다
     */

    public static final String getString(String strValue, String regularExpression, int group) {
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(strValue);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }
    //"KSC5601"
    public static int lengthByte(String string, String format) {
        try {
            return string.toString().getBytes(format).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public static String limitByte(String string, int length, String format) {
        try {
            byte[] byteArray = string.toString().getBytes(format);
            int count = byteArray.length;
            if (count > length) {
                int utfcount = new String(byteArray, 0, length, format).length();
                string = string.substring(0, utfcount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }
    public static boolean equals(String l, String r) {
        if ((l == null || l.trim().length() == 0) && (r == null || r.trim().length() == 0))
            return true;

        return l != null && l.equals(r);
    }
    public static int resid(Context c, int resid_attr) {
        c.getTheme().resolveAttribute(resid_attr, out, true);
        return out.resourceId;
    }
    public static int ver() {
        return Build.VERSION.SDK_INT;
    }

    @SuppressLint("ServiceCast")
    public static void copy(Context context, String msg) {

        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(msg);
        Toast.makeText(context, "복사 하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public static Uri removeQuery(Uri uri, String removeQuery) {

        String query = uri.getQuery();
        if (query == null)
            return uri;

        Set<String> set = new HashSet<String>();
        String[] params = query.split("&");

        for (int i = 0; i < params.length; i++) {
            final String keyvalue = params[i];
            int po = keyvalue.indexOf("=");
            if (-1 == po)
                continue;
            set.add(keyvalue.substring(0, po).trim());
        }

        set.remove("x");

        //		Builder builder = uri.buildUpon().clearQuery();
        Uri.Builder builder = uri.buildUpon();
        builder.query(null);
        for (String key : set)
            builder.appendQueryParameter(key, uri.getQueryParameter(key));

        return builder.build();
    }

    public static void trustHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                return;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                return;
            }
        }};

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier hv = (urlHostName, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    public static <T extends View> void findChildsByTag(final ViewGroup parent, final String tag, ArrayList<T> result) {
        int N = parent.getChildCount();
        for (int i = 0; i < N; i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup)
                findChildsByTag((ViewGroup) child, tag, result);

            if (tag.equals(child.getTag()))
                result.add((T) child);
        }
    }
    /**
     * 하나은행 사용 전화번호 포멧
     * 0505 010 011 016 017 018 019 번호에 한하여 문자를 보낼수 있다
     */
    public static String phoneNumberFormater(String phoneNo) {
        try {
            return phoneNo.replaceAll(BC.regularExpressionPhoneNo, "$1-$2-$3");
        } catch (Exception e) {
            return phoneNo;
        }
    }
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
        }

        return sb.toString();
    }
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    public static String onlyNumber(String value) {
        return value.replaceAll(BC.regularExpressionNotdecimal, "");
    }

    public static String format(String no, String regularExpression, String devider) {
        try {
            String noadj = no.replaceAll(BC.regularExpressionNotdecimal, "");
            Pattern pattern = Pattern.compile(regularExpression);
            Matcher matcher = pattern.matcher(noadj);
            if (noadj == null || !matcher.matches())
                return no;

            int N = matcher.groupCount();
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < N; i++) {
                sb.append(matcher.group(i));
                sb.append(devider);
            }
            sb.append(matcher.group(N));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return no;
        }
    }
    public static class LimitByteTextWatcher implements TextWatcher {
        private int bytelength;
        private String format;
        private Runnable what;

        public LimitByteTextWatcher(int bytelength, String incode_format) {
            this.bytelength = bytelength;
            this.format = incode_format;
        }

        public LimitByteTextWatcher(int bytelength, String incode_format, Runnable what) {
            this(bytelength, incode_format);
            this.what = what;
        }

        public void afterTextChanged(Editable s) {
            try {
                byte[] byteArray = s.toString().getBytes(format);
                int count = byteArray.length;
                if (count > bytelength) {
                    int utfcount = new String(byteArray, 0, bytelength, format).length();
                    s.delete(utfcount, s.length());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (what != null)
                what.run();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}