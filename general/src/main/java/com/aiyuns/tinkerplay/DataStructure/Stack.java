package com.aiyuns.tinkerplay.DataStructure;

/**
 * @Author: aiYunS @Date: 2022-10-18 下午 02:24 @Description: 栈的数据结构
 */
public class Stack {

  // 存储元素
  private Object[] elements;

  // 栈帧: 永远指向栈顶部元素
  // 如果index采用0，表示栈帧指向了顶部元素的上方
  // 如果index采用-1，表示栈帧指向了顶部元素
  private int index = -1;

  // 无参构造方法舒初始化栈
  public Stack() {
    this.elements = new Object[5];
  }

  // 有参构造方法,手动赋值初始化栈内存大小
  public Stack(int a) {
    this.elements = new Object[a];
  }

  // 压栈方法
  public void push(Object o) {
    if (index >= elements.length - 1) {
      System.out.println("栈内存已满! 压栈失败!");
      return;
    }
    // index += 1;
    elements[++index] = o;
    System.out.println("压栈: " + o + "元素成功, 栈帧指向: " + index);
  }

  // 弹栈方法
  public void pop() {
    if (index < 0) {
      System.out.println("栈内存已空! 栈帧指向栈底, 弹栈失败!");
      return;
    }
    System.out.print("弹栈: " + elements[index]);
    elements[index--] = null;
    // index -= 1;
    System.out.println("元素成功, 栈帧指向: " + index);
  }

  public Object[] getElements() {
    return elements;
  }

  public void setElements(Object[] elements) {
    this.elements = elements;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }
}
