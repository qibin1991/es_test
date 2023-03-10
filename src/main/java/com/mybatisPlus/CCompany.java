package com.mybatisPlus;

/**
 * @ClassName CCompany
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/31 08:47
 * @Version 1.0
 **/


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 单位表。从数据中台同步unit_id和unit_name(CCompany)实体类
 *
 * @author qibin
 * @since 2022-07-26 16:41:22
 */
@Data
@TableName("c_company")
public class CCompany implements Serializable {
    private static final long serialVersionUID = -75773515910053440L;
    /**
     * 组织id
     */
    @TableId
    private Long id;

    /**
     * 组织名称
     */
    private String companyName;
    /**
     * 管理员工号
     */
    private String adminPersonNumber;
    /**
     * 管理员姓名
     */
    private String adminName;
    /**
     * 分享图片url
     */
    private String sharePicUrl;
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
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 管理员手机号
     */
    private String adminMobile;


}


