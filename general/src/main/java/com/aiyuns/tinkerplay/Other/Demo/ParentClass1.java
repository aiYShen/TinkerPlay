package com.aiyuns.tinkerplay.Other.Demo;

/**
 * @Author: aiYunS @Date: 2023年6月30日, 0030 上午 11:23:52 @Description: 研究继承关系的程序执行顺序: 父类
 */
public class ParentClass1 implements ParentInterface1 {

  // 父类实现接口方法,并定义为final
  @Override
  public final void parentImplementsMethmod() {
    System.out.println("父类的实现接口的方法执行!");
    System.out.println("调用methmod方法开始!");
    methmod();
    System.out.println("调用methmod方法结束!");
  }

  // 父类受保护的方法
  protected void methmod() {
    System.out.println("父类protected修饰的方法执行");
  }
}
