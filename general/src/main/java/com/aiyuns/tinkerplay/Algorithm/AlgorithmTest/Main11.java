package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 5:22:55 @Description: 栈&矩阵 给定一个仅包含 0 和 1 、大小为 rows x
 * cols 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积
 */
public class Main11 {

  public int maximalRectangle(char[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return 0;
    }
    int m = matrix.length, n = matrix[0].length;
    int[] heights = new int[n];
    int maxArea = 0;
    for (int i = 0; i < m; i++) {
      // 更新 heights 数组
      for (int j = 0; j < n; j++) {
        if (matrix[i][j] == '1') {
          heights[j]++;
        } else {
          heights[j] = 0;
        }
      }
      // 计算最大矩形面积
      maxArea = Math.max(maxArea, largestRectangleArea(heights));
    }
    return maxArea;
  }

  private int largestRectangleArea(int[] heights) {
    if (heights == null || heights.length == 0) {
      return 0;
    }
    int n = heights.length;
    int[] left = new int[n];
    int[] right = new int[n];
    left[0] = -1;
    right[n - 1] = n;
    int maxArea = 0;
    for (int i = 1; i < n; i++) {
      int p = i - 1;
      while (p >= 0 && heights[p] >= heights[i]) {
        p = left[p];
      }
      left[i] = p;
    }
    for (int i = n - 2; i >= 0; i--) {
      int p = i + 1;
      while (p < n && heights[p] >= heights[i]) {
        p = right[p];
      }
      right[i] = p;
    }
    for (int i = 0; i < n; i++) {
      int area = heights[i] * (right[i] - left[i] - 1);
      maxArea = Math.max(maxArea, area);
    }
    return maxArea;
  }

  public static void main(String[] args) {
    char[][] matrix = {
      {'1', '0', '1', '0', '0'},
      {'1', '0', '1', '1', '1'},
      {'1', '1', '1', '1', '1'},
      {'1', '0', '0', '1', '0'}
    };
    Main11 main11 = new Main11();
    System.out.println(main11.maximalRectangle(matrix));
  }
}
