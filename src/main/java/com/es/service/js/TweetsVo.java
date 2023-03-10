package com.es.service.js;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.es.service.ESConnection;
import com.es.service.SearchRequestVO;
import com.es.service.SearchResponseVO;
import com.es.service.TwoAggreationUtil;
import lombok.Data;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>【描述】：帖子正文数据</p>
 * <p>【作者】: BeyMax</p>
 * <p>【日期】: 2019-08-16</p>
 **/

//   id,md5id, input_time, tid, spider_url, user_name fullname, screen_name username, user_id, user_url userUrl," +
//        " user_img, tweets_id tweetsId, conversation_id, tweets_url,time timeStampStr,timestr timedate, quotetweet_id, quotetweet_url," +
//        " outer_media_url, outer_media_shot_url, img_url_state, isori, is_reply isReply, retweet_id retweetId,
//        retweet_content retweetContent," +
//        " retweet_name retweetName, retweet_screen_name retweetScreenName, retweet_user_id retweetUserId, replied_id repliedId,
//        replied_user repliedUser, cmtcnt," +

//        " rpscnt, atdcnt, is_comments, cstate, total_collect totalCollect, mediatname, spider_type spiderType, spider_task_id," +
//        " server_node, location , location_id locationId, update_clr_time, next_spider_comments_time, collect_comments_frequent," +
//        " reserved1, reserved2, reserved3, tweets_type tweetsType, comments_scroll commentScroll, insert_data_type," +
//        " source_id sourceId,user_json userJson, tag_json tagJson, tweets_content tweetsContent, img_url, img_url_md5id, likes,
//        source_content sourceContent,dataset_id datasetId " +
@Data
public class TweetsVo {
    private Long id;

    private String md5id = "";

    private Date inputTime = new Date();

    private String tid = "";

    @JSONField(name = "spider_url")
    private String spiderUrl = "";
    @JSONField(name = "screen_name")
    private String username = "";
    @JSONField(name = "full_name")
    private String fullname = "";
    @JSONField(name = "user_id")
    private String userId = "";
    @JSONField(name = "user_url")
    private String userUrl = "";
    //        " user_img, tweets_id tweetsId, conversation_id, tweets_url,
    //        time timeStampStr,timestr timedate, quotetweet_id, quotetweet_url," +
    @JSONField(name = "user_img")
    private String userImg = "";
    @JSONField(name = "tweets_id")
    private String tweetsId = "";
    @JSONField(name = "conversation_id")
    private String conversationId = "";
    @JSONField(name = "tweets_url")
    private String tweetsUrl = "";

    private Long time;
    @JSONField(name = "time")
    private String timeStampStr;
    @JSONField(name = "timestr")
    private Date timedate;

    private String timeFormat = "";
    @JSONField(name = "quotetweet_id")
    private String quotetweetId = "";

    private String content_zh = "";
    @JSONField(name = "quotetweet_url")
    private String quotetweetUrl = "";

//      " outer_media_url, outer_media_shot_url, img_url_state, isori, is_reply isReply, retweet_id retweetId,
    //        retweet_content retweetContent," +
//        " retweet_name retweetName, retweet_screen_name retweetScreenName, retweet_user_id retweetUserId, replied_id repliedId,
//        replied_user repliedUser, cmtcnt," +
    @JSONField(name = "outer_media_url")
    private String outerMediaUrl = "";
    @JSONField(name = "outer_media_shot_url")
    private String outerMediaShotUrl = "";
    @JSONField(name = "img_url_state")
    private String imgUrlState = "";
    @JSONField(name = "isori")
    private String isori = "";
    @JSONField(name = "is_reply")
    private String isReply = "";
    @JSONField(name = "retweet_id")
    private String retweetId = "";
    @JSONField(name = "retweet_name")
    private String retweetName = "";
    @JSONField(name = "retweet_screen_name")
    private String retweetScreenName = "";
    @JSONField(name = "retweet_content")
    private String retweetContent = "";

    private String retweetContentZh = "";
    @JSONField(name = "retweet_user_id")
    private String retweetUserId = "";
    @JSONField(name = "replied_id")
    private String repliedId = "";
    @JSONField(name = "replied_user")
    private String repliedUser = "";
    @JSONField(name = "cmtcnt")
    private Integer cmtcnt = 0;
    //        " rpscnt, atdcnt, is_comments, cstate, total_collect totalCollect, mediatname, spider_type spiderType,
    //        spider_task_id," +
//        " server_node, location , location_id locationId, update_clr_time, next_spider_comments_time, collect_comments_frequent," +
//        " reserved1, reserved2, reserved3, tweets_type tweetsType, comments_scroll commentScroll, insert_data_type," +

    @JSONField(name = "rpscnt")
    private Integer rpscnt = 0;
    @JSONField(name = "atdcnt")
    private Integer atdcnt = 0;
    @JSONField(name = "is_comments")
    private String isComments = "";
    @JSONField(name = "cstate")
    private String cstate = "";
    @JSONField(name = "total_collect")
    private Integer totalCollect = 0;

    private String mediatname = "TwitterTweet";
    @JSONField(name = "spider_type")
    private String spiderType = "";
    @JSONField(name = "spider_task_id")
    private String spiderTaskId = "";
    @JSONField(name = "update_clr_time")
    private Date updateClrTime;

    private String tweetsType = "";
    //        " source_id sourceId,user_json userJson, tag_json tagJson,
    //        tweets_content tweetsContent, img_url, img_url_md5id, likes,
//        source_content sourceContent,dataset_id datasetId " +
    @JSONField(name = "source_id")
    private String sourceId = "";
    @JSONField(name = "user_json")
    private String userJson = "";
    @JSONField(name = "tag_json")
    private String tagJson = "";
    @JSONField(name = "tweets_content")
    private String tweetsContent = "";
    @JSONField(name = "img_url")
    private String imgUrl = "";

    private String datasetId = "9999";

    private String datasetName = "";

    private int delFlag = 0;
    private Long updateTimes = 0L;
    private String tweetAccoun = "";

    private String location = "";

    // 去重字段 md5（datasetId+tid+userId+retweetUserId）

    private String reserve4 = "";
    private int reserve1 = 0;
    private int reserve2 = 0;
    private String reserve3 = "";


    public static void main(String[] args) {
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchRequestVO searchRequestVO = new SearchRequestVO();

        searchRequestVO.setIndexName(new String[]{"js_qd_fb_e"});
        searchRequestVO.setType("edge");


        boolQueryBuilder.must(QueryBuilders.termQuery("mediatname.keyword", "topic"))
                .must(QueryBuilders.termQuery("dataSetId", "99999"))
                .must(QueryBuilders.termQuery("startV.keyword", "#uyghurpulse"));
        searchRequestVO.setQueryBuilder(boolQueryBuilder);

        searchRequestVO.setPageNo(1);
        searchRequestVO.setPageSize(3000);

        searchRequestVO.setQueryBuilder(boolQueryBuilder);

        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            for (Map<String, Object> datum : data) {
                datum.put("startV", "#Uyghurpulse");

                Object id = datum.get("_id");
                datum.remove("_id");
                UpdateRequest updateRequest = new UpdateRequest("js_qd_fb_e"
                        , "edge", id.toString())
                        .doc(JSONUtil.toJsonStr(datum), XContentType.JSON);
                try {
                    UpdateResponse update = ESConnection.getClient().update(updateRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
