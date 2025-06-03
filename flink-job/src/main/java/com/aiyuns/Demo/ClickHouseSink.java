package com.aiyuns.Demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class ClickHouseSink extends RichSinkFunction<String> {

  private transient Connection connection;
  private transient PreparedStatement preparedStatement;
  private ScheduledExecutorService scheduledExecutorService;

  // 初始化 Sink 的生命周期方法，在 Sink 任务启动时调用: 开启一些东西, 例如打开数据库jdbc连接
  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    // 启动一些定时任务: 例如攒批提交
    scheduledExecutorService = Executors.newScheduledThreadPool(1);
    Runnable runnable = () -> {};
  }

  // 在 Sink 任务结束时调用，用于清理资源: 关闭一些东西, 例如关闭数据库jdbc连接
  @Override
  public void close() throws Exception {
    super.close();
  }

  // 处理每条输入记录的核心方法: 业务逻辑处理代码
  @Override
  public void invoke(String value, Context context) throws Exception {
    super.invoke(value, context);
  }
}
