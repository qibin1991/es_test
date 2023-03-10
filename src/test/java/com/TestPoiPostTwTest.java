package com;

import com.es.redisConfig.RedisUtil;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPoiPostTwTest extends TestCase {


    @Ignore
    @Test
    public void getCl() {
        String path = getClass().getClassLoader().getResource("elasticsearch.properties").getPath();
        System.out.println(path);

    }

    @Resource
    JedisPool jedisPool;
    @Value("${spring.redis.password}")
    String redisAuth;
    public static final String _KEY = "myDelayQueue";

    @org.junit.jupiter.api.Test
    public void testRedisZset(){

        Jedis resource = jedisPool.getResource();
        resource.auth(redisAuth);
        resource.zadd(_KEY, 11, "1");
        resource.zadd(_KEY, 23, "1");

    }
}