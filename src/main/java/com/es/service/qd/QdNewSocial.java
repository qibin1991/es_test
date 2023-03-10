package com.es.service.qd;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @ClassName QdNewSocial
 * @Description TODO
 * @Author QiBin
 * @Date 2021/7/210:30
 * @Version 1.0
 **/
@Data
public class QdNewSocial {

    /**
     * only_id
     * news_section  新闻栏目
     * domain  栏目url
     * title  标题
     * text   正文
     * key_person   作者
     * publish_date   发布日期
     * web_name    网站名称
     * country   采集国家
     * collect_country   国家英文名
     * language   语言
     * url  文章url
     *
     * citylatitude	算法提取城市维度
     * citylongitude	算法提取城市经度
     * collect_city	算法提取城市
     * collect_country	国家英文名
     * country	采集国家
     * countrylat	算法提取国家维度
     * countrylng	算法提取国家经度
     * create_time	创建时间
     * domain	   栏目url
     * domain2	领域（二级）
     * emotion	情绪
     * key_org	关键机构
     * key_person	作者
     * key_word	关键词
     * language	语言
     * news_section	    新闻栏目
     * only_id	唯一id
     * platform	 路径
     * publish_date	发布时间
     * sensitive	敏感
     * sensitive_num	敏感分数
     * sensitive_words	敏感词
     * similarId	相似id
     * source	媒体来源
     * summary	摘要
     * text	原文
     * text_tag	带标签原文
     * thumbnail	缩略图
     * title	标题
     * url	url
     * web_name	网站名称
     * zh_text	中文译文
     * zh_text_tag	带标签中文译文
     * zh_title	中文标题
     * table	数据类型：news=新闻，zhiku=智库
     * keyword_hit_report	是否命中报告组提供的关键词，0=未命中，1=命中
     */
    String citylatitude;
    String citylongitude;
    String collect_city;
    @JSONField(name = "nation_en")
    String collect_country;
    @JSONField(name = "nation_zh")
    String country;
    String countrylat;
    String countrylng;
    @JSONField(name = "acquisition_time")
    String create_time;
    @JSONField(name = "column_url")
    String domain;
    String domain2;
    String emotion;
    String key_org;
    @JSONField(name = "author")
    String key_person;
    String key_word;
    String language;
    @JSONField(name = "news_section")
    String news_section;
    @JSONField(name = "news_id")
    String only_id;
    String platform;
    @JSONField(name = "release_time")
    String publish_date;
    String sensitive;
    String sensitive_num;
    String sensitive_words;
    String similarId;
    String source;
    String summary;
    @JSONField(name = "text_content")
    String text;
    String text_tag;
    //attachment_download
    @JSONField(name = "attachment_download")
    String thumbnail;
    String title;
    @JSONField(name = "news_url")
    String url;
    @JSONField(name = "domain")
    String web_name;
    String zh_text;
    String zh_text_tag;
    String zh_title;
    String table;
    String keyword_hit_report;
    Integer view = 0;
    String dataSetId;
    String reverse1;
    String reverse2;
    String reverse3;
    String reverse4;
    String pdf_name;
    Long create_time_long = 0L;
    Integer delFlag = 0;
    Long publish_time_long = 0L;
    List<String> dataset;


}
