package com.aiyuns.tinkerplay.Other.Thread.ThreadSafety;

/**
 * @Author: aiYunS @Date: 2023年6月2日, 0002 上午 9:11:33 @Description: synchronized 同步静态方法
 */
public class SyncStaticExample {

  private static int count = 0;

  // synchronized 关键字修饰了静态方法。这样可以获取当前类的 Class 对象的内置锁，并实现对静态变量的同步访问
  public static synchronized void increment() {
    count++;
  }

  public static synchronized int getCount() {
    return count;
  }
}
