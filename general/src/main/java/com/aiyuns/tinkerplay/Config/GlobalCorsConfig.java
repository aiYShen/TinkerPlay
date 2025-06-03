package com.aiyuns.tinkerplay.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: aiYunS @Date: 2023年2月16日, 0016 下午 2:19:09 @Description: 配合VUE允许跨域配置类
 */
@Configuration
public class GlobalCorsConfig {

  /** 允许跨域调用的过滤器 */
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    // 允许所有域名进行跨域调用
    config.addAllowedOrigin("*");
    // 允许跨越发送cookie
    config.setAllowCredentials(true);
    // 放行全部原始头信息
    config.addAllowedHeader("*");
    // 允许所有请求方法跨域调用
    config.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
