package com.aiyuns.tinkerplay.Other.Thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: aiYunS @Date: 2022-12-21 10:21 @Description: 用线程通信机制解决生产者消费者问题
 */
public class ProductorAndConsumerForLock {

  public static void main(String[] args) {

    Clerk1 clerk1 = new Clerk1();

    Productor1 pro = new Productor1(clerk1);
    Consumer1 con = new Consumer1(clerk1);

    new Thread(pro, "生产者 A").start();
    new Thread(con, "消费者 B").start();

    //		 new Thread(pro, "生产者 C").start();
    //		 new Thread(con, "消费者 D").start();
  }
}

class Clerk1 {
  private int product = 0;

  private Lock lock = new ReentrantLock();
  private Condition condition = lock.newCondition();

  // 进货
  public void get() {
    lock.lock();

    try {
      // 为了避免虚假唤醒，应该总是使用在循环中。
      if (product >= 1) {
        System.out.println("产品已满！");

        try {
          condition.await();
        } catch (InterruptedException e) {
        }
      }
      System.out.println(Thread.currentThread().getName() + " : " + ++product);

      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  // 卖货
  public void sale() {
    lock.lock();

    try {
      if (product <= 0) {
        System.out.println("缺货！");

        try {
          condition.await();
        } catch (InterruptedException e) {
        }
      }

      System.out.println(Thread.currentThread().getName() + " : " + --product);

      condition.signalAll();

    } finally {
      lock.unlock();
    }
  }
}

// 生产者
class Productor1 implements Runnable {

  private Clerk1 clerk1;

  public Productor1(Clerk1 clerk1) {
    this.clerk1 = clerk1;
  }

  @Override
  public void run() {
    for (int i = 0; i < 20; i++) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      clerk1.get();
    }
  }
}

// 消费者
class Consumer1 implements Runnable {

  private Clerk1 clerk1;

  public Consumer1(Clerk1 clerk1) {
    this.clerk1 = clerk1;
  }

  @Override
  public void run() {
    for (int i = 0; i < 20; i++) {
      clerk1.sale();
    }
  }
}
