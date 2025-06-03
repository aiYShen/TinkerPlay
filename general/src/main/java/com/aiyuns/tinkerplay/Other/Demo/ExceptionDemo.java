package com.aiyuns.tinkerplay.Other.Demo;

public class ExceptionDemo {

  static int count = 0;

  public static void main(String[] args) throws Exception {
    try {
      System.out.println(count++ + " :try块异常语句执行之前");
      int i = 10 / 0;
      System.out.println(count++ + " :try块异常语句执行之后");
    } catch (Exception e) {
      System.out.println(count++ + " :catch块执行");
    } finally {
      System.out.println(count++ + " :finally块执行");
    }
    throwstest();
  }

  public static void throwstest() throws Exception {
    System.out.println(count++ + " :throws异常语句执行之前");
    int i = 1 / 0;
    System.out.println(count++ + " :throws异常语句执行之后");
  }
}
