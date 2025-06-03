package com.aiyuns.tinkerplay.DataStructure;

/**
 * @Author: aiYunS @Date: 2023年8月15日, 0015 下午 3:38:19 @Description: 基于链表的队列实现
 */
public class LinkedQueue {
  private class Node {
    int data;
    Node next;

    public Node(int data) {
      this.data = data;
      this.next = null;
    }
  }

  private Node front;
  private Node rear;
  private int size;

  public LinkedQueue() {
    front = null;
    rear = null;
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void enqueue(int item) {
    Node newNode = new Node(item);
    if (isEmpty()) {
      front = newNode;
      rear = newNode;
    } else {
      rear.next = newNode;
      rear = newNode;
    }
    size++;
  }

  public int dequeue() {
    if (isEmpty()) {
      System.out.println("Queue is empty. Cannot dequeue.");
      return -1;
    }
    int item = front.data;
    front = front.next;
    size--;
    return item;
  }

  public int peek() {
    if (isEmpty()) {
      System.out.println("Queue is empty. Cannot peek.");
      return -1;
    }
    return front.data;
  }

  public int size() {
    return size;
  }
}
