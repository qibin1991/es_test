package com.es.service;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName FbFollowsVo
 * @Description TODO
 * @Author QiBin
 * @Date 2020/8/1115:21
 * @Version 1.0
 **/
@Data
public class FbFollowsVo {
    private static final long serialVersionUID = 1L;
    private Long mid;

    private String uuid;

    private Long inputTime = System.currentTimeMillis();

    //原始id
    @JSONField(name = "source_id")
    private String userId = "";

    private String userName = "";

    //粉丝id
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
