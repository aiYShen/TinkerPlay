package com.aiyuns.tinkerplay.TimedTask;

import java.util.concurrent.ScheduledFuture;

/**
 * @Author: aiYunS @Date: 2022-10-10 上午 11:01 @Description:
 * ScheduledFuture的包装类。ScheduledFuture是ScheduledExecutorService定时任务线程池的执行结果 被final修饰的类，不能被继承
 * 　　被final修饰的方法，不能被重写 　　被final修饰的成员变量，不能被重新赋值（接口中的成员变量默认为public static final修饰的静态常量）
 * String类为final修饰类，不能被继承
 */
public final class ScheduledTask {

  /**
   * volatile用来修饰会被不同线程访问和修改的变量。 JMM（Java内存模型）是围绕并发过程中如何处理可见性、原子性和有序性这3个特征建立起来的，而volatile可以保证其中的两个特性
   *
   * <p>可见性，是指线程之间的可见性，一个线程修改的状态对另一个线程是可见的。也就是一个线程修改的结果。另一个线程马上就能看到。 在 Java 中 volatile、synchronized
   * 和 final 都可以实现可见性。
   *
   * <p>原子性指的是某个线程正在执行某个操作时，中间不可以被加塞或分割，要么整体成功，要么整体失败。 比如 a=0；（a非long和double类型）
   * 这个操作是不可分割的，那么我们说这个操作是原子操作。再比如：a++； 这个操作实际是a = a + 1；是可分割的，
   * 所以他不是一个原子操作。非原子操作都会存在线程安全问题，需要我们使用同步技术（sychronized）来让它变成一个原子操作。一个操作是原子操作， 那么我们称它具有原子性。Java的
   * concurrent 包下提供了一些原子类，AtomicInteger、AtomicLong、AtomicReference等。 在 Java 中 synchronized 和在
   * lock、unlock 中操作保证原子性。
   *
   * <p>Java 语言提供了 volatile 和 synchronized 两个关键字来保证线程之间操作的有序性，volatile 是因为其本身包含“禁止指令重排序”的语义，
   * synchronized 是由“一个变量在同一个时刻只允许一条线程对其进行 lock 操作”这条规则获得的，此规则决定了持有同一个对象锁的两个同步块只能串行执行。
   */
  volatile ScheduledFuture<?> future;

  /** 取消定时任务 */
  public void cancel() {
    ScheduledFuture<?> future = this.future;
    if (future != null) {
      future.cancel(true);
    }
  }
}
