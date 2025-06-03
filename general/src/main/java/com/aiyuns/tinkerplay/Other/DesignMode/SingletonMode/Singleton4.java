package com.aiyuns.tinkerplay.Other.DesignMode.SingletonMode;

/**
 * @Author: aiYunS @Date: 2022-12-22 11:29 @Description: 双检锁/双重校验锁
 */
public class Singleton4 {

  // 上锁volatile
  private static volatile Singleton4 singleton;

  // 私有化构造方法
  private Singleton4() {}

  public static Singleton4 getSingleton() {
    if (singleton == null) {
      // 上锁synchronized
      synchronized (Singleton4.class) {
        if (singleton == null) {
          singleton = new Singleton4();
        }
      }
    }
    return singleton;
  }
}
