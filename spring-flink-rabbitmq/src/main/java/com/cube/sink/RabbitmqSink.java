package com.cube.sink;

import com.alibaba.fastjson.JSON;
import com.cube.manager.UrlInfoManager;
import com.cube.model.UrlInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.UUID;

/**
 * Author: 滕飞
 * Created: 2019-10-11 20:41
 * Description:
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class RabbitmqSink  extends RichSinkFunction<String> implements ApplicationContextAware {

    private UrlInfoManager urlInfoManager;

    private ApplicationContext applicationContext;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        log.info("applicationContext=" + applicationContext);
        if (applicationContext == null) {
            init();
        }
    }

    @Override
    public void invoke(String value, SinkFunction.Context context) throws Exception {
        if (urlInfoManager == null) {
            init();
        }
        List<UrlInfo> urlInfoList = urlInfoManager.queryAll();
        UrlInfo urlInfo1 = new UrlInfo();
        urlInfo1.setUrl(UUID.randomUUID().toString());
        urlInfoManager.insert(urlInfo1);
        log.info(JSON.toJSONString(urlInfoList));
        log.info("---insert url info:", JSON.toJSONString(value));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void init() {
        applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        urlInfoManager = (UrlInfoManager) applicationContext.getBean("urlInfoManager");
    }
}
