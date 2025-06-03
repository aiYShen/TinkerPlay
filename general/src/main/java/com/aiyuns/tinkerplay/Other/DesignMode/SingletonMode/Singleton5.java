package com.aiyuns.tinkerplay.Other.DesignMode.SingletonMode;

/**
 * @Author: aiYunS @Date: 2022-12-22 11:30 @Description: 登记式/静态内部类
 */
public class Singleton5 {

  // 私有方法
  private static class SingletonHolder {
    private static final Singleton5 INSTANCE = new Singleton5();
  }

  // 私有化构造方法
  private Singleton5() {}

  public static final Singleton5 getInstance() {
    return SingletonHolder.INSTANCE;
  }
}
