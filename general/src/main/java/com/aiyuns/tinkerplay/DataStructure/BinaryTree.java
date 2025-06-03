package com.aiyuns.tinkerplay.DataStructure;

/**
 * @Author: aiYunS @Date: 2023年5月16日, 0016 下午 3:51:52 @Description: Java模拟二叉树数据结构 5 / \ 3 8 / \ / \
 * 2 4 7 9
 */
public class BinaryTree {
  TreeNode root;

  public static void main(String[] args) {
    BinaryTree binaryTree = new BinaryTree();

    binaryTree.insert(5);
    binaryTree.insert(3);
    binaryTree.insert(7);
    binaryTree.insert(2);
    binaryTree.insert(4);

    System.out.println("Inorder traversal:");
    binaryTree.inorderTraversal();

    System.out.println("\nSearch for 4: " + binaryTree.search(4));
    System.out.println("Search for 6: " + binaryTree.search(6));
  }

  public BinaryTree() {
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

  public boolean search(int val) {
    return searchNode(root, val);
  }

  private boolean searchNode(TreeNode root, int val) {
    if (root == null) {
      return false;
    }

    if (val == root.val) {
      return true;
    } else if (val < root.val) {
      return searchNode(root.left, val);
    } else {
      return searchNode(root.right, val);
    }
  }

  public void inorderTraversal() {
    inorder(root);
  }

  private void inorder(TreeNode root) {
    if (root != null) {
      inorder(root.left);
      System.out.print(root.val + " ");
      inorder(root.right);
    }
  }
}

// 二叉树节点
class TreeNode {
  int val;
  TreeNode left;
  TreeNode right;

  public TreeNode(int val) {
    this.val = val;
    this.left = null;
    this.right = null;
  }
}
