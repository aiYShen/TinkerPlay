package com.aiyuns.tinkerplay.DecryptAndEncry;

import com.aiyuns.tinkerplay.Config.Properties.EncryptProperties;
import com.aiyuns.tinkerplay.CustomAnnotations.Encrypt;
import com.aiyuns.tinkerplay.Entity.R;
import com.aiyuns.tinkerplay.Utils.AESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: aiYunS @Date: 2022-9-25 上午 11:20 @Description: 加密返回结果类
 */
@EnableConfigurationProperties(EncryptProperties.class) // 开启ControllerAdvice
@ControllerAdvice // 实现ResponseBodyAdvice
public class EncrptResponse implements ResponseBodyAdvice<R> {

  @Autowired private EncryptProperties encryptProperties;

  // 用来Object对象的转换
  private ObjectMapper om = new ObjectMapper();

  /** 支持什么时候加密 */
  @Override
  public boolean supports(
      MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
    return methodParameter.hasMethodAnnotation(Encrypt.class);
  }

  /** 数据响应进行加密 */
  @Override
  public R beforeBodyWrite(
      R r,
      MethodParameter methodParameter,
      MediaType mediaType,
      Class<? extends HttpMessageConverter<?>> aClass,
      ServerHttpRequest serverHttpRequest,
      ServerHttpResponse serverHttpResponse) {
    // 获取key的字节
    byte[] keybytes = encryptProperties.getKey().getBytes();
    // 如果msg和data存在的话，则进行加密，最后进行返回
    try {
      if (r.getMsg() != null) {
        r.setMsg(AESUtil.encrypt(r.getMsg().getBytes(), keybytes));
      }
      if (r.getData() != null) {
        r.setData(AESUtil.encrypt(om.writeValueAsBytes(r.getData()), keybytes));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return r;
  }
}
