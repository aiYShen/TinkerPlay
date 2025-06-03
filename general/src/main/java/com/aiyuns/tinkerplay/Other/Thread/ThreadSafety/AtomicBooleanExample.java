package com.aiyuns.tinkerplay.Other.Thread.ThreadSafety;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: aiYunS @Date: 2023年6月2日, 0002 上午 9:05:36 @Description: 原子类AtomicInteger使用示例
 */
public class AtomicBooleanExample {

  private static AtomicBoolean flag = new AtomicBoolean(false);

  public static void main(String[] args) {
    Thread t1 = new Thread(new ToggleTask());
    Thread t2 = new Thread(new ToggleTask());
    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Final flag value: " + flag.get());
  }

  public static class ToggleTask implements Runnable {
    @Override
    public void run() {
      for (int i = 0; i < 1000; i++) {
        boolean currentValue = flag.get();
        boolean newValue = !currentValue;
        flag.compareAndSet(currentValue, newValue);
      }
    }
  }
}
