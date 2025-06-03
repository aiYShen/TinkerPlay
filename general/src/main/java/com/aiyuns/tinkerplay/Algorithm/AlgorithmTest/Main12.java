package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 5:39:31 @Description: 枚举
 * 如果一个正整数自身是回文数，而且它也是一个回文数的平方，那么我们称这个数为超级回文数。 现在，给定两个正整数L和R （以字符串形式表示），返回包含在范围 [L, R] 中的超级回文数的数目。
 */
public class Main12 {
  public int superpalindromesInRange(String left, String right) {
    long L = Long.valueOf(left);
    long R = Long.valueOf(right);
    int MAGIC = 100000;
    int ans = 0;

    // Count odd length palindromic numbers
    for (int k = 1; k < MAGIC; ++k) {
      StringBuilder sb = new StringBuilder(Integer.toString(k));
      for (int i = sb.length() - 2; i >= 0; --i) {
        sb.append(sb.charAt(i));
      }
      long v = Long.valueOf(sb.toString());
      v *= v;
      if (v > R) {
        break;
      }
      if (v >= L && isPalindrome(v)) {
        ans++;
      }
    }

    // Count even length palindromic numbers
    for (int k = 1; k < MAGIC; ++k) {
      StringBuilder sb = new StringBuilder(Integer.toString(k));
      for (int i = sb.length() - 1; i >= 0; --i) {
        sb.append(sb.charAt(i));
      }
      long v = Long.valueOf(sb.toString());
      v *= v;
      if (v > R) {
        break;
      }
      if (v >= L && isPalindrome(v)) {
        ans++;
      }
    }

    return ans;
  }

  public boolean isPalindrome(long x) {
    return x == Long.parseLong(new StringBuilder(String.valueOf(x)).reverse().toString());
  }

  public static void main(String[] args) {
    Main12 main12 = new Main12();
    System.out.println(main12.superpalindromesInRange("4", "1000"));
  }
}
