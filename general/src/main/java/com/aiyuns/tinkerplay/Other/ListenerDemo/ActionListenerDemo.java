package com.aiyuns.tinkerplay.Other.ListenerDemo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 10:15:13 @Description: 按钮监听案例
 */
public class ActionListenerDemo {

  public static void main(String[] args) {
    // 创建一个窗口
    JFrame frame = new JFrame("ActionListener Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200);

    // 创建一个按钮
    JButton button = new JButton("点击我");

    // 创建一个标签用于显示按钮点击次数
    JLabel label = new JLabel("按钮点击次数: 0");

    // 创建一个点击次数计数变量
    final int[] clickCount = {0};

    // 添加按钮点击事件监听器
    button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // 每次按钮被点击时，增加点击次数并更新标签文本
            clickCount[0]++;
            label.setText("按钮点击次数: " + clickCount[0]);
          }
        });

    // 将按钮和标签添加到窗口中
    frame.setLayout(new FlowLayout());
    frame.add(button);
    frame.add(label);

    // 显示窗口
    frame.setVisible(true);
  }
}
