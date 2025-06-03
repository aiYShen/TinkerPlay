package com.aiyuns.tinkerplay.Other.Thread.Lock;

/**
 * @Author: aiYunS @Date: 2022-12-20 16:09 @Description: 程序模拟死锁
 */
public class Deadlock {

  // 两根筷子资源,每个线程有一根
  private final Object o1 = new Object();
  private final Object o2 = new Object();

  public static void main(String[] args) {
    Deadlock deadlock = new Deadlock();
    new Thread(deadlock::m1).start();
    new Thread(deadlock::m2).start();
  }

  // (1)、原子性：所谓原子性就是指一个操作或者多个操作，要么全部执行并且执行的过程不会被任何因素打断，要么就都不执行。
  //     被synchronized修饰的类或对象的所有操作都是原子的，因为在执行操作之前必须先获得类或对象的锁，直到执行完才能释放。
  // (2)、可见性：可见性是指多个线程访问一个资源时，该资源的状态、值信息等对于其他线程都是可见的.
  //     synchronized和volatile都具有可见性，其中synchronized对一个类或对象加锁时，一个线程如果要访问该类或对象必须先获得它的锁，
  //     而这个锁的状态对于其他任何线程都是可见的，并且在释放锁之前会将对变量的修改刷新到共享内存当中，保证资源变量的可见性。
  // (3)、有序性：有序性值程序执行的顺序按照代码先后执行.
  // synchronized和volatile都具有有序性，Java允许编译器和处理器对指令进行重排，但是指令重排并不会影响单线程的顺序，
  // 它影响的是多线程并发执行的顺序性。synchronized保证了每个时刻都只有一个线程访问同步代码块，也就确定了线程执行同步代码块是分先后顺序的，保证了有序性。
  void m1() {
    System.out.println(Thread.currentThread().getName() + "启动等待...");
    synchronized (o1) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      synchronized (o2) {
        System.out.println("哈哈..");
      }
    }
  }

  void m2() {
    System.out.println(Thread.currentThread().getName() + "启动等待...");
    synchronized (o2) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      synchronized (o1) {
        System.out.println("哈哈..");
      }
    }
  }
}
