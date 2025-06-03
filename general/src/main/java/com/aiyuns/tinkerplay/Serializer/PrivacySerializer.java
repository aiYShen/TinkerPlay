package com.aiyuns.tinkerplay.Serializer;

import com.aiyuns.tinkerplay.CustomAnnotations.PrivacyEncrypt;
import com.aiyuns.tinkerplay.Enum.PrivacyTypeEnum;
import com.aiyuns.tinkerplay.Utils.PrivacyUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: aiYunS @Date: 2023-1-12 10:27 @Description: 自定义序列化器
 */
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySerializer extends JsonSerializer<String> implements ContextualSerializer {

  // 脱敏类型
  private PrivacyTypeEnum privacyTypeEnum;
  // 前几位不脱敏
  private Integer prefixNoMaskLen;
  // 最后几位不脱敏
  private Integer suffixNoMaskLen;
  // 用什么打码
  private String symbol;

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (StringUtils.isNotBlank(value) && null != privacyTypeEnum) {
      switch (privacyTypeEnum) {
        case CUSTOMER:
          gen.writeString(PrivacyUtil.desValue(value, prefixNoMaskLen, suffixNoMaskLen, symbol));
          break;
        case NAME:
          gen.writeString(PrivacyUtil.hideChineseName(value));
          break;
        case ID_CARD:
          gen.writeString(PrivacyUtil.hideIDCard(value));
          break;
        case PHONE:
          gen.writeString(PrivacyUtil.hidePhone(value));
          break;
        case EMAIL:
          gen.writeString(PrivacyUtil.hideEmail(value));
          break;
        default:
          throw new IllegalArgumentException("unknown privacy type enum " + privacyTypeEnum);
      }
    }
  }

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
      throws JsonMappingException {
    if (property != null) {
      if (Objects.equals(property.getType().getRawClass(), String.class)) {
        PrivacyEncrypt privacyEncrypt = property.getAnnotation(PrivacyEncrypt.class);
        if (privacyEncrypt == null) {
          privacyEncrypt = property.getContextAnnotation(PrivacyEncrypt.class);
        }
        if (privacyEncrypt != null) {
          return new PrivacySerializer(
              privacyEncrypt.type(),
              privacyEncrypt.prefixNoMaskLen(),
              privacyEncrypt.suffixNoMaskLen(),
              privacyEncrypt.symbol());
        }
      }
      return prov.findValueSerializer(property.getType(), property);
    }
    return prov.findNullValueSerializer(null);
  }
}
