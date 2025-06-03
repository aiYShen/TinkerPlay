package com.aiyuns.tinkerplay.Other.Frame.RPC;

/**
 * @Author: aiYunS @Date: 2023年8月29日, 0029 下午 4:53:33 @Description: 在远程服务器上调用的方法。这个接口需要被客户端和服务器共享
 */
public interface RemoteService {
  int add(int a, int b);
}
