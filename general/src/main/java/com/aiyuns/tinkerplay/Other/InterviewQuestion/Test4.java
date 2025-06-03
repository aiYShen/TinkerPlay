package com.aiyuns.tinkerplay.Other.InterviewQuestion;

/**
 * @Author: aiYunS @Date: 2023年5月9日, 0009 下午 3:59:38 @Description:
 */
public class Test4 {
  public static void main(String[] args) {
    // 父类和子类静态代码块-->父类非静态代码块和构造方法-->子类非静态代码块和构造方法
    F x = new Z();
    System.out.println("=======");
    // 子类普通方法
    x.Methmod();
    System.out.println("=======");
    // 父类非静态代码块-->父类构造方法
    F xx = new F();
    System.out.println("=======");
    // 父类普通方法
    xx.Methmod();
  }
}

class F {
  public F() {
    System.out.println("父类构造方法");
  }

  static {
    System.out.println("父类静态代码块，字节码文件加载即执行，只执行一次");
  }

  public void Fathers() {
    System.out.println("父类公有方法");
  }

  {
    System.out.println("父类非静态代码块");
  }

  public static void FuMethmod() {
    System.out.println("父类静态方法");
  }

  public void Methmod() {
    System.out.println("父类普通方法");
  }
}

class Z extends F {
  public Z() {
    System.out.println("子类构造方法");
  }

  static {
    System.out.println("子类静态代码块，字节码文件加载即执行，且执行一次");
  }

  {
    System.out.println("子类非静态代码块");
  }

  public void Zition() {
    System.out.println("子类非静态代码块");
  }

  public static void ZiMethmod() {
    System.out.println("子类静态方法");
  }

  @Override
  public void Methmod() {
    System.out.println("子类普通方法");
  }
}
