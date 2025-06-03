package com.aiyuns.tinkerplay.Utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2022-7-13 下午 04:00 @Description: 获取spring容器中bean工具类
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
  /** 上下文对象实例 */
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtils.applicationContext = applicationContext;
  }

  /**
   * 获取applicationContext
   *
   * @return applicationContext
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 通过name获取Bean.
   *
   * @param name 类名
   * @return bean
   */
  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

  /**
   * 通过class获取Bean.
   *
   * @param clazz 类
   * @param <T> 泛型
   * @return bean
   */
  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   *
   * @param name 类名称
   * @param clazz 类型
   * @param <T> 泛型
   * @return bean
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }
}
