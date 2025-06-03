package com.aiyuns.tinkerplay.Other.InterviewQuestion;

/**
 * @Author: aiYunS @Date: 2023年5月9日, 0009 下午 3:43:05 @Description:
 * 父和子静态代码块-->父非静态代码块、父构造方法-->子非静态代码块、子构造方法-->父公有方法
 */
public class Test3 {
  public static void main(String[] args) {
    // 创建子类对象
    Zi zi = new Zi();
    // 子类对象调用父类公有方法
    zi.Fathers();
  }
}

class Fu {
  public Fu() {
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
}

class Zi extends Fu {
  public Zi() {
    System.out.println("子类无参构造");
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
}
