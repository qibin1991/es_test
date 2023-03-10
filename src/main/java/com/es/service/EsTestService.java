package com.es.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName EsTestService
 * @Description TODO
 * @Author QiBin
 * @Date 2020/9/2916:42
 * @Version 1.0
 **/
@Service
public class EsTestService {






    public static List<JSONObject> queryCommentAnalysis(JSONObject jsonObject) {

        String datasetId = jsonObject.getString("datasetId");
        String type = jsonObject.getString("type");
        int num = jsonObject.getIntValue("num");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();

        AggRequestVO aggRequestVO = new AggRequestVO();
        if (0 == num) {
            aggRequestVO.setIndexName(new String[]{"js_saveas_tweet"});
            aggRequestVO.setType("tweet");
        } else if (1 == num) {
            aggRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
            aggRequestVO.setType("janusgraph_allv_twitter_text");
        }

        boolQueryBuilder.must(QueryBuilders.termQuery("datasetId__STRING", datasetId))
                .must(QueryBuilders.termQuery("delFlag", "0"));
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("agg")
                .field("username__STRING").size(10000);

        aggRequestVO.setQueryBuilder(boolQueryBuilder);
        aggRequestVO.setAggregationBuilder(aggregationBuilder);
        List<String> names = new ArrayList<>();

        List resultList = getEsData(aggRequestVO);
        if (CollectionUtil.isNotEmpty(resultList)) {
            for (Object o : resultList) {
                if (o instanceof JSONObject) {
                    JSONObject node = ((JSONObject) o).getJSONObject("node");
                    Set<String> strings = node.keySet();
                    if (CollectionUtil.isNotEmpty(strings)) {
                        for (String string : strings) {
                            names.add(string.toLowerCase());
                        }
                    }
                }
            }
        }
        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        TwoAggreationUtil su = new TwoAggreationUtil();
        AggregationBuilder ag = null;
        AggRequestVO ar = new AggRequestVO();
        switch (type) {
            case "1":
                //主贴
                ar.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
                ar.setType("janusgraph_allv_twitter_text");
                bq.must(QueryBuilders.termsQuery("username__STRING", names))
                        .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"))
                        .must(QueryBuilders.termQuery("delFlag", "0"));
                ag = AggregationBuilders.terms("sum")
                        .field("username__STRING").size(10000);

                break;
            case "2":
                //转发

                ar.setIndexName(new String[]{"js_twitter_e"});
                ar.setType("edge");
                bq.must(QueryBuilders.termsQuery("endV", names))
                        .must(QueryBuilders.termQuery("mediatname", "forward"));
                ag = AggregationBuilders.terms("agg")
                        .field("startV").size(10000);
                break;
            case "3":
                //评论
//                ar.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_comment"});
//                ar.setType("janusgraph_allv_twitter_comment");
//                bq.must(QueryBuilders.termsQuery("username__STRING", names))
//                        .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterComment"))
//                        .must(QueryBuilders.termQuery("delFlag", "0"))
//                        .must(QueryBuilders.termQuery("datasetId", "9999"));
//                ag = AggregationBuilders.terms("sum")
//                        .field("username__STRING.keyword").size(10000);

                ar.setIndexName(new String[]{"js_twitter_e"});
                ar.setType("edge");
                bq.must(QueryBuilders.termsQuery("endV", names))
                        .must(QueryBuilders.termQuery("mediatname", "comment"));
                ag = AggregationBuilders.terms("agg")
                        .field("startV").size(10000);

                break;
            default:

                break;
        }
        ar.setAggregationBuilder(ag);
        ar.setQueryBuilder(bq);
        try {
            List<JSONObject> jsonObjects = searchUtil.aggsThree(ar);
            return jsonObjects;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ArrayList<>();
    }







    /**
     * 用户最新发文时间
     */
    public static List<JSONObject> getNewPostTime(String datasetId) {

        List<String> asList = new ArrayList<>();

        asList.add("manyuen_ng");
        asList.add("cheungchiuhung");


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();

        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setIndexName(new String[]{"janusgraph_allv_twitter_text"});
        aggRequestVO.setType("allV_twitter_text");

        boolQueryBuilder.must(QueryBuilders.termQuery("delFlag", "0"))
                .must(QueryBuilders.termsQuery("username__STRING", asList));

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("user_post_time")
                .field("fullname__STRING").subAggregation(AggregationBuilders.topHits("timeStampStr__STRING")
                        .sort("timeFormat__STRING", SortOrder.DESC).size(1))
                .subAggregation(AggregationBuilders.terms("time").field("timeFormat__STRING"));

        aggRequestVO.setQueryBuilder(boolQueryBuilder);
        aggRequestVO.setAggregationBuilder(termsAggregationBuilder);
        try {
            List<JSONObject> jsonObjects = searchUtil.aggsThree(aggRequestVO);
            List<JSONObject> result = new ArrayList<>();

            for (JSONObject jsonObject : jsonObjects) {
                JSONObject res = new JSONObject();
                String node = jsonObject.getString("node");
                String substring1 = node.substring(node.indexOf("{") + 2, node.indexOf(":") - 1);

                res.put("name", substring1);


                if (jsonObject.containsKey("childNode")) {
                    String childNode = jsonObject.getString("childNode");
                    System.out.println(childNode);
                    if (childNode.contains(",")) {
                        String substring = childNode.substring(childNode.indexOf("{") + 2, childNode.indexOf(","));
                        String substring2 = substring.substring(0, substring.lastIndexOf(":") - 1);
                        res.put("time", substring2);
                    } else {
                        String substring = childNode.substring(childNode.indexOf("{") + 2, childNode.lastIndexOf(":"));
                        res.put("time", substring);
                    }


                }


                result.add(res);
            }


//            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return new ArrayList<>();
    }

    public static List queryAnalysisList(String datasetId, int type, int spiderType) {


        //js_saveas_tweet
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();

        AggRequestVO aggRequestVO = new AggRequestVO();


        aggRequestVO.setIndexName(new String[]{"js_saveas_tweet"});
        aggRequestVO.setType("tweet");


        aggRequestVO.setIndexName(new String[]{"janusgraph_allv_twitter_text"});
        aggRequestVO.setType("allV_twitter_text");
        boolQueryBuilder.must(QueryBuilders.termQuery("datasetId", datasetId))
                .must(QueryBuilders.termQuery("delFlag", "0"));

        AggregationBuilder aggregationBuilder = null;
        List resultList = new ArrayList();
        switch (spiderType) {
            case 1:
                //user
                break;
            case 2:
                //tweet
                switch (type) {
                    case 1:
                        TermsAggregationBuilder size = AggregationBuilders.terms("ids").field("userId").size(1);
                        aggRequestVO.setAggregationBuilder(size);
                        aggRequestVO.setQueryBuilder(boolQueryBuilder);

                        List<String> userIds = new ArrayList<>();
                        try {
                            List<JSONObject> jsonObjects = searchUtil.aggsThree(aggRequestVO);
                            if (!CollectionUtils.isEmpty(jsonObjects)) {
                                for (JSONObject jsonObject : jsonObjects) {
                                    String userId = jsonObject.getString("userId");
                                    userIds.add(userId);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return userIds;
                    //取userIdz


                    case 2:
                        //发帖账号
                        aggregationBuilder = AggregationBuilders.terms("sum").field("username__STRING");

//                        CardinalityAggregationBuilder field = AggregationBuilders.cardinality("md5id__STRING").field("md5id__STRING");

                        aggRequestVO.setAggregationBuilder(aggregationBuilder);
                        aggRequestVO.setQueryBuilder(boolQueryBuilder);
                        resultList = getEsData(aggRequestVO);
                        break;
                    case 3:
                        //发帖时间


                        break;

                    case 4:
                        //发帖地址
                        aggregationBuilder = AggregationBuilders.terms("sum")
                                .field("location__STRING");
                        TermsAggregationBuilder field = AggregationBuilders.terms("agg").field("md5id__STRING");
                        aggregationBuilder.subAggregation(field);
                        aggRequestVO.setQueryBuilder(boolQueryBuilder);
                        aggRequestVO.setAggregationBuilder(aggregationBuilder);
                        resultList = getEsData(aggRequestVO);
                        break;

                    default:
                        break;

                }
                break;
            default:
                break;
        }
        return resultList;

    }

    public static List<JSONObject> getEsData(AggRequestVO aggRequestVO) {
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        try {
            List<JSONObject> jsonObjects = searchUtil.aggs(aggRequestVO);
            return jsonObjects;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<JSONObject> postAnalysis(JSONObject jsonObject) {
        String username = jsonObject.getString("username");

        String beginTimeStr = jsonObject.getString("beginTime");
        String endTimeStr = jsonObject.getString("endTime");
        String type = jsonObject.getString("type");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        long beginTime = 0;
        long endTime = 0;

        if (StringUtils.isBlank(beginTimeStr) && StringUtils.isBlank(endTimeStr)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -30);
            Date time = cal.getTime();
            //起始时间
            beginTime = time.getTime() / 1000L;
            endTime = System.currentTimeMillis() / 1000L;
        } else {
            try {
                Date parse = simpleDateFormat.parse(beginTimeStr);
                Date parse1 = simpleDateFormat.parse(endTimeStr);
                beginTime = parse.getTime();
                endTime = parse1.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        AggRequestVO aggRequestVO = new AggRequestVO();

        AggregationBuilder aggregationBuilder = null;

        aggRequestVO.setIndexName(new String[]{"janusgraph_allv_twitter_text"});
        aggRequestVO.setType("allV_twitter_text");

        QueryBuilder queryBuilder = QueryBuilders.boolQuery();
        ((BoolQueryBuilder) queryBuilder).should(QueryBuilders.termQuery("retweetScreenName__STRING", username))
                .should(QueryBuilders.termQuery("username__STRING", username)).must(QueryBuilders.termQuery("delFlag", "0"));


        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("timeStampStr__STRING").from(beginTime).to(endTime);
        ((BoolQueryBuilder) queryBuilder).must(rangeQueryBuilder);
        aggRequestVO.setQueryBuilder(queryBuilder);

        switch (type) {
            case "hour":
                DateHistogramInterval hour = DateHistogramInterval.HOUR;
                String format = "yyyy-MM-dd HH";
                aggregationBuilder = AggregationBuilders.dateHistogram("count").field("timeStampStr__STRING").dateHistogramInterval(hour).format(format).minDocCount(1);

                aggRequestVO.setAggregationBuilder(aggregationBuilder);
                break;
            case "day":
                DateHistogramInterval dateHistogramInterval = DateHistogramInterval.DAY;
                String forMat = "yyyy-MM-dd";
                aggregationBuilder = AggregationBuilders.dateHistogram("count").field("timeStampStr__STRING").dateHistogramInterval(dateHistogramInterval).format(forMat).minDocCount(1);
                //将第二层封装到第一层
                aggRequestVO.setAggregationBuilder(aggregationBuilder);
                break;
            case "addr":
                aggregationBuilder = AggregationBuilders.terms("sum")
                        .field("location__STRING");

                aggRequestVO.setAggregationBuilder(aggregationBuilder);
                break;
            default:
                break;
        }
        List<JSONObject> list = new ArrayList<>();

        try {
            list = searchUtil.aggsThree(aggRequestVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean bulkUpdate(String indexName, String type, JSONArray idsAndDocs) {
        boolean isSuccess = false;
        BulkRequest bulkRequest = new BulkRequest();
        for (Object obj : idsAndDocs) {
            JSONObject idAndDoc = (JSONObject) obj;

            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(indexName);
            updateRequest.type(type);
            updateRequest.id(idAndDoc.getString("id"));
            updateRequest.doc(idAndDoc.getJSONObject("doc"));
            bulkRequest.add(updateRequest);
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        try {
            BulkResponse bulkResponse = ESConnection.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            if (!bulkResponse.hasFailures()) {
                isSuccess = true;
            } else {
//                log.error("批量更新有失败，具体消息{}" ,bulkResponse.buildFailureMessage());
            }
        } catch (IOException e) {
//            log.error("issues: ", e);
        }
        return isSuccess;
    }

    public void updateEs() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        searchRequestVO.setIndexName(new String[]{"janusgraph_allv_twitter_text"});
        searchRequestVO.setType("allV_twitter_text");
        searchRequestVO.setQueryBuilder(boolQueryBuilder);
        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List data = search.getData();
            for (Object datum : data) {
                if (datum instanceof Map) {
                    Integer time = (Integer) ((Map<?, ?>) datum).get("time");
                    Integer updatetime = (Integer) ((Map<?, ?>) datum).get("updateTimes");


                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void searchEsByLowcase() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        searchRequestVO.setIndexName(new String[]{"janus_post_bak_1"});
        searchRequestVO.setType("janusgraph_allv_twitter_text");

        boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("tweetsContent__STRING","#boycottbeijing"))
                .must(QueryBuilders.termQuery("datasetId__STRING", "1158"))
                .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"));

        searchRequestVO.setQueryBuilder(boolQueryBuilder);

        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            System.out.println(search.getTotalCount());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
//        List<JSONObject> list = getNewPostTime("9999");
////        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
////        List list = queryAnalysisList("9999", 2, 2);
//        System.out.println(list);
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type", "1");
        jsonObject.put("datasetId", "1178");
        jsonObject.put("num", 0);
//        jsonObject.put("username", "YNMlT1");
        jsonObject.put("type", "2");
//        jsonObject.put("beginTime", "");
//        jsonObject.put("endTime", "");

//        List<JSONObject> jsonObjects = postAnalysis(jsonObject);
//        System.out.println(jsonObjects);


        List<JSONObject> list = queryCommentAnalysis(jsonObject);
        System.out.println(list);
//        searchEsByLowcase();

    }




}
