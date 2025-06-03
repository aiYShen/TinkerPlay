package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月7日, 0007 上午 8:49:54 @Description: 字符串动态规划来解决最长公共子序列问题
 * 最长公共子序列（Longest Common Subsequence，LCS）是指在两个字符串中找到最长的子序列（可以不连续），使得它在两个原始字符串中都出现
 */
public class Main5 {

  public static int longestCommonSubsequence(String str1, String str2) {
    int m = str1.length(), n = str2.length();
    int[][] dp = new int[m + 1][n + 1];

    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        // 返回指定索引位置处的字符
        if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
          dp[i][j] = dp[i - 1][j - 1] + 1;
        } else {
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }
    for (int i = 0; i < dp.length; i++) {
      for (int j = 0; j < dp[i].length; j++) {
        System.out.print(dp[i][j] + " ");
      }
      System.out.println();
    }
    return dp[m][n];
  }

  public static void main(String[] args) {
    String str1 = "abcde";
    String str2 = "ace";
    System.out.println(longestCommonSubsequence(str1, str2));
  }
}
