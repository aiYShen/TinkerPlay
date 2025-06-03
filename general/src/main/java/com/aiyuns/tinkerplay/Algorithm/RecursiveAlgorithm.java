package com.aiyuns.tinkerplay.Algorithm;

import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * @Author: aiYunS @Date: 2022-10-8 上午 11:00 @Description: 递归算法及案例
 */
public class RecursiveAlgorithm {

  static TreeSet<String> filePath = new TreeSet<>();

  // 案例一:递归累计求和,n的累加和 = n + (n-1)
  public static int getNum(int num) {
    if (num == 1) {
      return 1;
    }
    return num + getNum(num - 1);
  }

  /**
   * @Author: aiYunS @Description: 递归计算 n 的阶乘 n! = n * (n-1) 1! = 1 2! = 1 * 2 3! = 1 * 2 * 3 4！ = 1
   * * 2 * 3 * 4 @Date: 2022-10-8 上午 11:15 @Param: int
   *
   * @return: int
   */
  public static int factorial(int n) {
    if (n == 1) {
      return 1;
    }
    return n * factorial(n - 1);
  }

  /**
   * @Author: aiYunS @Description: 文件递归搜索 @Date: 2022-10-8 上午 11:24 @Param: searchContent
   *
   * @param file
   * @return: java.util.ArrayList<java.lang.String>
   */
  public static TreeSet<String> fileSearch(File file, String searchContent) {
    File[] files = file.listFiles();
    if (files == null) {
      return null;
    }
    for (File file0 : files) {
      if (file0.isDirectory()) {
        fileSearch(file0, searchContent);
      } else if (file0.isFile() && file0.getName().contains(searchContent)) {
        filePath.add(file0.getAbsolutePath());
      }
    }
    return filePath;
  }

  // 判断是否清空全局变量
  public static void clean(String searchContent) {
    int i = 0;
    if (filePath != null && !filePath.isEmpty()) {
      for (Iterator iter = filePath.iterator(); iter.hasNext(); ) {
        if (iter.next().toString().contains(searchContent)) {
          i++;
        }
      }
      if (filePath.size() != i) {
        filePath.clear();
      }
    }
  }
}
