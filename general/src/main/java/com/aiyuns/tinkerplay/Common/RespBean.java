package com.aiyuns.tinkerplay.Common;

/**
 * @Author: aiYunS @Date: 2022-9-13 上午 10:56 @Description: 备用的响应类
 */
public class RespBean {

  private Integer status;
  private String msg;
  private Object obj;

  private RespBean() {}

  private RespBean(Integer status, String msg, Object obj) {
    this.status = status;
    this.msg = msg;
    this.obj = obj;
  }

  public static RespBean build() {
    return new RespBean();
  }

  public static RespBean ok(String msg) {
    return new RespBean(200, msg, null);
  }

  public static RespBean ok(String msg, Object obj) {
    return new RespBean(200, msg, obj);
  }

  public static RespBean error(String msg) {
    return new RespBean(500, msg, null);
  }

  public static RespBean error(String msg, Object obj) {
    return new RespBean(500, msg, obj);
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getObj() {
    return obj;
  }

  public void setObj(Object obj) {
    this.obj = obj;
  }

  @Override
  public String toString() {
    return "RespBean{" + "status=" + status + ", msg='" + msg + '\'' + ", obj=" + obj + '}';
  }
}
