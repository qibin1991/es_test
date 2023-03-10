package com.es.service.neo4j.config;


import org.neo4j.driver.Driver;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.internal.SessionFactory;
import org.neo4j.driver.internal.async.NetworkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;

import java.util.concurrent.CompletionStage;

/**
 * @ClassName MyConfig
 * @Description TODO
 * @Author QiBin
 * @Date 2020/11/2517:57
 * @Version 1.0
 **/
//@Configuration
//@EnableNeo4jRepositories(basePackages = "es/service/neo4j/repository")
//@EnableTransactionManagement
public class Neo4jTestConfig {

    @Bean
    public Neo4jTransactionManager transactionManager() throws Exception {
        return new Neo4jTransactionManager((Driver) sessionFactory());
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory() {
            @Override
            public NetworkSession newInstance(SessionConfig sessionConfig) {
                return null;
            }

            @Override
            public CompletionStage<Void> verifyConnectivity() {
                return null;
            }

            @Override
            public CompletionStage<Void> close() {
                return null;
            }

            @Override
            public CompletionStage<Boolean> supportsMultiDb() {
                return null;
            }
        };
    }

}
