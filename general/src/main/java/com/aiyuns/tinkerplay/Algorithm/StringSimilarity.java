package com.aiyuns.tinkerplay.Algorithm;

/**
 * @Author: aiYunS @Date: 2023年11月24日, 0024 上午 10:46:43 @Description: 判断字符串的相似程度算法
 */
public class StringSimilarity {
  public static int calculateLevenshteinDistance(String str1, String str2) {
    int[][] distance = new int[str1.length() + 1][str2.length() + 1];

    for (int i = 0; i <= str1.length(); i++) {
      for (int j = 0; j <= str2.length(); j++) {
        if (i == 0) {
          distance[i][j] = j;
        } else if (j == 0) {
          distance[i][j] = i;
        } else {
          distance[i][j] =
              min(
                  distance[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1),
                  distance[i - 1][j] + 1,
                  distance[i][j - 1] + 1);
        }
      }
    }

    return distance[str1.length()][str2.length()];
  }

  private static int min(int a, int b, int c) {
    return Math.min(Math.min(a, b), c);
  }

  public static void main(String[] args) {
    String str1 = "hello";
    String str2 = "hola";
    int distance = calculateLevenshteinDistance(str1, str2);

    System.out.println("Levenshtein Distance: " + distance);
  }
}
