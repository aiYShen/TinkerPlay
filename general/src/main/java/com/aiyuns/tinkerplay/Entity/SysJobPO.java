package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: aiYunS @Date: 2022-10-10 上午 11:24 @Description: 定时任务实体类
 */
@Schema(name = "系统定时任务封装实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysJobPO {

  @Schema(description = "任务ID")
  private Integer jobId;

  @Schema(description = "bean名称")
  private String beanName;

  @Schema(description = "方法名称")
  private String methodName;

  @Schema(description = "方法参数")
  private String methodParams;

  @Schema(description = "cron表达式")
  private String cronExpression;

  @Schema(description = "状态")
  private Integer jobStatus;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新时间")
  private Date updateTime;

  public Integer getJobId() {
    return jobId;
  }

  public void setJobId(Integer jobId) {
    this.jobId = jobId;
  }

  public String getBeanName() {
    return beanName;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getMethodParams() {
    return methodParams;
  }

  public void setMethodParams(String methodParams) {
    this.methodParams = methodParams;
  }

  public String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public Integer getJobStatus() {
    return jobStatus;
  }

  public void setJobStatus(Integer jobStatus) {
    this.jobStatus = jobStatus;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
