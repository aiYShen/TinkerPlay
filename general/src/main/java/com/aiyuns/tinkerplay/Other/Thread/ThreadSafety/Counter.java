package com.aiyuns.tinkerplay.Other.Thread.ThreadSafety;

/**
 * @Author: aiYunS @Date: 2023年6月2日, 0002 上午 9:14:21 @Description: synchronized 同步实例
 */
public class Counter {

  private int count = 0;

  // 被 synchronized 关键字修饰，这意味着每次只有一个线程可以执行这些方法。同一时间只允许一个线程获取该对象的内置锁，并执行同步代码块。
  public synchronized void increment() {
    count++;
  }

  public synchronized void decrement() {
    count--;
  }

  public synchronized int getCount() {
    return count;
  }
}
