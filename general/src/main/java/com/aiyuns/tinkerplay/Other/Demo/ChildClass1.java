package com.aiyuns.tinkerplay.Other.Demo;

/**
 * @Author: aiYunS @Date: 2023年6月30日, 0030 下午 2:07:22 @Description: 研究继承关系的程序执行顺序: 子类
 */
public class ChildClass1 extends ParentClass1 {

  // 子类的方法
  @Override
  public void methmod() {
    System.out.println("子类public修饰的方法执行");
  }
}
