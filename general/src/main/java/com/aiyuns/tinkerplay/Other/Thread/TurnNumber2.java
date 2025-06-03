package com.aiyuns.tinkerplay.Other.Thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: aiYunS @Date: 2022-12-20 17:25 @Description: 用join()实现三个线程，运行输出 A1 B2 C3 A4 B5 C6 ...
 */
public class TurnNumber2 {

  AtomicInteger num = new AtomicInteger(0);

  public void show() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    String tn = Thread.currentThread().getName();
    int i = num.incrementAndGet();
    String s = String.format("%s%d", tn, i);
    System.out.print(s + "  ");
    if ("C".equals(tn)) {
      System.out.println();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    TurnNumber2 tn = new TurnNumber2();
    while (true) {
      Thread a = new Thread(tn::show, "A");
      Thread b = new Thread(tn::show, "B");
      Thread c = new Thread(tn::show, "C");
      // join方法的主要作用就是同步，它可以使得线程之间的并行执行变为串行执行
      a.setPriority(Thread.MAX_PRIORITY);
      a.start();
      a.join();
      b.setPriority(Thread.NORM_PRIORITY);
      b.start();
      b.join();
      c.setPriority(Thread.MIN_PRIORITY);
      c.start();
      c.join();
    }
  }
}
