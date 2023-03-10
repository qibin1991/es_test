package com.es.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.es.service.qd.FBUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.es.service.EsTestService.getEsData;

/**
 * @ClassName AnalysisText
 * @Description TODO
 * @Author QiBin
 * @Date 2020/10/1314:16
 * @Version 1.0
 **/
@Slf4j
public class AnalysisText {


    public static StringBuffer readTxtFile(String filePath) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);

            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    stringBuffer.append(lineTxt);
                }
                read.close();
            } else {
                System.out.println("====找不到文件====");
            }
        } catch (Exception
                e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return stringBuffer;
    }


    //主贴
    public static void parseTweet(String path) {
        StringBuffer stringBuffer = readTxtFile(path);
        String s = "[" + stringBuffer + "]";
        List<Map<String, Object>> faceBookVos = new ArrayList<>();
        List<Map<String, Object>> fbUserVos = new ArrayList<>();
        List<Map<String, Object>> fbbooks = new ArrayList<>();

        JSONArray objects = JSONArray.parseArray(s);
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            jsonObject.put("userId", "100006656040045");
            String timestamp = jsonObject.getString("timestamp");
            String taskId = DigestUtils.md5Hex("100006656040045" + timestamp + new Random(100000));
            jsonObject.put("tweetsId", taskId);
            if (jsonObject.containsKey("comment_list")) {
                JSONArray comment_list = jsonObject.getJSONArray("comment_list");
                if (!CollectionUtils.isEmpty(comment_list)) {

                    for (int j = 0; j < comment_list.size(); j++) {
                        JSONObject comment = comment_list.getJSONObject(j);
                        FaceBookVo faceBookVo = new FaceBookVo();
                        faceBookVo.setSourceId(taskId);
                        faceBookVo.setOriginalContent(comment.getString("comment_text"));
                        faceBookVo.setOriginalName(comment.getString("original_screen_name"));
                        faceBookVo.setOriginalScreenName(comment.getString("original_screen_name"));
                        String comment_timestamp = comment.getString("comment_timestamp");

                        faceBookVo.setTime(parseTime(comment_timestamp));
                        if (StringUtils.isNotBlank(comment_timestamp))
                            faceBookVo.setTimeLong(Long.valueOf(comment_timestamp) * 1000L);
                        //评论入es comment
                        faceBookVo.setOriginalUrl(comment.getString("comment_userhref"));
                        faceBookVo.setUserName(jsonObject.getString("post_user"));
                        faceBookVo.setShareId(jsonObject.getString("post_id"));
                        faceBookVo.setOriginalId(faceBookVo.getShareId() + "_" + faceBookVo.getOriginalScreenName()
                                + "_" + faceBookVo.getOriginalTime());
                        faceBookVo.setDataSetId("99999");
                        Map<String, Object> fbMap = BeanUtil.beanToMap(faceBookVo);
                        faceBookVos.add(fbMap);

                        //评论人入user
                        FBUserVo fbUserVo = new FBUserVo();
                        fbUserVo.setUserName(faceBookVo.getOriginalScreenName());
                        fbUserVo.setFullName(faceBookVo.getOriginalScreenName());
                        fbUserVo.setUserUrl(faceBookVo.getOriginalUrl());
                        fbUserVo.setUserId(faceBookVo.getOriginalId());
                        fbUserVo.setDataSetId("99999");

                        Map<String, Object> userMap = BeanUtil.beanToMap(fbUserVo);
                        fbUserVos.add(userMap);
                    }

                }
            }

            FaceBookVo faceBookVo = jsonObject.toJavaObject(FaceBookVo.class);
            String time = faceBookVo.getTime();
            faceBookVo.setTime(parseTime(time));
            faceBookVo.setDataSetId("99999");
            if (StringUtils.isNotBlank(time))
                faceBookVo.setTimeLong(Long.valueOf(time) * 1000L);
            else faceBookVo.setTimeLong(0L);
            fbbooks.add(BeanUtil.beanToMap(faceBookVo));

            if (fbbooks.size() == 1000) {
                bulkInsert("js_facebook_post", "post", fbbooks);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fbbooks.clear();
            }
            if (fbUserVos.size() == 1000) {
                bulkInsert("js_facebook_user", "user", fbUserVos);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fbUserVos.clear();
            }

            if (faceBookVos.size() == 1000) {
                //评论
                bulkInsert("js_facebook_comment", "comment", faceBookVos);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                faceBookVos.clear();
            }

        }
        if (!CollectionUtils.isEmpty(faceBookVos))
            bulkInsert("js_facebook_comment", "comment", faceBookVos);
        if (!CollectionUtils.isEmpty(fbUserVos))
            bulkInsert("js_facebook_user", "user", fbUserVos);
        if (!CollectionUtils.isEmpty(fbbooks))
            bulkInsert("js_facebook_post", "post", fbbooks);

    }

    public static void gettableusers() {


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.termQuery("username__STRING", "realdonaldtrump"))
                .must(QueryBuilders.termQuery("datasetId", "9999"));


        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_pesonal"});
        aggRequestVO.setType("janusgraph_allv_twitter_pesonal");
        AggregationBuilder aggregation =
                AggregationBuilders
                        .terms("agg").field("username__STRING")
                        .subAggregation(
                                AggregationBuilders.topHits("top").sort("inputTime", SortOrder.DESC).size(1)
                        );

        aggRequestVO.setAggregationBuilder(aggregation);

        aggRequestVO.setQueryBuilder(boolQueryBuilder);


        try {
            List<cn.hutool.json.JSONObject> aggs = aggs(aggRequestVO);
            System.out.println(aggs.size());
            System.out.println(aggs.get(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 聚合（对一/二级聚合友好）
     *
     * @param aggRequestVO
     */
    public static List<cn.hutool.json.JSONObject> aggs(AggRequestVO aggRequestVO) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(aggRequestVO.getIndexName());
        searchRequest.types(aggRequestVO.getType());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(aggRequestVO.getQueryBuilder());
        searchSourceBuilder.aggregation(aggRequestVO.getAggregationBuilder());
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
//        logger.info("检索索引：{},\n检索语句：{}", Arrays.toString(searchRequest.indices()), searchRequest.source().toString());
        System.out.println("检索索引：" + Arrays.toString(searchRequest.indices()) + "\n检索语句：" + searchRequest.source().toString());
        Aggregations aggregations = ESConnection.getClient().search(searchRequest, RequestOptions.DEFAULT)
                .getAggregations();
        List<cn.hutool.json.JSONObject> list = new ArrayList<>();
        aggResultParser(aggregations, list, null);
        return list;
    }

    private static void aggResultParser(Aggregations aggregations, List<cn.hutool.json.JSONObject> list, JSONObject node) {
        aggregations.iterator().forEachRemaining(aggregation -> {

            Terms agg = aggregations.get("agg");
            for (Terms.Bucket entry : agg.getBuckets()) {
//                String key = String.valueOf(entry.getKey());
//                long docCount = entry.getDocCount();
                TopHits topHits = entry.getAggregations().get("top");
                for (SearchHit hit : topHits.getHits()) {
                    String id = hit.getId();
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    sourceAsMap.put("_id", id);
                    list.add(JSONUtil.parseFromMap(sourceAsMap));
                }
            }
        });
    }

    public static Map<String, Integer> userTopics(String username) {
//        GraphTraversalSource traversal = janusGraphConfig.graph.traversal();
//        List<Object> objects = traversal.V().has("username", username)
//                .has("mediatname", "TwitterUser")
//                .inE().has("mediatname", "topic").outV().
//                        groupCount().by(outE().count()).order(Scope.local)
//                .by(values(), Order.desc).limit(Scope.local, 30)
//                .values("tagJson").toList();

//        List<Object> objects = traversal.V().has("username", username)
//                .has("mediatname", "TwitterUser").in().has("mediatname", "TwitterTopic")
//                .groupCount().by("tagJson").order(Scope.local)
//                .by(__.values(), Order.desc).limit(Scope.local, 30).toList();
//        return objects;
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setIndexName(new String[]{"js_twitter_e"});
        aggRequestVO.setType("edge");

        AggregationBuilder aggregationBuilder = null;
        List<JSONObject> resultList = new ArrayList();
        boolQueryBuilder.must(QueryBuilders.termQuery("mediatname", "topic"))
                .must(QueryBuilders.termQuery("datasetId", "9999"));
        aggregationBuilder = AggregationBuilders.terms("sum")
                .field("startV");
        aggRequestVO.setAggregationBuilder(aggregationBuilder);
        aggRequestVO.setQueryBuilder(boolQueryBuilder);


        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        boolQueryBuilder1.must(QueryBuilders.termQuery("mediatname", "topic"))
                .must(QueryBuilders.termQuery("endV", username))
                .must(QueryBuilders.termQuery("datasetId", "9999"));
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        searchRequestVO.setQueryBuilder(boolQueryBuilder1);
        searchRequestVO.setIndexName(new String[]{"js_twitter_e"});
        searchRequestVO.setType("edge");

        Set<String> strings = new HashSet<>();
        HashMap<String, Integer> finalOut = new LinkedHashMap<>();

        Map<String, Integer> res = new HashMap<>();
        try {
            List<JSONObject> list = searchUtil.aggsThree(aggRequestVO);

            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            for (Map<String, Object> datum : data) {
                Object startV = datum.get("startV");
                if (startV != null) {
                    strings.add(startV.toString().trim());
                }
            }
            for (JSONObject jsonObject : list) {
                JSONObject node = jsonObject.getJSONObject("node");
                Set<Map.Entry<String, Object>> entries = node.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (strings.contains(key) && value != null) {
                        res.put(key, Integer.valueOf(value.toString()));
                    }
                }
            }
            if (res.size() > 30) {
                res.entrySet()
                        .stream()
                        .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
                        .collect(Collectors.toList()).subList(0, 30).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
                return finalOut;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }


    /**
     * 用户最新发文时间
     */
    public static List<JSONObject> getNewPostTime() {

        List<String> stringList = Arrays.asList("tomstites", "rhpsia", "deanstarkman", "nytimes", "austinramzy");
        List<String> asList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(stringList)) {
            for (String s1 : stringList) {
                asList.add(s1.toLowerCase());
            }
        }


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();

        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
        aggRequestVO.setType("janusgraph_allv_twitter_text");

        boolQueryBuilder.must(QueryBuilders.termQuery("delFlag", "0"))
                .must(QueryBuilders.termsQuery("username__STRING", asList))
                .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"));

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("user_post_time")
                .field("username__STRING").subAggregation(AggregationBuilders.topHits("timeFormat__STRING")
                        .sort("timeFormat__STRING", SortOrder.DESC).size(1))
                .subAggregation(AggregationBuilders.terms("time").field("timeFormat__STRING"));

        aggRequestVO.setSize(asList.size());
        aggRequestVO.setQueryBuilder(boolQueryBuilder);
        aggRequestVO.setAggregationBuilder(termsAggregationBuilder);
        try {
            List<JSONObject> jsonObjects = searchUtil.aggsThree(aggRequestVO);
            List<JSONObject> result = new ArrayList<>();
            if (!CollectionUtils.isEmpty(jsonObjects)) {
                for (JSONObject jsonObject : jsonObjects) {
                    JSONObject res = new JSONObject();
                    String node = jsonObject.getString("node");
                    String substring1 = node.substring(node.indexOf("{") + 2, node.indexOf(":") - 1);
                    res.put("name", substring1);
                    if (jsonObject.containsKey("childNode")) {
                        String childNode = jsonObject.getString("childNode");
                        System.out.println(childNode);
                        if (childNode.contains(",")) {
                            String substring = childNode.substring(childNode.indexOf("{") + 2, childNode.indexOf(","));
                            String substring2 = substring.substring(0, substring.lastIndexOf(":") - 1);
                            res.put("time", substring2);
                        } else {
                            String substring = childNode.substring(childNode.indexOf("{") + 2, childNode.lastIndexOf(":"));
                            res.put("time", substring);
                        }


                    }

                    result.add(res);
                }
            }

//            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return new ArrayList<>();
    }

    public static void tolow() {
        List<String> s = Arrays.asList("GulchehraHoja", "UyghurProject", "NoRightsNoGames", "amytheblue", "sashachavkin", "shirafu", "dtbyler", "HamutTahir", "MarinaWalkerG", "ICIJorg", "antoniocuga", "He_Shumei", "ChuBailiang", "dakekang", "BethanyAllenEbr", "robindbrant", "CUyghurs", "AASAsianStudies", "Ilhan", "HKokbore", "aygeryma", "JNBPage", "nytimes", "adrianzenz", "WeNeedToKnowNow", "JimMillward", "meclarke114", "WilliamYang120", "begbol", "KiyyaBaloch", "atajurt_kazak", "mbachelet", "Atajurt", "UyghurYadVashem", "AbdulahGulja", "Tumaris_Almas", "UygurHaber", "Weiboscope", "L10nLab", "BelgiumUyghur", "ABCChinese", "AustralianUygh1", "observatoryihr", "EpochTimes", "ak_mack", "SophieHRW", "Bencosmef", "XiaBamboohermit", "RobbieGramer", "mhzaman", "statedeptspox", "turkistan_tv", "donaldcclarke", "nuryturkel", "BitterWinterMag", "weiwuernews", "CAIRNational", "patrickpoon", "NijatTurkistan", "amnestyusa", "sedat_peker", "JewherIlham", "bequelin", "turkistaner", "Uyghurspeaker", "AsiaMattersEWC", "FergusShiel", "joshchin", "hrw", "PeteCIrwin", "Nurahmet9", "nukinfo", "RianThum", "AsiyeUyghur", "gerryshih", "saveeastturk", "Erkin_Azat", "UighurT", "MemetTohti", "NEDemocracy", "GulnazUyghur", "MuhammadRabiye", "UyghursVic", "UyghurCongress", "wang_maya", "Magnus_Fiskesjo", "Bsintash", "abimalaysia", "DrZuhdiJasser", "MPWangTingyu", "SCMPNews", "j_smithfinley", "aydinanwar_", "Mustafacan_Aksu", "rudder", "Radio_Azattyk", "UyghurN", "VanessaFrangvi1", "ShireenMazari1", "ajplus", "maryamhanim", "LouisaCGreve", "SigalSamuel", "AidUyghur", "tomstites", "badiucao", "ChrisRickleton", "eastturkistann", "Trailbl16797622", "babussokutan", "jardemalie", "Junmai1103", "mervesebnem", "0715Rita", "KuzzatAltay", "DGTam86", "BaldingsWorld", "uyghur28933032", "FaridaDeif", "shahitbiz", "Alfred_Uyghur", "SumOfUs", "UighurTurk", "Jenn_Chowdhury", "GroseTimothy", "ihhinsaniyardim", "meghara");

        List<String> r = Arrays.asList("tomstites", "rhpsia", "deanstarkman", "nytimes", "austinramzy", "ssboland", "rudder", "mbachelet", "kiyyabaloch", "shireenmazari1", "begbol", "radio_azattyk", "aygeryma", "l10nlab", "belgiumuyghur", "weiboscope", "sumofus", "statedeptspox", "intypython", "xiabamboohermit", "jardemalie", "abimalaysia", "babussokutan", "bencosmef", "mhzaman", "aslima03846189", "donaldcclarke", "mpwangtingyu", "baldingsworld", "molos123", "robindbrant", "gulchehrahoja", "cuyghurs", "ilhan", "aasasianstudies", "hoghuzkhan", "yanghin_uyghur", "uygurtimes", "atajurt_kazak", "mustafacan_aksu", "uyghuryadvashem", "yananw", "trailbl16797622", "arslanotkur", "sophia_yan", "atajurt", "uygurhaber", "muhammadrabiye", "turkistantmes", "uighurturk", "gulnazuyghur", "turkicuyghur", "abdulahgulja", "australianuygh1", "tarimuyghur", "sabriuyghur", "bsintash", "maryamhanim", "mamatatawulla", "uyghurn", "drzuhdijasser", "louisacgreve", "mjt1214", "gheribjan", "magnus_fiskesjo", "sophiehrw", "salih_hudayar", "beijingpalmer", "nijatturkistan", "bitterwintermag", "weiwuernews", "saveuighurusa", "amnestyusa", "uyghursvic", "nuryturkel", "uyghurcongress", "grosetimothy", "scmpnews", "ajplus", "dgtam86", "williamyang120", "vanessafrangvi1", "wang_maya", "j_smithfinley", "gerryshih", "joshchin", "meghara", "badiucao", "hrw", "uyghurproject", "nurahmet9", "sigalsamuel", "norightsnogames", "arslan_hidayat", "uighurt", "alfred_uyghur", "0715rita", "asiyeuyghur", "erkin_azat", "hkokbore", "aydinanwar_", "tombschrader", "weneedtoknownow", "adrianzenz", "meclarke114", "jimmillward", "andersonelisem", "jnbpage", "junmai1103", "chrisrickleton", "mervesebnem", "sophiemcneill", "jenn_chowdhury", "ak_mack", "robbiegramer", "abcchinese", "4corners", "epochtimes", "faridadeif", "tingdc", "tumaris_almas", "observatoryihr", "cairnational", "bequelin", "jewherilham", "turkistan_tv", "abdughenisabit", "ssbilir_", "sedat_peker", "patrickpoon", "sarkikebir", "turkistaner", "aiduyghur", "kuzzataltay", "millioyghunush", "eastturkistann", "nedemocracy", "ihhinsaniyardim", "memettohti", "gemkifujii", "uyghur28933032", "naokomizutani", "nukinfo", "saveeastturk", "isobelyeung", "petecirwin", "mattjtucker55", "hamish", "asiamattersewc", "abduwela", "yy56936953", "ssboland", "rudder", "hamish", "rianthum", "uyghurspeaker", "shahitbiz", "fergusshiel", "amytheblue", "sashachavkin", "shirafu", "dtbyler", "halmuratu", "hamuttahir", "uighurian", "marinawalkerg", "icijorg", "he_shumei", "antoniocuga", "chubailiang", "dakekang", "bethanyallenebr");

        List<String> ss = new ArrayList<>(r);
        List<String> result = new ArrayList<>();
        for (String s1 : s) {
            String s2 = s1.toLowerCase();
            result.add(s2);
        }

        for (String s1 : result) {
            if (ss.contains(s1)) {
                ss.remove(s1);
            }
        }
        System.out.println(ss);
    }




    //消费粉丝   消费好友
    public static Map parseFollows(JSONObject jsonObject) {
        FBFriendsVo fbFollowsVo = jsonObject.toJavaObject(FBFriendsVo.class);
        FBUserVo fbUserVo = new FBUserVo();
        fbUserVo.setSourceId("100006656040045");
        fbUserVo.setUserName(fbFollowsVo.getFriendsName());
        fbUserVo.setUserId(fbFollowsVo.getFriendsId());
        fbUserVo.setUserUrl(fbFollowsVo.getFriendsUrl());
        fbUserVo.setUserImg(fbFollowsVo.getFriendsImg());
        //入 es user
        fbUserVo.setDataSetId("99999");

        Map<String, Object> userMap = BeanUtil.beanToMap(fbUserVo);
        return userMap;
    }


    public static void parseUsers(JSONObject jsonObject) {
        try {

            FBUserVo fbUserVo = jsonObject.toJavaObject(FBUserVo.class);
            fbUserVo.setDataSetId("99999");

            IndexRequest request = new IndexRequest("js_facebook_user");
            request.type("user");
            String s = JSONUtil.toJsonStr(fbUserVo);
            request.source(s, XContentType.JSON);


            System.out.println(s);

            try {
                System.out.println("=======" + s);
                ESConnection.getClient().index(request, RequestOptions.DEFAULT);
                System.out.println("======111");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void searchTag() {
        //话题
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();

        AggRequestVO aggRequestVO = new AggRequestVO();


        aggRequestVO.setIndexName(new String[]{"js_saveas_tweet"});
        aggRequestVO.setType("tweet");
        boolQueryBuilder.must(QueryBuilders.termQuery("datasetId__STRING", "1"))
                .must(QueryBuilders.termQuery("delFlag", "0"))
                .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"));

        AggregationBuilder aggregationBuilder = null;
        List resultList = new ArrayList();
        aggregationBuilder = AggregationBuilders.terms("sum")
                .field("tagJson__STRING");

        aggRequestVO.setQueryBuilder(boolQueryBuilder);
        aggRequestVO.setAggregationBuilder(aggregationBuilder);
        resultList = getEsData(aggRequestVO);
        System.out.println(resultList);
    }


    public static void readIndex() {
        String path = "/Users/qibin/zyyt相关/facebook新版/原始1012/9.18gene.bunin/index.json";
        StringBuffer stringBuffer = readTxtFile(path);
        String s = stringBuffer.toString();
        JSONObject jsonObject = JSONObject.parseObject(s);
        parseUsers(jsonObject);
    }

    public static void readTweets() {
        parseTweet("/Users/qibin/zyyt相关/facebook新版/原始1012/timeline(1)/timeline.json");
    }

    public static void readFollow() {
        String path = "/Users/qibin/zyyt相关/facebook新版/原始1012/gene.bunin/friends.json";
        StringBuffer stringBuffer = readTxtFile(path);
        StringBuffer append = new StringBuffer().append("[").append(stringBuffer).append("]");
        String s = append.toString();
        JSONArray objects = JSONArray.parseArray(s);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            Map map = parseFollows(jsonObject);
            list.add(map);
        }

        bulkInsert("js_facebook_friend", "friend", list);

    }


//    public static void main(String[] args) {
////        reader();
//        searchTag();
//
//        System.out.println("=========");
//
//
//    }


    public static void reader() {
        String path = "/Users/qibin/Downloads/baike.txt";


        try {
//            FileReader fileReader = new FileReader(path,"gbk");

            List<Map<String, Object>> list = readTxt(path);
//            insertList(list);

//  一次性读取

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    /**
     * 递归  存es
     *
     * @param list
     */
    public static void insertList(List<Map<String, Object>> list) {
        try {
            if (!CollectionUtils.isEmpty(list)) {
                if (list.size() > 1000) {
                    List<Map<String, Object>> list1 = list.subList(0, 1000);
                    bulkInsert("js_bd_baike", "text", list1);
                    boolean b = list.removeAll(list1);
                    insertList(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     *
     * @param filePath
     */
    public static List<Map<String, Object>> readTxt(String filePath) {
        List<Map<String, Object>> list = new ArrayList<>();
        BaikeEntity baikeEntity = new BaikeEntity();
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if (lineTxt.contains("<ID>=")) {
                        if (baikeEntity.getId() != 0L) {
//                            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(baikeEntity);
                            IndexRequest request = new IndexRequest("js_bd_baike");
                            request.type("text");
                            request.source(JSON.toJSONString(baikeEntity), XContentType.JSON);
                            try {
                                ESConnection.getClient().index(request, RequestOptions.DEFAULT);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            list.add(stringObjectMap);

                            baikeEntity = new BaikeEntity();
                            String substring = lineTxt.substring(lineTxt.indexOf(">=") + 2);
                            if (substring.contains("=")) {
                                substring = substring.substring(substring.indexOf("=") + 1);
                            }
                            baikeEntity.setId(Long.valueOf(substring));
                        } else {
                            String substring = lineTxt.substring(lineTxt.indexOf(">=") + 2);
                            if (substring.contains("=")) {
                                substring = substring.substring(substring.indexOf("=") + 1);
                            }
                            System.out.println(substring);
                            baikeEntity.setId(Long.valueOf(substring));
                        }
                    } else if (lineTxt.contains("<TITLE>=")) {
                        String substring = lineTxt.substring(lineTxt.indexOf("=") + 1);
                        baikeEntity.setTitle(substring);
                    } else if (lineTxt.contains("<CATEGORY>=")) {
                        String substring = lineTxt.substring(lineTxt.indexOf("=") + 1);
                        baikeEntity.setCategory(substring);
                    } else if (lineTxt.contains("<RETITLE>=")) {
                        String substring = lineTxt.substring(lineTxt.indexOf("=") + 1);
                        baikeEntity.setRetitle(substring);
                    } else if (lineTxt.contains("<URL>=")) {
                        String substring = lineTxt.substring(lineTxt.indexOf("=") + 1);
                        baikeEntity.setUrl(substring);
                    }
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return list;
    }

    public static String parseTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(time)) {
            Date date = new Date(Long.valueOf(time) * 1000L);
            String format = simpleDateFormat.format(date);
            return format;
        } else {
            return "";
        }


    }

    public static void cacheLanguage() {
        //读excel
        ExcelReader reader = ExcelUtil.getReader("/Users/qibin/zyyt_java/es_test/src/main/lang.xlsx");
        List<List<Object>> read = reader.read();
        for (List<Object> objects : read) {
            String key = (String) objects.get(0);
            String value = (String) objects.get(1);

        }
    }


    /**
     * 检查文件
     *
     * @param file
     * @throws IOException
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            log.error("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            log.error(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return workbook;
    }

    public static String stringDateProcess(Cell cell) {
        String result = new String();
        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            double value = cell.getNumericCellValue();
            Date date = org.apache.poi.ss.usermodel.DateUtil
                    .getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if (temp.equals("General")) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }

        return result;
    }


    /**
     * 批量索引
     *
     * @param indexName
     * @param type
     * @param sources
     */
    public static boolean bulkInsert(String indexName, String type, List<Map<String, Object>> sources) {
        boolean isSuccess = false;
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> source : sources) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(indexName);
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
            System.out.println("issues: =======" + e);
        }
        return isSuccess;
    }


    public static void excelSearchEs() {
        try {
            TwoAggreationUtil searchUtil = new TwoAggreationUtil();
//            SearchRequestVO searchRequestVO = new SearchRequestVO();
//            log.info("=======es中主贴翻译查询====");
//            searchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
//            searchRequestVO.setType("janusgraph_allv_twitter_text");

            ScrollSearchRequestVO scrollSearchRequestVO = new ScrollSearchRequestVO();
            scrollSearchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
            scrollSearchRequestVO.setType("janusgraph_allv_twitter_text");
            scrollSearchRequestVO.setLimit(1000);
            List<String> stringList = Arrays.asList("tomstites", "rhpsia", "deanstarkman", "nytimes",
                    "austinramzy", "ssboland", "rudder", "mbachelet", "kiyyabaloch", "shireenmazari1", "begbol",
                    "radio_azattyk", "aygeryma", "l10nlab", "belgiumuyghur", "weiboscope", "sumofus", "statedeptspox",
                    "intypython", "xiabamboohermit", "jardemalie", "abimalaysia", "babussokutan", "bencosmef", "mhzaman",
                    "aslima03846189", "donaldcclarke", "mpwangtingyu", "baldingsworld", "molos123", "robindbrant", "gulchehrahoja",
                    "cuyghurs", "ilhan", "aasasianstudies", "hoghuzkhan", "yanghin_uyghur", "uygurtimes", "atajurt_kazak",
                    "mustafacan_aksu", "uyghuryadvashem", "yananw", "trailbl16797622", "arslanotkur", "sophia_yan", "atajurt",
                    "uygurhaber", "muhammadrabiye", "turkistantmes", "uighurturk", "gulnazuyghur", "turkicuyghur", "abdulahgulja",
                    "australianuygh1", "tarimuyghur", "sabriuyghur", "bsintash", "maryamhanim", "mamatatawulla", "uyghurn",
                    "drzuhdijasser", "louisacgreve", "mjt1214", "gheribjan", "magnus_fiskesjo", "sophiehrw", "salih_hudayar",
                    "beijingpalmer", "nijatturkistan", "bitterwintermag", "weiwuernews", "saveuighurusa", "amnestyusa", "uyghursvic",
                    "nuryturkel", "uyghurcongress", "grosetimothy", "scmpnews", "ajplus", "dgtam86", "williamyang120", "vanessafrangvi1",
                    "wang_maya", "j_smithfinley", "gerryshih", "joshchin", "meghara", "badiucao", "hrw", "uyghurproject", "nurahmet9",
                    "sigalsamuel", "norightsnogames", "arslan_hidayat", "uighurt", "alfred_uyghur", "0715rita", "asiyeuyghur", "erkin_azat",
                    "hkokbore", "aydinanwar_", "tombschrader", "weneedtoknownow", "adrianzenz", "meclarke114", "jimmillward", "andersonelisem",
                    "jnbpage", "junmai1103", "chrisrickleton", "mervesebnem", "sophiemcneill", "jenn_chowdhury", "ak_mack", "robbiegramer",
                    "abcchinese", "4corners", "epochtimes", "faridadeif", "tingdc", "tumaris_almas", "observatoryihr", "cairnational",
                    "bequelin", "jewherilham", "turkistan_tv", "abdughenisabit", "ssbilir_", "sedat_peker", "patrickpoon", "sarkikebir",
                    "turkistaner", "aiduyghur", "kuzzataltay", "millioyghunush", "eastturkistann", "nedemocracy", "ihhinsaniyardim",
                    "memettohti", "gemkifujii", "uyghur28933032", "naokomizutani", "nukinfo", "saveeastturk", "isobelyeung", "petecirwin",
                    "mattjtucker55", "hamish", "asiamattersewc", "abduwela", "yy56936953", "ssboland", "rudder", "hamish", "rianthum",
                    "uyghurspeaker", "shahitbiz", "fergusshiel", "amytheblue", "sashachavkin", "shirafu", "dtbyler", "halmuratu",
                    "hamuttahir", "uighurian", "marinawalkerg", "icijorg", "he_shumei", "antoniocuga", "chubailiang", "dakekang",
                    "bethanyallenebr");
            String dateStr = "2019-01-01";
            Date date = DateUtil.parse(dateStr);
            long beginTime = date.getTime();

            long endTime = System.currentTimeMillis();
            BoolQueryBuilder should = QueryBuilders.boolQuery().should(QueryBuilders.termsQuery("username__STRING", stringList))
                    .should(QueryBuilders.termsQuery("retweetScreenName__STRING", stringList));
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("timeStampStr__STRING").from(beginTime/1000).to(endTime/1000);

            QueryBuilder queryBuilder = QueryBuilders
                    .boolQuery()
                    .must(should)
                    .must(rangeQueryBuilder)
                    .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"))
                    .must(QueryBuilders.termQuery("datasetId__STRING", "9999"))
                    .mustNot(QueryBuilders.termQuery("tweetsContent__STRING", ""));
//        List<String> list = scrollDelete("janusgraph_janusgraph_allv_twitter_text", "janusgraph_allv_twitter_text");

            scrollSearchRequestVO.setKeepAlive(60);
            scrollSearchRequestVO.setQueryBuilder(queryBuilder);
            String scrollId = "";
            ArrayList<Object> objects = CollUtil.newArrayList();
//        List<String> fields = new ArrayList<>();

            while (true) {
                try {
                    scrollSearchRequestVO.setScrollId(scrollId);
                    ScrollSearchResponseVO scrollSearchResponseVO = searchUtil.scrollSearch(scrollSearchRequestVO);
                    log.info("=游标==" + scrollSearchResponseVO.getScrollId());
                    List<Map<String, Object>> data = scrollSearchResponseVO.getData();

                    if (!CollectionUtils.isEmpty(data)) {

    //                    if (CollectionUtils.isEmpty(fields)) {
    //                        Set<String> strings = data.get(0).keySet();
    //                        for (String string : strings) {
    //                            if (!string.contains("__")) {
    //                                fields.add(string);
    //                            }
    //                        }
    //                    }
                        for (Map<String, Object> datum : data) {
                            Map<String, Object> jsonObject = new JSONObject();
                            Set<String> strings = datum.keySet();
                            for (String string : strings) {
                                if (!string.contains("__")) {
                                    jsonObject.put(string, datum.get(string));
                                }
                            }
                            objects.add(jsonObject);
                        }
                        scrollId = scrollSearchResponseVO.getScrollId();
                    } else {
                        break;
                    }
                } catch (IOException e) {
                    log.info("======查询异常" + e.getMessage());
                    e.printStackTrace();
                    continue;
                }
            }

            log.info("======查询重复的id完成===="+objects.size());
            if (CollectionUtil.isNotEmpty(objects)){
                excelE(objects);
            }
        } catch (Exception e) {
            log.info("=====+++"+e.getMessage());
            e.printStackTrace();
        }

    }

    public static final String EXCEL_FILE_PATH = "/Users/qibin/weibo/writeBeanTest.xlsx";

    //保存到excel
    public static void excelE(ArrayList<Object> rows) {
        log.info("=======保存excel");
        BigExcelWriter writer = ExcelUtil.getBigWriter(EXCEL_FILE_PATH);
// 一次性写出内容，使用默认样式
        writer.write(rows);
// 关闭writer，释放内存
        writer.close();
    }

    public static void main(String[] args) {
//        userTopics("secpompeo");
//        gettableusers();

//        List<JSONObject> newPostTime = getNewPostTime();
//        System.out.println(newPostTime);

//        tolow();

//        cacheLanguage();

        excelSearchEs();
    }


}
