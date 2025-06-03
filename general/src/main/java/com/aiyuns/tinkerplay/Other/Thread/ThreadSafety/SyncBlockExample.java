package com.aiyuns.tinkerplay.Other.Thread.ThreadSafety;

/**
 * @Author: aiYunS @Date: 2023年6月2日, 0002 上午 9:12:42 @Description: 同步代码块 synchronized
 * 关键字来创建同步代码块。通过指定一个共享的对象（lock），我们可以在代码块中获取该对象的内置锁。 这样，只有一个线程可以同时执行同步代码块，确保对共享资源的安全访问。
 */
public class SyncBlockExample {

  private static final Object lock = new Object();
  private int count = 0;

  public void increment() {
    synchronized (lock) {
      count++;
    }
  }

  public int getCount() {
    synchronized (lock) {
      return count;
    }
  }
}
