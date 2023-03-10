package com.es.neo4j.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>【描述】：帖子正文数据</p>
 * <p>【作者】: BeyMax</p>
 * <p>【日期】: 2019-08-16</p>
 **/
@Node("TwitterTweet")
@Builder
@Data
public class TweetsVo implements Serializable {
    @Id
    @GeneratedValue
    private Long Id;

    @Property
    private String md5id = "";
    @Property
    private Date inputTime = new Date();
    @Property
    private String tid = "";
    @Property
    private String spiderUrl = "";
    @Property
    private String username = "";
    @Property
    private String fullname = "";
    @Property
    private String userId = "";
    @Property
    private String userUrl = "";
    @Property
    private String userImg = "";
    @Property
    private String tweetsId = "";
    @Property
    private String conversationId = "";
    @Property
    private String tweetsUrl = "";
    @Property
    private Long time;
    @Property
    private String timeStampStr;
    @Property
    private Date timedate;
    @Property
    private String timeFormat = "";
    @Property
    private String quotetweetId = "";
    @Property
    private String content_zh = "";
    @Property
    private String quotetweetUrl = "";
    @Property
    private String outerMediaUrl = "";
    @Property
    private String outerMediaShotUrl = "";
    @Property
    private String imgUrlState = "";
    @Property
    private String isori = "";
    @Property
    private String isReply = "";
    @Property
    private String retweetId = "";
    @Property
    private String retweetName = "";
    @Property
    private String retweetScreenName = "";
    @Property
    private String retweetContent = "";
    @Property
    private String retweetContentZh = "";
    @Property
    private String retweetUserId = "";
    @Property
    private String repliedId = "";
    @Property
    private String repliedUser = "";
    @Property
    private Integer cmtcnt = 0;
    @Property
    private Integer rpscnt = 0;
    @Property
    private Integer atdcnt = 0;
    @Property
    private String isComments = "";
    @Property
    private String cstate = "";
    @Property
    private Integer totalCollect = 0;
    @Property
    private String mediatname = "TwitterTweet";
    @Property
    private String spiderType = "";
    @Property
    private String spiderTaskId = "";
    @Property
    private Date updateClrTime;
    @Property
    private String tweetsType = "";

    @Property
    private String sourceId = "";
    @Property
    private String userJson = "";
    @Property
    private String tagJson = "";
    @Property
    private String tweetsContent = "";
    @Property
    private String imgUrl = "";
    @Property
    private String datasetId = "";
    @Property
    private String datasetName = "";
    @Property
    private int delFlag = 0;
    @Property
    private Long updateTimes = 0L;
    @Property
    private String tweetAccoun = "";
    @Property
    private String location = "";

    // 去重字段 md5（datasetId+tid+userId+retweetUserId）
    @Property
    private String reserve4 = "";
    @Property
    private int reserve1 = 0;
    @Property
    private int reserve2 = 0;
    @Property
    private String reserve3 = "";

}
