package com.aiyuns.tinkerplay.Other.ListenerDemo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 10:35:48 @Description: 鼠标监听案例
 */
public class MouseListenerDemo {

  public static void main(String[] args) {
    // 创建一个窗口
    JFrame frame = new JFrame("MouseListener Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200);

    // 创建一个面板
    JPanel panel = new JPanel();

    // 创建一个标签用于显示鼠标事件
    JLabel label = new JLabel("鼠标事件: ");

    // 添加鼠标事件监听器
    panel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            label.setText("鼠标事件: 点击");
          }

          @Override
          public void mousePressed(MouseEvent e) {
            label.setText("鼠标事件: 按下");
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            label.setText("鼠标事件: 释放");
          }
        });

    // 将标签和面板添加到窗口中
    frame.add(panel);
    frame.add(label);

    // 设置布局
    GroupLayout layout = new GroupLayout(frame.getContentPane());
    frame.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createSequentialGroup().addComponent(panel).addComponent(label));
    layout.setVerticalGroup(layout.createParallelGroup().addComponent(panel).addComponent(label));

    // 显示窗口
    frame.setVisible(true);
  }
}
