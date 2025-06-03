package com.aiyuns.tinkerplay.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2024年3月4日, 0004 下午 1:58:11 @Description: Flink配置类
 */
@Component
@Data
@ConfigurationProperties(prefix = "cdc.datasource")
public class FlinkCDCConfig {

  private boolean enabled;

  private String hostname;

  private Integer port;

  private String username;

  private String password;

  private String database;

  private String tableList;
}
