package com.aiyuns.tinkerplay.Other.Thread.ThreadCommunication;

/**
 * @Author: aiYunS @Date: 2023年12月1日, 0001 上午 11:23:41 @Description: 模拟线程通信
 */
public class NotifyingThread extends Thread {
  private SharedResource sharedResource;

  public NotifyingThread(SharedResource sharedResource) {
    this.sharedResource = sharedResource;
  }

  @Override
  public void run() {
    // 模拟一些工作
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    System.out.println("NotifyingThread 执行完毕，发出通知...");
    sharedResource.setFlag();
  }
}
