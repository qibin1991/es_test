package com.es.redisConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisAutoConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool,
                                                         RedisStandaloneConfiguration jedisConfig) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisConfig);
        connectionFactory.setPoolConfig(jedisPool);
        return connectionFactory;
    }

    @Configuration
    public static class JedisConf {
        @Value("${spring.redis.host:192.168.61.50}")
        private String host;
        @Value("${spring.redis.port:6379}")
        private Integer port;
        @Value("${spring.redis.password:}")
        private String password;
        @Value("${spring.redis.database:}")
        private Integer database;

        @Value("${spring.redis.jedis.pool.max-active:8}")
        private Integer maxActive;
        @Value("${spring.redis.jedis.pool.max-idle:8}")
        private Integer maxIdle;
        @Value("${spring.redis.jedis.pool.max-wait:-1}")
        private Long maxWait;
        @Value("${spring.redis.jedis.pool.min-idle:0}")
        private Integer minIdle;

        @Bean
        public JedisPoolConfig jedisPool() {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWaitMillis(maxWait);
            jedisPoolConfig.setMaxTotal(maxActive);
            jedisPoolConfig.setMinIdle(minIdle);
            return jedisPoolConfig;
        }

        @Bean
        public RedisStandaloneConfiguration jedisConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPort(port);
            config.setDatabase(database);
            config.setPassword(RedisPassword.of(password));
            return config;
        }
    }


    /**
     * package com.szcatic.configuration;
     *
     * import java.util.HashSet;
     * import java.util.Set;
     *
     * import org.springframework.beans.factory.annotation.Value;
     * import org.springframework.context.annotation.Bean;
     * import org.springframework.context.annotation.Configuration;
     *
     * import redis.clients.jedis.HostAndPort;
     * import redis.clients.jedis.JedisCluster;
     * import redis.clients.jedis.JedisPoolConfig;
     *
     * @Configuration
     * public class RedisClusterConfig {
     *
     *        @Value("${spring.redis.cluster.nodes}")
     *     private String clusterNodes;
     *
     *    @Value("${spring.redis.database}")
     *     private int database;
     *
     *     @Value("${spring.redis.timeout}")
     *     private int timeout;
     *
     *     @Value("${spring.redis.pool.max-idle}")
     *     private int maxIdle;
     *
     *     @Value("${spring.redis.pool.min-idle}")
     *     private int minIdle;
     *
     *     @Value("${spring.redis.pool.max-active}")
     *     private int maxActive;
     *
     *     @Value("${spring.redis.pool.max-wait}")
     *     private long maxWait;
     *
     *    @Bean
     *    public JedisCluster getJedisCluster() {
     * 		return new JedisCluster(getNodes(), timeout, poolConfig());
     *    }
     *
     * 	private JedisPoolConfig poolConfig() {
     * 		JedisPoolConfig config = new JedisPoolConfig();
     *         config.setMaxIdle(maxIdle);
     *         config.setMinIdle(minIdle);
     *         config.setMaxTotal(maxActive);
     *         config.setMaxWaitMillis(maxWait);
     *         return config;
     *    }
     *
     * 	private Set<HostAndPort> getNodes() {
     * 		String[] cNodes = clusterNodes.split(",");
     * 		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
     * 		// 分割出集群节点
     * 		String[] hp;
     * 		for (String node : cNodes) {
     * 			hp = node.split(":");
     * 			nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
     *        }
     * 		return nodes;
     *    }
     * }
     */
}