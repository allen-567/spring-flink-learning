package com.cube;

import com.cube.sink.UrlMysqlSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

/**
 * Author: 滕飞
 * Created: 2019-10-11 15:59
 * Description:
 */
@Slf4j
public class KafkaUrlSinkJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("zookeeper.connect", "localhost:2181");
        //properties.put("group.id", "metric-group");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("auto.offset.reset", "latest");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        SingleOutputStreamOperator<String> dataStreamSource = env.addSource(
                new FlinkKafkaConsumer010<String>(
                        "test",// topic
                        new SimpleStringSchema(),
                        properties
                )
        ).setParallelism(1);
                // map操作，转换，从一个数据流转换成另一个数据流，这里是从string-->UrlInfo


        dataStreamSource.addSink(new UrlMysqlSink());
        dataStreamSource.addSink(new PrintSinkFunction<>());

        env.execute("save url to db");
    }
}