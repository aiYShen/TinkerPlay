package com.aiyuns.tinkerplay.Other.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: aiYunS @Date: 2022-12-20 15:35 @Description: 闭锁(门栓锁) 实现一个容器，提供两个方法，add，count 写两个线程， *
 * 线程1添加10个元素到容器中，线程2实现监控元素的个数， * 当个数到5个时，线程2给出提示并结束。
 */
public class AddAndCount {

  // CountDownLatch能够使一个线程在等待另外一些线程完成各自工作之后，再继续执行。使用一个计数器进行实现。
  // 计数器初始值为线程的数量。当每一个线程完成自己任务后，计数器的值就会减一。
  // 当计数器的值为0时，表示所有的线程都已经完成一些任务，然后在CountDownLatch上等待的线程就可以恢复执行接下来的任务。
  CountDownLatch countDownLatch1 = new CountDownLatch(1);
  List<String> list = new ArrayList<>();

  public static void main(String[] args) {
    AddAndCount aac = new AddAndCount();
    new Thread(aac::add, "A").start();
    new Thread(aac::count, "B").start();
  }

  void add() {
    for (int i = 0; i < 10; i++) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      String item = String.format("%s - %d", "item", i);
      list.add(item);
      System.out.println(Thread.currentThread().getName() + ":" + item);
      // 新增到第五个值,计数器触发
      if (i == 4) {
        countDownLatch1.countDown();
      }
    }
  }

  void count() {
    try {
      countDownLatch1.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("程序结束...");
    System.exit(0);
  }
}
