package com.aiyuns.tinkerplay.Algorithm;

/**
 * @Author: aiYunS @Date: 2022-11-8 上午 10:29 @Description: 冒泡排序
 */
public class BubbleSort {

  public static int[] bubbleSort(int[] arr) {
    if (arr.length < 2 || arr == null) {
      return arr;
    }
    // 记录最后一次交换的位置
    int lastExchangeIndex = 0;
    // 无序数列的边界,每次比较只需要比到这里为止
    int sortBorder = arr.length - 1;
    for (int i = 0; i < arr.length - 1; i++) {
      // 倘若为有序数组,默认标记为true
      boolean isSorted = true;
      for (int j = 0; j < sortBorder; j++) {
        if (arr[j + 1] < arr[j]) {
          // 有元素交换,不是有序的数组,标记为false
          isSorted = false;
          int t = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = t;
          lastExchangeIndex = j;
        }
        System.out.println("交换元素的下标为: " + lastExchangeIndex);
        // 每一次排序后都瞅一眼
        for (int k = 0; k < arr.length; k++) {
          if (k == arr.length - 1) {
            System.out.println("arr[" + k + "] = " + arr[k]);
          } else {
            System.out.print("arr[" + k + "] = " + arr[k] + " ,");
          }
        }
      }
      sortBorder = lastExchangeIndex;
      // 如若没有发生交换,则直接跳出大循环
      if (isSorted) {
        break;
      }
    }
    return arr;
  }

  public static void main(String[] args) {
    int[] a = new int[] {5, 1, 3, 0, 2, 4};
    bubbleSort(a);
    System.out.println("============= 最后结果 =============");
    for (int k = 0; k < a.length; k++) {
      if (k == a.length - 1) {
        System.out.println("arr[" + k + "] = " + a[k]);
      } else {
        System.out.print("arr[" + k + "] = " + a[k] + " ,");
      }
    }
    System.out.println("==================================");
  }
}
