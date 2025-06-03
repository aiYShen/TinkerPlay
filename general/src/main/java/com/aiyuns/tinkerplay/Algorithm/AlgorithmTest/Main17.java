package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: aiYunS @Date: 2023年5月12日, 0012 上午 10:02:47 @Description: 字符串字符重新排序
 */
public class Main17 {
  public static List<String> getAllPermutations(String input) {
    List<String> result = new ArrayList<>();
    if (input == null || input.isEmpty()) {
      result.add("");
      return result;
    }

    char firstChar = input.charAt(0);
    String remainingString = input.substring(1);

    List<String> permutations = getAllPermutations(remainingString);
    for (String permutation : permutations) {
      for (int i = 0; i <= permutation.length(); i++) {
        String newPermutation = insertCharAt(permutation, firstChar, i);
        result.add(newPermutation);
      }
    }

    return result;
  }

  public static String insertCharAt(String str, char ch, int index) {
    String start = str.substring(0, index);
    String end = str.substring(index);
    return start + ch + end;
  }

  public static void main(String[] args) {
    System.out.println(
        "共" + getAllPermutations("abcd").size() + "种情况,分别为: " + getAllPermutations("abcd"));
  }
}
