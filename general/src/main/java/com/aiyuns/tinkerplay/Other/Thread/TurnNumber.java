package com.aiyuns.tinkerplay.Other.Thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: aiYunS @Date: 2022-12-20 17:04 @Description: 公平锁实现三个线程，运行输出 A1 B2 C3 A4 B5 C6 ...
 */
public class TurnNumber {

  // AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减
  AtomicInteger num = new AtomicInteger(0);
  // ReentrantLock是一个可重入的互斥锁，又被称为“独占锁”。
  // ReentrantLock锁在同一个时间点只能被一个线程锁持有；可重入表示，ReentrantLock锁可以被同一个线程多次获取。
  // ReentraantLock是通过一个FIFO的等待队列来管理获取该锁所有线程的。在“公平锁”的机制下，线程依次排队获取锁；
  // 而“非公平锁”在锁是可获取状态时，不管自己是不是在队列的开头都会获取锁。
  private final ReentrantLock reentrantLock = new ReentrantLock(true);

  public static void main(String[] args) throws InterruptedException {
    TurnNumber tn = new TurnNumber();
    Thread a = new Thread(tn::show, "A");
    Thread b = new Thread(tn::show, "B");
    Thread c = new Thread(tn::show, "C");
    // 设置最高优先级
    a.setPriority(Thread.MAX_PRIORITY);
    a.start();
    // 设置一般优先级
    b.setPriority(Thread.NORM_PRIORITY);
    b.start();
    // 设置最低优先级
    c.setPriority(Thread.MIN_PRIORITY);
    c.start();
  }

  public void show() {
    for (; ; ) {
      reentrantLock.lock();
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      String threadName = Thread.currentThread().getName();
      int i = num.incrementAndGet();
      String s = String.format("%s%d", threadName, i);
      System.out.print(s + "  ");
      if ("C".equals(threadName)) {
        System.out.println();
      }
      reentrantLock.unlock();
    }
  }
}
