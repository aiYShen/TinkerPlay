package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: aiYunS @Date: 2022-9-25 上午 11:05 @Description:
 */
@Schema(name = "返回信息封装实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {

  @Schema(description = "状态吗")
  private Integer code;

  @Schema(description = "信息")
  private String msg;

  @Schema(description = "数据")
  private Object data;

  public static R ok(String msg) {
    return new R(200, msg, null);
  }

  public static R ok(String msg, Object data) {
    return new R(200, msg, data);
  }

  public static R error(String msg) {
    return new R(500, msg, null);
  }

  public static R error(String msg, Object data) {
    return new R(500, msg, data);
  }
}
