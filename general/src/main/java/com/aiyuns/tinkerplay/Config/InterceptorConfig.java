package com.aiyuns.tinkerplay.Config;

import com.aiyuns.tinkerplay.InterceptorAndFilter.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: aiYunS @Date: 2023年6月12日, 0012 上午 10:24:18 @Description: 注册拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(new MyInterceptor(1.0))
        // 设置拦截器的匹配路径
        .addPathPatterns("/**/getConfigInfo");
  }
}
