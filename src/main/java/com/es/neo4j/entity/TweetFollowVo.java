package com.es.neo4j.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>【描述】：tweet用户粉丝</p>
 * <p>【作者】: BeyMax</p>
 * <p>【日期】: 2019-08-19</p>
 **/
@Node("TwitterFollow")
@Builder
@Data
public class TweetFollowVo implements Serializable {

    @org.springframework.data.neo4j.core.schema.Id
    @GeneratedValue
    private Long Id;

    private String tableName = "";


    private String md5id= "";

    private Date inputTime = new Date();
    private String uid= "";

    private String userId= "";

    private String userName= "";
    private String userScreanName= "";
    private String followScreanName= "";
    private String followUserName= "";
    private String followUserId= "";
    private String followUseravatar= "";

    private String followUseravatarMd5id= "";

    private String followUseravatarState= "";
    private String followBkgimg= "";

    private String followBkgimgMd5id= "";
    private String followBkgimgState= "";
    private String followUserflag= "";
    private String verified= "";
    private String protecte= "";


    private Date updateClrTime= new Date();

    private String remarks= "";

    private String reserved1= "";

    private String reserved2= "";

    private String reserved3= "";
    private String spiderTaskId= "";

    private String sourceId= "";
    private String datasetId= "";

    private String datasetName= "";

}