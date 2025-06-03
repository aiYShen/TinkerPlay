package com.aiyuns.tinkerplay;

import com.aiyuns.tinkerplay.Entity.User;
import com.aiyuns.tinkerplay.Other.JdbcConnectionPool.JdbcPool;
import com.aiyuns.tinkerplay.Other.JdbcConnectionPool.PoolManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: aiYunS @Date: 2022-8-11 下午 04:41 @Description: 测试没有连接池的效率
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcNoPoolMain {

  static final int threadSize = 1000;

  static JdbcPool jdbcPool = PoolManager.getInstance();
  static CountDownLatch countDownLatch1 = new CountDownLatch(1);
  static CountDownLatch countDownLatch2 = new CountDownLatch(threadSize);

  public static void main(String[] args) throws InterruptedException {
    threadTest();
  }

  public static void threadTest() throws InterruptedException {
    long time1 = System.currentTimeMillis();
    for (int i = 0; i < threadSize; i++) {
      new Thread(
              new Runnable() {
                @Override
                public void run() {
                  try {
                    // 使得线程阻塞到coutDownLatch1为0时才执行
                    countDownLatch1.await();
                    selectNoPool();
                    // 每个独立子线程执行完后，countDownLatch2减1
                    countDownLatch2.countDown();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  } catch (SQLException e) {
                    e.printStackTrace();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              })
          .start();
    }
    // 将countDownLatch1置为0，从而使线程并发执行
    countDownLatch1.countDown();

    // 等待countDownLatch2变为0时才继续执行
    countDownLatch2.await();
    long time2 = System.currentTimeMillis();

    System.out.println("thread size: " + threadSize + " no use pool :" + (time2 - time1));
  }

  public static void selectNoPool() throws Exception {
    User s = null;
    Connection conn = jdbcPool.getConnectionNoPool();
    Statement sm = null;
    ResultSet rs = null;
    try {
      sm = conn.createStatement();
      rs = sm.executeQuery("select * from user");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      while (rs.next()) {
        //                int id = rs.getInt("id");
        //                String username = rs.getString("username");
        //                String birthday = rs.getString("birthday");
        //                String address = rs.getString("address");
        //                String sex = rs.getString("sex");
        //                // 只要有数据每读取一行就创建一个Student对象
        //                s = new User();
        //                // 将从表中获取的数据存储到一个Student类对象中
        //                s.setId(id);
        //                s.setUsername(username);
        //                s.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        //                s.setAddress(address);
        //                s.setSex(sex);
        //
        // System.out.println("===========================================================================");
        //                System.out.println(s);
        //
        // System.out.println("===========================================================================");
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
    sm.close();
    conn.close();
  }
}
