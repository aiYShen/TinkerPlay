package com.aiyuns.tinkerplay.CustomAnnotations;

import java.lang.annotation.*;

/**
 * @Author: aiYunS @Date: 2022-9-13 上午 11:29 @Description: 自定义注解-RSA加密
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Encrypt {

  String description() default "";
}
