package com.aiyuns.tinkerplay.Algorithm;

import java.util.*;

/**
 * @Author: aiYunS @Date: 2023年5月7日, 0007 上午 9:31:49 @Description: 使用哈希表和排序来对一个整数数组进行排序
 */
public class HashSort {

  public static void main(String[] args) {
    int[] arr = {5, 3, 8, 4, 2};
    System.out.println("Before sorting: " + Arrays.toString(arr));
    hashSort(arr);
    System.out.println("After sorting: " + Arrays.toString(arr));
  }

  public static void hashSort(int[] arr) {
    Map<Integer, Integer> map = new HashMap<>();
    List<Integer> list = new ArrayList<>();

    for (int num : arr) {
      map.put(num, map.getOrDefault(num, 0) + 1);
    }

    for (int key : map.keySet()) {
      int count = map.get(key);
      for (int i = 0; i < count; i++) {
        list.add(key);
      }
    }

    Collections.sort(list);

    for (int i = 0; i < arr.length; i++) {
      arr[i] = list.get(i);
    }
  }
}
