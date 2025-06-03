package com.aiyuns.tinkerplay.Other.Thread.ThreadSafety;

/**
 * @Author: aiYunS @Date: 2023年6月2日, 0002 上午 8:56:51 @Description: ThreadLocal使用示例
 */
public class ThreadLocalExample {

  // 创建一个 ThreadLocal 对象
  private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

  public static void main(String[] args) {
    // 创建并启动三个线程
    Thread t1 = new Thread(new MyRunnable());
    Thread t2 = new Thread(new MyRunnable());
    Thread t3 = new Thread(new MyRunnable());
    t1.start();
    t2.start();
    t3.start();

    try {
      // 等待所有线程执行完毕
      t1.join();
      t2.join();
      t3.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static class MyRunnable implements Runnable {
    @Override
    public void run() {
      // 生成一个随机数，并将其存储到 ThreadLocal 变量中
      int randomNumber = (int) (Math.random() * 100);
      threadLocal.set(randomNumber);

      // 打印当前线程的 ID 和对应的 ThreadLocal 变量值
      System.out.println(
          "Thread ID: "
              + Thread.currentThread().threadId()
              + ", ThreadLocal value: "
              + threadLocal.get());

      // 暂停一段时间
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // 清除 ThreadLocal 变量值
      threadLocal.remove();
    }
  }
}
