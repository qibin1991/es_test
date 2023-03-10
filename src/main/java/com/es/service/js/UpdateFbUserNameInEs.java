package com.es.service.js;

import com.es.service.ESConnection;
import com.es.service.SearchRequestVO;
import com.es.service.SearchResponseVO;
import com.es.service.TwoAggreationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UpdateFbUserNameInEs
 * @Description TODO
 * @Author QiBin
 * @Date 2021/4/12上午11:24
 * @Version 1.0
 **/
public class UpdateFbUserNameInEs {




    public static void updateUserNameInEs(){
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        searchRequestVO.setPageNo(1);
        searchRequestVO.setPageSize(10000);

        searchRequestVO.setIndexName(new String[]{"js_qd_fb_post"});
        searchRequestVO.setType("post");
        boolQueryBuilder.must(QueryBuilders.rangeQuery("inputTime").from("1617899677000"));
        searchRequestVO.setQueryBuilder(boolQueryBuilder);
        int i = 0;
        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            if (CollectionUtils.isNotEmpty(data)){
                for (Map<String, Object> datum : data) {
                    Object userName = datum.get("userName");
                    if (userName != null && StringUtils.isNotBlank(userName.toString())){
                        if (userName.toString().contains("/")){
                            String id = datum.get("_id").toString();
                            String substring = userName.toString().substring(0, userName.toString().length() - 1);
                            updateEsUserNameInEs(id, substring);

                            System.out.println(i);
                            i++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateEsUserNameInEs(String id,String username){
        XContentBuilder source = null;
        try {
            source = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("userName", username)//browseNum：要修改的字段名，num 修改的值
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            UpdateRequest updateRequest = new UpdateRequest("js_qd_fb_post"
                    , "post", id).doc(source);
            UpdateResponse update = ESConnection.getClient().update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        updateUserNameInEs();
    }
}
