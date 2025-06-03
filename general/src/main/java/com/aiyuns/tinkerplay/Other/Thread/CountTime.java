package com.aiyuns.tinkerplay.Other.Thread;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: aiYunS @Date: 2022-12-21 11:18 @Description: 十个线程打印输出1~10000中偶数的值，计算总耗时 用闭锁(门栓)
 */
public class CountTime {

  static CountDownLatch cdl = new CountDownLatch(10);

  void show() {
    for (int i = 0; i < 10000; i++) {
      if (i % 2 == 0) {
        System.out.println(Thread.currentThread().getName() + ":" + i);
      }
    }
    cdl.countDown();
  }

  public static void main(String[] args) {
    CountTime ct = new CountTime();
    long start = System.currentTimeMillis();
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    for (int i = 0; i < 10; i++) {
      new Thread(ct::show).start();
    }
    try {
      cdl.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    long end = System.currentTimeMillis();
    System.out.println((end - start - 100) + "--------------");
  }
}
