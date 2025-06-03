package com.aiyuns.tinkerplay.Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/** 连接池只需要创建一次，有默认大小 数据库连接工具类 */
public class DBUtil {
  // 创建一个properties对象将配置文件加载进去，采用key-value的方式，将来可以通过键值进行获取
  // static修饰是为了静态方法可以直接获取
  static Properties p = new Properties();

  // 将HikariDataSource（数据源/也叫连接池）声明在外部用static修饰是为了让下面的静态方法能够访问到
  static HikariDataSource source = null;

  // 静态代码块 在类加载的时候就执行了，只执行一次
  // 静态代码块格式
  static { // 因为加载驱动只用加载一次不用反复进行加载，当调用JdbcUtils类时就对静态代码块中的进行执行了，且只创建一次
    try {

      // 利用properties将jdbc.properties配置文件转换成字节输入流再加载到properties中
      // 利用当前线程获取类加载器,利用类加载器将配置文件加载到properties中
      InputStream in =
          Thread.currentThread().getContextClassLoader().getResourceAsStream("db3.properties");

      // 将配置文件加载到properties
      p.load(in);

      // 获取配置文件中的参数 解决硬编码问题
      String driver = p.getProperty("db.driver");
      String url = p.getProperty("db.url");
      String username = p.getProperty("db.username");
      String password = p.getProperty("db.password");

      // 创建一个线程池配置文件对象将properties中的配置文件设置成线程池配置文件
      HikariConfig hi = new HikariConfig();

      hi.setDriverClassName(driver);
      hi.setJdbcUrl(url);
      hi.setUsername(username);
      hi.setPassword(password);

      // 创建一个连接池对象将连接池配置文件设为参数
      // HikariDataSource source = new HikariDataSource(hi);
      source = new HikariDataSource(hi);

      // 1.加载注册驱动：告诉程序使用哪一个数据库驱动jar包
      // Class.forName("com.mysql.jdbc.Driver");
      // 将连接驱动写活，解决硬编码问题
      Class.forName(driver);

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // 对1.加载驱动和2.获得数据库连接 的方法进行封装
  /** 返回值类型 Connection static修饰是为了方便进行调用 类名。方法名（） */
  public static Connection getConnection() {
    // 执行的时候遇到return就结束当前方法
    try {
      // 2.获取数据库连接对象
      // Connection conn =
      // DriverManager.getConnection("jdbc:mysql://localhost:3306/myjdbc",
      // "root", "mx1998");

      // return
      // DriverManager.getConnection("jdbc:mysql://localhost:3306/myjdbc",
      // "root", "mx1998");

      // 获取配置文件中的参数 解决硬编码问题
      /*	String url = p.getProperty("url");
      String username = p.getProperty("username");
      String password = p.getProperty("password");*/

      // 解决硬编码问题，从而在后续如果需要更换连接数据库直接在配置文件中更改value数据即可
      // return DriverManager.getConnection(url, username, password);

      // 用连接池直接与数据库进行连接，不需要传入参数，配置文件时已经将参数设定进去了
      return source.getConnection();

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // try...catch失败就返回null；
    return null;
  }

  /** 对工具类进行封装 */
  public static void release(Connection conn, PreparedStatement ps, ResultSet rs) {

    // 5.释放资源 顺序 先释放小的再释放大的
    try {
      if (rs != null) {
        rs.close();
      }

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        try {
          if (conn != null) {
            conn.close();
          }

        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

  public static void release(
      Connection conn, Statement statement, PreparedStatement preparedStatement, ResultSet rs) {

    // 5.释放资源 顺序 先释放小的再释放大的
    try {
      if (rs != null) {
        rs.close();
      }

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        try {
          if (statement != null) {
            statement.close();
          }

        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
          try {
            if (conn != null) {
              conn.close();
            }

          } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }
  }
}
