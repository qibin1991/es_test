package com.mybatisPlus;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisPlus.enumType.QueryWapper;
import com.mybatisPlus.enumType.QueryWapperEnum;
import com.mybatisPlus.enumType.SexEnum;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * User
 * @author cqh
 * @date 2022-05-09 15:22
 */
@Data
@TableName("c_user")
public class User implements Serializable, Domain {

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 组织id
     */
    private Long companyId;
    /**
     * 用户id（三方）
     */
//    @Pattern(regexp = "[\\w-]+",message = "请输⼊正确的用户id格式")
    private String personNumber;
    /**
     * 用户姓名
     */
    @TableField(condition = SqlCondition.LIKE, jdbcType = JdbcType.VARCHAR)
    private String realName;
    /**
     * 部门id
     */
    private Long orgId;

    @TableField(exist = false)
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private SexEnum sex;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 累计积分
     */
    private Integer totalScore;
    /**
     * 当前积分
     */
    private Integer currScore;
    /**
     * 累计减碳量
     */
    private BigDecimal totalReduction;

    /**
     * 收件人姓名
     */
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private String consignee;
    /**
     * day_score       int            default 0                 not null comment '日积分',
     *     week_score      int            default 0                 not null comment '周积分',
     *     month_score     int            default 0                 not null comment '月积分',
     *     day_reduction   decimal(20, 2) default 0.00              not null comment '日减碳量',
     *     week_reduction  decimal(20, 2) default 0.00              not null comment '周减碳量',
     *     month_reduction decimal(20, 2) default 0.00              not null comment '月减碳量',
     */

    private Integer dayScore;
    private Integer weekScore;
    private Integer monthScore;

    private BigDecimal dayReduction;
    private BigDecimal weekReduction;
    private BigDecimal monthReduction;

    /**
     * 用户状态。0 正常 -1 黑名单
     */
    private Integer userStatus;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 更新者
     */
    private Long updateBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill= FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill= FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 删除状态  0否 1是
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;

    private String address;
    @TableField(exist = false)
    private String orgName;
    @TableField(exist = false)
    private String companyName;
}
