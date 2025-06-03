package com.aiyuns.tinkerplay.Other.DesignMode.SingletonMode;

/**
 * @Author: aiYunS @Date: 2022-12-22 11:26 @Description: 懒汉式，线程安全
 */
public class Singleton2 {

  private static Singleton2 instance;

  // 私有化构造方法
  private Singleton2() {}

  // 加关键字synchronized
  public static synchronized Singleton2 getInstance() {
    if (instance == null) {
      instance = new Singleton2();
    }
    return instance;
  }
}
