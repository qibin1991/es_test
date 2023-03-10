package com.es.service.school;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExcelNews
 * @Description TODO
 * @Author QiBin
 * @Date 2021/10/1916:52
 * @Version 1.0
 **/

@Data
public class ExcelNews {

//    @JSONField(name = "发布时间")
    String pubdate;
//    @JSONField(name = "标题")
    String title;

    String onlyId;
    String type = "article";
//    @JSONField(name = "正文")
    String text;
//    @JSONField(name = "媒体国家")
    String countryNameZh;
//    @JSONField(name = "媒体类型")
    String mediaTname;
    String mediaType;
//    @JSONField(name = "媒体")
    String mediaName;
    String mediaNameEn;
    String mediaNameZh;
//    @JSONField(name = "语言")
    String languageTname;
//    @JSONField(name = "网址")
    String url;
//    @JSONField(name = "情感")
    String sentimentId;
//    @JSONField(name = "行业分类")
    String tagId;
//    @JSONField(name = "关键词")
    String keyword;
    List<String> keywords = new ArrayList<>();

    Integer emotionScore;
//    @JSONField(name = "情感得分")
    String docSentiment;
//    @JSONField(name = "主域名")
    String domain;
    long pubTime;
    String languageCode  = "zh";
    String uuid;
    String mediaLevel =  "3";
    List<Integer> dataset = new ArrayList<>();
    List<Integer> readSet;
    List<Integer> readUser;
    Integer version;

    public static void main(String[] args) {

    }



}
