package com.aiyuns.tinkerplay.Entity;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: aiYunS @Date: 2022-9-5 上午 09:27 @Description: projbaseException表结构
 */
@Schema(name = "错误信息封装实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjbaseException {

  @ExcelProperty("projId")
  @Schema(description = "流水号")
  private String projId;

  @ExcelProperty("cjsj")
  @Schema(description = "创建时间")
  private String cjsj;

  @ExcelProperty("projectName")
  @Schema(description = "事项名称")
  private String projectName;

  @ExcelProperty("applyName")
  @Schema(description = "创建人")
  private String applyName;

  @ExcelProperty("applyCardNo")
  @Schema(description = "创建人证件号码")
  private String applyCardNo;

  @ExcelProperty("qxdm")
  @Schema(description = "区县代码")
  private String qxdm;

  @ExcelProperty("recvUserName")
  @Schema(description = "账号名称")
  private String recvUserName;

  @ExcelProperty("recvDeptCode")
  @Schema(description = "查询区域代码")
  private String recvDeptCode;

  @ExcelProperty("recvUserId")
  @Schema(description = "账号用户ID")
  private String recvUserId;

  @ExcelProperty("faceValidationResult")
  @Schema(description = "人脸识别结果")
  private String faceValidationResult;

  public String getProjId() {
    return projId;
  }

  public void setProjId(String projId) {
    this.projId = projId;
  }

  public String getCjsj() {
    return cjsj;
  }

  public void setCjsj(String cjsj) {
    this.cjsj = cjsj;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
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

  public String getQxdm() {
    return qxdm;
  }

  public void setQxdm(String qxdm) {
    this.qxdm = qxdm;
  }

  public String getRecvUserName() {
    return recvUserName;
  }

  public void setRecvUserName(String recvUserName) {
    this.recvUserName = recvUserName;
  }

  public String getRecvDeptCode() {
    return recvDeptCode;
  }

  public void setRecvDeptCode(String recvDeptCode) {
    this.recvDeptCode = recvDeptCode;
  }

  public String getRecvUserId() {
    return recvUserId;
  }

  public void setRecvUserId(String recvUserId) {
    this.recvUserId = recvUserId;
  }

  public String getFaceValidationResult() {
    return faceValidationResult;
  }

  public void setFaceValidationResult(String faceValidationResult) {
    this.faceValidationResult = faceValidationResult;
  }

  @Override
  public String toString() {
    return "ProjbaseException{"
        + "projId='"
        + projId
        + '\''
        + ", cjsj="
        + cjsj
        + ", projectName='"
        + projectName
        + '\''
        + ", applyName='"
        + applyName
        + '\''
        + ", applyCardNo='"
        + applyCardNo
        + '\''
        + ", qxdm='"
        + qxdm
        + '\''
        + ", recvUserName='"
        + recvUserName
        + '\''
        + ", recvDeptCode='"
        + recvDeptCode
        + '\''
        + ", recvUserId='"
        + recvUserId
        + '\''
        + ", faceValidationResult='"
        + faceValidationResult
        + '\''
        + '}';
  }
}
