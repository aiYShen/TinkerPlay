package com.aiyuns.tinkerplay.DecryptAndEncry;

import com.aiyuns.tinkerplay.Config.Properties.EncryptProperties;
import com.aiyuns.tinkerplay.CustomAnnotations.Decrypt;
import com.aiyuns.tinkerplay.Utils.AESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

/**
 * @Author: aiYunS @Date: 2022-9-13 上午 10:18 @Description: 解密请求参数类,允许获取EncryptProperties配置类
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class DecryptRequest extends RequestBodyAdviceAdapter {

  private ObjectMapper om = new ObjectMapper();
  @Autowired EncryptProperties encryptProperties;

  @Override
  public boolean supports(
      MethodParameter methodParameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return methodParameter.hasMethodAnnotation(Decrypt.class)
        || methodParameter.hasParameterAnnotation(Decrypt.class);
  }

  @Override
  public HttpInputMessage beforeBodyRead(
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType)
      throws IOException {
    // 获取密钥的字节
    byte[] keyByte = encryptProperties.getKey().getBytes();
    // 读取请求参数成为字节body
    byte[] body = new byte[inputMessage.getBody().available()];
    inputMessage.getBody().read(body);

    try {
      // 解密
      byte[] decrypt = AESUtil.decrypt(body, keyByte);
      // 将解密的字节放入字节数组输入流中
      ByteArrayInputStream bais = new ByteArrayInputStream(decrypt);

      // 返回HttpInputMessage
      return new HttpInputMessage() {
        @Override
        public InputStream getBody() throws IOException {
          return bais;
        }

        @Override
        public HttpHeaders getHeaders() {
          return inputMessage.getHeaders();
        }
      };
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 如果不需要进行处理的话，则直接调用父类的beforeBodyRead，相当于直接返回inputMessage，不做处理
    return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
  }
}
