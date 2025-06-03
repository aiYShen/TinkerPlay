package com.aiyuns.tinkerplay.Other.DesignMode.SingletonMode;

/**
 * @Author: aiYunS @Date: 2022-12-22 11:27 @Description: 饿汉式
 */
public class Singleton3 {

  private static Singleton3 instance = new Singleton3();

  // 私有化构造方法
  private Singleton3() {}

  public static Singleton3 getInstance() {
    return instance;
  }
}
