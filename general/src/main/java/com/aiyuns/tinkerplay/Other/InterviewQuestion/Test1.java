package com.aiyuns.tinkerplay.Other.InterviewQuestion;

/**
 * @Author: aiYunS @Date: 2023年5月9日, 0009 下午 2:44:23 @Description: 22 34 17
 */
public class Test1 {
  public static void main(String[] args) {
    System.out.println(new B().getValue());
  }

  static class A {
    protected int value;

    public A(int v) {
      setValue(v);
    }

    public void setValue(int value) {
      this.value = value;
    }

    public int getValue() {
      try {
        value++;
        // 先return,后finally
        return value;
      } catch (Exception e) {
        System.out.println(e.toString());
      } finally {
        // 这个this指的到底是B类,因为现在是在执行B的构造方法
        this.setValue(value);
        System.out.println(value);
      }
      return value;
    }
  }

  static class B extends A {

    public B() {
      super(5); // 1
      setValue(getValue() - 3);
    }

    @Override
    public void setValue(int value) {
      super.setValue(2 * value);
    }
  }
}
