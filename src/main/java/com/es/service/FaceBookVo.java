package com.es.service;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>【描述】：facebook推文</p>
 * <p>【作者】: BayMax</p>
 **/
@Data
public class FaceBookVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long mid;

    String sourceId = "";

    private String uuid = "";

    private Long inputTime = System.currentTimeMillis();

    private String spiderUrl = "";

    private String userId = "";

    private String fullName = "";

    @JSONField(name = "post_user")
    private String userName = "";

//    @JSONField(name = "user_url")
    private String userUrl = "";

    /**
     * @
     * "@text": "@某个用户会变成蓝体字，这里为蓝体字的内容",
     * "@link": "蓝体字对应链接"
     */
    private String userJson = "";
    //原始主贴id
    private String shareId = "";

    @JSONField(name = "post_data")
    private String shareContent = "";

    private String shareContentZh = "";

    private String languageCode = "";

    @JSONField(name = "post_data")
    private String sourceContent = "";

    private String longState = "";

    private String shareUrl = "";
    @JSONField(name = "timestamp")
    private String time = "";

    private Long timeLong = 0L;

    @JSONField(name = "timestamp")
    private String publishTime = "";

    private Long publishTimeLong = 0L;

//    @JSONField(name = "timestamp")
    private String updatedTime = "";

    private Long updatedTimeLong = 0L;

    //转发
//    @JSONField(name = "quote_id")
    private String quoteId = "";
    //转发的发帖人连接

    private String quoteUrl = "";


    private String quoteContent = "";


    private String quoteUserName = "";


    private String quoteTimesTamp = "";

//    @JSONField(name = "outer_media_url")
    private String outerMediaUrl = "";

//    @JSONField(name = "outer_media_url_description")
    private String outerMediaUrlDescription = "";

//    @JSONField(name = "outer_media_shot_url")
    private String outerMediaShotUrl = "";

    @JSONField(name = "post_pic")
    private String imgUrl = "";

//    @JSONField(name = "img_url_uuid")
    private String imgUrlUuid;

//    @JSONField(name = "img_url_state")
    private String imgUrlState = "";

    private String isori = "";

    //评论
    private String originalId = "";

//    @JSONField(name = "comment_username")
    private String originalName = "";

//    @JSONField(name = "original_screen_name")
    private String originalScreenName = "";

//    @JSONField(name = "comment_text")
    private String originalContent = "";

//    @JSONField(name = "comment_userhref")
    private String originalUrl = "";

//    @JSONField(name = "comment_timestamp")
    private String originalTime = "";


    private Integer commentGreat = 0;

    private String location = "";

//    @JSONField(name = "location_url")
    private String locationUrl = "";

    @JSONField(name = "greater_num")
    private Integer likeCount = 0;


    private Integer replyCount = 0;


    private Integer forwardCount = 0;

    //话题
    /**
     * "#text": "#号后面的文字",
     * "#link": "#后面的文字对应链接"
     */

    private String tagsJson = "";


    //是否转发

    private String shareType = "";

//    @JSONField(name = "post_type")
    private String accountType = "";

//    @JSONField(name = "update_clr_time")
    private String updateClrTime = "";

    private String videoUrl = "";

    private String reactionCount = "";

//    @JSONField(name = "reaction_type")
    private String reactionType = "";

    private String mentions = "";

    private String verified = "";

//    @JSONField(name = "spider_task_id")
    private String spiderTaskId = "";

//    @JSONField(name = "locate_name")
    private String locateName = "";

//    @JSONField(name = "locate_type")
    private String locateType = "";

//    @JSONField(name = "locate_1")
    private String locate1 = "";

//    @JSONField(name = "locate_2")
    private String locate2 = "";

    private String dataSetId = "";

    private String reserve4 ="";
    private int reserve1 = 0;
    private int reserve2 = 0;
    private String reserve3 ="";
}