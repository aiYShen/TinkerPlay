package com.aiyuns.tinkerplay;

import com.aiyuns.tinkerplay.Other.JdbcConnectionPool.JdbcPool;
import com.aiyuns.tinkerplay.Other.JdbcConnectionPool.PoolConnection;
import com.aiyuns.tinkerplay.Other.JdbcConnectionPool.PoolManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: aiYunS @Date: 2022-8-11 下午 05:43 @Description: 测试有连接池的效率
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcPoolMain {

  static final int threadSize = 10;

  static JdbcPool jdbcPool = PoolManager.getInstance();

  static CountDownLatch countDownLatch1 = new CountDownLatch(1);
  static CountDownLatch countDownLatch2 = new CountDownLatch(threadSize);

  public static void main(String[] args) throws InterruptedException {
    threadTest();
  }

  public static void select() throws SQLException {
    PoolConnection conn = jdbcPool.getConnection();
    ResultSet rs = conn.queryBySql("select * from user");
    try {
      while (rs.next()) {
        System.out.println(
            Thread.currentThread().getName()
                + " ==== "
                + "name: "
                + rs.getString("username")
                + " id: "
                + rs.getInt("id"));
      }
      Thread.sleep(100);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    rs.close();
    conn.close();
  }

  public static void threadTest() throws InterruptedException {
    long time1 = System.currentTimeMillis();
    for (int i = 0; i < threadSize; i++) {
      new Thread(
              new Runnable() {
                @Override
                public void run() {
                  try {
                    // 阻塞到当countDownLatch1为0才执行
                    countDownLatch1.await();
                    select();
                    // 将countDownLatch2减1
                    countDownLatch2.countDown();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  } catch (SQLException e) {
                    e.printStackTrace();
                  }
                }
              })
          .start();
    }
    // 将countDownLatch1减1，从而使所有子线程同时并发执行
    countDownLatch1.countDown();
    // 等待countDownLatch2为0时继续执行
    countDownLatch2.await();
    long time2 = System.currentTimeMillis();

    System.out.println("pool size:" + jdbcPool.getSize());

    System.out.println("thread size: " + threadSize + " use pool :" + (time2 - time1));
  }
}
