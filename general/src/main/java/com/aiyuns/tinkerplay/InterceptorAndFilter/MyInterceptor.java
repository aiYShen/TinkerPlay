package com.aiyuns.tinkerplay.InterceptorAndFilter;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: aiYunS @Date: 2023年6月12日, 0012 上午 10:21:20 @Description: 自定义拦截器
 */
public class MyInterceptor implements HandlerInterceptor {

  private final RateLimiter rateLimiter;

  public MyInterceptor(double rate) {
    this.rateLimiter = RateLimiter.create(rate);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // 在请求处理之前执行的逻辑
    // 尝试获取一个令牌，如果获取不到则进行限流处理
    if (rateLimiter.tryAcquire()) {
      // 允许请求继续执行
      System.out.println("流量不大, 正常执行!");
      // 返回true表示继续执行请求处理，返回false表示终止请求处理
      return true;
    } else {
      // 进行限流处理，返回限流提示或其他响应
      System.out.println("流量太大, 阻断执行!");
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/plain;charset=UTF-8");
      response.setStatus(777);
      response.getWriter().write("请求过于频繁，请稍后再试");
      return false;
    }
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    // 在请求处理之后、视图渲染之前执行的逻辑
    System.out.println("请求处理之后、视图渲染之前执行!");
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // 在请求完成之后执行的逻辑，包括异常处理
    System.out.println("请求完成之后执行!");
  }
}
