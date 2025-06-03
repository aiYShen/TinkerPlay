package com.aiyuns.tinkerplay.Algorithm;

import java.util.concurrent.TimeUnit;

/**
 * @Author: aiYunS @Date: 2023年12月25日, 0025 上午 9:26:46 @Description: 漏桶算法
 */
public class LeakyBucketAlgorithm {

  // 桶的容量
  private final int capacity;
  // 当前水位
  private int waterLevel;
  // 上一次漏水的时间
  private long lastLeakTime;
  // 漏水时间间隔
  private final long leakInterval;

  public LeakyBucketAlgorithm(int capacity, long leakInterval, TimeUnit timeUnit) {
    this.capacity = capacity;
    this.waterLevel = 0;
    this.lastLeakTime = System.currentTimeMillis();
    this.leakInterval = timeUnit.toMillis(leakInterval);
  }

  public synchronized boolean tryConsume(int water) {
    leak(); // 漏水
    if (waterLevel + water <= capacity) {
      waterLevel += water;
      // 执行业务逻辑
      return true;
    } else {
      // 限流，执行相应的处理逻辑
      return false;
    }
  }

  private void leak() {
    long currentTime = System.currentTimeMillis();
    long elapsedTime = currentTime - lastLeakTime;
    // 计算漏水量
    long leaked = elapsedTime / leakInterval;
    waterLevel = Math.max(0, waterLevel - (int) leaked);
    lastLeakTime = currentTime;
  }

  public static void main(String[] args) {
    LeakyBucketAlgorithm leakyBucket = new LeakyBucketAlgorithm(10, 1000, TimeUnit.MILLISECONDS);
    for (int i = 0; i < 15; i++) {
      if (leakyBucket.tryConsume(1)) {
        System.out.println(i + ": 执行业务逻辑");
      } else {
        System.out.println(i + ": 限流处理");
      }
      try {
        // 模拟请求间隔
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
