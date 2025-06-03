package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 2:58:24 @Description: 贪心算法:
 * 按照国际象棋的规则，皇后可以攻击与之处在同一行或同一列或同一斜线上的棋子。
 *
 * <p>n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 *
 * <p>给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案
 */
public class Main2 {

  private List<List<String>> solutions;
  private int[] queens;
  private int n;

  public static void main(String[] args) {
    Main2 main2 = new Main2();
    int n = 4;
    List<List<String>> solutions = main2.solveNQueens(n);
    System.out.println("Number of solutions for " + n + "-queens problem: " + solutions.size());
    for (List<String> solution : solutions) {
      System.out.println("Solution:");
      for (String row : solution) {
        System.out.println(row);
      }
    }
  }

  public List<List<String>> solveNQueens(int n) {
    solutions = new ArrayList<>();
    queens = new int[n];
    this.n = n;
    backtrack(0);
    return solutions;
  }

  private void backtrack(int row) {
    if (row == n) {
      List<String> solution = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < n; j++) {
          if (queens[i] == j) {
            sb.append("Q");
          } else {
            sb.append(".");
          }
        }
        solution.add(sb.toString());
      }
      solutions.add(solution);
    } else {
      for (int i = 0; i < n; i++) {
        if (isValid(row, i)) {
          queens[row] = i;
          backtrack(row + 1);
        }
      }
    }
  }

  private boolean isValid(int row, int col) {
    for (int i = 0; i < row; i++) {
      if (queens[i] == col || row - i == Math.abs(col - queens[i])) {
        return false;
      }
    }
    return true;
  }
}
