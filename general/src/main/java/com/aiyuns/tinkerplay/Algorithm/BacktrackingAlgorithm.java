package com.aiyuns.tinkerplay.Algorithm;

/**
 * @Author: aiYunS @Date: 2023年5月6日, 0006 上午 11:50:28 @Description: 回溯算法;求解0-1背包问题
 * 有一个固定容量的背包和一些物品，每个物品有自己的重量和价值。要求在限定的总重量内，选出若干件物品放入背包中，使得物品的总价值最大。 其中，每种物品只有一件，可以选择放入或不放入。
 */
public class BacktrackingAlgorithm {

  public int knapsack(int[] weight, int[] value, int n, int w) {
    int[][] dp = new int[n][w + 1];

    // 初始化第一行
    for (int j = 0; j <= w; j++) {
      dp[0][j] = j >= weight[0] ? value[0] : 0;
    }

    // 动态规划求解
    for (int i = 1; i < n; i++) {
      for (int j = 0; j <= w; j++) {
        if (j < weight[i]) {
          dp[i][j] = dp[i - 1][j];
        } else {
          dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight[i]] + value[i]);
        }
      }
    }

    return dp[n - 1][w];
  }

  public static void main(String[] args) {
    // 物品重量
    int[] weight = {2, 2, 4, 6, 3};
    // 物品价值
    int[] value = {3, 4, 8, 9, 6};
    // 物品个数
    int n = 5;
    // 背包承重
    int w = 9;
    BacktrackingAlgorithm ks = new BacktrackingAlgorithm();
    int maxV = ks.knapsack(weight, value, n, w);
    // 输出：18
    System.out.println(maxV);
  }
}
