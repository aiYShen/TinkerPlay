package com.aiyuns.tinkerplay.Other.Thread.ThreadCommunication;

/**
 * @Author: aiYunS @Date: 2023年12月1日, 0001 上午 11:23:21 @Description: 模拟线程通信
 */
public class SharedResource {
  private boolean flag = false;

  public synchronized void waitForFlag() {
    while (!flag) {
      try {
        wait(); // 等待条件满足
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public synchronized void setFlag() {
    flag = true;
    notifyAll(); // 唤醒等待的线程
  }
}
