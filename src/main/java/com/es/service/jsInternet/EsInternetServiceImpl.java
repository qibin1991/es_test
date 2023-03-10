package com.es.service.jsInternet;

import com.alibaba.fastjson.JSONObject;
import com.es.service.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName EsInternetServiceImpl
 * @Description TODO
 * @Author QiBin
 * @Date 2020/12/1417:38
 * @Version 1.0
 **/
@Slf4j
@Component
public class EsInternetServiceImpl {



    //点赞数
    public static void getEsabulousCount(String username) {

        ScrollSearchRequestVO scrollSearchRequestVO = new ScrollSearchRequestVO();
        scrollSearchRequestVO.setLimit(1000);

        scrollSearchRequestVO.setIndexName(new String[]{"js_twitter_e"});
        scrollSearchRequestVO.setType("edge");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        scrollSearchRequestVO.setQueryBuilder(queryBuilder);
        scrollSearchRequestVO.setKeepAlive(60);
        String scrollId = "";
        BoolQueryBuilder should = queryBuilder.should(QueryBuilders.termQuery("username__STRING", username))
                .should(QueryBuilders.termQuery("retweetScreenName__STRING", username));

        queryBuilder
                .must(should)
                .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterUser"))
                .must(QueryBuilders.termQuery("datasetId__STRING", "9999"));
        List<String> results = new ArrayList<>();
        TwoAggreationUtil serviceUtil = new TwoAggreationUtil();
        while (true) {
            try {
                scrollSearchRequestVO.setScrollId(scrollId);
                ScrollSearchResponseVO scrollSearchResponseVO = serviceUtil.scrollSearch(scrollSearchRequestVO);
                log.info("=游标==" + scrollSearchResponseVO.getScrollId());
                List<Map<String, Object>> data = scrollSearchResponseVO.getData();

                if (!CollectionUtils.isEmpty(data)) {
//                        for (Map<String, Object> datum : data) {
//                            BeanUtil.mapToBean(datum, LinkedinEdge.class, true);
//                        }

                    for (Map<String, Object> datum : data) {
                        datum.get("");
                    }
                    scrollId = scrollSearchResponseVO.getScrollId();
                } else {
                    break;
                }
            } catch (IOException e) {
                log.info("======查询异常" + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }


    }

    //取话题  被提及
    public static Long getEsTopicOutCount(String tagJson) {
        TwoAggreationUtil serviceUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        searchRequestVO.setType("edge");
        searchRequestVO.setIndexName(new String[]{"js_twitter_e"});
        searchRequestVO.setPageSize(2);
        searchRequestVO.setPageNo(1);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("datasetId", "9999"))
                .must(QueryBuilders.termQuery("endV", tagJson))
                .must(QueryBuilders.termQuery("mediatname", "topic"));
        searchRequestVO.setQueryBuilder(queryBuilder);

        try {
            SearchResponseVO search = serviceUtil.search(searchRequestVO);
            Long totalCount = search.getTotalCount();
            return totalCount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;

    }

    //取 user 提及话题
    public Long getEsTopicInCount(String username) {
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        searchRequestVO.setType("edge");
        searchRequestVO.setIndexName(new String[]{"js_twitter_e"});
        searchRequestVO.setPageSize(2);
        searchRequestVO.setPageNo(1);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder
                .mustNot(QueryBuilders.termQuery("startV", ""))
                .must(QueryBuilders.termQuery("datasetId", "9999"))
                .must(QueryBuilders.termQuery("startV", username))
                .mustNot(QueryBuilders.termQuery("endV", ""))
                .must(QueryBuilders.termQuery("mediatname", "topic"));
        searchRequestVO.setQueryBuilder(queryBuilder);
        TwoAggreationUtil serviceUtil = new TwoAggreationUtil();
        try {
            SearchResponseVO search = serviceUtil.search(searchRequestVO);
            Long totalCount = search.getTotalCount();
            return totalCount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    public static List<JSONObject> getEsTop30(Set<String> list, String type) {

        ArrayList<String> strings = new ArrayList<>(list);
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        AggRequestVO aggRequestVO = new AggRequestVO();

        AggregationBuilder aggregationBuilder = null;


        aggRequestVO.setIndexName(new String[]{"js_twitter_e"});
        aggRequestVO.setType("edge");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        queryBuilder
                .mustNot(QueryBuilders.termQuery("startV", ""))
                .must(QueryBuilders.termsQuery("startV", strings))
                .mustNot(QueryBuilders.termQuery("endV", ""))
                .must(QueryBuilders.termQuery("mediatname", type))
                .must(QueryBuilders.termQuery("datasetId", "9999"));
        aggregationBuilder = AggregationBuilders.terms("agg").field("endV")
                .order(BucketOrder.count(false)).size(30);


        aggRequestVO.setQueryBuilder(queryBuilder);
        aggRequestVO.setAggregationBuilder(aggregationBuilder);

        try {
            List<JSONObject> aggs = searchUtil.aggs(aggRequestVO);
            return aggs;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("======查询top30 话题 异常");
        }

        return new ArrayList<>();
    }

    public void getEsMentionTop30() {

    }
}
