package com.aiyuns.tinkerplay.Other.ListenerDemo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 10:43:02 @Description: 键盘监听器案例
 */
public class KeyListenerDemo {
  public static void main(String[] args) {
    // 创建一个窗口
    JFrame frame = new JFrame("KeyListener Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200);

    // 创建一个文本框
    JTextField textField = new JTextField();

    // 添加一个标签用于显示键盘事件
    JLabel label = new JLabel("键盘事件: ");

    // 添加键盘事件监听器
    textField.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            // 键盘按键被键入时触发
            label.setText("键盘事件: 键入 - 字符: " + e.getKeyChar());
          }

          @Override
          public void keyPressed(KeyEvent e) {
            // 键盘按键被按下时触发
            label.setText("键盘事件: 按下 - 键码: " + e.getKeyCode());
          }

          @Override
          public void keyReleased(KeyEvent e) {
            // 键盘按键被释放时触发
            label.setText("键盘事件: 释放 - 键码: " + e.getKeyCode());
          }
        });

    // 将文本框和标签添加到窗口中
    frame.add(textField);
    frame.add(label);

    // 设置布局
    GroupLayout layout = new GroupLayout(frame.getContentPane());
    frame.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createSequentialGroup().addComponent(textField).addComponent(label));
    layout.setVerticalGroup(
        layout.createParallelGroup().addComponent(textField).addComponent(label));

    // 显示窗口
    frame.setVisible(true);
  }
}
