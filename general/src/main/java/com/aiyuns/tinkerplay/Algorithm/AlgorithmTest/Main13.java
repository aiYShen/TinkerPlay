package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 5:46:27 @Description: 模拟 给你一个 m 行 n 列的矩阵 matrix ，请按照
 * 顺时针螺旋顺序 ，返回矩阵中的所有元素
 */
public class Main13 {
  public static List<Integer> spiralOrder(int[][] matrix) {
    List<Integer> res = new ArrayList<>();
    int m = matrix.length;
    int n = matrix[0].length;
    int left = 0, right = n - 1, top = 0, bottom = m - 1;

    while (left <= right && top <= bottom) {
      // Traverse right
      for (int j = left; j <= right; j++) {
        res.add(matrix[top][j]);
      }
      // Traverse down
      for (int i = top + 1; i <= bottom; i++) {
        res.add(matrix[i][right]);
      }
      // Traverse left and up only if the rows and cols are not the same
      if (left < right && top < bottom) {
        // Traverse left
        for (int j = right - 1; j >= left; j--) {
          res.add(matrix[bottom][j]);
        }
        // Traverse up
        for (int i = bottom - 1; i > top; i--) {
          res.add(matrix[i][left]);
        }
      }
      // Update pointers for next iteration
      left++;
      right--;
      top++;
      bottom--;
    }

    return res;
  }

  public static void main(String[] args) {
    int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    List<Integer> res = spiralOrder(matrix);
    System.out.println(res);
  }
}
