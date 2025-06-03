package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.Arrays;

/**
 * @Author: aiYunS @Date: 2023年5月16日, 0016 下午 5:20:40 @Description:
 * 一只兔子一次可以跳跃2,3,5,7,9,11个台阶,问n个台阶,兔子最少跳几次?
 */
public class Main19 {

  public static int minimumJumps(int n) {
    int[] dp = new int[n + 1];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;

    int[] steps = {2, 3, 5, 7, 9, 11};

    for (int i = 1; i <= n; i++) {
      for (int step : steps) {
        if (i - step >= 0 && dp[i - step] != Integer.MAX_VALUE) {
          dp[i] = Math.min(dp[i], dp[i - step] + 1);
        }
      }
    }

    return dp[n];
  }

  public static void main(String[] args) {
    int n = 20;
    int minimumJumps = minimumJumps(n);
    System.out.println("兔子跳跃到" + n + "个台阶最少需要跳跃次数：" + minimumJumps);
  }
}
