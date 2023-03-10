package com.es.service.thinkTank;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.es.service.ESConnection;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ReadFileQd
 * @Description TODO
 * @Author QiBin
 * @Date 2021/8/2717:58
 * @Version 1.0
 **/
public class ReadFileQd {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {

        BulkRequest bulkRequest = new BulkRequest();
        try {
            BulkResponse bulkResponse = ESConnection.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(1);
            ExcelReader reader = ExcelUtil.getReader("/Users/qibin/zyyt相关/school/重点媒体（2021年1-10月）.xlsx");
            System.out.println(2);
            List<Map<String, Object>> maps = reader.readAll();
            List<JSONObject> list = new ArrayList<>();
            System.out.println(maps.size());





            for (Map<String, Object> map : maps) {
                QdNewSocial qdNewSocial = JSONUtil.toBean(JSONUtil.parseObj(map), QdNewSocial.class);
                if (StringUtils.isBlank(qdNewSocial.getDomain())) {
                    continue;
                }
                qdNewSocial.setOnly_id(DigestUtils.md5Hex(qdNewSocial.getUrl().getBytes()));

                String thumbnail = qdNewSocial.getThumbnail();
                if (StringUtils.isNotBlank(thumbnail)) {
                    thumbnail.replaceAll("###", ",");
                    StringBuilder pn = new StringBuilder();
                    if (thumbnail.contains(",")) {
                        String[] split = thumbnail.split(",");
                        for (String s : split) {
                            String substring = s.substring(s.lastIndexOf("/") + 1);
                            pn.append(",").append(substring);
                        }
                    } else {
//                        System.out.println(thumbnail);
                        String substring = thumbnail.substring(thumbnail.lastIndexOf("/") + 1);
//                        System.out.println(substring);
                        pn.append(",").append(substring);
                    }
                    String substring = pn.substring(1);
                    System.out.println(substring);
                    qdNewSocial.setPdf_name(substring);
                }

                qdNewSocial.setDataSetId("88");
                String publish_date = qdNewSocial.getPublish_date();
                Date date = null;
                try {
//                    date = parseTime(publish_date);
                    //01 июня 2021, 17:53
                    if (StringUtils.isNotBlank(publish_date)) {
                        System.out.println(publish_date);
                        String dateTimeStr = "";
                        if (publish_date.length() == 10) {
                            //时间戳
                            date = new Date(Long.valueOf(publish_date) * 1000);
                        } else if (publish_date.contains(".")) {
                            String[] split = publish_date.split("\\.");
                            String s = split[0];
                            String s1 = split[1];
                            String s2 = split[2];

                            if (s.length() > 2) {
                                s = s.substring(s.length() - 3);
                            }
                            if (s2.length() > 4) {
                                s2 = s2.substring(0, 4);
                            }

                            dateTimeStr = s2 + "-" + s1 + "-" + s;
                            date = simpleDateFormat.parse(dateTimeStr);
                        } else if (publish_date.contains(",")) {
                            String[] split = publish_date.split(",");
                            String s = split[0];
                            if (s.length() > 6) {
                                String[] s1 = s.split(" ");
                                String s10 = s1[0].replaceAll(" ", "");
                                String s11 = s1[1].replaceAll(" ", "");
                                String s12 = s1[2].replaceAll(" ", "");

//                                String s2 = DigestUtils.md5Hex(s11);
                                com.alibaba.fastjson.JSONObject timeJson = new com.alibaba.fastjson.JSONObject();
                                timeJson.put("tgtl", "nzh");
                                timeJson.put("text", s11);
                                timeJson.put("app_source", "9002");
                                timeJson.put("detoken", true);
                                timeJson.put("srcl", "nru");
                                String postForString = translatePostUtil("http://192.168.52.3/new/translate", timeJson);
                                String sRe = UnicodeUtil.toString(postForString);


                                if (!"翻译接口调用出错".equals(sRe) && StringUtils.isNotBlank(sRe)) {

                                    try {
                                        com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(sRe);
                                        String data = jo.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("text");
                                        if (StringUtils.isNotBlank(data)) {
                                            s11 = data;
                                            if (s11.contains("月")) {
                                                s11 = s11.substring(0, s11.length() - 1);
                                            }
                                            if (s11.length() == 1) {
                                                s11 = ("0" + s11);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("=======" + e.getMessage());
                                    }
                                }
                                dateTimeStr = s12 + "-" + s11 + "-" + s10;
                                date = simpleDateFormat.parse(dateTimeStr);
                            } else {
                                String ss = split[1].substring(1);
                                String[] s1 = ss.split(" ");
                                String s10 = s1[0].replaceAll(" ", "");
                                String s11 = s1[1].replaceAll(" ", "");
                                String s12 = s1[2].replaceAll(" ", "");

                                com.alibaba.fastjson.JSONObject timeJson = new com.alibaba.fastjson.JSONObject();
                                timeJson.put("tgtl", "nzh");
                                timeJson.put("text", s11);
                                timeJson.put("app_source", "9002");
                                timeJson.put("detoken", true);
                                timeJson.put("srcl", "nru");
                                String postForString = translatePostUtil("http://192.168.52.3/new/translate", timeJson);
                                String sRe = UnicodeUtil.toString(postForString);


                                if (!"翻译接口调用出错".equals(sRe) && StringUtils.isNotBlank(sRe)) {

                                    try {
                                        com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(sRe);
                                        String data = jo.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("text");
                                        if (StringUtils.isNotBlank(data)) {
                                            s11 = data;
                                            if (s11.contains("月")) {
                                                s11 = s11.substring(0, s11.length() - 1);
                                            }
                                            if (s11.length() == 1) {
                                                s11 = ("0" + s11);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("=======" + e.getMessage());
                                    }
                                }
                                dateTimeStr = s12 + "-" + s11 + "-" + s10;
                                date = simpleDateFormat.parse(dateTimeStr);
                            }
                        }  else {
                            System.out.println(publish_date);
                            String s3 = publish_date.substring(publish_date.indexOf("за") + 3, publish_date.indexOf("года"));
                            String[] ss = s3.split(" ");
                            String ss0 = ss[0];
                            if (ss0.length() == 1) {
                                ss0 = ("0" + ss0);
                            }
                            String ss1 = ss[1];
                            String ss2 = ss[2];

                            com.alibaba.fastjson.JSONObject timeJson = new com.alibaba.fastjson.JSONObject();
                            timeJson.put("tgtl", "nzh");
                            timeJson.put("text", ss1);
                            timeJson.put("app_source", "9002");
                            timeJson.put("detoken", true);
                            timeJson.put("srcl", "nru");
                            String postForString = translatePostUtil("http://192.168.52.3/new/translate", timeJson);
                            String sRe = UnicodeUtil.toString(postForString);


                            if (!"翻译接口调用出错".equals(sRe) && StringUtils.isNotBlank(sRe)) {

                                try {
                                    com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(sRe);
                                    String data = jo.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("text");
                                    if (StringUtils.isNotBlank(data)) {
                                        ss1 = data.substring(0, data.indexOf("月"));
                                        if (ss1.length() == 1) {
                                            ss1 = ("0" + ss1);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("=======" + e.getMessage());
                                }
                            }
                            dateTimeStr = ss2 + "-" + ss1 + "-" + ss0;
                            date = simpleDateFormat.parse(dateTimeStr);

                        }

                    }


//                    俄语的时间
//                   if (StringUtils.isNotBlank(publish_date)){
//
//                       String substring = publish_date.substring(publish_date.indexOf(",") + 2);
//                       System.out.println(substring);
//                       String[] s = substring.split(" ");
//
//                       String s1 = s[0];
//                       System.out.println(s1);
//                       System.out.println(s1);
//                       if (s1.length() == 1){
//                           s1 = ("0"+s1);
//                       }
//                       String s2 = "09";
//                       String s3 = s[2];
//                       date = simpleDateFormat.parse(s3+"-"+s2+"-"+s1);
//
//                   }
//
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (date != null) {
                    qdNewSocial.setPublish_date(simpleDateFormat.format(date));
                    long time = date.getTime();
                    qdNewSocial.setPublish_time_long(time);
                }
                qdNewSocial.setCreate_time(simpleDateFormat.format(qdNewSocial.getCreate_time_long()));

//                翻译标题和正文
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                String language = qdNewSocial.getLanguage();
                if (language.equals("英语")) {
                    json.put("srcl", "nen");
                } else if (language.equals("韩语")) {
                    json.put("srcl", "nko");
                } else if (language.equals("俄语")) {
                    json.put("srcl", "nru");
                }


                json.put("tgtl", "nzh");
                json.put("text", qdNewSocial.getTitle());
                json.put("app_source", "9002");
                json.put("detoken", true);
                String postForString = translatePostUtil("http://192.168.52.3/new/translate", json);
                String s = UnicodeUtil.toString(postForString);
                if (!"翻译接口调用出错".equals(postForString) && StringUtils.isNotBlank(postForString)) {
                    try {
                        com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(s);
                        String data = jo.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("text");
                        if (StringUtils.isNotBlank(data)) {
                            qdNewSocial.setZh_title(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("=======" + e.getMessage());
                    }
                }
                if (StringUtils.isNotBlank(qdNewSocial.getText())) {
                    json.put("text", qdNewSocial.getText());

                    String textJson = translatePostUtil("http://192.168.52.3/new/translate", json);
                    String textRe = UnicodeUtil.toString(textJson);

                    if (!"翻译接口调用出错".equals(textRe) && StringUtils.isNotBlank(textRe)) {
                        try {
                            com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(textRe);
                            String data = jo.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("text");
                            if (StringUtils.isNotBlank(data)) {
                                qdNewSocial.setZh_text(data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("=======" + e.getMessage());
                        }
                    }

                }


                //俄语  翻译时间
//                String publish_date1 = qdNewSocial.getPublish_date();
//                json.put("text", publish_date1);
//                String textJson = translatePostUtil("http://192.168.52.3/new/translate", json);
//                String textRe = UnicodeUtil.toString(textJson);
//
//                if (!"翻译接口调用出错".equals(textRe) && StringUtils.isNotBlank(textRe)) {
//                    try {
//                        com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(textRe);
//                        String data = jo.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("text");
//                        if (StringUtils.isNotBlank(data)){
//                            System.out.println(data);
//                            String year = data.substring(data.indexOf("于") + 1, data.indexOf("年"));
//                            String month = data.substring(data.indexOf("年") + 1, data.indexOf("月"));
//                            if (month.length() == 1){
//                                month = "0"+month;
//                            }
//                            String day = data.substring(data.indexOf("月") + 1, data.indexOf("日"));
//                            if (day.length() == 1){
//                                day = "0"+day;
//                            }
//                            String date = year+"-"+month+"-"+day;
//                            System.out.println(date);
//                            qdNewSocial.setPublish_date(date);
//                            long time = simpleDateFormat.parse(date).getTime();
//                            qdNewSocial.setPublish_time_long(time);
//
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("======="+e.getMessage());
//                    }
//                }


                qdNewSocial.setPlatform("/data/qb");
                JSONObject jsonObject = JSONUtil.parseObj(qdNewSocial);
                list.add(jsonObject);
                System.out.println("list.size==" + list.size());
                if (list.size() == 1000) {
                    bulkInsert("thinktank-data", "article", list);
                    list.clear();
                }
            }
            if (list.size() > 0) {
                bulkInsert("thinktank-data", "article", list);
            }

            System.out.println("=======完成======");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String translatePostUtil(String url, com.alibaba.fastjson.JSONObject json) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
//        HttpPost httpPost = new HttpPost(url);
        String httpStr = "翻译接口调用出错";
        try {

            String post = HttpUtil.post(url, json.toJSONString());
            return post;

        } catch (Exception e) {
            e.printStackTrace();
        }
//            StringEntity entity = new StringEntity(json.toJSONString());
////            entity.setContentEncoding("UTF-8");
//            entity.setContentType("application/json;charset=UTF-8");
//            httpPost.setEntity(entity);
//            response = httpClient.execute(httpPost);
//            int statusCode = response.getStatusLine().getStatusCode();
//            log.info("翻译状态码：{}", statusCode);
//            if (statusCode == 200) {
//                httpStr = EntityUtils.toString(response.getEntity(), "UTF-8");
//                log.info("翻译接口返回内容：{}", httpStr);
//            } else {
//
//                log.info("翻译接口返回内容：{}", EntityUtils.toString(response.getEntity(), "UTF-8"));
//            }
//
//        } catch (Exception var17) {
//            var17.printStackTrace();
//        } finally {
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException var16) {
//                    var16.printStackTrace();
//                }
//            }
//
//        }

        return "";
    }


    //一月January —— Jan.
    //
    //二月February —— Feb.
    //
    //三月March —— Mar.
    //
    //四月April —— Apr.
    //
    //五月May —— May.
    //
    //六月June —— Jun.
    //
    //七月July  —— Jul.
    //
    //八月August—— Aug.
    //
    //九月September —— Sept.
    //
    //十月October —— Oct.
    //
    //十一月November —— Nov.
    //
    //十二月December—— Dec.
    public static Date parseTime(String time) {
        try {
            if (StringUtils.isNotBlank(time)) {
                if (time.contains("/")) {
                    String[] split = time.split("/");
                    System.out.println(split.toString());
                    String s = split[0];
                    String s1 = split[1];
                    String s2 = split[2];
                    if (s.length() == 1) {
                        s = ("0" + s);
                    }
                    if (s1.length() == 1) {
                        s1 = ("0" + s1);
                    }
                    if (s2.length() > 2) {
                        s2 = s2.substring(0, 2);
                    }

//                    Date parse = simpleDateFormat.parse(s2 + "-" + s1 + "-" + s);
                    Date parse = simpleDateFormat.parse(s + "-" + s1 + "-" + s2);
                    return parse;

                }
//                else if (time.contains(".")) {
//                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy.MM.dd");
//                    return simpleDateFormat1.parse(time);
//                }
                else if (time.contains("-")) {
                    Date parse = simpleDateFormat.parse(time);
                    return parse;
                } else {
                    String[] s = time.split(" ");
                    String s1 = "";
                    String s2 = "";
                    String s3 = "";
                    if (time.contains(",")) {
                        s1 = s[0];

                        s2 = s[1].substring(0, 2);

                        s3 = s[2].substring(0, 4);
                    } else {
                        s1 = s[1];

                        s2 = s[0];

                        s3 = s[2].substring(0, 4);
                    }
                    //Jul 27, 2021.


                    String mm = "";
                    switch (s1) {
                        case "January":
                            mm = "01";
                            break;
                        case "February":
                            mm = "02";
                            break;
                        case "March":
                            mm = "03";
                            break;
                        case "April":
                            mm = "04";
                            break;
                        case "June":
                            mm = "06";
                            break;
                        case "July":
                            mm = "07";
                            break;
                        case "August":
                            mm = "08";
                            break;
                        case "November":
                            mm = "09";
                            break;
                        case "October":
                            mm = "10";
                            break;
                        case "Sept":
                            mm = "09";
                            break;
                        case "Sept.":
                            mm = "09";
                            break;
                        case "September":
                            mm = "09";
                            break;
                        case "December":
                            mm = "10";
                            break;
                        case "Jan":
                            mm = "01";
                            break;
                        case "Jan.":
                            mm = "01";
                            break;
                        case "Feb":
                            mm = "02";
                            break;
                        case "Feb.":
                            mm = "02";
                            break;
                        case "Mar":
                            mm = "03";
                            break;
                        case "Apr":
                            mm = "04";
                            break;
                        case "May":
                            mm = "05";
                            break;
                        case "Jun":
                            mm = "06";
                            break;
                        case "Jul":
                            mm = "07";
                            break;
                        case "Aug":
                            mm = "08";
                            break;
                        case "Aug.":
                            mm = "08";
                            break;
                        case "Sep":
                            mm = "09";
                            break;
                        case "Oct":
                            mm = "10";
                            break;
                        case "Oct.":
                            mm = "10";
                            break;
                        case "Nov":
                            mm = "11";
                            break;
                        case "Nov.":
                            mm = "11";
                            break;
                        case "Dec":
                            mm = "12";
                            break;
                        case "Dec.":
                            mm = "12";
                            break;
                    }
                    //July 30, 2021  May 25, 2021
//                    String[] split = s[1].split(",");

                    //Oct 06, 1972
                    System.out.println(time);
                    System.out.println(mm);


                    Date parse = simpleDateFormat.parse(s3 + "-" + mm + "-" + s2);
                    return parse;
//                } else {
//                    String[] split = time.split("-");
//                    String s = split[0]; // 日
//                    String s1 = split[1];// "Oct"
//                    String s2 = split[2]; //年
//                    String timestr = "19"+s2+"-10-"+s;


//                    String[] split = time.split("-");
//                    String s = split[0];
//                    String s1 = split[1];
//                    String m = "";
//                    if (s1.equals("Jul")){
//                        m = "05";
//                    }else if (s1.equals("May")){
//                        m = "07";
//                    }
//                    String s2 = split[2];
//
//                    String timestr = "20"+s2+"-"+m+"-"+s;
//                    Date parse = simpleDateFormat.parse(timestr);
//                    Date parse;
//                    if (time.contains(".")){
//                        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
//                        parse = simpleDateFormat.parse(time);
//                    }else{
//                        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        parse = simpleDateFormat.parse(time);
//                    }
//                    return parse;
//                }
                }

            } else {
                return null;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 批量索引
     *
     * @param indexName
     * @param type
     * @param sources
     */
    public static boolean bulkInsert(String indexName, String type, List<JSONObject> sources) {
        boolean isSuccess = false;
        BulkRequest bulkRequest = new BulkRequest();
        for (JSONObject source : sources) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(indexName);
            indexRequest.id(source.getStr("only_id") + "_88");
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
                System.out.println("批量索引有失败，具体消息：{}" + bulkResponse.buildFailureMessage());
            }
        } catch (IOException e) {
            System.out.println("issues: " + e);
        }
        return isSuccess;
    }


}
