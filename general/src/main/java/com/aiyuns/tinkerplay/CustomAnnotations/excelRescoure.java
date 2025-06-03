package com.aiyuns.tinkerplay.CustomAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: aiYunS @Date: 2022-10-12 上午 10:02 @Description: 自定义注解(用于实体类读取)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface excelRescoure {

  String value() default ""; // 默认为空
}
