package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Author: aiYunS @Date: 2021/6/30 下午 02:25 @Description: 行政区实体类
 */
@Schema(name = "行政区信息实体类")
public class xzq {

  @Schema(description = "标识码")
  int bsm;

  @Schema(description = "sss")
  String sss;

  @Schema(description = "ssx")
  String ssx;

  @Schema(description = "行政区编码")
  String bzxzqbm;

  @Schema(description = "不动产行政区编码")
  String bdcxzqbm;

  @Schema(description = "创建人")
  String createby;

  @Schema(description = "创建时间")
  String createtime;

  @Schema(description = "编辑人")
  String editby;

  @Schema(description = "编辑时间")
  String edittime;

  @Schema(description = "查询地区编码")
  String deptcode;

  public int getBsm() {
    return bsm;
  }

  public void setBsm(int bsm) {
    this.bsm = bsm;
  }

  public String getSss() {
    return sss;
  }

  public void setSss(String sss) {
    this.sss = sss;
  }

  public String getSsx() {
    return ssx;
  }

  public void setSsx(String ssx) {
    this.ssx = ssx;
  }

  public String getBzxzqbm() {
    return bzxzqbm;
  }

  public void setBzxzqbm(String bzxzqbm) {
    this.bzxzqbm = bzxzqbm;
  }

  public String getBdcxzqbm() {
    return bdcxzqbm;
  }

  public void setBdcxzqbm(String bdcxzqbm) {
    this.bdcxzqbm = bdcxzqbm;
  }

  public String getCreateby() {
    return createby;
  }

  public void setCreateby(String createby) {
    this.createby = createby;
  }

  public String getCreatetime() {
    return createtime;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }

  public String getEditby() {
    return editby;
  }

  public void setEditby(String editby) {
    this.editby = editby;
  }

  public String getEdittime() {
    return edittime;
  }

  public void setEdittime(String edittime) {
    this.edittime = edittime;
  }

  public String getDeptcode() {
    return deptcode;
  }

  public void setDeptcode(String deptcode) {
    this.deptcode = deptcode;
  }

  @Override
  public String toString() {
    return "xzq{"
        + "bsm="
        + bsm
        + ", sss='"
        + sss
        + '\''
        + ", ssx='"
        + ssx
        + '\''
        + ", bzxzqbm='"
        + bzxzqbm
        + '\''
        + ", bdcxzqbm='"
        + bdcxzqbm
        + '\''
        + ", createby='"
        + createby
        + '\''
        + ", createtime='"
        + createtime
        + '\''
        + ", editby='"
        + editby
        + '\''
        + ", edittime='"
        + edittime
        + '\''
        + ", deptcode='"
        + deptcode
        + '\''
        + '}';
  }
}
