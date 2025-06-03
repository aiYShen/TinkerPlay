package com.aiyuns.tinkerplay.Algorithm;

/**
 * @Author: aiYunS @Date: 2022-10-11 下午 02:43 @Description: 二分查找(非递归方式)
 */
public class BinarySearchNonRecursive {

  public static void main(String[] args) {
    int[] arr = {1, 3, 8, 10, 11, 67, 100};
    int index = binarySearch(arr, 1);
    if (index != -1) {
      System.out.println("找到了，下标为：" + index);
    } else {
      System.out.println("没有找到--");
    }
  }

  private static int binarySearch(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;
    while (left <= right) {
      int mid = (left + right) / 2;
      if (arr[mid] == target) {
        return mid;
      } else if (arr[mid] > target) {
        // 向左找
        right = mid - 1;
      } else {
        // 向右找
        left = mid + 1;
      }
    }
    return -1;
  }
}
