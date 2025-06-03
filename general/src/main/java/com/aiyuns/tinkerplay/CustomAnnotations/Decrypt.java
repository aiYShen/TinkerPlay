package com.aiyuns.tinkerplay.CustomAnnotations;

import java.lang.annotation.*;

/**
 * @Author: aiYunS @Date: 2022-9-13 上午 11:23 @Description: 自定义注解-RSA解密
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface Decrypt {

  String description() default "";
}
