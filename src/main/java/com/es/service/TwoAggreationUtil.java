package com.es.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by sangpeng on 2018/11/29.
 */
@Component
public class TwoAggreationUtil {

    private static final Logger logger = LoggerFactory.getLogger(TwoAggreationUtil.class);


    /**
     * 普通检索
     * @param searchRequestVO
     * @return
     * @throws IOException
     */
    public SearchResponseVO search(SearchRequestVO searchRequestVO) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(searchRequestVO.getIndexName());
        searchRequest.types(searchRequestVO.getType());
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(searchRequestVO.getQueryBuilder());
        searchSourceBuilder.from(searchRequestVO.getStartFrom());
        searchSourceBuilder.size(searchRequestVO.getPageSize());
        if (StringUtils.isNotEmpty(searchRequestVO.getSortField())){
            searchSourceBuilder.sort(searchRequestVO.getSortField(), searchRequestVO.getSortOrder());
        }
        if (searchRequestVO.getIncludeFields().length>0 || searchRequestVO.getExcludeFields().length>0){
            searchSourceBuilder.fetchSource(searchRequestVO.getIncludeFields(), searchRequestVO.getExcludeFields());
        }
        searchRequest.source(searchSourceBuilder);
        logger.info("检索索引：{},\n检索语句：{}", Arrays.toString(searchRequest.indices()), searchRequest.source().toString());
        System.out.println("检索索引："+Arrays.toString(searchRequest.indices())+ "\n检索语句："+searchRequest.source().toString());
        SearchResponse searchResponse = ESConnection.getClient().search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchResponseVO<Map<String, Object>> searchResponseVO = new SearchResponseVO<>();
        searchResponseVO.setTotalCount(searchHits.getTotalHits());
        List<Map<String, Object>> list = new ArrayList<>();
        searchHits.iterator().forEachRemaining(hit -> {
            String id = hit.getId();
            Map<String, Object> hMap= hit.getSourceAsMap();
            hMap.put("_id", id);
            list.add(hMap);
        });
        searchResponseVO.setData(list);
        return searchResponseVO;
    }

    /**
     * 滚动查询
     * @param scrollSearchRequestVO
     * @return
     * @throws IOException
     */
    public ScrollSearchResponseVO scrollSearch(ScrollSearchRequestVO scrollSearchRequestVO) throws IOException {

        if (StringUtils.isEmpty(scrollSearchRequestVO.getScrollId())) {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(scrollSearchRequestVO.getIndexName());
            searchRequest.types(scrollSearchRequestVO.getType());
//            searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(scrollSearchRequestVO.getQueryBuilder());
            searchSourceBuilder.size(scrollSearchRequestVO.getLimit());
            if (scrollSearchRequestVO.getIncludeFields().length>0 || scrollSearchRequestVO.getExcludeFields().length>0){
                searchSourceBuilder.fetchSource(scrollSearchRequestVO.getIncludeFields(), scrollSearchRequestVO.getExcludeFields());
            }
            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(TimeValue.timeValueSeconds(scrollSearchRequestVO.getKeepAlive()));
            logger.info("检索索引：{},\n检索语句：{}", Arrays.toString(searchRequest.indices()), searchRequest.source().toString());
            System.out.println("检索索引："+Arrays.toString(searchRequest.indices())+ "\n检索语句："+searchRequest.source().toString());
            SearchResponse searchResponse = ESConnection.getClient().search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();
            ScrollSearchResponseVO<Map<String, Object>> scrollSearchResponseVO = new ScrollSearchResponseVO<>();
            scrollSearchResponseVO.setScrollId(searchResponse.getScrollId());
            scrollSearchResponseVO.setTotalCount(searchHits.getTotalHits());
            List<Map<String, Object>> list = new ArrayList<>();
            searchHits.iterator().forEachRemaining(hit -> {
                String id = hit.getId();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                sourceAsMap.put("_id", id);
                list.add(sourceAsMap);
            });
            scrollSearchResponseVO.setData(list);
            return scrollSearchResponseVO;
        } else {
            return notFirstScrollSearch(scrollSearchRequestVO);
        }

    }

    /**
     * 非首次滚动查询
     * @param scrollSearchRequestVO
     * @return
     * @throws IOException
     */
    private ScrollSearchResponseVO notFirstScrollSearch(ScrollSearchRequestVO scrollSearchRequestVO) throws IOException {
        SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollSearchRequestVO.getScrollId());
        searchScrollRequest.scroll(TimeValue.timeValueSeconds(scrollSearchRequestVO.getKeepAlive()));
        SearchResponse searchResponse = ESConnection.getClient().scroll(searchScrollRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        ScrollSearchResponseVO<Map<String, Object>> scrollSearchResponseVO = new ScrollSearchResponseVO<>();
        scrollSearchResponseVO.setScrollId(searchResponse.getScrollId());
        scrollSearchResponseVO.setTotalCount(searchHits.getTotalHits());
        List<Map<String, Object>> list = new ArrayList<>();
        searchHits.iterator().forEachRemaining(hit -> {
            list.add(hit.getSourceAsMap());
        });
        scrollSearchResponseVO.setData(list);
        return scrollSearchResponseVO;
    }


    /**
     * 清除滚动查询游标
     * @param scrollId
     * @return
     * @throws IOException
     */
    public boolean clearScroll(String scrollId) throws IOException {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = ESConnection.getClient()
                .clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        return clearScrollResponse.isSucceeded();
    }
    /**
     * 聚合（对一/二级聚合友好）
     * @param aggRequestVO
     */
    public List<JSONObject> aggs(AggRequestVO aggRequestVO) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(aggRequestVO.getIndexName());
        searchRequest.types(aggRequestVO.getType());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(aggRequestVO.getQueryBuilder());
        searchSourceBuilder.aggregation(aggRequestVO.getAggregationBuilder());
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        logger.info("检索索引：{},\n检索语句：{}", Arrays.toString(searchRequest.indices()), searchRequest.source().toString());
        System.out.println("检索索引："+Arrays.toString(searchRequest.indices())+ "\n检索语句："+searchRequest.source().toString());
        Aggregations aggregations = ESConnection.getClient().search(searchRequest, RequestOptions.DEFAULT)
                .getAggregations();
        List<JSONObject> list = new ArrayList<>();
        aggResultParser(aggregations, list, null);
        return list;
    }

    /**
     * 聚合结果解析
     * @param aggregations
     * @param list
     * @param node
     */
    private void aggResultParser(Aggregations aggregations, List<JSONObject> list, JSONObject node) {
        aggregations.iterator().forEachRemaining(aggregation -> {
            Object obj = aggregations.get(aggregation.getName());
            if (obj instanceof Terms) {
                Terms terms = (Terms) obj;
                terms.getBuckets().forEach(bucket -> {
                    bucketParser(list, node, bucket.getKeyAsString(), bucket.getDocCount(), bucket.getAggregations());
                });
            } else if(obj instanceof ParsedDateHistogram){
                ParsedDateHistogram terms = (ParsedDateHistogram) obj;
                terms.getBuckets().forEach(bucket -> {
                    bucketParser(list, node, bucket.getKeyAsString(), bucket.getDocCount(), bucket.getAggregations());
                });
            }
        });
    }

    /**
     * bucket 解析
     * @param list
     * @param node
     * @param keyAsString
     * @param docCount
     * @param aggregations
     */
    private void bucketParser(List<JSONObject> list,
                              JSONObject node,
                              String keyAsString,
                              long docCount,
                              Aggregations aggregations) {
        JSONObject childNode = new JSONObject();
        if (node == null) {
            node = new JSONObject();
            JSONObject parentNode = new JSONObject();
            parentNode.put(keyAsString, docCount);
            node.put("node", parentNode);
            list.add(node);
        } else {
            childNode = node.getJSONObject("childNode");
            if (childNode == null) {
                childNode = new JSONObject();
                node.put("childNode", childNode);
            }
            childNode.put(keyAsString, docCount);
        }
        if (!aggregations.asList().isEmpty()) {
            Aggregation aggregation = aggregations.get("agg");
            if (aggregation instanceof Sum) {
                Sum sum = (Sum) aggregation;
                System.out.println(sum.getValue());
                node.put("value",sum.getValue());
            }else if(aggregation instanceof Avg){
                Avg avg = (Avg) aggregation;
                System.out.println(avg.getValue());
                node.put("value",avg.getValue());
            }
            aggResultParser(aggregations, list, node);
        }
    }

    /**
     * 聚合（对三级聚合友好）
     * @param aggRequestVO
     */
    public List<JSONObject> aggsThree(AggRequestVO aggRequestVO) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(aggRequestVO.getIndexName());
        searchRequest.types(aggRequestVO.getType());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(aggRequestVO.getQueryBuilder());
        searchSourceBuilder.aggregation(aggRequestVO.getAggregationBuilder());
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        logger.info("检索索引：{},\n检索语句：{}", Arrays.toString(searchRequest.indices()), searchRequest.source().toString());
        System.out.println("检索索引："+Arrays.toString(searchRequest.indices())+ "\n检索语句："+searchRequest.source().toString());
        Aggregations aggregations = ESConnection.getClient().search(searchRequest, RequestOptions.DEFAULT)
                .getAggregations();
        List<JSONObject> list = new ArrayList<>();
        aggResultParserThree(aggregations, list, null);
        return list;
    }

    /**
     * 聚合结果解析
     * @param aggregations
     * @param list
     * @param node
     */
    private void aggResultParserThree(Aggregations aggregations, List<JSONObject> list, JSONObject node) {
        aggregations.iterator().forEachRemaining(aggregation -> {
            Object obj = aggregations.get(aggregation.getName());
            if (obj instanceof Terms) {
                Terms terms = (Terms) obj;
                terms.getBuckets().forEach(bucket -> {
                    bucketParserThree(list, node, bucket.getKeyAsString(), bucket.getDocCount(), bucket.getAggregations());
                });
            } else if(obj instanceof ParsedDateHistogram){
                ParsedDateHistogram terms = (ParsedDateHistogram) obj;
                terms.getBuckets().forEach(bucket -> {
                    bucketParserThree(list, node, bucket.getKeyAsString(), bucket.getDocCount(), bucket.getAggregations());
                });
            }else if(obj instanceof Sum){
                Sum sum = (Sum)obj;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value",sum.getValue());
                list.add(jsonObject);
            }else if(obj instanceof Avg){
                Avg avg = (Avg)obj;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value",avg.getValue());
                list.add(jsonObject);
            }else if(obj instanceof Max){
                Max avg = (Max)obj;
                JSONObject jsonObject = new JSONObject();
                double value = avg.getValue();
                if (value > 0){
                    BigDecimal bigDecimal = new BigDecimal(avg.getValue());
                    jsonObject.put("value",bigDecimal.toPlainString());
                    list.add(jsonObject);
                }

            }
        });
    }

    /**
     * bucket 解析
     * @param list
     * @param node
     * @param keyAsString
     * @param docCount
     * @param aggregations
     */
    private void bucketParserThree(List<JSONObject> list, JSONObject node, String keyAsString, long docCount, Aggregations aggregations) {
        JSONObject childNode = new JSONObject();
        if (node == null) {
            node = new JSONObject();
            JSONObject parentNode = new JSONObject();
            parentNode.put(keyAsString, docCount);
            node.put("node", parentNode);
            list.add(node);
        } else {
            childNode = node.getJSONObject("childNode");
            if (childNode == null) {
                childNode = new JSONObject();
                node.put("childNode", childNode);
            }
            childNode.put(keyAsString, docCount);
        }
        if (!aggregations.asList().isEmpty()) {
            JSONObject value = node.getJSONObject("value");
            if(value == null){
                value = new JSONObject();
            }
            JSONObject agg = new JSONObject();
            Aggregation aggregation = aggregations.get("agg");
            if (aggregation instanceof Sum) {
                Sum sum = (Sum) aggregation;
                value.put(keyAsString,sum.getValue());
            }else if(aggregation instanceof Avg){
                Avg avg = (Avg) aggregation;
                value.put(keyAsString,avg.getValue());
            }
            if(value.size()>0){
                node.put("value",value);
            }
            aggResultParserThree(aggregations, list, node);
        }
    }



    /**
     * 日期直方图分组统计
     * @param index
     * @param type
     * @param histogram 间隔时间，格式：1s/1m/1h/1d/1w/1M/1q/1y,详情见 DateHistogramInterval
     * @param groupCol 目前解析只支持四层分组统计结果，可增加
     * @param match 过滤字段，Map格式，可传入多个
     * @param size
     * @param startTime
     * @param endTime
     * @return
     */
//    public static List<Map<String, Object>> getHistogramSubCountList(String index, String type, String histogram, String groupCol, Map<String, Object> match, int size, long startTime, long endTime){
//        List<Map<String, Object>> listMap = new LinkedList<>();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SearchRequestBuilder searchRequest = client.prepareSearch(index);
//        if (StringUtils.isNotEmpty(type)) {
//            searchRequest.setTypes(type.split(","));
//        }
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(
//                QueryBuilders.rangeQuery("@timestamp")
//                        .from(startTime)
//                        .to(endTime)
//                        .includeLower(true)
//                        .includeUpper(true));
//        if (match != null) {
//            for (Map.Entry<String, Object> entry : match.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                if (value != null && !value.equals(""))
//                    boolQueryBuilder.must(QueryBuilders.termsQuery(key, value));
//            }
//        }
//
//        DateHistogramBuilder db = AggregationBuilders.dateHistogram("ts").field("processTime").interval(new DateHistogramInterval(histogram));
//
//        String[] groupCols = groupCol.split(",");
//        AggregationBuilder tb = null;
//        AggregationBuilder stb = null;
//        for (int i = 0; i < groupCols.length; i++) {
//            if (tb == null) {
//                tb = AggregationBuilders.terms(i + "").field(groupCols[i]).size(size);
//                stb = tb;
//            }
//            else{
//                AggregationBuilder ntb = AggregationBuilders.terms(i + "").field(groupCols[i]).size(size);
//                stb.subAggregation(ntb);
//                stb = ntb;
//            }
//        }
//        db.subAggregation(tb);
//
//        searchRequest.setQuery(boolQueryBuilder)
//                .setSearchType(SearchType.QUERY_THEN_FETCH)
//                .addAggregation(db);
//        SearchResponse searchResponse = searchRequest.execute().actionGet();
//
//        InternalHistogram ts = searchResponse.getAggregations().get("ts");
//        List<InternalHistogram.Bucket> buckets = ts.getBuckets();
//        for (InternalHistogram.Bucket bt : buckets) {
//            String processTime = bt.getKeyAsString();
//            Terms terms = bt.getAggregations().get("0");
//            for (Terms.Bucket bucket : terms.getBuckets()) {
//                String srcAddress = (String) bucket.getKey();
//                if(groupCols.length == 4) {
//                    Terms terms1 = bucket.getAggregations().get("1");
//                    for (Terms.Bucket bucket1 : terms1.getBuckets()) {
//                        Terms terms2 = bucket1.getAggregations().get("2");
//                        for (Terms.Bucket bucket2 : terms2.getBuckets()) {
//                            Terms terms3 = bucket2.getAggregations().get("3");
//                            for (Terms.Bucket bucket3 : terms3.getBuckets()) {
//                                Long docCount = bucket3.getDocCount();
//                                Map<String, Object> map = new HashMap<>();
//                                map.put("processTime", processTime);
//                                map.put(groupCols[0], bucket.getKey());
//                                map.put(groupCols[1], bucket1.getKey());
//                                map.put(groupCols[2], bucket2.getKey());
//                                map.put(groupCols[3], bucket3.getKey());
//                                map.put("docCount", docCount.intValue());
//                                listMap.add(map);
//                            }
//                        }
//
//                    }
//                } else if(groupCols.length == 3) {
//                    Terms terms1 = bucket.getAggregations().get("1");
//                    for (Terms.Bucket bucket1 : terms1.getBuckets()) {
//                        Terms terms2 = bucket1.getAggregations().get("2");
//                        for (Terms.Bucket bucket2 : terms2.getBuckets()) {
//                            Long docCount = bucket2.getDocCount();
//                            Map<String, Object> map = new HashMap<>();
//                            map.put("processTime", processTime);
//                            map.put(groupCols[0], bucket.getKey());
//                            map.put(groupCols[1], bucket1.getKey());
//                            map.put(groupCols[2], bucket2.getKey());
//                            map.put("docCount", docCount.intValue());
//
//                            listMap.add(map);
//                        }
//
//                    }
//                } else if (groupCols.length == 2) {
//                    Terms terms1 = bucket.getAggregations().get("1");
//                    for (Terms.Bucket bucket1 : terms1.getBuckets()) {
//                        Long docCount = bucket1.getDocCount();
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("processTime", processTime);
//                        map.put(groupCols[0], bucket.getKey());
//                        map.put(groupCols[1], bucket1.getKey());
//                        map.put("docCount", docCount.intValue());
//
//                        listMap.add(map);
//                    }
//                } else {
//                    Long docCount = bucket.getDocCount();
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("processTime", processTime);
//                    map.put(groupCols[0], bucket.getKey());
//                    map.put("docCount", docCount.intValue());
//                    listMap.add(map);
//                }
//            }
//        }
//        return listMap;
//    }
}
