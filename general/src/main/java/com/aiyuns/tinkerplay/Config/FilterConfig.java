package com.aiyuns.tinkerplay.Config;

import com.aiyuns.tinkerplay.InterceptorAndFilter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: aiYunS @Date: 2023年6月12日, 0012 上午 11:00:57 @Description: 注册过滤器配置
 */
@Configuration
public class FilterConfig {
  @Bean
  public FilterRegistrationBean<MyFilter> myFilterRegistrationBean() {
    FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new MyFilter());
    // 设置过滤器的匹配路径
    registrationBean.addUrlPatterns("/*");
    // 设置过滤器的执行顺序，可选
    registrationBean.setOrder(1);
    return registrationBean;
  }
}
