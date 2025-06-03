package com.aiyuns.tinkerplay.CustomAnnotations;

import java.lang.annotation.*;

/**
 * @Author: aiYunS @Date: 2022-8-29 下午 02:28 @Description: 自定义注解-参数判空
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface NullDisable {
  String description() default "";
}
