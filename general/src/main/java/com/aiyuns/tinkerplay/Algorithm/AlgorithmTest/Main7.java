package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 4:47:48 @Description: 字符串&动态规划 一个整数数组 nums
 * ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和
 */
public class Main7 {

  public static int maxSubArray(int[] nums) {
    int maxSum = nums[0];
    int curSum = nums[0];

    for (int i = 1; i < nums.length; i++) {
      curSum = Math.max(nums[i], curSum + nums[i]);
      maxSum = Math.max(maxSum, curSum);
    }

    return maxSum;
  }

  public static void main(String[] args) {
    int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    int maxSum = maxSubArray(nums);
    System.out.println(maxSum);
  }
}
