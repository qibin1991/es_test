package com.es.service.js;

import com.es.service.AggRequestVO;
import com.es.service.TwoAggreationUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName JsonTest
 * @Description TODO
 * @Author QiBin
 * @Date 2021/1/25下午3:43
 * @Version 1.0
 **/
public class JsonTest {

    public static void main(String[] args) {
//        String s = "{\"timestamp\": 1610827800, \"crawl_time\": 1610936405, \"post_user\": \"Sheng Xue\", \"share_num\": \"0\", \"post_data\": \"Thank you for paying attention Thank you for shearing Thank you for supporting\", \"hashtag_list\": [], \"profilelink_list\": [], \"post_video\": \"https://www.facebook.com/IlhamTohtiInstitute/videos/127539565875465/?__cft__[0]=AZWoL_gzt56It6vmqiXqJlZov7c7_msCkf2ag4mfA6xWdP54jmQ-hWxfGXchN0Zzu_mLLYfrGJ_vimCD5lD8M3sVEKfAXp-Z2PuNN-C-UUnmwr0n6uOSKfV3gwMcJixYlSfFLaKwW8DQmM6iHwyRH7x3y-2Tk2rg-R3uOzeNFZ-VZQ&__tn__=%2B%3FFH-y-R\", \"post_pic\": \"\", \"post_link\": \"https://www.facebook.com/shengxue.ca/posts/10158702484214627?__cft__[0]=AZWoL_gzt56It6vmqiXqJlZov7c7_msCkf2ag4mfA6xWdP54jmQ-hWxfGXchN0Zzu_mLLYfrGJ_vimCD5lD8M3sVEKfAXp-Z2PuNN-C-UUnmwr0n6uOSKfV3gwMcJixYlSfFLaKwW8DQmM6iHwyRH7x3y-2Tk2rg-R3uOzeNFZ-VZQ&__tn__=%2CO%2CP-R\", \"post_id\": \"10158702484214627\", \"is_quote\": true, \"quote_fullname\": \"Ilham Tohti Institute\", \"quote_href\": \"https://www.facebook.com/IlhamTohtiInstitute/?__cft__[0]=AZWoL_gzt56It6vmqiXqJlZov7c7_msCkf2ag4mfA6xWdP54jmQ-hWxfGXchN0Zzu_mLLYfrGJ_vimCD5lD8M3sVEKfAXp-Z2PuNN-C-UUnmwr0n6uOSKfV3gwMcJixYlSfFLaKwW8DQmM6iHwyRH7x3y-2Tk2rg-R3uOzeNFZ-VZQ&__tn__=-UC%2CP-y-R\", \"quote_timestamp\": 1610750220, \"quote_text\": \"The Uyghur Issue(2):Commemoration of the 7th Anniversaryof Prof. Ilham Tohti's Imprisonment\", \"quote_hashtag_list\": [], \"quote_profilelink_list\": [], \"great_num\": 5, \"comment_num\": 1, \"comment_list\": [{\"comment_timestamp\": 0, \"comment_username\": \"甄倪\", \"comment_userhref\": \"https://www.facebook.com/profile.php?id=100012593957957&comment_id=Y29tbWVudDoxMDE1ODcwMjQ4NDIxNDYyN18xMDE1ODcwMzE5NzEwNDYyNw%3D%3D&__cft__[0]=AZWoL_gzt56It6vmqiXqJlZov7c7_msCkf2ag4mfA6xWdP54jmQ-hWxfGXchN0Zzu_mLLYfrGJ_vimCD5lD8M3sVEKfAXp-Z2PuNN-C-UUnmwr0n6uOSKfV3gwMcJixYlSfFLaKwW8DQmM6iHwyRH7x3y-2Tk2rg-R3uOzeNFZ-VZQ&__tn__=R]-R\", \"comment_userid\": \"profile.php\", \"comment_id\": \"Y29tbWVudDoxMDE1ODcwMjQ4NDIxNDYyN18xMDE1ODcwMzE5NzEwNDYyNw%3D%3D\", \"comment_text\": \"expression\"}]}";

//        JSONObject jsonObject = JSONUtil.parseObj(s);


//        System.out.println(jsonObject);



        relationships("ErkinAsiyaRadiosi", "mention", 1, 10);

    }
    public static void relationships(String username, String type, int pageNo, int pageSize) {

        TwoAggreationUtil serchUtil  = new TwoAggreationUtil();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder
                .must(QueryBuilders.termQuery("startV.keyword", username))
                .must(QueryBuilders.termQuery("delFlag", 0));


        AggRequestVO aggRequestVO = new AggRequestVO();

        aggRequestVO.setSize(10000);
        aggRequestVO.setIndexName(new String[]{"js_qd_fb_e"});
        aggRequestVO.setType("edge");

        AggregationBuilder aggregationBuilder = null;


        BoolQueryBuilder endShould = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("endV.keyword", username));
        BoolQueryBuilder startShould = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("startV.keyword", username));

        if ("follow".equals(type) || "topic".equals(type)) {
            boolQueryBuilder.must(endShould)
                    .must(QueryBuilders.termQuery("mediatname.keyword", type))
                    .mustNot(QueryBuilders.termQuery("startV.keyword", ""));
            aggregationBuilder = AggregationBuilders
                    .terms("agg").field("startV.keyword").size(2000).order(BucketOrder.count(false))
                    .subAggregation(
                            AggregationBuilders.topHits("top").size(1)
                    );
        } else if ("forwardTo".equals(type)) {
            boolQueryBuilder.must(endShould)
                    .mustNot(QueryBuilders.termQuery("startV.keyword", ""))
                    .must(QueryBuilders.termQuery("mediatname.keyword", "forward"));
            aggregationBuilder = AggregationBuilders
                    .terms("agg").field("startV.keyword").size(5000).order(BucketOrder.count(false))
                    .subAggregation(
                            AggregationBuilders.topHits("top").size(1)
                    );
        } else if ("friend".equals(type)) {
            boolQueryBuilder.must(startShould)
                    .mustNot(QueryBuilders.termQuery("endV.keyword", ""))
                    .must(QueryBuilders.termQuery("mediatname.keyword", "follow"));

            aggregationBuilder = AggregationBuilders
                    .terms("agg").field("endV.keyword").size(5000).order(BucketOrder.count(false))
                    .subAggregation(
                            AggregationBuilders.topHits("top").size(1)
                    );
        } else {
            boolQueryBuilder.must(startShould)
                    .must(QueryBuilders.termQuery("mediatname.keyword", type));
            aggregationBuilder = AggregationBuilders
                    .terms("avg").field("endV.keyword").order(BucketOrder.count(true));
        }
        boolQueryBuilder.must(QueryBuilders.termQuery("dataSetId", "99999"));

        aggRequestVO.setAggregationBuilder(aggregationBuilder);
        aggRequestVO.setQueryBuilder(boolQueryBuilder);

        try {
            List<com.alibaba.fastjson.JSONObject> aggs = serchUtil.aggs(aggRequestVO);
            System.out.println(aggs.size());
            System.out.println(aggs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
