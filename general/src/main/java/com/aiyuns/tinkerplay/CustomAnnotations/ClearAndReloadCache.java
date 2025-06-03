package com.aiyuns.tinkerplay.CustomAnnotations;

import java.lang.annotation.*;

/**
 * @Author: aiYunS @Date: 2022-9-27 上午 10:24 @Description: 自定义注解-延时双删
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ClearAndReloadCache {
  String name() default "";
}
