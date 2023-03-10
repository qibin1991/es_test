package com.es.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.es.redisConfig.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName cacheRedis
 * @Description TODO
 * @Author QiBin
 * @Date 2020/11/1615:50
 * @Version 1.0
 **/
@Component
@Slf4j
public class CacheRedis {

    @Resource
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedisUtil redisUtil;

    //    @Scheduled(initialDelay = 100,fixedDelay = 1000000)
    public void cacheLanguage() {
        //读excel
        ExcelReader reader = ExcelUtil.getReader("/Users/qibin/zyyt_java/es_test/src/main/lang.xlsx");
        List<List<Object>> read = reader.read();
        for (List<Object> objects : read) {
            String key = (String) objects.get(0);
            String value = (String) objects.get(1);
            log.info(key + "===" + value);
            redisUtil.hset("tw-language", key, value, 1000 * 60 * 60 * 12 * 365 * 10);
        }
    }

    //    @Scheduled(initialDelay = 100,fixedDelay = 1000000)
    public static void deleteTweets() {


        Set<String> list = deleteTweetsInEs();
        if (CollectionUtils.isNotEmpty(list)) {
            for (String s : list) {
                delete("js_facebook_user", s, "user");
            }
        }

    }




    /**
     * 物理删除ES数据
     */
    public static void delete(String indexName, String _id, String type) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.id(_id);
        deleteRequest.index(indexName);
        deleteRequest.type(type);
        try {
            ESConnection.getClient().delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //去重 scroll
    public static List<String> scrollDelete(String INDEX, String type) {
        List<String> list = new ArrayList<>();
//设定滚动时间间隔,60秒,不是处理查询结果的所有文档的所需时间
//游标查询的过期时间会在每次做查询的时候刷新，所以这个时间只需要足够处理当前批的结果就可以了
        final Scroll scroll = new Scroll(TimeValue.timeValueSeconds(60));
        SearchRequest searchRequest = new SearchRequest(INDEX);
//        searchRequest.searchType(type);
        searchRequest.types(type);
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("mediatname", "FbUser"))
                .must(QueryBuilders.termQuery("dataSetId", "99999"));
        //每个批次实际返回的数量
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = ESConnection.getClient().search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("获取数据错误1 ->", e.getMessage());
        }
        String scrollId = "";
        do {
            if (searchResponse != null) {
                for (SearchHit hit : searchResponse.getHits().getHits()) {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    //获取需要数据
                    if (sourceAsMap.containsKey("userUrl")) {
                        Object userUrl = sourceAsMap.get("userUrl");
                        if (userUrl == null || StringUtils.isBlank(userUrl.toString())) {

                        }
                    }
                }
                //每次循环完后取得scrollId,用于记录下次将从这个游标开始取数
                scrollId = searchResponse.getScrollId();
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                try {
                    //进行下次查询
                    searchResponse = ESConnection.getClient().scroll(scrollRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    log.error("获取数据错误2 ->", e);
                }
            }
        } while (searchResponse.getHits().getHits().length != 0);
        //清除滚屏
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        //也可以选择setScrollIds()将多个scrollId一起使用
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = null;
        try {
            clearScrollResponse = ESConnection.getClient().clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.warn("清除滚屏错误 ->", e);
        }
        boolean succeeded = false;
        if (clearScrollResponse != null) {
            succeeded = clearScrollResponse.isSucceeded();
        }
        log.info("==" + succeeded);
        return list;
    }


    //    @Scheduled(initialDelay = 100,fixedDelay = 1000000)
    public static Set<String> deleteTweetsInEs() {

        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        log.info("=======es中主贴去重查询====");
        searchRequestVO.setIndexName(new String[]{"js_facebook_user"});
        searchRequestVO.setType("user");

        ScrollSearchRequestVO scrollSearchRequestVO = new ScrollSearchRequestVO();
        scrollSearchRequestVO.setIndexName(new String[]{"js_facebook_user"});
        scrollSearchRequestVO.setType("user");

        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("mediatname.keyword", "FbUser"))
                .must(QueryBuilders.termQuery("dataSetId", "99999"))
                .must(QueryBuilders.termQuery("delFlag", 0))
                .mustNot(QueryBuilders.existsQuery("userUrl"));
//        List<String> list = scrollDelete("janusgraph_janusgraph_allv_twitter_text", "janusgraph_allv_twitter_text");

        scrollSearchRequestVO.setKeepAlive(60);
        scrollSearchRequestVO.setQueryBuilder(queryBuilder);
        String scrollId = "";
        Set<String> list = new HashSet<>();
        while (true) {
            try {
                scrollSearchRequestVO.setScrollId(scrollId);
                ScrollSearchResponseVO scrollSearchResponseVO = searchUtil.scrollSearch(scrollSearchRequestVO);
                List<Map<String, Object>> data = scrollSearchResponseVO.getData();
                if (CollectionUtils.isNotEmpty(data)) {
                    for (Map<String, Object> datum : data) {
                        Object id = datum.get("_id");
                        if (id != null && StringUtils.isNotBlank(id.toString())) {
                            list.add(id.toString());
//                            if (datum.containsKey("userUrl")) {
//                                Object userUrl = datum.get("userUrl");
//                                if (userUrl != null && StringUtils.isNotBlank(userUrl.toString())) {
//                                    continue;
//                                }
//                                list.add(id.toString());
//                            } else {
//                                log.info("=========" + id);
//                                list.add(id.toString());
//                            }
                        }

                    }
                    scrollId = scrollSearchResponseVO.getScrollId();
                } else {
                    break;
                }


            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
        log.info("+" + list.size());
        return list;
    }

    public static void main(String[] args) {
        try {
//            List<JSONObject> newPostTime = getNewPostTime("turmuhammet.hashim,maya.mitalipova,urhun.uyghur,abduwali.ayup.3,omer.kanat.39");
//            System.out.println(newPostTime);
//            deleteEsFollow();
//            deleteTweets();

            String s = UnicodeUtil.toString("\\u0415\\u0432\\u0433\\u0435\\u043d\\u0438\\u0439");
            System.out.println(s);
            String a = UnicodeUtil.toString("a");
            System.out.println(a);

        } catch (Exception e) {
            log.info("===" + e.getMessage());
            e.printStackTrace();
        }


    }

    public static void deleteEsFollow() {
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchRequestVO searchRequestVO = new SearchRequestVO();

        searchRequestVO.setIndexName(new String[]{"js_facebook_user"});
        searchRequestVO.setType("user");


        boolQueryBuilder.must(QueryBuilders.termQuery("mediatname.keyword", "FbUser"))
                .must(QueryBuilders.termQuery("dataSetId", "99999"))
                .must(QueryBuilders.termQuery("userUrl", ""));
        searchRequestVO.setQueryBuilder(boolQueryBuilder);

        searchRequestVO.setPageNo(2);
        searchRequestVO.setPageSize(1000);
        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            int i = 0;
            if (CollectionUtil.isNotEmpty(data)) {
                for (Map<String, Object> datum : data) {
                    Object id = datum.get("_id");
                    delete("js_facebook_user", id.toString(), "user");
//                    if (datum.containsKey("userUrl")) {
//                        Object userUrl = datum.get("userUrl");
//                        if (userUrl == null || StringUtils.isBlank(userUrl.toString())) {
//                            log.info("+" + id);
//                            delete("js_facebook_user", id.toString(), "user");
//                        }
//                    } else {
//                        log.info("+" + id);
//                        delete("js_facebook_user", id.toString(), "user");
//                    }
                    i++;
                    System.out.println(i);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static List<JSONObject> getNewPostTime(String s) {

        List<String> asList = new ArrayList<>();
        if (StringUtils.isNotBlank(s)) {
            if (s.contains(",")) {
                String[] split = s.split(",");
                List<String> stringList = Arrays.asList(split);
                if (CollectionUtils.isNotEmpty(stringList)) {
                    for (String s1 : stringList) {
                        asList.add(s1);
                    }
                }
            } else {
                asList.add(s);
            }
        }
        log.info("====fb最新发文时间====" + s);
        List<JSONObject> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(asList)) {
            for (String s1 : asList) {
                JSONObject jsonObject = new JSONObject();
                String maxTime = "";
                try {
                    maxTime = getMaxTime(s1);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                jsonObject.put(s1, maxTime);
                result.add(jsonObject);
            }
        }
        return result;


    }

    public static String getMaxTime(String name) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        log.info("========fb最新发文时间======" + name);
        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setIndexName(new String[]{"js_facebook_post"});
        aggRequestVO.setType("post");

        boolQueryBuilder.must(QueryBuilders.termQuery("delFlag", "0"))
                .must(QueryBuilders.termQuery("userName", name))
                .must(QueryBuilders.termQuery("mediatname.keyword", "FbBook"))
                .must(QueryBuilders.existsQuery("timeLong"));

//        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("agg")
//                .field("username__STRING").subAggregation(AggregationBuilders.topHits("timeFormat__STRING")
//                        .sort("timeFormat__STRING", SortOrder.DESC).size(1))
//                .subAggregation(AggregationBuilders.terms("agg").field("timeFormat__STRING"));
        MaxAggregationBuilder field = AggregationBuilders.max("aggTIme").field("timeLong");
        aggRequestVO.setSize(10000);
        aggRequestVO.setQueryBuilder(boolQueryBuilder);
        aggRequestVO.setAggregationBuilder(field);
        try {
            List<JSONObject> jsonObjects = searchUtil.aggsThree(aggRequestVO);
            if (CollectionUtils.isNotEmpty(jsonObjects)) {
                JSONObject jsonObject = jsonObjects.get(0);
                if (jsonObject != null && jsonObject.containsKey("value")) {
                    return jsonObject.getString("value");
                } else {
                    return "";
                }
            }
//            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("=====查询用户最新发文时间异常=====");
            return "";
        }
        return "";
    }


//    @Scheduled(initialDelay = 100, fixedDelay = 1000 * 60 * 5)
    public void deleteCacheTweets() {
        Set<Object> objects = redisUtil.sGet("tw-delete-distinct-tweet");
        log.info("=====去重");
        if (CollectionUtil.isNotEmpty(objects)) {

            Iterator<Object> iterator = objects.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                try {

                    delete("js_fb_user", next.toString(), "user");
                    log.info("=====删除====" + next);
                    redisUtil.setRemove("tw-delete-distinct-tweet", next.toString());
                } catch (Exception e) {
                    log.info("删除重复tweet异常=====" + e.getMessage());
                    redisUtil.sSet("tw-delete-distinct-tweet", next.toString());
                    e.printStackTrace();
                }
            }
        }

    }


}
