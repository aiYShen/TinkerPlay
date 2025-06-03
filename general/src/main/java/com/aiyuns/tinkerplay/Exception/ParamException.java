package com.aiyuns.tinkerplay.Exception;

/**
 * @Author: aiYunS @Date: 2022-8-29 下午 02:34 @Description: 自定义参数异常类
 */
public class ParamException extends RuntimeException {
  private static final long serialVersionUID = -4993447045204262508L;

  public ParamException() {
    super("参数不能为空");
  }

  public ParamException(String message) {
    super(message);
  }
}
