package com.aiyuns.tinkerplay.CustomAnnotations;

import com.aiyuns.tinkerplay.Enum.PrivacyTypeEnum;
import com.aiyuns.tinkerplay.Serializer.PrivacySerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.*;

/**
 * @Author: aiYunS @Date: 2023-1-12 10:25 @Description: 自定义数据脱敏注解
 */
@Documented
@Target(ElementType.FIELD) // 作用在字段上
@Retention(RetentionPolicy.RUNTIME) // class文件中保留，运行时也保留，能通过反射读取到
@JacksonAnnotationsInside // 表示自定义自己的注解PrivacyEncrypt
@JsonSerialize(using = PrivacySerializer.class) // 该注解使用序列化的方式
public @interface PrivacyEncrypt {
  /** 脱敏数据类型（没给默认值，所以使用时必须指定type） */
  PrivacyTypeEnum type();

  /** 前置不需要打码的长度 */
  int prefixNoMaskLen() default 1;

  /** 后置不需要打码的长度 */
  int suffixNoMaskLen() default 1;

  /** 用什么打码 */
  String symbol() default "*";
}
