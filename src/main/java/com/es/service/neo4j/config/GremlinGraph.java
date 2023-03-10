package com.es.service.neo4j.config;


import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GremlinGraph
 * @Description TODO
 * @Author QiBin
 * @Date 2020/12/218:01
 * @Version 1.0
 **/
//@Component
public class GremlinGraph {

    //    @Autowired
//    private Configuration config;
//
//    @Autowired
//    private Driver neo4jDriver;
//
//    /**
//     * 获取驱动
//     *
//     * @return
//     */

    @Value("${spring.data.neo4j.uri}")
    String url;
    @Value("{spring.data.neo4j.username}")
    String username;
    @Value("{spring.data.neo4j.password}")
    String password;
//
//
//
//
    @Bean(name = "neo4jDriver")
    private Driver getNeo4jDriver() {
//        String url = config.getUrl();
//        String username = config.getUsername();
//        String password = config.getPassword();
//        List<URI> urls = config.getUrls();
        List<URI> urls = new ArrayList<>();
        Driver driver;
        if (StringUtils.isBlank(url)) {
            driver = GraphDatabase.routingDriver(urls, AuthTokens.basic(username, password), Config.defaultConfig());
        } else {
            driver = GraphDatabase.driver(url, AuthTokens.basic(username, password));
        }

        return driver;
    }
}
//    public Neo4JGraph getGraph() {
//        Neo4JNativeElementIdProvider vertexIdProvider = new Neo4JNativeElementIdProvider();
//        Neo4JNativeElementIdProvider edgeIdProvider = new Neo4JNativeElementIdProvider();
//        Neo4JGraph graph = new Neo4JGraph(neo4jDriver, vertexIdProvider, edgeIdProvider);
//        graph.setProfilerEnabled(true);
//
//        return graph;
//    }
//}
