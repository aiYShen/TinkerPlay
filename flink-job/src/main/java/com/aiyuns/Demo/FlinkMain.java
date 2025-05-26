package com.aiyuns.Demo;

import com.aiyuns.ClickHouse;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class FlinkMain {

    public static void main(String[] args) {

        try {
            final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setStateBackend(new FsStateBackend("file:///tmp/flink"));
            env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
            env.getCheckpointConfig().setCheckpointInterval(180000);
            Properties properties = new Properties();
            properties.setProperty("bootstrap.servers", "xxx.xxx.xxx.xxx:9092,xxx.xxx.xxx.xxx:9092");
            properties.setProperty("group.id", "xxxxx");
            properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("xxxxx", new SimpleStringSchema(), properties);
            kafkaConsumer.setCommitOffsetsOnCheckpoints(true);
            DataStream<String> kafkaStream = env.addSource(kafkaConsumer);
            kafkaStream.map(new MapFunction<String, String>() {

                @Override
                public String map(String value) throws Exception {
                    return value;
                }
            });
            kafkaStream.addSink(new ClickHouseSink()).name("xxxxx");
            env.execute("xxxxx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
