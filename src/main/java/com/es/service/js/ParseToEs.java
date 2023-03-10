package com.es.service.js;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carrotsearch.labs.langid.DetectedLanguage;
import com.carrotsearch.labs.langid.ILangIdClassifier;
import com.carrotsearch.labs.langid.LangIdV3;
import com.es.service.ESConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ParseToEs
 * @Description TODO
 * @Author QiBin
 * @Date 2021/1/23下午12:51
 * @Version 1.0
 **/
@Slf4j
@Component
public class ParseToEs {

    public static String Em_Regex = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
    public static String Pm_Regex = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

    public static void parse(TweetsVo tweetsVo) {
        RegexUtil regexUtil = new RegexUtil();

        if (StringUtils.isBlank(tweetsVo.getRepliedUser()) && StringUtils.isEmpty(tweetsVo.getRepliedUser())) {
            String retweetScreenName = tweetsVo.getRetweetScreenName();
            if (StringUtils.isNotBlank(retweetScreenName)) {
                tweetsVo.setTweetAccoun(retweetScreenName);
                //转发
                tweetsVo.setSpiderType("1");
            } else {
                tweetsVo.setTweetAccoun(tweetsVo.getUsername());
                tweetsVo.setSpiderType("0");
            }

            if (StringUtils.isNotBlank(tweetsVo.getUsername()))
                tweetsVo.setUsername(tweetsVo.getUsername().toLowerCase());

            if (StringUtils.isNotBlank(tweetsVo.getRetweetScreenName()))
                tweetsVo.setRetweetScreenName(tweetsVo.getRetweetScreenName().toLowerCase());

            // 将@用户、提及的话题解析出来
            cn.hutool.json.JSONObject jo = JSONUtil.parseObj(tweetsVo);
//            JSONObject jo = (JSONObject) JSONObject.toJSON(tweetsVo);

            // 将发帖时间格式化
            Date time = tweetsVo.getTimedate();
            if (time != null) {
                long timeLong = time.getTime();
                if (String.valueOf(timeLong).length() == 10) {
                    jo.put("time", timeLong * 1000L);
                    jo.put("updateTimes", timeLong * 1000L);
                } else if (String.valueOf(timeLong).length() == 13) {
                    jo.put("time", timeLong);
                    jo.put("updateTimes", timeLong);
                }
                jo.put("timeFormat", stampToDate(timeLong));

            } else {
                jo.put("time", 0);
                jo.put("timeFormat", "");
                jo.put("updateTimes", 0);
            }
            //添加字段  取出正则 电话号码和邮箱
            try {
                List<String> numString = new ArrayList<>();
                if (jo.containsKey("tweetsContent")) {
                    String at_tweetsContent = (String) jo.get("tweetsContent");
                    if (StringUtils.isNotBlank(at_tweetsContent)) {
                        //语种判断
                        ILangIdClassifier langIdClassifier = new LangIdV3();
                        DetectedLanguage classify = langIdClassifier.classify(at_tweetsContent, false);
                        String languageCode = classify.getLangCode();
                        jo.put("spiderUrl", languageCode);

                        List<String> subUtil = regexUtil.getSubUtil(at_tweetsContent, Em_Regex);
                        List<String> subUtil1 = regexUtil.getSubUtil(at_tweetsContent, Pm_Regex);
                        if (CollectionUtils.isNotEmpty(subUtil1))
                            numString.addAll(subUtil1);
                        if (CollectionUtils.isNotEmpty(subUtil))
                            numString.addAll(subUtil);
                    } else {
                        jo.put("spiderUrl", "en");
                    }
                }

                StringBuffer str = new StringBuffer();
                if (CollectionUtils.isNotEmpty(numString)) {
                    for (int k = 0; k < numString.size(); k++) {
                        if (k < numString.size() - 1) {
                            str.append(numString.get(k)).append(",");
                        } else {
                            str.append(numString.get(k));
                        }
                    }
                }
                jo.put("reserve3", str.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (StringUtils.isNotBlank(jo.getStr("userJson"))) {
                System.out.println(jo.getStr("userJson"));
                JSONArray jsonArray = JSONArray.parseArray(UnicodeUtil.toString(jo.getStr("userJson")));
                if (jsonArray.size() > 1) {
                    for (int n = 1; n < jsonArray.size(); n++) {
                        JSONObject ja = jsonArray.getJSONObject(n);
                        String userName = ja.getString("screen_name").toLowerCase();
                        String fullName = ja.getString("name");
                        String userId = ja.getString("id_str");

                        // 1.将账号信息存到kafka
                        TweetUserVo userVo = new TweetUserVo();
                        userVo.setRid(userId);
                        // 判断是否已经存在信息完整的节点
//                            if (userNameList.contains(userName)) {
//                                continue;
//                            }
                        userVo.setUsername(userName.toLowerCase());
                        userVo.setFullname(UnicodeUtil.toString(fullName));
                        userVo.setInputTime(new Date());
                        userVo.setDatasetId(tweetsVo.getDatasetId());
                        userVo.setDatasetName(tweetsVo.getDatasetId());


                        // 账号名转小写
//                            if (StringUtils.isNotBlank(userVo.getUsername())) {
//                                userVo.setReserve3(userVo.getUsername().toLowerCase());
//                            }
                        JSONObject user = (JSONObject) JSONObject.toJSON(userVo);
                        user.put("registeredTime", 0);
                        user.put("inputTime", 0);
                        user.put("mediatname", "TwitterUser");
                        user.put("datasetId", 9999);
                        user.put("delFlag", 0);
                        user.put("updateTimes", 0);


                        //边关系
                        String username = ja.getString("screen_name").toLowerCase();

                        // 将关系推送到kafka
                        JSONObject result = new JSONObject();
                        result.put("data", "");
                        result.put("edgue", "mention");
                        result.put("startV", tweetsVo.getUsername().toLowerCase());
                        result.put("endV", username);
                        result.put("startField", "username");
                        result.put("endField", "username");
                        if (StringUtils.isNotBlank(tweetsVo.getRetweetId())) {
                            result.put("uniqueField", tweetsVo.getRetweetId());
                        } else
                            result.put("uniqueField", tweetsVo.getTweetsId());
                        result.put("datasetId", 9999);
//                            result.put("startlable", "Pesonal");
                        result.put("startlable", "TwitterUser");
//                            result.put("endlable", "Pesonal");
                        result.put("endlable", "TwitterUser");
                        result.put("mediatname", "mention");
                        result.put("delFlag", 0);
                        result.put("updateTimes", jo.getStr("time"));

                        //入es  边
//                        System.out.println(result);
//                        indexEs("js_twitter_e", "edge", result);
                    }
                }
            }
            // 解析userJson ，提取关键数据
            if (StringUtils.isNotBlank(jo.getStr("userJson"))) {
                JSONArray jsonArray = JSONArray.parseArray(UnicodeUtil.toString(jo.getStr("userJson")));
                List<String> userArray = new ArrayList<>();
                if (jsonArray.size() > 1) {
                    for (int n = 1; n < jsonArray.size(); n++) {
                        JSONObject ja = jsonArray.getJSONObject(n);
                        String tagName = ja.getString("screen_name");
                        userArray.add(tagName);
                    }
                    String users = userArray.toString().replace("[", "").replace("]", "");
                    jo.put("userJson", users);

                } else {
                    jo.put("userJson", "");
                }
            } else {
                jo.put("userJson", "");
            }


            if (StringUtils.isNotBlank(tweetsVo.getRetweetScreenName())) {
                // 主帖【转发关系（边）】
                String username = tweetsVo.getUsername().toLowerCase();
                if (StringUtils.isNotBlank(retweetScreenName)) {
                    JSONObject result = new JSONObject();
                    result.put("data", "");
                    result.put("edgue", "forward");
                    result.put("startV", retweetScreenName.toLowerCase());
                    result.put("endV", username);
                    result.put("startField", "username");
                    result.put("endField", "username");
                    result.put("uniqueField", tweetsVo.getTweetsId());
                    result.put("datasetId", 9999);
//                result.put("startlable", "Pesonal");
                    result.put("startlable", "TwitterUser");
//                result.put("endlable", "Pesonal");
                    result.put("endlable", "TwitterUser");
                    result.put("mediatname", "forward");
                    result.put("delFlag", 0);
                    result.put("updateTimes", jo.getStr("time"));

                    //转发关系入es
//                    System.out.println(result);
//                    indexEs("js_twitter_e","edge",result);
                }
            }

            // 解析 tagJson，提取关键数据
            if (StringUtils.isNotBlank(jo.getStr("tagJson"))) {
                JSONArray jsonArray = JSONArray.parseArray(jo.getStr("tagJson"));
                StringBuffer stringBuffer = new StringBuffer();

                for (int n = 0; n < jsonArray.size(); n++) {
                    JSONObject ja = jsonArray.getJSONObject(n);
                    String tagName = ja.getString("text");
                    stringBuffer.append("#" + tagName);

                    //边关系
                    String username = tweetsVo.getUsername().toLowerCase();

                    JSONObject result = new JSONObject();
                    result.put("data", "");
                    result.put("edgue", "topic");
                    result.put("startV", tagName);
                    result.put("endV", username);
                    result.put("startField", "tagJson");
                    result.put("endField", "username");
                    result.put("uniqueField", tweetsVo.getTweetsId());
                    result.put("datasetId", 9999);
//                        result.put("startlable", "Topic");
                    result.put("startlable", "TwitterTopic");
//                        result.put("endlable", "Pesonal");
                    result.put("endlable", "TwitterUser");
                    result.put("mediatname", "topic");
                    result.put("delFlag", 0);
                    result.put("updateTimes", jo.getStr("time"));

                    //话题关系入es

//                    System.out.println(result);
//                    indexEs("js_twitter_e", "edge", result);

                }
                //String tags = tagList.toString().replace("[", "").replace("]", "").replace(",", "");
                String tags = stringBuffer.toString();
                tweetsVo.setTagJson(tags);
                jo.put("tagJson", tags);
            } else {
                jo.put("tagJson", "");
            }

            jo.put("mediatname", "TwitterTweet");
            //判断  如果不是任务如果不是指定关键词  入9999  如果是  入本身的数据集id

            jo.put("delFlag", 0);

            JSONObject jsonObject = new JSONObject();
            Set<Map.Entry<String, Object>> entries = jo.entrySet();

            if (CollectionUtils.isNotEmpty(entries)) {
                for (Map.Entry<String, Object> entry : entries) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    jsonObject.put(key, value);
                    jsonObject.put(key + "__STRING", value);

                }
            }
            indexEs("janusgraph_janusgraph_allv_twitter_text", "janusgraph_allv_twitter_text", jsonObject);

        }
    }

    public static void indexEs(String indexName, String type, Object o) {
        IndexRequest request = new IndexRequest(indexName);
        request.type(type);

        String s = JSONUtil.toJsonStr(o);
        request.source(s, XContentType.JSON);
        try {
            ESConnection.getClient().index(request, RequestOptions.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * <p>【描述】：将时间戳转换为时间</p>
     * <p>【方法】：stampToDate</p>
     * <p>【参数】: [s]</p>
     * <p>【作者】: BeyMax</p>
     * <p>【日期】: 2020/1/8</p>
     **/
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * 对象转map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) != null) {
                    map.put(key + "", beanMap.get(key));
                }
            }
        }
        return map;
    }
}
