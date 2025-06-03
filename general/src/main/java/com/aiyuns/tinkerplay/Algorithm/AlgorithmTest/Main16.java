package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.*;

/**
 * @Author: aiYunS @Date: 2023年5月11日, 0011 下午 2:37:37 @Description: ab --> ab ba 2 - 2 abc --> abc
 * acb bac bca cab cba 3 - 6 abcd --> abcd abdc adcb adbc acbd acdb bacd badc bcad bcda bdac bdca
 * cabd cadb cbad cbda cdab cdba dabc dacb dbac dbca dcab dcba 4 - 24
 *
 * <p>1 - 1 1 2 - 2 1*2 3 - 6 1*2*3 4 - 24 - 1*2*3*4 n - 1*2*3*4...*n
 */
public class Main16 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    String Content = in.next();
    String Word = in.next();
    System.out.println(a(Content, Word));
  }

  public static int a(String Content, String Word) {
    // 计数
    int count = 0;
    int len = Word.length();
    if (len == 1) {
      if (Content.contains(Word)) {
        return 1;
      }
    } else {
      List<String> list = strUtil(Word);
      for (String s : list) {
        if (Content.contains(s)) {
          count++;
        }
      }
    }
    return count;
  }

  // 递归计算阶乘数
  public static int factorial(int n) {
    if (n == 0 || n == 1) {
      // 0! = 1, 1! = 1
      return 1;
    } else {
      // n! = n * (n-1)!
      return n * factorial(n - 1);
    }
  }

  // 字符串重新排序的情况
  public static List<String> strUtil(String str) {
    List<String> strings = new ArrayList<>();
    if (str == null || str.isEmpty()) {
      strings.add("");
      return strings;
    }
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      List<String> subPermutations = strUtil(str.substring(0, i) + str.substring(i + 1));
      for (String subPermutation : subPermutations) {
        strings.add(c + subPermutation);
      }
    }
    return strings;
  }
}
