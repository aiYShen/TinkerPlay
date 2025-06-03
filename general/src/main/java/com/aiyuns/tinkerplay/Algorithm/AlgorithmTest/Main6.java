package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 4:33:58 @Description: 二维数组: 杨辉三角
 */
public class Main6 {

  public static void main(String[] args) {
    int numRows = 5;
    List<List<Integer>> triangle = generate(numRows);
    for (List<Integer> row : triangle) {
      System.out.println(row);
    }
  }

  public static List<List<Integer>> generate(int numRows) {
    List<List<Integer>> triangle = new ArrayList<>();

    // 特判 numRows 为 0 的情况
    if (numRows == 0) {
      return triangle;
    }

    // 第一行总是只有一个元素
    triangle.add(new ArrayList<>());
    triangle.get(0).add(1);

    for (int i = 1; i < numRows; i++) {
      List<Integer> row = new ArrayList<>();
      List<Integer> preRow = triangle.get(i - 1);

      // 每一行的第一个元素总是 1
      row.add(1);

      for (int j = 1; j < i; j++) {
        row.add(preRow.get(j - 1) + preRow.get(j));
      }

      // 每一行的最后一个元素总是 1
      row.add(1);

      triangle.add(row);
    }

    return triangle;
  }
}
