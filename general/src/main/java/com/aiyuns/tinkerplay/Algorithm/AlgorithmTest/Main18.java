package com.aiyuns.tinkerplay.Algorithm.AlgorithmTest;

/**
 * @Author: aiYunS @Date: 2023年5月16日, 0016 下午 3:48:58 @Description: Java打印二叉树数据结构 F / \ B G / \ \ A
 * D I / \ / C E H
 */
public class Main18 {

  public static void main(String[] args) {
    BinaryTree2 binaryTree2 = new BinaryTree2();

    binaryTree2.insert(5);
    binaryTree2.insert(3);
    binaryTree2.insert(7);
    binaryTree2.insert(2);
    binaryTree2.insert(4);

    // 中序遍历
    binaryTree2.printTreeInorder();
    // 前序遍历
    binaryTree2.printTreePreorder();
    // 后序遍历
    binaryTree2.printTreePostorder();
  }
}

class BinaryTree2 {
  TreeNode root;

  public BinaryTree2() {
    this.root = null;
  }

  public void insert(int val) {
    root = insertNode(root, val);
  }

  private TreeNode insertNode(TreeNode root, int val) {
    if (root == null) {
      return new TreeNode(val);
    }

    if (val < root.val) {
      root.left = insertNode(root.left, val);
    } else if (val > root.val) {
      root.right = insertNode(root.right, val);
    }

    return root;
  }

  public void printTreeInorder() {
    System.out.print("Inorder traversal: ");
    inorder(root);
    System.out.println();
  }

  private void inorder(TreeNode root) {
    if (root != null) {
      inorder(root.left);
      System.out.print(root.val + " ");
      inorder(root.right);
    }
  }

  public void printTreePreorder() {
    System.out.print("Preorder traversal: ");
    preorder(root);
    System.out.println();
  }

  private void preorder(TreeNode root) {
    if (root != null) {
      System.out.print(root.val + " ");
      preorder(root.left);
      preorder(root.right);
    }
  }

  public void printTreePostorder() {
    System.out.print("Postorder traversal: ");
    postorder(root);
    System.out.println();
  }

  private void postorder(TreeNode root) {
    if (root != null) {
      postorder(root.left);
      postorder(root.right);
      System.out.print(root.val + " ");
    }
  }
}

// 二叉树节点
class TreeNode {
  // 节点值
  int val;
  // 左子节点
  TreeNode left;
  // 右子节点
  TreeNode right;

  public TreeNode(int val) {
    this.val = val;
    this.left = null;
    this.right = null;
  }
}
