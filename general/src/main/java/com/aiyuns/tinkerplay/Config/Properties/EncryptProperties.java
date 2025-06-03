package com.aiyuns.tinkerplay.Config.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: aiYunS @Date: 2022-9-13 上午 11:33 @Description: 读取用户自定义配置的key
 */
@ConfigurationProperties(prefix = "body.encrypt")
@Data
public class EncryptProperties {
  // 默认的key
  // private final static String DEFAULT_KEY = "iamwalkerencrypt";

  private String key;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
