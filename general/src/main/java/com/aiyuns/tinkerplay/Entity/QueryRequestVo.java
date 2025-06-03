package com.aiyuns.tinkerplay.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: aiYunS @Date: 2021/9/8 下午 08:20 @Description: 包装查询请求
 */
@Schema(name = "查询请求封装实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequestVo {

  @Schema(description = "用户对象")
  private User user;

  @Schema(description = "用户id")
  private int id;

  @Schema(description = "用户名字")
  private String name;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "开始时间")
  private Date startTime;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "时间区间")
  private String qj;

  @Schema(description = "超时时间")
  private String timeOut;

  @Schema(description = "行政区编码")
  private String xzqbm;

  @Schema(description = "行政区级别")
  private String xzqjib;

  @Schema(description = "是否查询列表")
  private boolean lists;

  @Schema(description = "查询页码")
  private int start;

  @Schema(description = "每页大小")
  private int size;

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date kssj) {
    if (kssj == null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      try {
        this.startTime = dateFormat.parse(dateFormat.format(new Date()));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    } else {
      this.startTime = kssj;
    }
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date jssj) {
    if (jssj == null) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      try {
        this.endTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    } else {
      this.endTime = jssj;
    }
  }

  public String getQj() {
    return qj;
  }

  public void setQj(String qj) {
    this.qj = qj;
  }

  public String getTimeOut() {
    return timeOut;
  }

  public void setTimeOut(String timeOut) {
    this.timeOut = timeOut;
  }

  public String getXzqbm() {
    return xzqbm;
  }

  public void setXzqbm(String xzqbm) {
    this.xzqbm = xzqbm;
  }

  public String getXzqjib() {
    return xzqjib;
  }

  public void setXzqjib(String xzqjib) {
    this.xzqjib = xzqjib;
  }

  public boolean isLists() {
    return lists;
  }

  public void setLists(boolean lists) {
    this.lists = lists;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
