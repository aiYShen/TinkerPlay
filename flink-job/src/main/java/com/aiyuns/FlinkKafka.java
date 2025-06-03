package com.aiyuns;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

public class FlinkKafka {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    try {
      final ParameterTool params = ParameterTool.fromArgs(args);

      // 创建 Flink 执行环境
      final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
      env.setRestartStrategy(
          RestartStrategies.fixedDelayRestart(
              3, // 重试次数
              Time.of(10, TimeUnit.SECONDS) // 每次重试的间隔时间
              ));
      env.setRestartStrategy(
          RestartStrategies.failureRateRestart(
              3, // 时间窗口内允许的最大失败次数
              Time.of(5, TimeUnit.MINUTES), // 时间窗口大小
              Time.of(10, TimeUnit.SECONDS) // 每次重试的间隔时间
              ));

      // 配置 Kafka 消费者属性
      Properties properties = new Properties();
      properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "FlinkKafka-consumer-group");
      properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "FlinkKafka");
      properties.setProperty("auto.offset.reset", "earliest");

      // 创建 Kafka 消费者
      FlinkKafkaConsumer<String> kafkaConsumer =
          new FlinkKafkaConsumer<>(
              "flink_kafka_topic", // Kafka topic 名称
              new SimpleStringSchema(), // 数据的序列化模式
              properties);

      // 创建 Kafka 数据流
      DataStream<String> kafkaStream = env.addSource(kafkaConsumer);

      // 处理 Kafka 数据流
      kafkaStream.map(value -> "Processed: " + value).print(); // 这里只是简单地输出处理后的数据

      // 启动 Flink 作业
      env.execute("Flink Kafka Job");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
