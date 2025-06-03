package com.aiyuns.tinkerplay.Other.ListenerDemo;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @Author: aiYunS @Date: 2023年11月6日, 0006 上午 10:49:44 @Description: 窗口监听器案例
 */
public class WindowListenerDemo {

  public static void main(String[] args) {
    // 创建一个窗口
    Frame frame = new Frame("WindowListener Example");
    // 创建一个标签用于显示窗口事件
    Label label = new Label("窗口事件: ");

    // 添加窗口事件监听器
    frame.addWindowListener(
        new WindowListener() {
          @Override
          public void windowOpened(WindowEvent e) {
            label.setText("窗口事件: 打开");
          }

          @Override
          public void windowClosing(WindowEvent e) {
            label.setText("窗口事件: 关闭");
            System.exit(0); // 关闭应用程序
          }

          @Override
          public void windowClosed(WindowEvent e) {
            // 窗口关闭后触发
            System.out.println("窗口已经关闭!");
          }

          @Override
          public void windowIconified(WindowEvent e) {
            label.setText("窗口事件: 最小化");
          }

          @Override
          public void windowDeiconified(WindowEvent e) {
            label.setText("窗口事件: 恢复");
          }

          @Override
          public void windowActivated(WindowEvent e) {
            label.setText("窗口事件: 激活");
          }

          @Override
          public void windowDeactivated(WindowEvent e) {
            label.setText("窗口事件: 失去焦点");
          }
        });

    // 将标签添加到窗口中
    frame.add(label);

    // 设置窗口大小和可见性
    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}
