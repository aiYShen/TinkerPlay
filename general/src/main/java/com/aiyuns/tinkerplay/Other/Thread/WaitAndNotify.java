package com.aiyuns.tinkerplay.Other.Thread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: aiYunS @Date: 2022-12-20 17:29 @Description:
 * 创建五个线程并进入等待状态，等两秒后主线程开始并释放全部线程，最后主线程结束,用的是wait() 与notifyAll()组合形式
 */
public class WaitAndNotify {

  public static void main(String[] args) {
    Object co = new Object();

    for (int i = 0; i < 5; i++) {
      MyThread t = new MyThread("Thread" + i, co);
      // 调用线程的start()方法的顺序并不能决定线程的执行顺序
      t.start();
    }

    try {
      // 给定单元中进行跨单元转换和执行计时及延迟操作的实用工具方法
      TimeUnit.SECONDS.sleep(2);
      System.out.println("-----Main Thread notify-----");
      synchronized (co) {
        // 唤醒一个wait()的线程，当notify所在的代码块的锁释放之后，wait的线程开始抢锁
        // co.notify();
        // 唤醒所有wait线程，notify的高级版本
        co.notifyAll();
        // wait是放弃了当前线程的锁，被notify之后还需要自己去抢锁，如果notify所在的代码块还没有抢到锁，或者被其他线程把锁抢到了，
        // 那wait所在线程还需要接着努力抢锁。
      }

      TimeUnit.SECONDS.sleep(2);
      System.out.println("Main Thread is end.");

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  static class MyThread extends Thread {
    private String name;
    private Object co;

    public MyThread(String name, Object o) {
      this.name = name;
      this.co = o;
    }

    @Override
    public void run() {
      System.out.println(name + " is waiting.");
      try {
        synchronized (co) {
          // 让出当前线程的锁，进入等待状态，让其他线程先用会儿锁 ，这里注意了:当前线程必须要先获得锁，所以它一般会与synchronized配合使用
          co.wait();
        }
        System.out.println(name + " has been notified.");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
