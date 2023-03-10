package com.es.service.es;

import com.es.service.SearchResponseVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

/**
 * @ClassName SearchAfterTest
 * @Description TODO
 * @Author QiBin
 * @Date 2021/7/516:45
 * @Version 1.0
 **/
public class SearchAfterTest {
    public static void main(String[] args) {
        EsSearchAfterHandler esSearchAfterHandler = new EsSearchAfterHandler();

        BoolQueryBuilder b = QueryBuilders.boolQuery();
        b.must(QueryBuilders.termQuery("datasetId__STRING", "948"));

        SearchResponseVO searchResponseVO = esSearchAfterHandler.searchEs(b);
        Long totalCount = searchResponseVO.getTotalCount();
        List data = searchResponseVO.getData();
        System.out.println(totalCount);
        System.out.println(data.size());
    }
}
