package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 5:16:00 @Description: 双指针 给定一个长度为 n 的整数数组height。有n条垂线，第
 * i 条线的两个端点是(i, 0)和(i, height[i])。
 *
 * <p>找出其中的两条线，使得它们与x轴共同构成的容器可以容纳最多的水。
 *
 * <p>返回容器可以储存的最大水量。
 */
public class Main10 {

  public int maxArea(int[] height) {
    int n = height.length;
    int left = 0, right = n - 1;
    int maxArea = 0;
    while (left < right) {
      int h = Math.min(height[left], height[right]);
      maxArea = Math.max(maxArea, h * (right - left));
      while (left < right && height[left] <= h) {
        left++;
      }
      while (left < right && height[right] <= h) {
        right--;
      }
    }
    return maxArea;
  }

  public static void main(String[] args) {
    Main10 sol = new Main10();
    int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};
    // expected output: 49
    System.out.println(sol.maxArea(height));
  }
}
