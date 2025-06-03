package com.aiyuns.tinkerplay.Utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * @Author: aiYunS @Date: 2022-11-25 13:45 @Description: 从网站获取java所需的证书，调用时传入域名
 */
public class InstallCertUtil {

  private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

  public static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 3);
    for (int b : bytes) {
      b &= 0xff;
      sb.append(HEXDIGITS[b >> 4]);
      sb.append(HEXDIGITS[b & 15]);
      sb.append(' ');
    }
    return sb.toString();
  }

  public static class SavingTrustManager implements X509TrustManager {

    private final X509TrustManager tm;
    public X509Certificate[] chain;

    // private X509Certificate[] chain;

    public SavingTrustManager(X509TrustManager tm) {
      this.tm = tm;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
      throw new UnsupportedOperationException();
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
      this.chain = chain;
      tm.checkServerTrusted(chain, authType);
    }
  }
}
