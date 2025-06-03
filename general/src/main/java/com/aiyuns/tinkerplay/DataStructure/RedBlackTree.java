package com.aiyuns.tinkerplay.DataStructure;

/**
 * @Author: aiYunS @Date: 2023年5月16日, 0016 下午 4:31:53 @Description: Java模拟红黑树数据结构 13 (黑) / \ 8 (红)
 * 17 (红) / \ / \ 1 (黑) 11 (黑) 15 (黑) 25 (黑) / / \ 9 (红) 14 (红) 20 (红)
 */
enum Color {
  RED,
  BLACK;
}

class Node {
  int val;
  Color color;
  Node left;
  Node right;
  Node parent;

  public Node(int val) {
    this.val = val;
    this.color = Color.RED;
    this.left = null;
    this.right = null;
    this.parent = null;
  }
}

public class RedBlackTree {
  private Node root;
  private Node NIL;

  public RedBlackTree() {
    NIL = new Node(0);
    NIL.color = Color.BLACK;
    root = NIL;
  }

  private void leftRotate(Node node) {
    Node rightChild = node.right;
    node.right = rightChild.left;

    if (rightChild.left != NIL) {
      rightChild.left.parent = node;
    }

    rightChild.parent = node.parent;

    if (node.parent == NIL) {
      root = rightChild;
    } else if (node == node.parent.left) {
      node.parent.left = rightChild;
    } else {
      node.parent.right = rightChild;
    }

    rightChild.left = node;
    node.parent = rightChild;
  }

  private void rightRotate(Node node) {
    Node leftChild = node.left;
    node.left = leftChild.right;

    if (leftChild.right != NIL) {
      leftChild.right.parent = node;
    }

    leftChild.parent = node.parent;

    if (node.parent == NIL) {
      root = leftChild;
    } else if (node == node.parent.right) {
      node.parent.right = leftChild;
    } else {
      node.parent.left = leftChild;
    }

    leftChild.right = node;
    node.parent = leftChild;
  }

  private void insertFixup(Node node) {
    while (node.parent != NIL && node.parent.color == Color.RED) {
      if (node.parent == node.parent.parent.left) {
        Node uncle = node.parent.parent.right;
        if (uncle != NIL && uncle.color == Color.RED) {
          node.parent.color = Color.BLACK;
          uncle.color = Color.BLACK;
          node.parent.parent.color = Color.RED;
          node = node.parent.parent;
        } else {
          if (node == node.parent.right) {
            node = node.parent;
            leftRotate(node);
          }
          node.parent.color = Color.BLACK;
          node.parent.parent.color = Color.RED;
          rightRotate(node.parent.parent);
        }
      } else {
        Node uncle = node.parent.parent.left;
        if (uncle != NIL && uncle.color == Color.RED) {
          node.parent.color = Color.BLACK;
          uncle.color = Color.BLACK;
          node.parent.parent.color = Color.RED;
          node = node.parent.parent;
        } else {
          if (node == node.parent.left) {
            node = node.parent;
            rightRotate(node);
          }
          node.parent.color = Color.BLACK;
          node.parent.parent.color = Color.RED;
          leftRotate(node.parent.parent);
        }
      }
    }

    root.color = Color.BLACK;
  }

  public void insert(int val) {
    Node newNode = new Node(val);
    Node x = root;
    Node y = NIL;

    while (x != NIL) {
      y = x;
      if (newNode.val < x.val) {
        x = x.left;
      } else {
        x = x.right;
      }
    }

    newNode.parent = y;

    if (y == NIL) {
      root = newNode;
    } else if (newNode.val < y.val) {
      y.left = newNode;
    } else {
      y.right = newNode;
    }

    newNode.left = NIL;
    newNode.right = NIL;
    newNode.color = Color.RED;

    insertFixup(newNode);
  }

  // 其他方法，如删除节点、查找节点等，可以根据需要添加

  public static void main(String[] args) {
    RedBlackTree redBlackTree = new RedBlackTree();
    redBlackTree.insert(5);
    redBlackTree.insert(3);
    redBlackTree.insert(7);
    redBlackTree.insert(2);
    redBlackTree.insert(4);

    System.out.println("Red-Black Tree Data:");
    redBlackTree.printTree(redBlackTree.root);
  }

  private void printTree(Node node) {
    if (node != NIL) {
      printTree(node.left);
      System.out.print(node.val + " ");
      printTree(node.right);
    }
  }
}
