package com.es.neo4j.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.es.neo4j.entity.ForwardRelation;
import com.es.neo4j.entity.TweetsVo;
import com.es.neo4j.repository.TweetUserRepository;
import com.es.neo4j.repository.TweetsRepository;
import com.es.neo4j.service.AnalysisSerive;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.es.neo4j.repository.ForwardRelationRepository;
import com.es.redisConfig.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AnalysisServiceImpl
 * @Description TODO
 * @Author QiBin
 * @Date 2020/12/23下午2:09
 * @Version 1.0
 **/
//@Service("analysisService")
@Slf4j
public class AnalysisServiceImpl implements AnalysisSerive {

    @Autowired
    TweetUserRepository tweetUserRepository;
    @Autowired
    Driver driver;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    TweetsRepository tweetsRepository;
    @Autowired
    ForwardRelationRepository forwardRelationRespository;

    final static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        ForwardRelation build = ForwardRelation.builder().datasetId("9999").startV("twTest").endV("twTest1")
                .updateTimes(0L).uniqueField("1111").mediatname("topic")
                .edgue("topic").build();



    }
    @Override
    public void addForwardEdge(ForwardRelation forwardRelation) throws JsonProcessingException {

//        ForwardRelation.ForwardRelationBuilder forwardRelation =  ForwardRelation.builder();

//        ForwardRelation build = forwardRelation.datasetId("").eduge("").startNode()
//        .endNode().uniqueField().inputTIme().build();

        String params = mapper.writeValueAsString(forwardRelation);
        //先查是否存在关系   然后查  unique是否一样  如果不一样   创建 如果一样  修改

        boolean js_twitter_edge_distinct = redisUtil.hHasKey("js_twitter_edge_distinct",
                forwardRelation.getDatasetId() + "_" + forwardRelation.getStartV()
                        + "_" + forwardRelation.getEndV() + "_" + forwardRelation.getMediatname()
                        + "_" + forwardRelation.getUniqueField());

        if (js_twitter_edge_distinct){
            return;
        }

        String queryString = String.format("merge (a:TwitterUser {username: %d}),(b:TwitterUser {username : %d}) " +
                " create (a)-[r:forward %s]->(b) return r", forwardRelation.getStartV(), forwardRelation.getEndV(), params);

        Session session = driver.session();
        Transaction transaction = session.beginTransaction();
        if (transaction != null) {
//            Result run = transaction.run("", parameters("",""));
            Result run = transaction.run(queryString);
            if (run != null) {
                boolean b = run.hasNext();

                transaction.close();
                session.close();
            }
        }
    }


    public void getKafkaForwardToNeo(JSONObject parseObject) {
        try {
            if (parseObject.containsKey("lable")) {
                String lable = parseObject.getString("lable");
                if (StringUtils.isNotBlank(lable) && lable.equals("Text")
                        && parseObject.containsKey("data") && parseObject.getJSONObject("data") != null) {
                    JSONObject data = parseObject.getJSONObject("data");
                    if (data != null) {
                        //主贴
                        String tweetsId = data.getString("tweetsId");
                        boolean b = redisUtil.hHasKey("js_twitter_tweet_distinct",
                                tweetsId + "_" + data.getString("datasetId") + "_" +
                                        data.getString("repliedId"));
                        if (b) {
                            log.info("======" + b);
                            //此处  添加  更新逻辑   判断更新时间  设置5天一更新
                            Object hget = redisUtil.hget("js_twitter_tweet_distinct",
                                    tweetsId + "_" + data.getString("datasetId") + "_" +
                                            data.getString("repliedId"));

                            if (hget != null){
                                String s = hget.toString();
                                String inputTIme = data.getString("inputTIme");
                                if (StringUtils.isNotBlank(inputTIme)){
                                    int days = Days.daysBetween(new LocalDate(s)
                                            ,new LocalDate(inputTIme)
                                            ).getDays();
                                    if (days >= 5){
                                        updateTweets(data);
                                    }
                                }
                            }else {
                                updateTweets(data);
                            }

                            return;
                        }
                        JSONObject jsonObject = new JSONObject();

                        Set<Map.Entry<String, Object>> entries = data.entrySet();
                        log.info("=======keyset====" + entries);
                        if (CollectionUtils.isNotEmpty(entries)) {
                            for (Map.Entry<String, Object> entry : entries) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (StringUtils.isNotBlank(key)) {
                                    if (key.equals("username")) {
                                        jsonObject.put("username", data.getString("username").toLowerCase());
                                    } else if (key.equals("retweetScreenName")) {
                                        if (StringUtils.isNotBlank(data.getString(key))) {
                                            jsonObject.put("retweetScreenName", data.getString("retweetScreenName").toLowerCase());
                                        }
                                    } else {
                                        jsonObject.put(key, value);
                                    }
                                }
                            }

                            if (!jsonObject.containsKey("content_zh")) {
                                jsonObject.put("content_zh", "");
                            }
                        }

                        //新增主贴顶点
                        TweetsVo save = tweetsRepository.save(jsonObject.toJavaObject(TweetsVo.class));
                        log.info("neo save tweets :" + save);
                        if (save != null) {
                            redisUtil.hset("js_twitter_tweet_distinct",
                                    tweetsId + "_" + data.getString("datasetId") + "_" +
                                            data.getString("repliedId"),data.getString("inputTIme"));
                        }
                    }
                }
            } else {
                log.info("=====转发边=====" + parseObject);
                if (parseObject.containsKey("edgue") && parseObject.containsKey("mediatname")) {
                    String edgue = parseObject.getString("edgue");
                    //边
                    if (StringUtils.isNotBlank(edgue)) {
                        log.info("====forward边入库前=====" + parseObject);
                        ForwardRelation forwardRelation = parseObject.toJavaObject(ForwardRelation.class);
                        addForwardEdge(forwardRelation);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //更新 主贴属性
    public void updateTweets(JSONObject data) {
        //MERGE (n:Node {id: 'argan'}) SET n += {id: 'argan', age: 30, sex: 'male', email: 'arganzheng@gmail.com'}
        //RETURN n

        JSONObject jsonObject = new JSONObject();

        Set<Map.Entry<String, Object>> entries = data.entrySet();
        log.info("=======keyset====" + entries);
        if (CollectionUtils.isNotEmpty(entries)) {
            for (Map.Entry<String, Object> entry : entries) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isNotBlank(key)) {
                    if (key.equals("username")) {
                        jsonObject.put("username", data.getString("username").toLowerCase());
                    } else if (key.equals("retweetScreenName")) {
                        if (StringUtils.isNotBlank(data.getString(key))) {
                            jsonObject.put("retweetScreenName", data.getString("retweetScreenName").toLowerCase());
                        }
                    } else {
                        jsonObject.put(key, value);
                    }
                }
            }

            if (!jsonObject.containsKey("content_zh")) {
                jsonObject.put("content_zh", "");
            }
        }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("MERGE (n:TwitterTweet {tweetsId : %d}) SET n += {");
        Set<String> strings = jsonObject.keySet();
        List<String> paramsLists = new ArrayList<>(strings);

        if (CollectionUtil.isNotEmpty(strings)) {
            for (int i = 0; i < paramsLists.size(); i++) {
                if (i <  paramsLists.size()-1){
                    stringBuffer.append(paramsLists.get(i)+":"+jsonObject.get(paramsLists.get(i))+",");
                }else {
                    stringBuffer.append(paramsLists.get(i)+","+jsonObject.get(paramsLists.get(i)));
                }
            }
        }
        stringBuffer.append("}");

        String queryString = String.format(stringBuffer.toString(), jsonObject.getString("tweetsId"));
        Session session = driver.session();
        Transaction transaction = session.beginTransaction();
        if (transaction != null) {
//            Result run = transaction.run("", parameters("",""));
            Result run = transaction.run(queryString);
            if (run != null) {
                boolean b = run.hasNext();
                transaction.close();
                session.close();
            }
        }


    }


//    public void graphDbService(){
//         GraphDatabaseService graphDb = new GraphDatabaseFactory()
//                .newEmbeddedDatabaseBuilder( "target/database/learn1" )
//                .loadPropertiesFromFile(AnalysisSerive.class.getResource("/").getPath()+"neo4j.properties" )
//                .newGraphDatabase();
//    }

//    public void cypherService(){
//        File file = new File("D:\\neo4j-community-3.2.6\\data\\databases\\graph.db");
//        //Create a new Object of Graph Database
//        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);
//        System.out.println("Server is up and Running");
//    }
}
