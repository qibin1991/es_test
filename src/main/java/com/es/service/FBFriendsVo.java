package com.es.service;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>【描述】：facebook用户好友</p>
 * <p>【作者】: BayMax</p>
 * <p>【日期】: 2020/6/4</p>
 **/
@Data
public class FBFriendsVo implements Serializable {
    private static final long serialVersionUID = 3L;
    private Long mid;

    private String uuid;

    private Long inputTime = System.currentTimeMillis();

    private String uid = "";

    //原始id
    private String userId = "";

    private String userName = "";

    //好友id
    @JSONField(name = "fb_id")
    private String friendsId = "";
    @JSONField(name = "user_nickname")
    private String friendsName = "";

    @JSONField(name = "user_href")
    private String friendsUrl = "";

    @JSONField(name = "head_pic")
    private String friendsImg = "";


    private String friendsImgUuid = "";


    private String friendsImgState = "";


    private String friendsFlag = "";


    private String friendsType = "";


    private String spiderTaskId = "";


    private String updateClrTime = "";

    private String datasetId = "";
}