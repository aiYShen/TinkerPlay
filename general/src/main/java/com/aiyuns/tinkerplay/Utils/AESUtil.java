package com.aiyuns.tinkerplay.Utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: aiYunS @Date: 2022-9-13 上午 10:26 @Description: AES加解密工具类
 */
public class AESUtil {

  private static final String AES = "AES";
  private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

  // 获取cipher
  private static Cipher getCipher(byte[] key, int model) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
    cipher.init(model, secretKeySpec);
    return cipher;
  }

  // AES加密
  public static String encrypt(byte[] data, byte[] key) throws Exception {
    Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
    return Base64.getEncoder().encodeToString(cipher.doFinal(data));
  }

  // AES解密
  public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
    Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
    // return cipher.doFinal(Base64.getDecoder().decode(data));
    return cipher.doFinal(Base64.getMimeDecoder().decode(data));
  }
}
