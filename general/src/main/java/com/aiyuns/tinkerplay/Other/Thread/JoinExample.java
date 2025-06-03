package com.aiyuns.tinkerplay.Other.Thread;

/**
 * @Author: aiYunS @Date: 2023年6月20日, 0020 上午 8:55:52 @Description:
 * join()是一个线程方法，它允许一个线程等待另一个线程完成执行后再继续执行。
 * 当一个线程调用另一个线程的join()方法时，它将被阻塞，直到目标线程完成执行。也就是说，调用join()方法的线程将等待目标线程的终止。 join()方法有以下两种形式： join():
 * 无参形式的join()方法会使当前线程等待调用该方法的线程终止后再继续执行。 join(long millis):
 * 带有超时参数的join()方法会使当前线程等待调用该方法的线程终止，或者等待超过指定的超时时间后再继续执行。
 */
public class JoinExample {
  public static void main(String[] args) throws InterruptedException {
    Thread thread1 =
        new Thread(
            () -> {
              try {
                Thread.sleep(2000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              System.out.println("Thread 1 finished");
            });

    Thread thread2 =
        new Thread(
            () -> {
              try {
                Thread.sleep(3000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              System.out.println("Thread 2 finished");
            });

    thread1.start();
    thread2.start();

    System.out.println("Main thread is waiting for thread1 and thread2 to finish");

    thread1.join();
    thread2.join();

    System.out.println("All threads have finished. Main thread resumes execution.");
  }
}
