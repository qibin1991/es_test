package com.es.service.school;

import com.es.service.SearchResponseVO;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @ClassName ScrolUpdate
 * @Description TODO
 * @Author QiBin
 * @Date 2021/10/2217:29
 * @Version 1.0
 **/
public class ScrolUpdate {

    public static void main(String[] args) {

    }


    public void searchEs(BoolQueryBuilder boolQueryBuilder) {
        // example: 127.0.0.1
        TransportClient esClient = null;
        try {
            esClient = buildEsTransportClient("elasticsearch", "192.168.242.230", 9300);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        int batchSize = 1000;
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch("transcontent_index")
                .setTypes("transcontent")
                .setQuery(boolQueryBuilder)
                .setSize(batchSize);
        List<Map<String, Object>> list = new ArrayList<>();
        SearchResponseVO<Map<String, Object>> searchResponseVO = new SearchResponseVO<>();

        long id1 = searchForHit(searchRequestBuilder, searchHit -> {
            // your code ...

            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            sourceAsMap.put("_id", id);
            list.add(sourceAsMap);
        });




    }
    private  TransportClient buildEsTransportClient(String esCluster, String esIp, Integer esPort) {




        Settings esSettings = Settings.builder()
                .put("cluster.name", esCluster)
                .build();
        TransportClient transportClient = null;
        try {
            transportClient = new PreBuiltTransportClient(esSettings);
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(esIp), 9300);
            transportClient.addTransportAddresses(transportAddress);

//            return new PreBuiltTransportClient(esSettings)
//                    .addTransportAddress(new TransportAddress(InetAddress.getByName(esIp), esPort));
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return transportClient;
    }

    /**
     * Search for hit.
     *
     * @param requestBuilder the request builder
     * @param consumer       the consumer
     */
    public long searchForHit(SearchRequestBuilder requestBuilder, Consumer<SearchHit> consumer) {
        return searchForResponse(requestBuilder, searchResponse -> {
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                consumer.accept(searchHit);
            }
        });
    }

    /**
     * Search for response.
     *
     * @param requestBuilder the request builder
     * @param consumer       the consumer
     */
    public long searchForResponse(SearchRequestBuilder requestBuilder, Consumer<SearchResponse> consumer) {
        if (requestBuilder == null || consumer == null) {
            return 0;
        }
        //requestBuilder.setSize(100); //在构建查询条件时，即可设置大小
        //设置排序字段
        SearchRequestBuilder sortBuilder = requestBuilder.addSort("time", SortOrder.ASC);
        SearchResponse searchResponse = sortBuilder.get();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        long totalHits = searchResponse.getHits().getTotalHits();
        while (searchHits.length > 0) {
            consumer.accept(searchResponse);
            SearchHit last = searchHits[searchHits.length - 1];
            sortBuilder = sortBuilder.searchAfter(last.getSortValues());
            searchResponse = sortBuilder.get();
            searchHits = searchResponse.getHits().getHits();
        }
        return totalHits;
    }

}
