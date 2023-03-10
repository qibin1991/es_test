package com.es.fb;

import com.alibaba.fastjson.JSONObject;
import com.es.service.AggRequestVO;
import com.es.service.SearchRequestVO;
import com.es.service.SearchResponseVO;
import com.es.service.TwoAggreationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import java.io.IOException;
import java.util.*;

/*
 * @ClassName FbTestJs
 * @Description TODO
 * @Author QiBin
 * @Date 2021/5/1316:04
 * @Version 1.0
 **/
public class FbTestJs {

    public static void main(String[] args) {
        List<JSONObject> jsonObjects = userTopics("gene.bunin");
        System.out.println(jsonObjects);
    }


    public static List<JSONObject> userTopics(String username) {

        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        List<String> usernames = new ArrayList<>();
        if (username.contains(",")) {
            usernames.addAll(Arrays.asList(username.split(",")));
        } else {
            usernames.add(username);
        }


        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        BoolQueryBuilder should = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("endV.keyword", usernames));

        boolQueryBuilder1.must(QueryBuilders.termQuery("mediatname.keyword", "topic"))
                .must(should)
                .must(QueryBuilders.termQuery("dataSetId", "99999"));

        SearchRequestVO searchRequestVO = new SearchRequestVO();
        searchRequestVO.setQueryBuilder(boolQueryBuilder1);
        searchRequestVO.setIndexName(new String[]{"js_qd_fb_e"});
        searchRequestVO.setType("edge");
        searchRequestVO.setPageSize(10000);
        Set<String> list = new HashSet<>();
        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            if (CollectionUtils.isNotEmpty(data)) {
                for (Map<String, Object> datum : data) {
                    Object startV = datum.get("startV");
                    if (startV != null && StringUtils.isNotBlank(startV.toString())) {
                        list.add(startV.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setIndexName(new String[]{"js_qd_fb_e"});
        aggRequestVO.setType("edge");

        AggregationBuilder aggregationBuilder = null;
        List<JSONObject> resultList = new ArrayList();
        boolQueryBuilder.must(QueryBuilders.termQuery("mediatname.keyword", "topic"))
                .must(QueryBuilders.termsQuery("startV.keyword", list));
        aggregationBuilder = AggregationBuilders.terms("sum")
                .field("startV.keyword").size(10000);
        aggRequestVO.setAggregationBuilder(aggregationBuilder);
        aggRequestVO.setQueryBuilder(boolQueryBuilder);
//        aggRequestVO.setSize(30);

        List<JSONObject> data = new ArrayList<>();
//        Map<String, Integer> res = new HashMap<>();
        try {
            List<JSONObject> aggs = searchUtil.aggs(aggRequestVO);
            if (CollectionUtils.isNotEmpty(aggs)) {
                if (aggs.size() > 30) {
                    aggs = aggs.subList(0, 30);
                }

                for (JSONObject agg : aggs) {
                    JSONObject node = agg.getJSONObject("node");
                    data.add(node);
                }
            }

//            SearchResponseVO search = searchUtil.search(searchRequestVO);
//            List<Map<String,Object>> data = search.getData();
//            if (res.size() > 30) {
//                res.entrySet()
//                        .stream()
//                        .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
//                        .collect(Collectors.toList()).subList(0, 30).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
//                return finalOut;
//            }

            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
