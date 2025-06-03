package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: aiYunS @Date: 2023年6月1日, 0001 下午 5:26:58 @Description: 短信发送实体类
 */
@Schema(name = "短信发送实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sms {

  @Schema(description = "手机号码")
  private String[] phoneNumbers;

  @Schema(description = "模板ID")
  private Integer templateId;

  @Schema(description = "sms标识")
  private String smsSign;

  @Schema(description = "参数")
  private String[] params;
}
