package com.aiyuns.tinkerplay.Other.Thread.ThreadCommunication;

/**
 * @Author: aiYunS @Date: 2023年12月1日, 0001 上午 11:24:38 @Description: 模拟线程通信
 */
public class ThreadCommunicationDemo {
  public static void main(String[] args) {
    SharedResource sharedResource = new SharedResource();

    WaitingThread waitingThread = new WaitingThread(sharedResource);
    NotifyingThread notifyingThread = new NotifyingThread(sharedResource);

    waitingThread.start();
    notifyingThread.start();
  }
}
