package com.aiyuns.tinkerplay.Entity;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: aiYunS @Date: 2022-9-5 上午 09:27 @Description: projbase表结构
 */
@Schema(name = "信息封装实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Projbase {

  @ExcelProperty("ywh")
  @Schema(description = "业务号")
  private String ywh;

  @ExcelProperty("projId")
  @Schema(description = "流水号")
  private String projId;

  @ExcelProperty("jsonObj")
  @Schema(description = "JSON请求")
  private String jsonObj;

  @ExcelProperty("cjsj")
  @Schema(description = "创建时间")
  private Date cjsj;

  @ExcelProperty("matterCode")
  @Schema(description = "状态码")
  private String matterCode;

  @ExcelProperty("projectName")
  @Schema(description = "事项名称")
  private String projectName;

  @ExcelProperty("gmtApply")
  @Schema(description = "创建申请")
  private String gmtApply;

  @ExcelProperty("applyName")
  @Schema(description = "创建人")
  private String applyName;

  @ExcelProperty("applyCardNo")
  @Schema(description = "创建人证件号码")
  private String applyCardNo;

  @ExcelProperty("bdcqzh")
  @Schema(description = "不动产权证号")
  private String bdcqzh;

  @ExcelProperty("bdcdjzmh")
  @Schema(description = "不动产登记证明")
  private String bdcdjzmh;

  @ExcelProperty("zl")
  @Schema(description = "坐落")
  private String zl;

  @ExcelProperty("areaCode")
  @Schema(description = "地区编码")
  private String areaCode;

  @ExcelProperty("zt")
  @Schema(description = "状态码")
  private Integer zt;

  public String getYwh() {
    return ywh;
  }

  public void setYwh(String ywh) {
    this.ywh = ywh;
  }

  public String getProjId() {
    return projId;
  }

  public void setProjId(String projId) {
    this.projId = projId;
  }

  public String getJsonObj() {
    return jsonObj;
  }

  public void setJsonObj(String jsonObj) {
    this.jsonObj = jsonObj;
  }

  public Date getCjsj() {
    return cjsj;
  }

  public void setCjsj(Date cjsj) {
    this.cjsj = cjsj;
  }

  public String getMatterCode() {
    return matterCode;
  }

  public void setMatterCode(String matterCode) {
    this.matterCode = matterCode;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getGmtApply() {
    return gmtApply;
  }

  public void setGmtApply(String gmtApply) {
    this.gmtApply = gmtApply;
  }

  public String getApplyName() {
    return applyName;
  }

  public void setApplyName(String applyName) {
    this.applyName = applyName;
  }

  public String getApplyCardNo() {
    return applyCardNo;
  }

  public void setApplyCardNo(String applyCardNo) {
    this.applyCardNo = applyCardNo;
  }

  public String getBdcqzh() {
    return bdcqzh;
  }

  public void setBdcqzh(String bdcqzh) {
    this.bdcqzh = bdcqzh;
  }

  public String getBdcdjzmh() {
    return bdcdjzmh;
  }

  public void setBdcdjzmh(String bdcdjzmh) {
    this.bdcdjzmh = bdcdjzmh;
  }

  public String getZl() {
    return zl;
  }

  public void setZl(String zl) {
    this.zl = zl;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public Integer getZt() {
    return zt;
  }

  public void setZt(Integer zt) {
    this.zt = zt;
  }

  @Override
  public String toString() {
    return "projbase{"
        + "ywh='"
        + ywh
        + '\''
        + ", projId='"
        + projId
        + '\''
        + ", jsonObj='"
        + jsonObj
        + '\''
        + ", cjsj="
        + cjsj
        + ", matterCode='"
        + matterCode
        + '\''
        + ", projectName='"
        + projectName
        + '\''
        + ", gmtApply='"
        + gmtApply
        + '\''
        + ", applyName='"
        + applyName
        + '\''
        + ", applyCardNo='"
        + applyCardNo
        + '\''
        + ", bdcqzh='"
        + bdcqzh
        + '\''
        + ", bdcdjzmh='"
        + bdcdjzmh
        + '\''
        + ", zl='"
        + zl
        + '\''
        + ", areaCode='"
        + areaCode
        + '\''
        + ", zt="
        + zt
        + '}';
  }
}
