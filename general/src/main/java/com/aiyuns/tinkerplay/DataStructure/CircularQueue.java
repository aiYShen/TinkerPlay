package com.aiyuns.tinkerplay.DataStructure;

/**
 * @Author: aiYunS @Date: 2023年8月15日, 0015 下午 3:37:46 @Description: 基于数组的循环队列实现
 */
public class CircularQueue {
  private int[] queue;
  private int front;
  private int rear;
  private int size;

  public CircularQueue(int capacity) {
    queue = new int[capacity];
    front = 0;
    rear = -1;
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean isFull() {
    return size == queue.length;
  }

  public void enqueue(int item) {
    if (isFull()) {
      System.out.println("Queue is full. Cannot enqueue.");
      return;
    }
    rear = (rear + 1) % queue.length;
    queue[rear] = item;
    size++;
  }

  public int dequeue() {
    if (isEmpty()) {
      System.out.println("Queue is empty. Cannot dequeue.");
      return -1;
    }
    int item = queue[front];
    front = (front + 1) % queue.length;
    size--;
    return item;
  }

  public int peek() {
    if (isEmpty()) {
      System.out.println("Queue is empty. Cannot peek.");
      return -1;
    }
    return queue[front];
  }

  public int size() {
    return size;
  }
}
