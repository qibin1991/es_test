package com.es.service;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Objects;

/**
 * @ClassName: ESConnection
 * @Description: elasticsearch 获取客户端连接
 * @Author: yfeng
 * @Date: 2018/11/15 11:53 AM
 */
//@Component
public class ESConnection {
    //http://60.205.219.205/
//    private String hosts =  ApplicationUtil.getPropertiesValue("elasticsearch.properties","elasticsearch.host");
    private String hosts =  "60.205.219.205";
//    private int port = 9200;
    private int port = 80;
    private String schema = "http";
    private int connectTimeOut = 1000;
    private int socketTimeOut = 30000;
    private int connectionRequestTimeOut = 500;
    private int maxConnectNum = 100;
    private int maxConnectPerRoute = 100;
    private boolean uniqueConnectTimeConfig = true;
    private boolean uniqueConnectMaxConfig = true;
    private RestClientBuilder builder;
    private static RestHighLevelClient client;

    public ESConnection() {

    }

    /**
     * 获取ES客户端连接
     * @return
     */
    public static RestHighLevelClient getClient() {
        if (client == null) {
            synchronized (ESConnection.class) {
                if (client == null) {
                    ESConnection elasticsearch = new ESConnection();
                    client = elasticsearch.client();
                }
            }
        }
        return client;
    }

    /**
     * 实例化RestHighLevelClient客户端
     * @return
     */
    private  RestHighLevelClient client() {
        String[] hostArray = Objects.requireNonNull(hosts, "hosts can not null").split(",");
        HttpHost[] httpHosts = new HttpHost[hostArray.length];
        for (int i=0; i<hostArray.length; i++){
            httpHosts[i] = new HttpHost(hostArray[i], port, schema);
        }
        builder = RestClient.builder(httpHosts);
        if (uniqueConnectTimeConfig) {
            setConnectTimeOutConfig();
        }
        if (uniqueConnectMaxConfig) {
            setMultiConnectConfig();
        }
        client = new RestHighLevelClient(builder);
        return client;
    }

    /**
     * 异步http client的连接延时配置
     */
    private void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
    }


    /**
     * 异步http client的连接数配置
     */
    private void setMultiConnectConfig() {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
    }

    /**
     * 关闭连接
     */
    public static void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(ESConnection.getClient());
    }

}
