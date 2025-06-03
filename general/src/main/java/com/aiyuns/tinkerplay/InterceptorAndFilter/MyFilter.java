package com.aiyuns.tinkerplay.InterceptorAndFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: aiYunS @Date: 2023年6月12日, 0012 上午 10:49:13 @Description: 自定义过滤器
 */
public class MyFilter implements jakarta.servlet.Filter {

  Logger logger = LoggerFactory.getLogger(MyFilter.class);

  // 可选的初始化方法
  @Override
  public void init(jakarta.servlet.FilterConfig filterConfig)
      throws jakarta.servlet.ServletException {
    logger.info("请求处理");
    System.out.println("过滤器初始化方法执行!");
    jakarta.servlet.Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(
      jakarta.servlet.ServletRequest servletRequest,
      jakarta.servlet.ServletResponse servletResponse,
      jakarta.servlet.FilterChain filterChain)
      throws IOException, jakarta.servlet.ServletException {
    // 执行过滤逻辑
    logger.info("请求处理");
    System.out.println("Request received: " + servletRequest.getRemoteAddr());
    // 继续处理请求
    filterChain.doFilter(servletRequest, servletResponse);
  }

  // 可选的销毁方法
  @Override
  public void destroy() {
    logger.info("销毁");
    System.out.println("过滤器销毁方法执行!");
    jakarta.servlet.Filter.super.destroy();
  }
}
