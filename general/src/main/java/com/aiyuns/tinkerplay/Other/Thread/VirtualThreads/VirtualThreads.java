package com.aiyuns.tinkerplay.Other.Thread.VirtualThreads;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: aiYunS @Date: 2023年9月27日, 0027 上午 9:29:45 @Description: 虚拟线程
 */
public class VirtualThreads {
  public static void main(String[] atgs) {
    // <<<<<<<<<<<<<<<<< 虚拟线程 >>>>>>>>>>>>>>>>
    long time2 = 0;
    var b = new AtomicInteger(0);
    // 创建无固定数量的虚拟线程池
    try (var vs = Executors.newVirtualThreadPerTaskExecutor()) {
      List<Future<Integer>> futures = new ArrayList<>();
      var begin = System.currentTimeMillis();
      // 向线程池提交100000个sleep 1s的任务
      for (int i = 0; i < 1_00000; i++) {
        var future =
            vs.submit(
                () -> {
                  Thread.sleep(Duration.ofSeconds(1));
                  return b.addAndGet(1);
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
      time2 = System.currentTimeMillis() - begin;
      System.out.println();
      System.out.println("虚拟线程执行完成.");
      System.out.printf("耗 时: %dms.%n", time2);
      System.out.println();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
