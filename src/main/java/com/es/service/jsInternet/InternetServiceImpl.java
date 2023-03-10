package com.es.service.jsInternet;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.es.service.TwoAggreationUtil;
import com.es.service.jsInternet.louvain.Louvain;
import com.es.service.jsInternet.pagerank.PageRank;
import com.es.service.ScrollSearchRequestVO;
import com.es.service.ScrollSearchResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;


/**
 * <p>【描述】：主面板</p>
 * <p>【作者】: lizheng</p>
 **/
@Slf4j
public class InternetServiceImpl  {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    //    @Autowired
//    GraphSourceConfig config;




//    static {
//         graphTraversalSource = JanusGraphUtil.graphTraversalSource;
//    }



    Logger logger = LoggerFactory.getLogger(InternetServiceImpl.class);



    @Autowired
    TwoAggreationUtil searchUtil;



    public static JSONObject getEsInternetType(String type, String datasetId) {
        EsInternetServiceImpl esInternetService =  new EsInternetServiceImpl();
        List<Map<String, Object>> maps2 = getEsInternet(type, datasetId);
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> mapList = new ArrayList<>();
        Set<String> startNames = new HashSet<>();
        Set<String> endNames = new HashSet<>();
        for (Map<String, Object> stringObjectMap : maps2) {
            String startV = (String) stringObjectMap.get("startV");
            String endV = (String) stringObjectMap.get("endV");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(startV))
                startNames.add(startV);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(endV))
                endNames.add(endV);
        }
        List<String> values = new ArrayList<>();

        List<JSONObject> topicList = new ArrayList<>();
        List<JSONObject> mentionList = new ArrayList<>();
        if (type.equals("topic")) {
            if (CollectionUtil.isNotEmpty(endNames)) {
                if (endNames.contains(null)){
                    endNames.remove(null);
                }
                List<Map<String, Object>> topic = getEsNode(endNames, "topic");
                if (CollectionUtil.isNotEmpty(topic))
                    mapList.addAll(topic);

                //话题前30
//                top30Topics = esInternetService.getEsTop30(endNames, "topic");

            }
            if (CollectionUtil.isNotEmpty(startNames)) {
                if (startNames.contains(null)){
                    startNames.remove(null);
                }
                topicList = esInternetService.getEsTop30(startNames, "topic");
                mentionList = esInternetService.getEsTop30(startNames, "mention");
                List<Map<String, Object>> user = getEsNode(startNames, "user");
                if (CollectionUtil.isNotEmpty(user))
                    mapList.addAll(user);
            }
        } else {
            if (CollectionUtil.isNotEmpty(endNames)) {
                startNames.addAll(endNames);
                //
                if (startNames.contains(null)){
                    startNames.remove(null);
                }
                topicList = esInternetService.getEsTop30(startNames, "topic");
                mentionList = esInternetService.getEsTop30(startNames, "mention");
                List<Map<String, Object>> user = getEsNode(startNames, "user");
                if (CollectionUtil.isNotEmpty(user))
                    mapList.addAll(user);
            }
        }

        List<JSONObject> edges = new ArrayList<>();

        String max = "";
        String min = "";
        List<JSONObject> js = new ArrayList<>();
        Map<String, Integer> newPageRankMap = new HashMap<>();
        List<String> newLouvainList = new ArrayList<>();
        Set<String> nodeList = new HashSet<>();
        List<Object> impotList = new ArrayList();
        if (!CollectionUtils.isEmpty(maps2)) {
            for (Map<String, Object> result : maps2) {
                JSONObject nodeJson = new JSONObject();
//                Edge edge = result.getEdge();
                String from = (String) result.get("startV");
                String to = (String) result.get("endV");
                nodeJson.put("source", from);
                nodeJson.put("target", to);
                nodeJson.put("number", 1);
                edges.add(nodeJson);
                newPageRankMap.put(from + "," + to, 1);
                newLouvainList.add(from + "," + to);
                nodeList.add(from);
                nodeList.add(to);
            }

            LinkedHashMap<String, Double> stringDoubleLinkedHashMap = new PageRank().pageRankRun(newPageRankMap);
            Set<String> set = stringDoubleLinkedHashMap.keySet();
            List<String> usernames = new ArrayList<>();
            if (!CollectionUtils.isEmpty(set)) {
                if (set.size() > 30) {
                    usernames = new ArrayList<>(set).subList(0, 29);
                } else {
                    usernames = new ArrayList<>(set);
                }
            }
            if (!CollectionUtils.isEmpty(usernames)) {
                impotList.addAll(usernames);
            }

            //louvain
            Louvain louvain = new Louvain();
            List<String> edgeTwoList = louvain.pretreatmentData(newLouvainList, new ArrayList<>(nodeList));
            louvain.init(edgeTwoList, nodeList.size(), newLouvainList.size());
            try {
                louvain.louvain();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, Integer> stringIntegerMap = louvain.changeData();


            if (!CollectionUtils.isEmpty(mapList)) {
                for (Map<String, Object> vertex : mapList) {
                    Object s = vertex.get("uuid");
                    String uuid = JSON.toJSONString(s);
                    JSONObject jsonNodes = new JSONObject();
                    jsonNodes.put("id", s);

                    if (vertex.containsKey("username")) {
                        jsonNodes.put("pageRankScore", stringDoubleLinkedHashMap.get(vertex.get("username").toString()));
                        jsonNodes.put("louvain", stringIntegerMap.get(vertex.get("username").toString()));
                    } else if (vertex.containsKey("tagJson")) {
                        jsonNodes.put("pageRankScore", stringDoubleLinkedHashMap.get(vertex.get("tagJson").toString()));
                        jsonNodes.put("louvain", stringIntegerMap.get(vertex.get("tagJson").toString()));
                    } else {
                        jsonNodes.put("pageRankScore", "1");
                        jsonNodes.put("louvain", "0");
                    }
                    if (null != vertex.get("username")) {
                        jsonNodes.put("name", vertex.get("username"));
                    } else if (null != vertex.get("tagJson")) {
                        jsonNodes.put("name", vertex.get("tagJson"));
                    }
                    jsonNodes.put("degree", 1);

                    if (null != vertex.get("userAddr")) {
                        jsonNodes.put("userAddr", vertex.get("userAddr"));
                    }
                    if (null != vertex.get("mediatname")) {
                        jsonNodes.put("mediatname", vertex.get("mediatname"));
                    }

                    //取点赞数
                    if (vertex.containsKey("listed")) {
                        jsonNodes.put("fabulous", vertex.get("listed"));
                    } else
                        jsonNodes.put("fabulous", 0);

                    //取关注数   或者话题的时候  取度
                    if (!("topic").equals(type)) {
                        if (null != vertex.get("followers")) {
                            String followers = vertex.get("followers").toString();
                            values.add(followers);
                            jsonNodes.put("value", followers);

                        } else {
                            values.add("0");
                            jsonNodes.put("value", 0);
                        }
                    } else {
                        if (null != vertex.get("followers")) {
                            String followers = vertex.get("followers").toString();
                            values.add(followers);
                            jsonNodes.put("value", followers);
                        } else {
                            jsonNodes.put("value", 0);
                            values.add("0");
                        }
                        Object mediatnameObj = vertex.get("mediatname");
                        String mediatname = mediatnameObj.toString();

                        if (("TwitterTopic").equals(mediatname)) {
                            String tagJson = vertex.get("tagJson").toString();
                            //查 数量
//                            int next = traversalSource.V(vertex.get("uuid")).outE("topic").valueMap("uuid").toList().size();
//                            jsonNodes.put("value", next);
                            Long esTopicOutCount = esInternetService.getEsTopicOutCount(tagJson);
                            jsonNodes.put("value", esTopicOutCount);
                        } else {
                            //查 数量
                            String username = vertex.get("username").toString();
//                            int next = traversalSource.V(vertex.get("uuid")).inE("topic").valueMap("uuid").toList().size();
//                            jsonNodes.put("value", next);
                            Long esTopicInCount = esInternetService.getEsTopicInCount(username);
                            jsonNodes.put("value", esTopicInCount);
                        }

                    }
                    js.add(jsonNodes);
                }
            }
            if (!CollectionUtils.isEmpty(values)) {
                max = Collections.max(values);

                min = Collections.min(values);

            }
        }


        JSONObject boardInfo = new JSONObject();

//        boardInfo.put("accountArea", addrSet);
        boardInfo.put("allNodeNum", nodeList.size());
        boardInfo.put("edgeNum", edges.size());
        boardInfo.put("topicTop30", topicList);
        boardInfo.put("mentionTop30", mentionList);
        boardInfo.put("sensitiveNodeNum", 0);
        boardInfo.put("importantNode", impotList);
        boardInfo.put("accountArea", new ArrayList<>());

        jsonObject.put("nodes", js);
        jsonObject.put("maxNum", max);
        jsonObject.put("minNum", min);
        jsonObject.put("edges", edges);
        jsonObject.put("boardInfo", boardInfo);
        return jsonObject;
    }


    //根据  边的 name 获取所有顶点
    public static List<Map<String, Object>> getEsNode(Set<String> usernames, String type) {


        //查询 es中的顶点
        ArrayList<String> names = new ArrayList<>(usernames);

        ScrollSearchRequestVO scrollSearchRequestVO = new ScrollSearchRequestVO();

        scrollSearchRequestVO.setLimit(1000);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (type.equals("topic")) {
            scrollSearchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_topic"});
            scrollSearchRequestVO.setType("janusgraph_allv_twitter_topic");
            queryBuilder.must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTopic"))
                    .must(QueryBuilders.termsQuery("tagJson__STRING", names))
                    .mustNot(QueryBuilders.termQuery("tagJson__STRING", ""));
        } else if (type.equals("user")) {
            scrollSearchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_pesonal"});
            scrollSearchRequestVO.setType("janusgraph_allv_twitter_pesonal");
            queryBuilder.must(QueryBuilders.termQuery("mediatname__STRING", "TwitterUser"))
                    .must(QueryBuilders.termsQuery("username__STRING", names));
        }
        queryBuilder.must(QueryBuilders.termQuery("datasetId__STRING", "9999"));
        scrollSearchRequestVO.setQueryBuilder(queryBuilder);
        scrollSearchRequestVO.setKeepAlive(60);
        String scrollId = "";
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        List<Map<String, Object>> results = new ArrayList<>();
        while (true) {
            try {
                scrollSearchRequestVO.setScrollId(scrollId);
                ScrollSearchResponseVO scrollSearchResponseVO = searchUtil.scrollSearch(scrollSearchRequestVO);
                log.info("=游标==" + scrollSearchResponseVO.getScrollId());
                List<Map<String, Object>> data = scrollSearchResponseVO.getData();

                if (!CollectionUtils.isEmpty(data)) {
//                        for (Map<String, Object> datum : data) {
//                            BeanUtil.mapToBean(datum, LinkedinEdge.class, true);
//                        }

                    results.addAll(data);
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


        return results;

    }


    public static List<Map<String, Object>> getEsInternet(String type, String datasetId) {
        ScrollSearchRequestVO scrollSearchRequestVO = new ScrollSearchRequestVO();
        scrollSearchRequestVO.setIndexName(new String[]{"js_twitter_e"});
        scrollSearchRequestVO.setType("edge");
        scrollSearchRequestVO.setLimit(1000);


        List<String> nameList = Arrays.asList("tomstites","rhpsia","deanstarkman",
                "nytimes","austinramzy","ssboland","rudder","mbachelet","kiyyabaloch",
                "shireenmazari1","begbol","radio_azattyk","aygeryma","l10nlab","belgiumuyghur",
                "weiboscope","sumofus","statedeptspox","intypython","xiabamboohermit","jardemalie",
                "abimalaysia","babussokutan","bencosmef","mhzaman","aslima03846189","donaldcclarke",
                "mpwangtingyu","baldingsworld","molos123","robindbrant","gulchehrahoja","cuyghurs","ilhan","aasasianstudies","hoghuzkhan","yanghin_uyghur","uygurtimes","atajurt_kazak","mustafacan_aksu","uyghuryadvashem","yananw","trailbl16797622","arslanotkur","sophia_yan","atajurt","uygurhaber","muhammadrabiye","turkistantmes","uighurturk","gulnazuyghur","turkicuyghur","abdulahgulja","australianuygh1","tarimuyghur","sabriuyghur","bsintash","maryamhanim","mamatatawulla","uyghurn","drzuhdijasser","louisacgreve","mjt1214","gheribjan","magnus_fiskesjo","sophiehrw","salih_hudayar","beijingpalmer","nijatturkistan","bitterwintermag","weiwuernews","saveuighurusa","amnestyusa","uyghursvic","nuryturkel","uyghurcongress","grosetimothy","scmpnews","ajplus","dgtam86","williamyang120","vanessafrangvi1","wang_maya","j_smithfinley","gerryshih","joshchin","meghara","badiucao","hrw","uyghurproject","nurahmet9","sigalsamuel","norightsnogames","arslan_hidayat","uighurt","alfred_uyghur","0715rita","asiyeuyghur","erkin_azat","hkokbore","aydinanwar_","tombschrader","weneedtoknownow","adrianzenz","meclarke114","jimmillward","andersonelisem","jnbpage","junmai1103","chrisrickleton","mervesebnem","sophiemcneill","jenn_chowdhury","ak_mack","robbiegramer","abcchinese","4corners","epochtimes","faridadeif","tingdc","tumaris_almas","observatoryihr","cairnational","bequelin","jewherilham","turkistan_tv","abdughenisabit","ssbilir_","sedat_peker","patrickpoon","sarkikebir","turkistaner","aiduyghur","kuzzataltay","millioyghunush","eastturkistann","nedemocracy","ihhinsaniyardim","memettohti","gemkifujii","uyghur28933032","naokomizutani","nukinfo","saveeastturk","isobelyeung","petecirwin","mattjtucker55","hamish","asiamattersewc","abduwela","yy56936953","ssboland","rudder","hamish","rianthum","uyghurspeaker","shahitbiz","fergusshiel","amytheblue","sashachavkin","shirafu","dtbyler","halmuratu","hamuttahir","uighurian","marinawalkerg","icijorg","he_shumei",
                "antoniocuga","chubailiang","dakekang","bethanyallenebr");
        List<Map<String, Object>> results = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(nameList)) {

            BoolQueryBuilder should = QueryBuilders.boolQuery().should(QueryBuilders.termsQuery("startV", nameList))
                    .should(QueryBuilders.termsQuery("endV", nameList));


            scrollSearchRequestVO.setKeepAlive(60);

            String dateStr = "2019-01-01";
            Date date = DateUtil.parse(dateStr);
            long beginTime = date.getTime();

            long endTime = System.currentTimeMillis();
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("updateTimes").from(beginTime/1000).to(endTime/1000);
            QueryBuilder queryBuilder = QueryBuilders
                    .boolQuery()
                    .must(should)
                    .must(rangeQueryBuilder)
                    .must(QueryBuilders.termQuery("mediatname", type))
                    .must(QueryBuilders.termQuery("datasetId", "9999"))
                    .mustNot(QueryBuilders.termQuery("startV", ""))
                    .mustNot(QueryBuilders.termQuery("endV", ""));
            scrollSearchRequestVO.setQueryBuilder(queryBuilder);
            String scrollId = "";

            TwoAggreationUtil searchUtil = new TwoAggreationUtil();
            while (true) {
                try {
                    scrollSearchRequestVO.setScrollId(scrollId);
                    ScrollSearchResponseVO scrollSearchResponseVO = searchUtil.scrollSearch(scrollSearchRequestVO);
                    log.info("=游标==" + scrollSearchResponseVO.getScrollId());
                    List<Map<String, Object>> data = scrollSearchResponseVO.getData();

                    if (!CollectionUtils.isEmpty(data)) {
//                        for (Map<String, Object> datum : data) {
//                            BeanUtil.mapToBean(datum, LinkedinEdge.class, true);
//                        }

                        results.addAll(data);
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
        }

        return results;
    }

    public static void main(String[] args) {
        JSONObject topic = getEsInternetType("topic", "1123");
        System.out.println(topic);
    }

}
