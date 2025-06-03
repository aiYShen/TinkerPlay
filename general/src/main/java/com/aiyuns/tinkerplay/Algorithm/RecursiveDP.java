package com.aiyuns.tinkerplay.Algorithm;

/**
 * @Author: aiYunS @Date: 2023年5月7日, 0007 上午 9:00:37 @Description: 递归来解决动态规划问题: 计算斐波那契数列的第6项
 */
public class RecursiveDP {

  public static int fib(int n) {
    if (n == 0 || n == 1) {
      return n;
    }
    return fib(n - 1) + fib(n - 2);
  }

  public static void main(String[] args) {
    int n = 6;
    System.out.println(fib(n));
  }
}
