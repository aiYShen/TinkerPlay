package com.aiyuns.tinkerplay.Other.Thread.ThreadPool;

import java.util.concurrent.*;

/**
 * @Author: aiYunS @Date: 2023年6月2日, 0002 上午 11:43:06 @Description: 创建线程池的三种方式
 */
public class ThreadPool {

  public static void main(String[] args) {
    // 使用 Executors 工厂类创建固定大小为 5 的线程池
    ExecutorService executor1 = Executors.newFixedThreadPool(5);
    // 创建一个缓存线程池
    ExecutorService executor2 = Executors.newCachedThreadPool();
    // 创建一个单线程线程池
    ExecutorService executor3 = Executors.newSingleThreadExecutor();

    // 使用 ThreadPoolExecutor 类手动创建线程池
    // 核心线程数
    int corePoolSize = 5;
    // 最大线程数
    int maxPoolSize = 10;
    // 线程空闲时间
    long keepAliveTime = 60L;
    TimeUnit unit = TimeUnit.SECONDS;
    // 任务队列来保存待执行的任务
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
    ExecutorService executor4 =
        new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue);

    // 使用 ForkJoinPool 类创建线程池
    // 特殊类型的线程池，用于支持任务分割与合并的并行计算。它适用于执行可以通过递归或分治方式划分为子任务的任务，例如并行计算、分割合并算法等
    ForkJoinPool executor5 = new ForkJoinPool();

    // 提交任务
    executor1.execute(
        () -> {
          // 执行任务逻辑
          System.out.println("Task 1 is running");
        });
    executor2.execute(
        () -> {
          System.out.println("Task 2 is running");
        });
    executor3.execute(
        () -> {
          System.out.println("Task 3 is running");
        });
    executor4.execute(
        () -> {
          System.out.println("Task 4 is running");
        });
    executor5.execute(
        () -> {
          System.out.println("Task 5 is running");
        });

    // 关闭线程池
    executor1.shutdown();
    executor2.shutdown();
    executor3.shutdown();
    executor4.shutdown();
    executor5.shutdown();
  }
}
