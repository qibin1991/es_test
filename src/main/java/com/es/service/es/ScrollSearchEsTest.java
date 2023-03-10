package com.es.service.es;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.es.service.ESConnection;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScrollSearchEsTest
 * @Description TODO
 * @Author QiBin
 * @Date 2021/4/16下午1:36
 * @Version 1.0
 **/
public class ScrollSearchEsTest {


    public static void main(String[] args) {

        try {
            SearchRequest scrollSearchRequestVO = new SearchRequest();
            scrollSearchRequestVO.indices(new String[]{""});

            scrollSearchRequestVO.types("");

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();


            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(10000);

            scrollSearchRequestVO.source(searchSourceBuilder);
            scrollSearchRequestVO.scroll(TimeValue.timeValueSeconds(120));
            SearchResponse search = ESConnection.getClient().search(scrollSearchRequestVO, RequestOptions.DEFAULT);

            SearchHits hits = search.getHits();
            long totalHits = hits.getTotalHits();

            String scrollId = search.getScrollId();

            List<Map> results = new ArrayList<>();
            toJson(results, hits);
            int i = 0;
            long n = 0;
            long pageSize = totalHits % 100 == 0 ? totalHits / 100 : totalHits / 100 + 1;

            String path = "/Users/qibin/zyyt_java/janus-test-e";
            while (true) {
                if (results.size() == 20 * 1000) {
                    BigExcelWriter writer = ExcelUtil.getBigWriter(path + "-v" + i + ".xlsx");
                    i++;
                    List<Map> maps = CollUtil.newArrayList(results);
                    // 一次性写出内容，使用默认样式
                    writer.write(maps);
                    // 关闭writer，释放内存
                    writer.close();
                    results.clear();
                }
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
                searchScrollRequest.scroll(TimeValue.timeValueSeconds(120));
                SearchResponse scrollResponse = ESConnection.getClient().scroll(searchScrollRequest, RequestOptions.DEFAULT);
                SearchHits scrollHits = scrollResponse.getHits();
                toJson(results, scrollHits);
                scrollId = scrollResponse.getScrollId();

                if (n == pageSize) {
                    if (CollectionUtil.isNotEmpty(results)) {
                        BigExcelWriter writer = ExcelUtil.getBigWriter(path + "-v" + i);
                        i++;
                        // 一次性写出内容，使用默认样式
                        writer.write(results);
                        // 关闭writer，释放内存
                        writer.close();
                        results.clear();
                    }
                    break;
                }
                n++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //pubdate、title、mediaNameZh、text、url、domain
    public static void toJson(List<Map> results, SearchHits hits) {
        hits.iterator().forEachRemaining(hit -> {
            JSONObject obj = JSONObject.parseObject(JSON.toJSONString(hit.getSourceAsMap()));
            String pubdate = "##";
            String title = "##";
            String mediaNameZh = "##";
            String text = "##";
            String url = "##";
            String domain = "##";
            if (obj.containsKey("pubdate"))
                pubdate = obj.getString("pubdate");
            if (obj.containsKey("title"))
                title = obj.getString("title");
            if (obj.containsKey("mediaNameZh"))
                mediaNameZh = obj.getString("mediaNameZh");
            if (obj.containsKey("text"))
                text = obj.getString("text");
            if (obj.containsKey("url"))
                url = obj.getString("url");
            if (obj.containsKey("domain"))
                domain = obj.getString("domain");
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("pubdate", pubdate);
            row1.put("title", title);
            row1.put("mediaNameZh", mediaNameZh);
            row1.put("text", text);
            row1.put("url", url);
            row1.put("domain", domain);

            results.add(row1);

        });
    }

    public static void getDataset(Map<String, String> LanMap, String scrollId) throws IOException {
        while (true) {
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            searchScrollRequest.scroll(TimeValue.timeValueSeconds(120));
            SearchResponse scrollResponse = ESConnection.getClient().scroll(searchScrollRequest, RequestOptions.DEFAULT);
            SearchHits scrollHits = scrollResponse.getHits();
            int size = LanMap.size();

            if (LanMap.size() == size) {
                break;
            }
            scrollId = scrollResponse.getScrollId();
        }
    }
}
