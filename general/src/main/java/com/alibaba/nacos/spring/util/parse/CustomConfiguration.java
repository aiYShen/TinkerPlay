package com.alibaba.nacos.spring.util.parse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Author: aiYunS @Date: 2023年12月19日, 0019 下午 2:59:10 @Description: 将替代类注册成bean
 */
@Configuration
public class CustomConfiguration {

  // 使用 @Primary 注解，将自定义类设置为首选实现
  @Primary
  @Bean
  public DefaultYamlConfigParse customThirdPartyClass() {
    return new DefaultYamlConfigParse();
  }

  // 使用 @ConditionalOnBean 注解，当原始类存在时才注册自定义类
  /*
  @Bean
  @ConditionalOnBean(DefaultYamlConfigParse.class)
  public CustomThirdPartyClass customThirdPartyClass() {
      return new CustomThirdPartyClass();
  }
  */
}
