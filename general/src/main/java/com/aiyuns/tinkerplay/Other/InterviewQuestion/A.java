package com.aiyuns.tinkerplay.Other.InterviewQuestion;

/**
 * @Author: aiYunS @Date: 2023年5月9日, 0009 下午 4:17:31 @Description: 又又又忘了
 */
public interface A {
  /*
     接口没有构造方法,所以不能用于实例化对象
     接口中所有的方法必须是抽象方法，Java 8 之后 接口中可以使用 default 关键字修饰的非抽象方法。
     接口不能包含成员变量，除了 static 和 final 变量。
     接口不是被类继承了，而是要被类实现。
     接口支持多继承。
  */
  static int a = 0;
  final int b = 1;

  // 抽象方法
  abstract void methomd1();

  // default修饰的非抽象方法
  default void methmod2() {}

  // 静态方法包含方法体
  static void methmod3() {}
  ;
}

// 一个接口可以继承另一个接口,但是不可以实现另一个接口
interface B extends A {}

// 接口可以继承其他多个接口
interface C extends A, B {}
