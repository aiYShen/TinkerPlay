package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

import java.util.PriorityQueue;

/**
 * @Author: aiYunS @Date: 2023年5月8日, 0008 下午 3:24:00 @Description: 优先队列: 给定整数数组 nums 和整数 k，请返回数组中第 k
 * 个最大的元素。
 *
 * <p>请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 *
 * <p>你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。
 */
public class Main4 {

  public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    // 将前k个元素添加到小根堆中
    for (int i = 0; i < k; i++) {
      minHeap.offer(nums[i]);
    }

    // 对于剩下的元素，如果大于小根堆堆顶，则删除堆顶并将该元素添加到小根堆中
    for (int i = k; i < nums.length; i++) {
      // peek()：获取队列中权值最小的元素，但是不删除该元素
      if (nums[i] > minHeap.peek()) {
        // poll()：获取并删除队列中权值最小的元素
        minHeap.poll();
        // add(E e) 或 offer(E e)：将元素添加到队列中
        minHeap.offer(nums[i]);
      }
    }

    // 最终小根堆堆顶即为第k大元素
    return minHeap.peek();
  }

  public static void main(String[] args) {
    Main4 main4 = new Main4();
    int[] nums = {3, 2, 3, 1, 2, 4, 5, 5, 6};
    int k = 4;
    int kthLargestNum = main4.findKthLargest(nums, k);
    System.out.println("The " + k + "th largest number in the array is: " + kthLargestNum);
  }
}
