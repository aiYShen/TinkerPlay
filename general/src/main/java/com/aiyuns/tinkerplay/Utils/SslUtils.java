package com.aiyuns.tinkerplay.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import javax.net.ssl.*;

/**
 * @Author: aiYunS @Date: 2022-11-25 10:16 @Description: ssl工具类,跳过SSL证书认证
 */
public class SslUtils {

  static HostnameVerifier hv =
      new HostnameVerifier() {
        @Override
        public boolean verify(String urlHostName, SSLSession session) {
          System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
          return true;
        }
      };

  public static final String retrieveResponseFromServer(final String url) {

    HttpURLConnection connection = null;

    try {
      URI uri = new URI(url);
      URL validationUrl = uri.toURL();
      trustAllHttpsCertificates();
      HttpsURLConnection.setDefaultHostnameVerifier(hv);

      connection = (HttpURLConnection) validationUrl.openConnection();
      final BufferedReader in =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));

      String line;
      final StringBuffer stringBuffer = new StringBuffer(255);

      synchronized (stringBuffer) {
        while ((line = in.readLine()) != null) {
          stringBuffer.append(line);
          stringBuffer.append("\n");
        }
        return stringBuffer.toString();
      }

    } catch (final IOException e) {
      System.out.println(e.getMessage());
      return null;
    } catch (final Exception e1) {
      System.out.println(e1.getMessage());
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public static void trustAllHttpsCertificates() throws Exception {
    javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
    javax.net.ssl.TrustManager tm = new miTM();
    trustAllCerts[0] = tm;
    javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, null);
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }

  static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
      return true;
    }

    public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
      return true;
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
        throws java.security.cert.CertificateException {
      return;
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
        throws java.security.cert.CertificateException {
      return;
    }
  }
}
