package com.aiyuns.tinkerplay.Config.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope // 动态刷新
@ConfigurationProperties(prefix = NacosInfoProperty.PREFIX)
public class NacosInfoProperty {

  public static final String PREFIX = "nacos-config";

  private String info;

  InnerClass innerClass = new InnerClass();

  @Data
  public static class InnerClass {
    private String name;
    private int age;
    private boolean enable;
  }
}
