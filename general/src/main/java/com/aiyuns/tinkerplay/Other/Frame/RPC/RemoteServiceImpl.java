package com.aiyuns.tinkerplay.Other.Frame.RPC;

/**
 * @Author: aiYunS @Date: 2023年8月29日, 0029 下午 4:58:04 @Description: 实现服务接口的具体实现类
 */
public class RemoteServiceImpl implements RemoteService {
  @Override
  public int add(int a, int b) {
    return a + b;
  }
}
