package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: aiYunS @Date: 2023年6月7日, 0007 下午 5:06:16 @Description: 文件下载实体
 */
@Schema(name = "文件下载实体类")
@Data
public class ResultEntity<T> {

  @Schema(description = "结果")
  private String result;

  @Schema(description = "失败信息")
  private String message;

  @Schema(description = "返回数据")
  private T data;

  private static final String SUCCESS = "SUCCESS";
  private static final String FAILED = "FAILED";

  public ResultEntity() {}

  public ResultEntity(String result, String message, T data) {
    this.result = result;
    this.message = message;
    this.data = data;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  /**
   * 成功不携带数据的统一格式 比如增删改
   *
   * @param <Type>
   * @return
   */
  public static <Type> ResultEntity<Type> successWithoutData() {
    return new ResultEntity<Type>(SUCCESS, null, null);
  }

  /**
   * 成功携带数据的统一格式 比如查询
   *
   * @param data
   * @param <Type>
   * @return
   */
  public static <Type> ResultEntity<Type> successWithData(Type data) {
    return new ResultEntity<Type>(SUCCESS, null, data);
  }

  /**
   * 返回操作失败
   *
   * @param message
   * @param <Type>
   * @return
   */
  public static <Type> ResultEntity<Type> failed(String message) {
    return new ResultEntity<Type>(FAILED, message, null);
  }

  @Override
  public String toString() {
    return "ResultEntity{"
        + "result='"
        + result
        + '\''
        + ", message='"
        + message
        + '\''
        + ", data="
        + data
        + '}';
  }
}
