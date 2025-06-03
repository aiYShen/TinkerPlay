package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 5:11:41 @Description: 回溯算法
 * 给定一个仅包含数字2-9的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
 *
 * <p>给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母
 */
public class Main9 {
  public List<String> letterCombinations(String digits) {
    List<String> res = new ArrayList<>();
    if (digits == null || digits.length() == 0) {
      return res;
    }
    Map<Character, String> map = new HashMap<>();
    map.put('2', "abc");
    map.put('3', "def");
    map.put('4', "ghi");
    map.put('5', "jkl");
    map.put('6', "mno");
    map.put('7', "pqrs");
    map.put('8', "tuv");
    map.put('9', "wxyz");
    backtrack(digits, map, new StringBuilder(), res, 0);
    return res;
  }

  private void backtrack(
      String digits, Map<Character, String> map, StringBuilder sb, List<String> res, int index) {
    if (index == digits.length()) {
      res.add(sb.toString());
      return;
    }
    char digit = digits.charAt(index);
    String letters = map.get(digit);
    for (int i = 0; i < letters.length(); i++) {
      sb.append(letters.charAt(i));
      backtrack(digits, map, sb, res, index + 1);
      sb.deleteCharAt(sb.length() - 1);
    }
  }

  public static void main(String[] args) {
    Main9 solution = new Main9();
    List<String> res = solution.letterCombinations("23");
    System.out.println(res);
  }
}
