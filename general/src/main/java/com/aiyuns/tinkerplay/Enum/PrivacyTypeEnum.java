package com.aiyuns.tinkerplay.Enum;

import lombok.Getter;

/**
 * @Author: aiYunS @Date: 2023-1-12 10:18 @Description: 隐私数据类型枚举
 */
@Getter
public enum PrivacyTypeEnum {

  /** 自定义（此项需设置脱敏的范围） */
  CUSTOMER,

  /** 姓名 */
  NAME,

  /** 身份证号 */
  ID_CARD,

  /** 手机号 */
  PHONE,

  /** 邮箱 */
  EMAIL,
}
