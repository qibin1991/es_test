package com.es.service;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName FbQdCommentVo
 * @Description TODO
 * @Author QiBin
 * @Date 2020/9/1913:41
 * @Version 1.0
 **/
@Data
public class FbQdCommentVo {
    @JSONField(name = "comment_timestamp")
    private Long mid;

    private String uuid = "";

    //    @JSONField(name = "input_time")
    private Long inputTime = System.currentTimeMillis();

    private String spiderUrl = "";

    private String userId = "";

    private String sourceId = "";

    @JSONField(name = "comment_username")
    private String fullName = "";

    @JSONField(name = "comment_username")
    private String userName = "";

    //    @JSONField(name = "user_url")
    private String userUrl = "";

    //    @JSONField(name = "user_json")
    private String userJson = "";
    @JSONField(name = "comment_timestamp")
    private String shareId = "";

    @JSONField(name = "comment_text")
    private String shareContent = "";

    private String shareContentZh = "";

    private String languageCode = "";

    private String sourceContent = "";

    private String longState = "";

    @JSONField(name = "comment_userhref")
    private String shareUrl = "";

    private String time = "";

    private Long timeLong = 0L;

    @JSONField(name = "comment_timestamp")
    private String publishTime = "";

    private Long publishTimeLong = 0L;

    private String updatedTime = "";

    private Long updatedTimeLong = 0L;

    //    @JSONField(name = "quote_id")
    private String quoteId = "";

    //    @JSONField(name = "quote_url")
    private String quoteUrl = "";

    //    @JSONField(name = "quote_content")
    private String quoteContent = "";

    //    @JSONField(name = "outer_media_url")
    private String outerMediaUrl = "";

    //    @JSONField(name = "outer_media_url_description")
    private String outerMediaUrlDescription = "";

    //    @JSONField(name = "outer_media_shot_url")
    private String outerMediaShotUrl = "";

    private String imgUrl = "";

    //    @JSONField(name = "img_url_uuid")
    private String imgUrlUuid;

    //    @JSONField(name = "img_url_state")
    private String imgUrlState = "";

    private String isori = "";

    //    @JSONField(name = "original_id")
    private String originalId = "";

    //    @JSONField(name = "original_name")
    private String originalName = "";

    //    @JSONField(name = "original_screen_name")
    private String originalScreenName = "";

    private String location = "";

    //    @JSONField(name = "location_url")
    private String locationUrl = "";


    private Integer likeCount = 0;


    private Integer replyCount = 0;


    private Integer forwardCount = 0;

    //    @JSONField(name = "tags_json")
    private String tagsJson = "";


    private String shareType = "3";


    private String accountType = "";

    //    @JSONField(name = "update_clr_time")
    private String updateClrTime = "";

    //    @JSONField(name = "video_url")
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
