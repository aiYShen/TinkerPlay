package com.aiyuns.tinkerplay.Other.Thread.DaemonThread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 守护线程 Demo
public class ConnectionMonitor {

  private static Connection connection;

  public static void main(String[] args) {
    // 启动守护线程，定期检查数据库连接
    Thread monitorThread = new Thread(new ConnectionCheckTask());
    monitorThread.setDaemon(true); // 设置为守护线程
    monitorThread.start();

    // 连接数据库（用于模拟）
    try {
      connection =
          DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "user", "password");
      System.out.println("初始数据库连接建立成功！");
    } catch (SQLException e) {
      System.out.println("初始数据库连接失败: " + e.getMessage());
    }

    // 主线程模拟应用程序运行10秒
    try {
      Thread.sleep(10000); // 主线程运行10秒后结束
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("主线程结束，程序退出！");
  }

  // 定义连接检查任务
  static class ConnectionCheckTask implements Runnable {
    @Override
    public void run() {
      while (true) {
        if (connection != null) {
          try {
            // 检查连接是否有效
            if (!connection.isValid(2)) {
              System.out.println("检测到连接已断开，尝试重新连接...");
              reconnect();
            } else {
              System.out.println("连接正常");
            }
          } catch (SQLException e) {
            System.out.println("连接检查时发生错误: " + e.getMessage());
          }
        } else {
          System.out.println("连接未初始化，跳过检查...");
        }

        // 每隔5秒检查一次
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    // 尝试重新连接
    private void reconnect() {
      try {
        connection =
            DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "user", "password");
        System.out.println("重新连接成功！");
      } catch (SQLException e) {
        System.out.println("重新连接失败: " + e.getMessage());
      }
    }
  }
}
