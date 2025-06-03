package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: aiYunS @Date: 2022-10-12 下午 05:15 @Description: 实体类
 */
@Schema(name = "Token信息封装实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenAccess {

  @Schema(description = "员工姓名")
  private String staff_name;

  @Schema(description = "用户ID")
  private String user_id;

  @Schema(description = "用户姓名")
  private String username;

  @Schema(description = "状态")
  private Integer status;

  public String getStaff_name() {
    return staff_name;
  }

  public void setStaff_name(String staff_name) {
    this.staff_name = staff_name;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
