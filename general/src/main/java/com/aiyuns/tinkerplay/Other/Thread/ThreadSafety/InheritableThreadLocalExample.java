package com.aiyuns.tinkerplay.Other.Thread.ThreadSafety;

/**
 * @Author: aiYunS @Date: 2023年6月20日, 0020 上午 8:49:52 @Description:
 * InheritableThreadLocal是Java中的一个特殊类型的ThreadLocal变量，它允许子线程继承父线程的线程局部变量的值。
 */
public class InheritableThreadLocalExample {

  private static ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

  public static void main(String[] args) {
    threadLocal.set(42);

    Thread parentThread =
        new Thread(
            () -> {
              System.out.println("Parent Thread: " + threadLocal.get());

              Thread childThread =
                  new Thread(
                      () -> {
                        System.out.println("Child Thread: " + threadLocal.get());
                      });

              childThread.start();
              try {
                childThread.join();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    parentThread.start();
    try {
      parentThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
