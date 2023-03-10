package com.report.entity;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @ClassName MinioConfig
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/10 09:46
 * @Version 1.0
 **/
@Configuration
public class MinioConfig {

    @Resource
    private MinioProperties minioProperties;

    @Bean("minioClient")
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}