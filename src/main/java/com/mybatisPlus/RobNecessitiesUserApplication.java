package com.mybatisPlus;

/**
 * @ClassName RobNecessitiesUserApplication
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/29 15:06
 * @Version 1.0
 **/

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@MapperScan("com.mybatisPlus")
@SpringBootApplication
public class RobNecessitiesUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(RobNecessitiesUserApplication.class, args);
    }
//    @Bean
//    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
//        return properties -> {
//            GlobalConfig globalConfig = properties.getGlobalConfig();
//            globalConfig.setBanner(false);
//            MybatisConfiguration configuration = new MybatisConfiguration();
//            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
//            properties.setConfiguration(configuration);
//        };
//    }

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        // 序列化枚举值为数据库存储值
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteEnumUsingToString);

        return properties -> {
            GlobalConfig globalConfig = properties.getGlobalConfig();
            globalConfig.setBanner(false);
            MybatisConfiguration configuration = new MybatisConfiguration();
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
            properties.setConfiguration(configuration);
        };
    }
}
