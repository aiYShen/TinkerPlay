package com.aiyuns.tinkerplay.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2022-10-13 下午 05:36 @Description: 生成token工具类
 */
@Component
public class TokenCreateUtil {

  Logger logger = LoggerFactory.getLogger(TokenCreateUtil.class);
  // token秘钥 与 网关配置的密钥要一致
  private static String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0e618";

  /**
   * 生成token值
   *
   * @param username
   * @param password
   * @return
   */
  public static String token(String username, String password) {
    String token = "";
    try {
      // 设置过期时间12个小时
      Calendar calendar = Calendar.getInstance();
      // Date now = calendar.getTime();
      // calendar.add(Calendar.MINUTE, 12*60);
      calendar.add(Calendar.MINUTE, 1);
      Date expireDate = calendar.getTime();
      // 秘钥以及加密算法
      Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
      // 设置头部算法
      Map<String, Object> header = new HashMap<>();
      header.put("typ", "JWT");
      header.put("alg", "HS256");
      token =
          JWT.create()
              .withHeader(header)
              // 测试添加
              .withClaim("key", "user-key")
              .withClaim("userName", username)
              .withClaim("passWord", password)
              .withExpiresAt(expireDate)
              .sign(algorithm);
    } catch (Exception e) {
      System.out.println("生成token值失败: " + e);
      return null;
    }

    return token;
  }

  /**
   * 验证token是否正确
   *
   * @param token
   * @return
   */
  public static boolean checkToken(String token) {
    try {
      // 秘钥以及加密算法
      Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT jwt = verifier.verify(token);
      System.out.println("userName：-------" + jwt.getClaim("userName").asString());
      System.out.println("passWord：-------" + jwt.getClaim("passWord").asString());
      DecodedJWT TokenInfo = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build().verify(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static String parseJWT(String token) {
    DecodedJWT decodeToken = JWT.decode(token);
    return decodeToken.getClaim("loginName").asString();
  }

  /**
   * @desc 判断token是否过期
   */
  public static boolean isJwtExpired(String token) {

    try {
      DecodedJWT decodeToken = JWT.decode(token);
      return decodeToken.getExpiresAt().before(new Date());
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }
  }

  @Value("${token.TOKEN_SECRET}")
  public void setTokenSecret(String tokenSecret) {
    TOKEN_SECRET = tokenSecret;
  }
}
