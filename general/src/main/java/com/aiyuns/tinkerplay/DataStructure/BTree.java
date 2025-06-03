package com.aiyuns.tinkerplay.DataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: aiYunS @Date: 2023年6月14日, 0014 下午 2:54:24 @Description: BTree +---+ | K | +-+-+ |
 * +--------+--------+--------+ | | | | +-+-+ +-+-+ +-+-+ +-+-+ | K | | K | | K | | K | +-+-+ +-+-+
 * +-+-+ +-+-+ / \ / \ / \ / \ . . . . . . . .
 */
public class BTree {

  private BTreeNode root;
  private int degree;

  public static void main(String[] args) {
    BTree bTree = new BTree(3);

    bTree.insert(10);
    bTree.insert(20);
    bTree.insert(5);
    bTree.insert(15);
    bTree.insert(25);

    // Output: true
    System.out.println(bTree.search(5));
    // Output: false
    System.out.println(bTree.search(50));
  }

  public BTree(int degree) {
    this.root = null;
    this.degree = degree;
  }

  public void insert(int key) {
    if (root == null) {
      root = new BTreeNode();
      root.keys.add(key);
    } else {
      if (root.keys.size() == (2 * degree) - 1) {
        BTreeNode newRoot = new BTreeNode();
        newRoot.children.add(root);
        splitChild(newRoot, 0, root);
        insertNonFull(newRoot, key);
        root = newRoot;
      } else {
        insertNonFull(root, key);
      }
    }
  }

  private void splitChild(BTreeNode parent, int index, BTreeNode child) {
    BTreeNode newNode = new BTreeNode();
    newNode.leaf = child.leaf;

    for (int i = 0; i < degree - 1; i++) {
      newNode.keys.add(child.keys.remove(degree));
    }

    if (!child.leaf) {
      for (int i = 0; i < degree; i++) {
        newNode.children.add(child.children.remove(degree));
      }
    }

    parent.keys.add(index, child.keys.get(degree - 1));
    parent.children.add(index + 1, newNode);
  }

  private void insertNonFull(BTreeNode node, int key) {
    int i = node.keys.size() - 1;

    if (node.leaf) {
      node.keys.add(key);
      while (i >= 0 && key < node.keys.get(i)) {
        node.keys.set(i + 1, node.keys.get(i));
        i--;
      }
      node.keys.set(i + 1, key);
    } else {
      while (i >= 0 && key < node.keys.get(i)) {
        i--;
      }
      i++;
      if (node.children.get(i).keys.size() == (2 * degree) - 1) {
        splitChild(node, i, node.children.get(i));
        if (key > node.keys.get(i)) {
          i++;
        }
      }
      insertNonFull(node.children.get(i), key);
    }
  }

  public boolean search(int key) {
    return search(root, key);
  }

  private boolean search(BTreeNode node, int key) {
    int i = 0;
    while (i < node.keys.size() && key > node.keys.get(i)) {
      i++;
    }
    if (i < node.keys.size() && key == node.keys.get(i)) {
      return true;
    } else if (node.leaf) {
      return false;
    } else {
      return search(node.children.get(i), key);
    }
  }
}

class BTreeNode {
  List<Integer> keys;
  List<BTreeNode> children;
  boolean leaf;

  public BTreeNode() {
    this.keys = new ArrayList<>();
    this.children = new ArrayList<>();
    this.leaf = true;
  }
}
