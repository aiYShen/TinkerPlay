package com.aiyuns.tinkerplay.Other.InterviewQuestion;

/**
 * @Author: aiYunS @Date: 2023年5月9日, 0009 下午 3:36:56 @Description: 6 6
 */
public class Test2 {
  public static void main(String[] args) {
    System.out.println(new A(5).getValue());
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
        this.setValue(value);
        System.out.println(value);
      }
      return value;
    }
  }

  static class B extends Test1.A {

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
