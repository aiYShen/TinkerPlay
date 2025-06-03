package com.aiyuns.tinkerplay.Other.Thread.ThreadSafety;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: aiYunS @Date: 2023年6月2日, 0002 上午 9:04:09 @Description: 原子类AtomicInteger使用示例
 */
public class AtomicIntegerExample {

  private static AtomicInteger counter = new AtomicInteger(0);

  public static void main(String[] args) {
    Thread t1 = new Thread(new IncrementTask());
    Thread t2 = new Thread(new IncrementTask());
    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Final value: " + counter.get());
  }

  public static class IncrementTask implements Runnable {
    @Override
    public void run() {
      for (int i = 0; i < 1000; i++) {
        counter.incrementAndGet();
      }
    }
  }
}
