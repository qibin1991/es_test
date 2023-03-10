package com.kafka;

/**
 * @ClassName TopicInitRunner
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/27 11:15
 * @Version 1.0
 **/

import com.google.common.collect.Lists;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化多分区的topic 基于springboot2
 */
//@Component
public class TopicInitRunner implements ApplicationRunner {

    @Autowired
    private AdminClient adminClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 通过配置文件读取自定义配置的topic名及分区数 省略...
        // Key topic V 分区数
        Map<String, Integer> topicPartitionMap = new HashMap<>();
        for (Map.Entry<String, Integer> e : topicPartitionMap.entrySet()) {
            createTopic(e.getKey(), e.getValue(), (short) 1);
        }

    }

    public void createTopic(String topic, int partition,short replication) {
        NewTopic newTopic = new NewTopic(topic, partition,replication);
        adminClient.createTopics(Lists.newArrayList(newTopic));
    }
}

