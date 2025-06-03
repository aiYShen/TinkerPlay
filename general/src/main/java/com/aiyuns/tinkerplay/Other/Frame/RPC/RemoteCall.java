package com.aiyuns.tinkerplay.Other.Frame.RPC;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @Author: aiYunS @Date: 2023年8月29日, 0029 下午 4:56:51 @Description: 用于封装调用信息，包括方法名和参数
 */
public class RemoteCall implements Serializable {
  private String methodName;
  private Object[] args;

  public RemoteCall(String methodName, Object[] args) {
    this.methodName = methodName;
    this.args = args;
  }

  public int invoke(RemoteService service) {
    try {
      Method method = service.getClass().getMethod(methodName, Integer.TYPE, Integer.TYPE);
      return (int) method.invoke(service, args);
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
}
