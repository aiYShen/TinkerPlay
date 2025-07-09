package com.aiyuns.tinkerplay.Config.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2022-9-13 上午 11:33 @Description: 读取用户自定义配置的key
 */
@Data
@Component
@ConfigurationProperties(prefix = EncryptProperty.PREFIX)
public class EncryptProperty {

  public static final String PREFIX = "body.encrypt";

  private String key;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
