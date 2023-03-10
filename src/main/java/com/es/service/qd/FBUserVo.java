package com.es.service.qd;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>【描述】：facebook博主基本信息</p>
 * <p>【作者】: BayMax</p>
 * <p>【日期】: 2020/6/3</p>
 **/
@Data
public class FBUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long mid;

    private String uuid = "";

    int delFlag = 0;

    @JSONField(name = "input_time")
    private Long inputTime = System.currentTimeMillis();

    //    @JSONField(name = "register_time")
    private String registerTime = "";

    private Long registerTimeLong = 0L;

    String mediatname = "FbUser";


    @JSONField(name = "user_url")
    private String userUrl = "";


    private String userWebUrl = "";

    @JSONField(name = "user_id")
    private String userId = "";

    @JSONField(name = "full_name")
    private String userName = "";

    @JSONField(name = "user_name")
    private String fullName = "";

    @JSONField(name = "place")
    private String userAddr = "";

    @JSONField(name = "user_img")
    private String userImg = "";


    private String userFlag = "";

    private String favorites = "";

    //    @JSONField(name = "likes")
    private Integer likes = 0;
    @JSONField(name = "follow")
    private Integer follows = 0;

    private String hometown = "";

    private String currentCity = "";

    private String story = "";

    private String other = "";

    private String work = "";

    private String education = "";

    private String professional = "";

    //    @JSONField(name = "lived_places")
    private String livedPlaces = "";

    private String relationship = "";

    private String birthday = "";

    private String email = "";

    @JSONField(name = "language")
    private String languages = "";


    private String gender = "";


    private String sex = "";


    private String political = "";

    private String religious = "";

    private String websites = "";

    private String about = "";

    private String phone = "";

    //    @JSONField(name = "category")
    private String category = "";

    //    @JSONField(name = "update_clr_time")
    private String updateClrTime = "";

    private String dataSetId = "99999";

    private String sourceId = "";

    private String reserve4 = "";
    private int reserve1 = 0;
    private int reserve2 = 0;
    private String reserve3 = "";
}