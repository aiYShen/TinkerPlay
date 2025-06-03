package com.aiyuns.tinkerplay.Entity;

import com.aiyuns.tinkerplay.CustomAnnotations.excelRescoure;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Author: aiYunS @Date: 2022-10-12 上午 09:56 @Description: ExcelPOJO实体类 * 实体类所有成员变量都需要有GET,SET方法 *
 * 所有成员变量都要加上注解@excelRescoure(value = "?"),?为Excel真实列名，必须一一对应 * @excelRescoure(value =
 * "?"),?可为空,需要用到才赋值 * 成员变量目前只允许String,Double,Interge,Float
 */
@Schema(name = "Excel导出封装实体类")
public class ExcelPOJO {

  @excelRescoure(value = "业务号")
  @Schema(description = "业务号")
  private String ywh;

  @excelRescoure(value = "权利标识码")
  @Schema(description = "权利标识码")
  private String qlbsm;

  @excelRescoure(value = "区县代码")
  @Schema(description = "区县代码")
  private String qxdm;

  @excelRescoure(value = "服务器编码")
  @Schema(description = "服务器编码")
  private String fwqbm;

  @excelRescoure(value = "抵押金额(万元)")
  @Schema(description = "抵押金额(万元)")
  private String dyje;

  @excelRescoure(value = "登簿时间")
  @Schema(description = "登簿时间")
  private String dbsj;

  @excelRescoure(value = "房屋面积㎡")
  @Schema(description = "房屋面积㎡")
  private String fwmj;

  @excelRescoure(value = "土地使用权面积㎡")
  @Schema(description = "土地使用权面积㎡")
  private String tdsyqmj;

  public ExcelPOJO() {}

  public String getYwh() {
    return ywh;
  }

  public void setYwh(String ywh) {
    this.ywh = ywh;
  }

  public String getQlbsm() {
    return qlbsm;
  }

  public void setQlbsm(String qlbsm) {
    this.qlbsm = qlbsm;
  }

  public String getQxdm() {
    return qxdm;
  }

  public void setQxdm(String qxdm) {
    this.qxdm = qxdm;
  }

  public String getFwqbm() {
    return fwqbm;
  }

  public void setFwqbm(String fwqbm) {
    this.fwqbm = fwqbm;
  }

  public String getDyje() {
    return dyje;
  }

  public void setDyje(String dyje) {
    this.dyje = dyje;
  }

  public String getDbsj() {
    return dbsj;
  }

  public void setDbsj(String dbsj) {
    this.dbsj = dbsj;
  }

  public String getFwmj() {
    return fwmj;
  }

  public void setFwmj(String fwmj) {
    this.fwmj = fwmj;
  }

  public String getTdsyqmj() {
    return tdsyqmj;
  }

  public void setTdsyqmj(String tdsyqmj) {
    this.tdsyqmj = tdsyqmj;
  }

  @Override
  public String toString() {
    return "ExcelPOJO{"
        + "ywh='"
        + ywh
        + '\''
        + ", qlbsm='"
        + qlbsm
        + '\''
        + ", qxdm='"
        + qxdm
        + '\''
        + ", fwqbm='"
        + fwqbm
        + '\''
        + ", dyje='"
        + dyje
        + '\''
        + ", dbsj='"
        + dbsj
        + '\''
        + ", fwmj='"
        + fwmj
        + '\''
        + ", tdsyqmj='"
        + tdsyqmj
        + '\''
        + '}';
  }
}
