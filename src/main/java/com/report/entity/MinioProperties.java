package com.report.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName MinioProperties
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/10 09:47
 * @Version 1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class MinioProperties {

    @Value("${file.endpoint}")
    private String endpoint;
    @Value("${file.accessKey}")
    private String accessKey;
    @Value("${file.secretKey}")
    private String secretKey;
    @Value("${file.bucketName}")
    private String bucketName;
    @Value("${file.nginxPort}")
    private String nginxPort;
}