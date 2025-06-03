package com.aiyuns.tinkerplay.Other.Play;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserImplFactory;

/*
   数据库
   1、数据一旦提交，就不可回滚
   2、哪些数据会导致数据的自动提交
       >DDL(create,alter,drop,truncate)操作一旦执行，都会自动提交
       ->set autocommit =false  对DDL操作无效
       >DML(insert,update,delete)默认的情况下，就会自动提交
       ->我们可以通过set autocommit =false 的方式取消DML操作的自动提交
       >默认在关闭链接时，会自动提交数据
*/
public class DB extends Thread {

  private static final String driverClass = "com.mysql.cj.jdbc.Driver";

  // 开发环境数据库
  private String developUrl =
      "jdbc:mysql://xxx.xxx.xx.xxx:3306/xxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
  // 性能测试环境数据库
  private String performanceTestUrl =
      "jdbc:mysql://xxx.xxx.xx.xxx:3306/xxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
  // 测试环境数据库
  private String sitUrl =
      "jdbc:mysql://xxx.xxx.xx.xxx:3306/xxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
  // 准生产环境数据库
  private String uatUrl =
      "jdbc:mysql://xxx.xxx.xx.xxx:3306/xxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
  // 生产环境数据库
  private String productionUrl =
      "jdbc:mysql://xxx.xxx.xx.xxx:3306/xxx?autoreconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";

  private static final String username = "root";
  private static final String password = "root";

  private Connection connection;
  private java.sql.Statement statement;

  static HashSet<String> writeResault = new HashSet<>();

  private LinkedHashMap<String, ArrayList<String>> sqlMap;
  public static LinkedHashMap<String, String> errorInfo = new LinkedHashMap<>();

  public DB() {}

  public DB(String environment, LinkedHashMap<String, ArrayList<String>> sqlMap) {
    try {
      Class.forName(driverClass);
      switch (environment) {
        case "develop":
          connection = DriverManager.getConnection(developUrl, username, password);
          break;
        case "performanceTest":
          connection = DriverManager.getConnection(performanceTestUrl, username, password);
          break;
        case "sit":
          connection = DriverManager.getConnection(sitUrl, username, password);
          break;
        case "uat":
          connection = DriverManager.getConnection(uatUrl, username, password);
          break;
        case "prd":
          connection = DriverManager.getConnection(productionUrl, username, password);
          break;
      }
      // 关闭自动提交,开启事务
      connection.setAutoCommit(false);
      statement = connection.createStatement();
      this.sqlMap = sqlMap;
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    boolean b = true;
    for (String key : sqlMap.keySet()) {
      ArrayList<String> arrsql = sqlMap.get(key);
      boolean isSuccess = true;
      for (String sql : arrsql) {
        try {
          if ((sql.toLowerCase().contains("drop")
                  && !sql.toLowerCase().replace(" ", "").contains("droptableifexists"))
              || (sql.toLowerCase().contains("delete") && !sql.toLowerCase().contains("deleted"))) {
            rollback();
            ;
            isSuccess = false;
            errorInfo.put(key + " ==> " + sql, "当前sql疑似存在危险的drop或delete语句, 请谨慎确认!!!");
            break;
          }
          statement.execute(sql);
        } catch (SQLException throwables) {
          isSuccess = false;
          // 记录错误的sql语句
          errorInfo.put(key, sql);
          // 执行发生错误则回滚
          rollback();
          throwables.printStackTrace();
          break;
        }
      }
      // 一个文件里面所有的SQL执行成功,则提交一次
      if (isSuccess) {
        try {
          connection.commit();
          writeResault.add(key);
        } catch (SQLException throwables) {
          // 提交发生错误则回滚
          rollback();
          throwables.printStackTrace();
        }
      } else {
        b = isSuccess;
      }
    }
    // 关闭连接
    this.close();
    if (b) {
      System.out.println(this.getName() + " --> 子线程执行成功!");
    } else {
      System.out.println(this.getName() + " --> 子线程执行失败!");
    }
  }

  private void rollback() {
    try {
      if (connection != null) {
        connection.rollback();
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void close() {
    try {
      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private boolean chenkSql(String sql) {
    try {
      SqlParser.Config config = SqlParser.config();
      SqlParserImplFactory factory = config.parserFactory();
      SqlParser parser = SqlParser.create(sql, config.withParserFactory(factory));
      SqlNode node = parser.parseStmt();
      return true;
    } catch (SqlParseException e) {
      return false;
    }
  }
}
