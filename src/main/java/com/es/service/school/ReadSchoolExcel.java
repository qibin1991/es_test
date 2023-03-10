package com.es.service.school;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.es.service.ESConnection;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ReadSchoolExcel
 * @Description TODO
 * @Author QiBin
 * @Date 2021/10/2011:44
 * @Version 1.0
 **/
public class ReadSchoolExcel {

    public static void main(String[] args) {
        try {
//            File file = new File("/Users/qibin/zyyt相关/school/重点媒体（2021年1-10月）.xlsx");
//            boolean exists = file.exists();
//            System.out.println("=========定时任务读取excel==========" + exists);

//            File file1 = new File("/Users/qibin/zyyt相关/school/2020");
//
//            boolean directory = file1.isDirectory();
//            System.out.println(directory);
//            File[] files = file1.listFiles();
//
//            String absolutePath = file1.getAbsolutePath();
//
//            for (File file2 : files) {
//                System.out.println(file2.getName());
//                ExcelReader reader = ExcelUtil.getReader(file2.getAbsolutePath());
//                List<Map<String, Object>> maps = reader.readAll();
//                readExcel(maps);
//                System.out.println("=======完成====="+file2.getName());
//            }
//
//
//
            File file2 = new File("/Users/qibin/zyyt相关/school/境外");
            File[] files2 = file2.listFiles();
            for (File fi : files2) {
                try {
                    System.out.println(fi.getName());
                    ExcelReader reader = ExcelUtil.getReader(fi.getAbsolutePath());
                    List<Map<String, Object>> maps = reader.readAll();
                    readExcel(maps);
                    System.out.println("=======完成====="+fi.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    continue;
                }
            }





//            ExcelReader reader = ExcelUtil.getReader("/Users/qibin/zyyt相关/school/重点媒体（2021年1-10月）.xlsx");
//            System.out.println("========reader完毕=====");
//            List<Map<String, Object>> maps = reader.readAll();
//            System.out.println("==========定时任务读取excel=======" + maps.size());
//
//            readExcel(maps);
//            System.out.println("=======完成1====");
//            ExcelReader reader1 = ExcelUtil.getReader("/Users/qibin/zyyt相关/school/重点媒体2020年1-12月.xlsx");
//            List<Map<String, Object>> maps1 = reader1.readAll();
//
////            maps.addAll(maps1);
//
//            System.out.println("==========定时任务读取excel=======" + maps1.size());
//            readExcel(maps1);
            System.out.println("=======完成2====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static List<Integer> datasetIdList = Arrays.asList(123, 124, 125);

    //
    public static void readExcel(List<Map<String, Object>> maps) {
        System.out.println("============入库=========");
        List<ExcelNews> list = new ArrayList<>();
        try {
            for (Map<String, Object> stringObjectMap : maps) {
                ExcelNews excelNews = JSONUtil.parseObj(stringObjectMap).toBean(ExcelNews.class);
//                byte[] bytes = DigestUtil.md5(excelNews.getUrl());
                String s = DigestUtils.md5Hex(excelNews.getUrl());
                excelNews.setOnlyId(s);
//                UUID uuid = UUID.randomUUID();

//                String s1 = uuid.toString().replaceAll("-", "");
                excelNews.setUuid(s);
                String pubdate = excelNews.getPubdate();
                Date parse = simpleDateFormat.parse(pubdate);

                excelNews.setPubTime(parse.getTime());
                String keyword = excelNews.getKeyword();
                if (StringUtils.isNotBlank(keyword)){
                    String substring = keyword.substring(1, keyword.length() - 1);
                    String[] split = substring.split(",");
                    excelNews.setKeywords(Arrays.asList(split));
                }


                excelNews.setDataset(datasetIdList);
                String tagId = excelNews.getTagId();

                Integer integer = tagId(tagId);
                excelNews.setTagId(integer.toString());
                excelNews.setMediaNameEn(excelNews.getMediaName());
                excelNews.setMediaNameZh(excelNews.getMediaName());
                excelNews.setMediaType("1");
                if (StringUtils.isNotBlank(excelNews.getDocSentiment())){
                    excelNews.setEmotionScore(Integer.valueOf(excelNews.getDocSentiment()));
                }else {
                    excelNews.setEmotionScore(0);
                }

                String sentimentId = excelNews.getSentimentId();

                String languageTname = excelNews.getLanguageTname();
                excelNews.setLanguageCode(lang(languageTname));


                if ("积极".equals(sentimentId)){
                    excelNews.setSentimentId("1");
                }else if ("消极".equals(sentimentId)){
                    excelNews.setSentimentId("-1");
                }else {
                    excelNews.setSentimentId("0");
                }

                list.add(excelNews);

                if (list.size() == 1000) {
                    bulkInsert("news_858278", "article", list);
                    Thread.sleep(300);
                    bulkInsert("news_234763", "article", list);
                    Thread.sleep(300);
                    bulkInsert("news_630547", "article", list);
                    System.out.println("======bulk+1======");
                    list.clear();
                }

//                insert(excelNews, "news_858278");
//                insert(excelNews, "news_234763");
//                insert(excelNews, "news_630547");
            }
            if (CollectionUtil.isNotEmpty(list)) {
                bulkInsert("news_858278", "article", list);
                Thread.sleep(300);
                bulkInsert("news_234763", "article", list);
                Thread.sleep(300);
                bulkInsert("news_630547", "article", list);
                System.out.println("======bulk 完成======");
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("================================="+e.getMessage());
        }

    }

    public static String lang(String lang){
        String langCode = "";
        switch (lang){
            case "中文":
                langCode = "zh";
                break;
            case "英语":
                langCode = "en";
                break;
            case "德语":
                langCode = "de";
                break;
            case "法语":
                langCode = "fr";
                break;
            case "西班牙语":
                langCode = "es";
                break;
            case "日语":
                langCode = "ja";
                break;
            case "韩语":
                langCode = "ko";
                break;
            case "俄语":
                langCode = "ru";
                break;
            case "葡萄牙语":
                langCode = "pt";
                break;
            case "阿拉伯语":
                langCode = "ar";
                break;
/*                    case "it":
                        list.get(i).put("languageTname", "意大利语");
                        break;*/
            default:
                break;
        }
        return langCode;
    }


    public static void insert(ExcelNews excelNews, String indexName) {

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index(indexName);
        indexRequest.id(excelNews.getOnlyId());
        indexRequest.type("article");
        indexRequest.source(JSONUtil.toJsonStr(excelNews), XContentType.JSON);
        try {

            ESConnection.getClient().index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("=====另存异常==="+e.getMessage());
        }
    }

    public static Integer tagId(String tagId){
        Integer tag =null;
        switch (tagId) {
            case "政治":
                tag = 1;
                break;
            case "法律、司法":
                tag = 2;
                break;
            case "对外关系、国际关系":
                tag = 3;
                break;
            case "军事":
                tag = 4;
                break;
            case "社会、劳动":
                tag = 5;
                break;
            case "灾难、事故":
                tag = 6;
                break;
            case "医药、卫生":
                tag = 38;
                break;
            case "环境、气象":
                tag = 22;
                break;
            case "财政、金融":
                tag = 12;
                break;
            case "基本设施、建筑业、房地产":
                tag = 13;
                break;
            case "商业、外贸、海关":
                tag = 19;
                break;
            case "科学技术":
                tag = 33;
                break;
            case "电子信息产业":
                tag = 17;
                break;
            case "农业、农村":
                tag = 14;
                break;
            case "矿业、工业":
                tag = 15;
                break;
            case "能源、水务、水利":
                tag = 16;
                break;
            case "交通运输、邮政、物流":
                tag = 18;
                break;
            case "服务业、旅游业":
                tag = 21;
                break;
            case "教育":
                tag = 31;
                break;
            case "文学、艺术":
                tag = 36;
                break;
            case "传媒":
                tag = 37;
                break;
            case "文化、休闲娱乐":
                tag = 35;
                break;
            case "体育":
                tag = 39;
                break;
            case "经济":
                tag = 11;
                break;
            default:
                tag = 0;
        }
        return tag;
    }



    public static boolean bulkInsert(String indexName, String type, List<ExcelNews> sources) {
        boolean isSuccess = false;
        BulkRequest bulkRequest = new BulkRequest();
        for (ExcelNews jsonObject : sources) {
            cn.hutool.json.JSONObject source = JSONUtil.parseObj(jsonObject);
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(indexName);
            indexRequest.id(source.getStr("onlyId"));
            indexRequest.type(type);
            indexRequest.source(source);
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
//            indexRequest.id(source.get("id").toString());
            bulkRequest.add(indexRequest);
        }
        try {
            BulkResponse bulkResponse = ESConnection.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            if (!bulkResponse.hasFailures()) {
                isSuccess = true;
            } else {
                System.out.println("==========批量索引有失败，具体消息：{}" + bulkResponse.buildFailureMessage());
            }
        } catch (IOException e) {
            System.out.println("issues: " + e);
        }
        return isSuccess;
    }

}
