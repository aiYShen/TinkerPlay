package com.aiyuns;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkKafkaToClickHouse {
    public static void main(String[] args) throws Exception {
        // 设置流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 创建 TableEnvironment
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 注册 Kafka 数据源表
        tableEnv.executeSql(
                "CREATE TABLE kafka_input_table ("
                        + "id INT, "
                        + "name STRING, "
                        + "value DOUBLE"
                        + ") WITH ("
                        + "'connector' = 'kafka',"
                        + "'topic' = 'input_topic',"
                        + "'properties.bootstrap.servers' = 'localhost:9092',"
                        + "'format' = 'json'"
                        + ")");

        // 从 Kafka 读取数据
        Table kafkaTable = tableEnv.from("kafka_input_table");

        // 数据处理 (比如过滤)
        Table resultTable = kafkaTable.filter($("value").isGreater(100))
                .select($("id"), $("name"), $("value"));

        // 注册 ClickHouse 数据目标表
        tableEnv.executeSql(
                "CREATE TABLE clickhouse_output_table ("
                        + "id INT, "
                        + "name STRING, "
                        + "value DOUBLE"
                        + ") WITH ("
                        + "'connector' = 'clickhouse',"
                        + "'url' = 'jdbc:clickhouse://localhost:8123',"
                        + "'database' = 'default',"
                        + "'table-name' = 'output_table',"
                        + "'username' = 'default',"
                        + "'password' = ''"
                        + ")");

        // 将数据写入 ClickHouse
        resultTable.executeInsert("clickhouse_output_table");

        // 执行作业
        env.execute("Flink Kafka to ClickHouse");
    }
}