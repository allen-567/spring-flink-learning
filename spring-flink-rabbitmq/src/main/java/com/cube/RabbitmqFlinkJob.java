package com.cube;

import com.cube.common.utils.ExecutionEnvUtil;
import com.cube.sink.RabbitmqSink;
import lombok.extern.slf4j.Slf4j;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;

/**
 * Author: 滕飞
 * Created: 2019-10-11 17:34
 * Description:
 */
@Slf4j
public class RabbitmqFlinkJob {

    //本地环境
    public static String host = "172.16.31.5";
    public static String virtualHost = "danke";
    public static String userName = "rabbitmq";
    public static String password = "1q2w3e4r";
    public static String queneName = "bi-city-probe";
    public static int port = 5672;

    public static void main(String[] args) throws Exception {
        log.info("==========Flink 监听binlog程序，开始启动============");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameterTool = ExecutionEnvUtil.PARAMETER_TOOL;

        //下面这些写死的参数可以放在配置文件中，然后通过 parameterTool 获取
        final RMQConnectionConfig connectionConfig = new RMQConnectionConfig
                .Builder().setHost(host).setVirtualHost(virtualHost)
                .setPort(port).setUserName(userName).setPassword(password)
                .build();

        DataStreamSource<String> priceSync = env.addSource(new RMQSource<>(connectionConfig,
                queneName,
                true,
                new SimpleStringSchema()))
                .setParallelism(1);
        //DataStreamSource<String> priceSync = env.fromElements("234");

        priceSync.print();

        //注意，换一个新的 queue，否则也会报错
        //priceSync.addSink(new RMQSink<>(connectionConfig, "lh", new MetricSchema()));
        log.info("==========Flink 监听binlog程序，接入修改客源匹配表功能============");
        priceSync.addSink(new RabbitmqSink());

        //如果想保证 exactly-once 或 at-least-once 需要把 checkpoint 开启
//        env.enableCheckpointing(10000);
        env.execute("flink-rabbitmq-update-customer-match");
    }
}
