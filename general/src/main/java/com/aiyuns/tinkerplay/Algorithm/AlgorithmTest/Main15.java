package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月10日, 0010 下午 5:13:14 @Description: 最长公共前缀
 */
public class Main15 {
  public static String a(String[] strs) {
    if (strs == null || strs.length == 0) {
      return "";
    }
    String prefix = strs[0];
    for (int i = 1; i < strs.length; i++) {
      while (strs[i].indexOf(prefix) != 0) {
        prefix = prefix.substring(0, prefix.length() - 1);
        if (prefix.isEmpty()) {
          return "";
        }
      }
    }
    return prefix;
  }

  public static void main(String[] args) {
    String[] a = {"flower", "flow", "flight"};
    System.out.println(a(a));
  }
}
