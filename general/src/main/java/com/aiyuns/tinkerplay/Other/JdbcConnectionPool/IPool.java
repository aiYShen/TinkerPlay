package com.aiyuns.tinkerplay.Other.JdbcConnectionPool;

import java.sql.Connection;

/**
 * @Author: aiYunS @Date: 2022-8-11 下午 04:07 @Description:
 */
public interface IPool {

  // 获取连接池中可用连接
  PoolConnection getConnection();

  // 获取一个数据库连接（不使用连接池）
  Connection getConnectionNoPool();
}
