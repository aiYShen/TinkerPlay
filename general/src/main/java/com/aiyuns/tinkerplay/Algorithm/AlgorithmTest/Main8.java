package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.*;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 4:51:54 @Description: 哈希&排序 给你一个字符串数组，请你将 字母异位词
 * 组合在一起。可以按任意顺序返回结果列表。
 *
 * <p>字母异位词 是由重新排列源单词的字母得到的一个新单词，所有源单词中的字母通常恰好只用一次。
 */
public class Main8 {
  public static List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> map = new HashMap<>();
    for (String str : strs) {
      char[] chars = str.toCharArray();
      Arrays.sort(chars);
      String sortedStr = new String(chars);
      List<String> anagramsList = map.getOrDefault(sortedStr, new ArrayList<>());
      anagramsList.add(str);
      map.put(sortedStr, anagramsList);
    }
    return new ArrayList<>(map.values());
  }

  public static void main(String[] args) {
    String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
    List<List<String>> result = groupAnagrams(strs);
    System.out.println(result);
  }
}
