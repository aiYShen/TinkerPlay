package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 5:51:24 @Description: 正则表达式&排序&动态规划
 * 给你一个字符串s和一个字符规律p,请你来实现一个支持'.'和'*'的正则表达式
 */
public class Main14 {
  public static boolean isMatch(String s, String p) {
    int m = s.length(), n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    dp[0][0] = true;
    for (int i = 0; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        if (p.charAt(j - 1) == '*') {
          dp[i][j] = dp[i][j - 2];
          if (match(s, p, i, j - 1)) {
            dp[i][j] = dp[i][j] || dp[i - 1][j];
          }
        } else {
          if (match(s, p, i, j)) {
            dp[i][j] = dp[i - 1][j - 1];
          }
        }
      }
    }
    return dp[m][n];
  }

  public static boolean match(String s, String p, int i, int j) {
    if (i == 0) {
      return false;
    }
    if (p.charAt(j - 1) == '.') {
      return true;
    }
    return s.charAt(i - 1) == p.charAt(j - 1);
  }

  public static void main(String[] args) {
    String s1 = "aa", p1 = "a";
    String s2 = "aa", p2 = "a*";
    String s3 = "ab", p3 = ".*";
    String s4 = "aab", p4 = "c*a*b";
    String s5 = "mississippi", p5 = "mis*is*p*.";
    // false
    System.out.println(isMatch(s1, p1));
    // true
    System.out.println(isMatch(s2, p2));
    // true
    System.out.println(isMatch(s3, p3));
    // true
    System.out.println(isMatch(s4, p4));
    // false
    System.out.println(isMatch(s5, p5));
  }
}
