package com.aiyuns.tinkerplay.Algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author: aiYunS @Date: 2023年5月7日, 0007 上午 9:16:50 @Description: 深度搜索 广度搜索 二叉树
 */
class Graph2 {

  private int V;
  private LinkedList<Integer>[] adj;

  public Graph2(int v) {
    V = v;
    adj = new LinkedList[v];
    for (int i = 0; i < v; ++i) {
      adj[i] = new LinkedList<Integer>();
    }
  }

  public void addEdge(int v, int w) {
    adj[v].add(w);
  }

  // 深度优先搜索（DFS）
  public void DFS(int v) {
    boolean[] visited = new boolean[V];
    DFSUtil(v, visited);
  }

  private void DFSUtil(int v, boolean[] visited) {
    visited[v] = true;
    System.out.print(v + " ");
    Iterator<Integer> i = adj[v].listIterator();
    while (i.hasNext()) {
      int n = i.next();
      if (!visited[n]) {
        DFSUtil(n, visited);
      }
    }
  }

  // 广度优先搜索（BFS）
  public void BFS(int s) {
    boolean[] visited = new boolean[V];
    LinkedList<Integer> queue = new LinkedList<Integer>();
    visited[s] = true;
    queue.add(s);
    while (queue.size() != 0) {
      s = queue.poll();
      System.out.print(s + " ");
      Iterator<Integer> i = adj[s].listIterator();
      while (i.hasNext()) {
        int n = i.next();
        if (!visited[n]) {
          visited[n] = true;
          queue.add(n);
        }
      }
    }
  }
}

class TreeNode {
  int val;
  TreeNode left;
  TreeNode right;

  TreeNode(int x) {
    val = x;
  }
}

// 二叉树
class BinaryTree {
  public static void main(String[] args) {
    TreeNode root = new TreeNode(1);
    root.left = new TreeNode(2);
    root.right = new TreeNode(3);
    root.left.left = new TreeNode(4);
    root.left.right = new TreeNode(5);

    System.out.println("Depth First Traversal (DFS):");
    DFS(root);
    System.out.println("\nBreadth First Traversal (BFS):");
    BFS(root);
  }

  // 深度优先搜索(DFS)
  public static void DFS(TreeNode root) {
    if (root == null) {
      return;
    }
    System.out.print(root.val + " ");
    DFS(root.left);
    DFS(root.right);
  }

  // 广度优先搜索(BFS)
  public static void BFS(TreeNode root) {
    if (root == null) {
      return;
    }
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      TreeNode node = queue.poll();
      System.out.print(node.val + " ");
      if (node.left != null) {
        queue.add(node.left);
      }
      if (node.right != null) {
        queue.add(node.right);
      }
    }
  }
}
