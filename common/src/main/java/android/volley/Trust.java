package android.volley;

import android.content.Context;
import android.log.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * <pre>
 * &lt;uses-permission android:name="android.permission.INTERNET" />
 * </pre>
 */
public class Trust {
    public static void SELF_TRUST_HTTPS_CERTIFICATES(Context context, int resid) {
        try {
            // Load CAs from an InputStream(could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            //InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
            InputStream caInput = context.getResources().openRawResource(resid); //R.raw.tft : tft.cer
            Certificate ca = cf.generateCertificate(caInput);
            Log.i("ca=" + ((X509Certificate) ca).getSubjectDN());
            caInput.close();

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, tmf.getTrustManagers(), null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((urlHostName, session) -> true);
        } catch (Exception e) {
            Log.w(e);
        }
    }
}
