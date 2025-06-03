package com.aiyuns.tinkerplay.Other.Thread.VirtualThreads;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: aiYunS @Date: 2023年9月27日, 0027 上午 10:16:10 @Description: 平台线程
 */
public class PlatformThreads {
  public static void main(String[] args) {
    // <<<<<<<<<<<<<<<<< 平台线程 >>>>>>>>>>>>>>>>
    long time1 = 0;
    var a = new AtomicInteger(0);
    // 创建一个不固定线程数的线程池
    try (var vs = Executors.newCachedThreadPool()) {
      List<Future<Integer>> futures = new ArrayList<>();
      var begin = System.currentTimeMillis();
      // 向线程池提交100000个sleep 1s的任务
      for (int i = 0; i < 1_00000; i++) {
        // 提交任务
        var future =
            vs.submit(
                () -> {
                  Thread.sleep(Duration.ofSeconds(1));
                  return a.addAndGet(1);
                });
        futures.add(future);
      }
      // 获取这每个线程任务的结果
      for (var future : futures) {
        var i = future.get();
        if (i % 100 == 0) {
          System.out.print(i + " ");
        }
      }
      // 打印总耗时
      time1 = System.currentTimeMillis() - begin;
      System.out.println();
      System.out.println("平台线程执行完成.");
      System.out.printf("耗 时: %dms.%n", time1);
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
