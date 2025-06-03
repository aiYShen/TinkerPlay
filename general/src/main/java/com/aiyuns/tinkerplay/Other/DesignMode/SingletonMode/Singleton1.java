package com.aiyuns.tinkerplay.Other.DesignMode.SingletonMode;

/**
 * @Author: aiYunS @Date: 2022-12-22 11:25 @Description: 懒汉式，线程不安全
 */
public class Singleton1 {

  private static Singleton1 instance;

  // 私有化构造方法
  private Singleton1() {}

  public static Singleton1 getInstance() {
    if (instance == null) {
      instance = new Singleton1();
    }
    return instance;
  }
}
