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
 * <p>【描述】：tweet用户信息</p>
 * <p>【作者】: BeyMax</p>
 * <p>【日期】: 2019-08-16</p>
 **/
@Node
@Builder
@Data
public class TweetUserVo implements Serializable {

    /**
     *     md5id	string	社交源id
     *     input_time	date	 采集时间
     *     user_url	string	 博主url地址
     *     user_web_url	string	 个人站点
     *     screen_name	string	 博主名@名称
     *     blogger_id	string	 博主id
     *     fullname	string	 博主全名
     *     user_addr	string	 注册地区
     *     born_time	string	 出生日期
     *     registered_time	date	 注册时间
     *     useravatar	string	 头像url
     *     bkgdurl	string	 背景图片url
     *     userflag	string	用户描述
     *     tweets	integer	 发文数
     *     following	integer	 博主关数
     *     followers	integer	 粉丝数
     *     likes	integer	 收藏数
     *     listed	integer	 所属公共列表数
     *     moments 	string	 瞬间
     *     verified	string	 是否是验证账号
     *     protected	string	 是否是锁定账号
     *     tf_effective	string	 是否有博文
     *     time_zone	string	用户所在时区
     *     email	string	用户邮箱
     *     lang	string	 用户语言
     *     comefrom	string	来源
     *     source_tag	string	来源对象
     *     remarks	string	项目编号列表
     *     level	string	等级编号
     */
    @Id
    @GeneratedValue
    private Long id;

    @Property
    private String md5id = "";
    private String taskId = "";
    private Date inputTime;
    private String userUrl = "";
    private String userWebUrl = "";
    private String username = "";
    private String rid = "";
    private String fullname = "";
    private String userAddr = "*";
    private String bornTime = "";
    private String registeredTime = "";
    private String registeredTimeFormat = "";
    private String useravatar = "";
    private String useravatarMd5 = "";
    private String useravatarState = "";
    private String bkgdurl = "";
    private String bkgdurlMd5 = "";
    private String bkgdurlState = "";
    private String userflag = "";
    private Integer tweets = 0;
    private Integer following = 0;
    private Integer followers = 0;
    private Integer likes = 0;
    private String listed = "";
    private String moments = "";
    private String verified = "";
    private String protecte = "";
    private String tfEffective = "";
    private Float everydayTweets = 0f;
    private Date updateClrTime;
    private String remarks = "";
    /**
     * 项目编号列表	# 0- A~B 我关注的 1- B~A 关注我的 3- B@A 收藏我的
     * 原始博主名@名称	 原始博主名
     * 等级编号	等级编号
     */

    private String spiderTaskId = "";

    private String datasetId = "";

    private String datasetName = "";

    private String follow_label = "";
    private String topic_label = "";
    private String mention_label = "";
    private String forward_label = "";
    private String comment_label = "";
    private int follow_more = 1;
    private int topic_more = 1;
    private int mention_more = 1;
    private int forward_more = 1;
    private int comment_more = 1;
    private int follow_conceal = 1;
    private int topic_conceal = 1;
    private int mention_conceal = 1;
    private int forward_conceal = 1;
    private int comment_conceal = 1;
    private String mediatname = "TwitterUser";
    private int forward_status = 0;
    private int topic_status = 0;
    private int mention_status = 0;
    private int follow_status = 0;
    private int comment_status = 0;

    private int delFlag =0;
    private Long updateTimes =0L;

    private int semen_label = 0;
    private int reserve1 = 0;
    private int reserve2 = 0;
    private String reserve3 ="";
    private String reserve4 ="";


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getMd5id() {
        return md5id;
    }

    public void setMd5id(String md5id) {
        this.md5id = md5id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
//    @JSONField(name="inputTime")
    public Date getInputTime() {
        return inputTime;
    }
//    @JSONField(name="input_time")
    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserWebUrl() {
        return userWebUrl;
    }

    public void setUserWebUrl(String userWebUrl) {
        this.userWebUrl = userWebUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getBornTime() {
        return bornTime;
    }

    public void setBornTime(String bornTime) {
        this.bornTime = bornTime;
    }

    public String getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(String registeredTime) {
        this.registeredTime = registeredTime;
    }

    public String getRegisteredTimeFormat() {
        return registeredTimeFormat;
    }

    public void setRegisteredTimeFormat(String registeredTimeFormat) {
        this.registeredTimeFormat = registeredTimeFormat;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }

    public String getUseravatarMd5() {
        return useravatarMd5;
    }

    public void setUseravatarMd5(String useravatarMd5) {
        this.useravatarMd5 = useravatarMd5;
    }

    public String getUseravatarState() {
        return useravatarState;
    }

    public void setUseravatarState(String useravatarState) {
        this.useravatarState = useravatarState;
    }

    public String getBkgdurl() {
        return bkgdurl;
    }

    public void setBkgdurl(String bkgdurl) {
        this.bkgdurl = bkgdurl;
    }

    public String getBkgdurlMd5() {
        return bkgdurlMd5;
    }

    public void setBkgdurlMd5(String bkgdurlMd5) {
        this.bkgdurlMd5 = bkgdurlMd5;
    }

    public String getBkgdurlState() {
        return bkgdurlState;
    }

    public void setBkgdurlState(String bkgdurlState) {
        this.bkgdurlState = bkgdurlState;
    }

    public String getUserflag() {
        return userflag;
    }

    public void setUserflag(String userflag) {
        this.userflag = userflag;
    }

    public Integer getTweets() {
        return tweets;
    }

    public void setTweets(Integer tweets) {
        this.tweets = tweets;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getListed() {
        return listed;
    }

    public void setListed(String listed) {
        this.listed = listed;
    }

    public String getMoments() {
        return moments;
    }

    public void setMoments(String moments) {
        this.moments = moments;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getProtecte() {
        return protecte;
    }

    public void setProtecte(String protecte) {
        this.protecte = protecte;
    }

    public String getTfEffective() {
        return tfEffective;
    }

    public void setTfEffective(String tfEffective) {
        this.tfEffective = tfEffective;
    }

    public Float getEverydayTweets() {
        return everydayTweets;
    }

    public void setEverydayTweets(Float everydayTweets) {
        this.everydayTweets = everydayTweets;
    }

    public Date getUpdateClrTime() {
        return updateClrTime;
    }

    public void setUpdateClrTime(Date updateClrTime) {
        this.updateClrTime = updateClrTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSpiderTaskId() {
        return spiderTaskId;
    }

    public void setSpiderTaskId(String spiderTaskId) {
        this.spiderTaskId = spiderTaskId;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getFollow_label() {
        return follow_label;
    }

    public void setFollow_label(String follow_label) {
        this.follow_label = follow_label;
    }

    public String getTopic_label() {
        return topic_label;
    }

    public void setTopic_label(String topic_label) {
        this.topic_label = topic_label;
    }

    public String getMention_label() {
        return mention_label;
    }

    public void setMention_label(String mention_label) {
        this.mention_label = mention_label;
    }

    public String getForward_label() {
        return forward_label;
    }

    public void setForward_label(String forward_label) {
        this.forward_label = forward_label;
    }

    public String getComment_label() {
        return comment_label;
    }

    public void setComment_label(String comment_label) {
        this.comment_label = comment_label;
    }

    public int getFollow_more() {
        return follow_more;
    }

    public void setFollow_more(int follow_more) {
        this.follow_more = follow_more;
    }

    public int getTopic_more() {
        return topic_more;
    }

    public void setTopic_more(int topic_more) {
        this.topic_more = topic_more;
    }

    public int getMention_more() {
        return mention_more;
    }

    public void setMention_more(int mention_more) {
        this.mention_more = mention_more;
    }

    public int getForward_more() {
        return forward_more;
    }

    public void setForward_more(int forward_more) {
        this.forward_more = forward_more;
    }

    public int getComment_more() {
        return comment_more;
    }

    public void setComment_more(int comment_more) {
        this.comment_more = comment_more;
    }

    public int getFollow_conceal() {
        return follow_conceal;
    }

    public void setFollow_conceal(int follow_conceal) {
        this.follow_conceal = follow_conceal;
    }

    public int getTopic_conceal() {
        return topic_conceal;
    }

    public void setTopic_conceal(int topic_conceal) {
        this.topic_conceal = topic_conceal;
    }

    public int getMention_conceal() {
        return mention_conceal;
    }

    public void setMention_conceal(int mention_conceal) {
        this.mention_conceal = mention_conceal;
    }

    public int getForward_conceal() {
        return forward_conceal;
    }

    public void setForward_conceal(int forward_conceal) {
        this.forward_conceal = forward_conceal;
    }

    public int getComment_conceal() {
        return comment_conceal;
    }

    public void setComment_conceal(int comment_conceal) {
        this.comment_conceal = comment_conceal;
    }

    public int getForward_status() {
        return forward_status;
    }

    public void setForward_status(int forward_status) {
        this.forward_status = forward_status;
    }

    public int getTopic_status() {
        return topic_status;
    }

    public void setTopic_status(int topic_status) {
        this.topic_status = topic_status;
    }

    public int getMention_status() {
        return mention_status;
    }

    public void setMention_status(int mention_status) {
        this.mention_status = mention_status;
    }

    public int getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(int follow_status) {
        this.follow_status = follow_status;
    }

    public int getComment_status() {
        return comment_status;
    }

    public void setComment_status(int comment_status) {
        this.comment_status = comment_status;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public Long getUpdateTimes() {
        return updateTimes;
    }

    public void setUpdateTimes(Long updateTimes) {
        this.updateTimes = updateTimes;
    }

    public int getSemen_label() {
        return semen_label;
    }

    public void setSemen_label(int semen_label) {
        this.semen_label = semen_label;
    }

    public int getReserve1() {
        return reserve1;
    }

    public void setReserve1(int reserve1) {
        this.reserve1 = reserve1;
    }

    public int getReserve2() {
        return reserve2;
    }

    public void setReserve2(int reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }
}