package com.kafka;

/**
 * @ClassName KafkaConfig
 * @Description   https://juejin.cn/post/6995746569580445709
 * @Author QiBin
 * @Date 2022/6/27 11:14
 * @Version 1.0
 **/

import com.google.common.collect.Maps;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

/**
 * 配置类参考 基于springboot2
 * 如果只进行普通的单消息发送 无需添加此配置到项目中
 */
//@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = Maps.newHashMap();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        return new KafkaAdmin(props);
    }
}

