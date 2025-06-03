package com.aiyuns.tinkerplay.Other.Demo;

// 位运算
public class BitwiseOperationsDemo {

  public static void main(String[] args) {
    // 0101
    int a = 5;
    // 0011
    int b = 3;

    // 按位与
    // 0001
    int andResult = a & b;
    System.out.println("a & b = " + andResult);

    // 按位或
    // 0111
    int orResult = a | b;
    System.out.println("a | b = " + orResult);

    // 按位异或
    // 0110
    int xorResult = a ^ b;
    System.out.println("a ^ b = " + xorResult);

    // 按位取反
    // 1010（补码表示为 -6）
    int notResult = ~a;
    System.out.println("~a = " + notResult);

    // 左移
    // 010100
    int leftShiftResult = a << 2;
    System.out.println("a << 2 = " + leftShiftResult);

    // 右移
    // 0010
    int rightShiftResult = a >> 1;
    System.out.println("a >> 1 = " + rightShiftResult);

    // 无符号右移
    int unsignedRightShiftResult = -5 >>> 1;
    System.out.println("-5 >>> 1 = " + unsignedRightShiftResult);
  }
}
