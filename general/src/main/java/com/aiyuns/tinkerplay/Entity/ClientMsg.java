package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: aiYunS @Date: 2022-9-15 上午 10:08 @Description: 客户端信息
 */
@Schema(name = "设备信息实体类")
@Data
public class ClientMsg {

  @Schema(description = "客户端类型")
  private String ClientType;

  @Schema(description = "系统类型")
  private String SystemType;

  @Schema(description = "浏览器类型")
  private String BrowserType;

  public String getClientType() {
    return ClientType;
  }

  public void setClientType(String clientType) {
    ClientType = clientType;
  }

  public String getSystemType() {
    return SystemType;
  }

  public void setSystemType(String systemType) {
    SystemType = systemType;
  }

  public String getBrowserType() {
    return BrowserType;
  }

  public void setBrowserType(String browserType) {
    BrowserType = browserType;
  }
}
