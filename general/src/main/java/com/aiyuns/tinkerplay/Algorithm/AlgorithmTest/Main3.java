package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.Arrays;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 3:14:30 @Description: 贪心算法: n 个孩子站成一排。给你一个整数数组 ratings
 * 表示每个孩子的评分。
 *
 * <p>你需要按照以下要求，给这些孩子分发糖果：
 *
 * <p>每个孩子至少分配到 1 个糖果。 相邻两个孩子评分更高的孩子会获得更多的糖果。 请你给每个孩子分发糖果，计算并返回需要准备的 最少糖果数目
 */
public class Main3 {

  public int candy(int[] ratings) {
    int n = ratings.length;
    int[] left2Right = new int[n];
    int[] right2Left = new int[n];
    Arrays.fill(left2Right, 1);
    Arrays.fill(right2Left, 1);

    // 从左往右扫描，保证右边的孩子评分高的糖果数比左边的孩子多
    for (int i = 1; i < n; i++) {
      if (ratings[i] > ratings[i - 1]) {
        left2Right[i] = left2Right[i - 1] + 1;
      }
    }

    // 从右往左扫描，保证左边的孩子评分高的糖果数比右边的孩子多
    for (int i = n - 2; i >= 0; i--) {
      if (ratings[i] > ratings[i + 1]) {
        right2Left[i] = right2Left[i + 1] + 1;
      }
    }

    int minCandies = 0;
    for (int i = 0; i < n; i++) {
      // 每个孩子的糖果数取左右扫描得到的糖果数的最大值
      minCandies += Math.max(left2Right[i], right2Left[i]);
    }

    return minCandies;
  }

  public static void main(String[] args) {
    Main3 candyDistribution = new Main3();
    int[] ratings = {1, 4, 2, 3};
    int minCandies = candyDistribution.candy(ratings);
    System.out.println("Minimum number of candies required: " + minCandies);
  }
}
