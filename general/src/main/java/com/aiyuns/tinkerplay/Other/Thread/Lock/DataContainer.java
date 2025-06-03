package com.aiyuns.tinkerplay.Other.Thread.Lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

// 读多写少 ReentrantReadWriteLock
public class DataContainer {

  private int data; // 共享资源
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
  private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

  // 读取数据方法
  public int readData() {
    readLock.lock(); // 获取读锁
    try {
      System.out.println(Thread.currentThread().getName() + " 正在读取数据：" + data);
      return data;
    } finally {
      readLock.unlock(); // 释放读锁
    }
  }

  // 修改数据方法
  public void writeData(int newData) {
    writeLock.lock(); // 获取写锁
    try {
      System.out.println(Thread.currentThread().getName() + " 正在写入数据：" + newData);
      data = newData;
    } finally {
      writeLock.unlock(); // 释放写锁
    }
  }

  public static void main(String[] args) {
    DataContainer container = new DataContainer();

    // 启动多个线程读取数据
    for (int i = 0; i < 3; i++) {
      new Thread(container::readData).start();
    }

    // 启动一个线程修改数据
    new Thread(() -> container.writeData(42)).start();
  }
}
