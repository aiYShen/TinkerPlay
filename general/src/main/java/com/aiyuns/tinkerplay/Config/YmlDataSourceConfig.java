package com.aiyuns.tinkerplay.Config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2023年9月7日, 0007 上午 10:59:40 @Description: 单独写个类获取baomidou多数据源存储好的数据源信息
 */
@Configuration
@Component("YmlDataSourceConfig")
@RequiredArgsConstructor
// @EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class YmlDataSourceConfig {

  private final DynamicDataSourceProperties properties;

  public DynamicDataSourceProperties getProperties() {
    return this.properties;
  }
}
