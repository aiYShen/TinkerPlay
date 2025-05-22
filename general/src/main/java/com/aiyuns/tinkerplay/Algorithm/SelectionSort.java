package com.aiyuns.tinkerplay.Algorithm;

import java.util.Arrays;

public class SelectionSort {

    public void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j; // 记录最小元素索引
                }
            }
            // 交换
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        SelectionSort sorter = new SelectionSort();
        sorter.selectionSort(arr);
        System.out.println("选择排序结果: " + Arrays.toString(arr));
    }
}
