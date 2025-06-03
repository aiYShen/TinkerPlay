package com.aiyuns.tinkerplay.Algorithm;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: aiYunS @Date: 2023年12月25日, 0025 上午 9:19:04 @Description: 令牌桶算法
 */
public class TokenBucketAlgorithm {

  // 令牌桶容量
  private final int capacity;
  // 当前令牌数量，使用 double 类型以支持部分令牌
  private double tokens;
  // 上一次令牌补充时间
  private long lastRefillTime;
  // 令牌补充时间间隔
  private final long refillInterval;
  // 用于保证线程安全
  private final Lock lock;

  public TokenBucketAlgorithm(int capacity, long refillInterval, TimeUnit timeUnit) {
    this.capacity = capacity;
    this.tokens = 0;
    this.lastRefillTime = System.currentTimeMillis();
    this.refillInterval = timeUnit.toMillis(refillInterval);
    this.lock = new ReentrantLock();
  }

  public boolean tryConsume(int tokens) {
    lock.lock();
    try {
      // 补充令牌
      refillTokens();
      if (tokens <= this.tokens) {
        this.tokens -= tokens;
        return true;
      } else {
        return false;
      }
    } finally {
      lock.unlock();
    }
  }

  private void refillTokens() {
    long currentTime = System.currentTimeMillis();
    long elapsedTime = currentTime - lastRefillTime;
    // 补充的令牌数量
    double tokensToAdd = elapsedTime / (double) refillInterval;
    this.tokens = Math.min(capacity, this.tokens + tokensToAdd);
    this.lastRefillTime = currentTime;
  }

  public static void main(String[] args) {
    TokenBucketAlgorithm tokenBucket = new TokenBucketAlgorithm(10, 1000, TimeUnit.MILLISECONDS);
    for (int i = 0; i < 100; i++) {
      if (tokenBucket.tryConsume(1)) {
        // 执行业务逻辑
        System.out.println(i + ": 执行业务逻辑");
      } else {
        // 限流处理
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
