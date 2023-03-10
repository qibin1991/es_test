package com.es.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.es.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName DeleteTwService
 * @Description TODO
 * @Author QiBin
 * @Date 2021/1/21下午12:15
 * @Version 1.0
 **/
@Component
@Slf4j
public class DeleteTwService {


//    public static void main(String[] args) {
//        Set<String> strings = deleteTweetsInEs();
//        for (String string : strings) {
////            delete("janusgraph_janusgraph_allv_twitter_pesonal", string, "janusgraph_allv_twitter_pesonal");
//        }
//    }

    /**
     * 物理删除ES数据
     */
    public static void delete(String indexName, String _id, String type) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.id(_id);
        deleteRequest.index(indexName);
        deleteRequest.type(type);
        try {
            ESConnection.getClient().delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //    @Scheduled(initialDelay = 100,fixedDelay = 1000000)
    public static Set<String> deleteTweetsInEs() {

        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        log.info("=======es中主贴去重查询====");
        searchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_pesonal"});
        searchRequestVO.setType("janusgraph_allv_twitter_pesonal");

        ScrollSearchRequestVO scrollSearchRequestVO = new ScrollSearchRequestVO();
        scrollSearchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_pesonal"});
        scrollSearchRequestVO.setType("janusgraph_allv_twitter_pesonal");
        String s = "tomstites,rhpsia,deanstarkman,nytimes,austinramzy,ssboland,rudder,mbachelet,kiyyabaloch,shireenmazari1,begbol,radio_azattyk,aygeryma,l10nlab,belgiumuyghur,weiboscope,sumofus,statedeptspox,intypython,xiabamboohermit,jardemalie,abimalaysia,babussokutan,bencosmef,mhzaman,aslima03846189,donaldcclarke,mpwangtingyu,baldingsworld,molos123,robindbrant,gulchehrahoja,cuyghurs,ilhan,aasasianstudies,hoghuzkhan,yanghin_uyghur,uygurtimes,atajurt_kazak,mustafacan_aksu,uyghuryadvashem,yananw,trailbl16797622,arslanotkur,sophia_yan,atajurt,uygurhaber,muhammadrabiye,turkistantmes,uighurturk,gulnazuyghur,turkicuyghur,abdulahgulja,australianuygh1,tarimuyghur,sabriuyghur,bsintash,maryamhanim,mamatatawulla,uyghurn,drzuhdijasser,louisacgreve,mjt1214,gheribjan,magnus_fiskesjo,sophiehrw,salih_hudayar,beijingpalmer,nijatturkistan,bitterwintermag,weiwuernews,saveuighurusa,amnestyusa,uyghursvic,nuryturkel,uyghurcongress,grosetimothy,scmpnews,ajplus,dgtam86,williamyang120,vanessafrangvi1,wang_maya,j_smithfinley,gerryshih,joshchin,meghara,badiucao,hrw,uyghurproject,nurahmet9,sigalsamuel,norightsnogames,arslan_hidayat,uighurt,alfred_uyghur,0715rita,asiyeuyghur,erkin_azat,hkokbore,aydinanwar_,tombschrader,weneedtoknownow,adrianzenz,meclarke114,jimmillward,andersonelisem,jnbpage,junmai1103,chrisrickleton,mervesebnem,sophiemcneill,jenn_chowdhury,ak_mack,robbiegramer,abcchinese,4corners,epochtimes,faridadeif,tingdc,tumaris_almas,observatoryihr,cairnational,bequelin,jewherilham,turkistan_tv,abdughenisabit,ssbilir_,sedat_peker,patrickpoon,sarkikebir,turkistaner,aiduyghur,kuzzataltay,millioyghunush,eastturkistann,nedemocracy,ihhinsaniyardim,memettohti,gemkifujii,uyghur28933032,naokomizutani,nukinfo,saveeastturk,isobelyeung,petecirwin,mattjtucker55,hamish,asiamattersewc,abduwela,yy56936953,ssboland,rudder,hamish,rianthum,uyghurspeaker,,shahitbiz,fergusshiel,amytheblue,sashachavkin,shirafu,dtbyler,halmuratu,hamuttahir,uighurian,marinawalkerg,icijorg,he_shumei,antoniocuga,chubailiang,dakekang,bethanyallenebr";
        List<String> list1 = Arrays.asList(s.split(","));
        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termsQuery("username__STRING", list1))
                .must(QueryBuilders.termQuery("mediatname__STRING.keyword", "TwitterUser"))
                .must(QueryBuilders.termQuery("datasetId", "9999"))
                .must(QueryBuilders.termQuery("delFlag", 0));
//        List<String> list = scrollDelete("janusgraph_janusgraph_allv_twitter_text", "janusgraph_allv_twitter_text");

        scrollSearchRequestVO.setKeepAlive(60);
        scrollSearchRequestVO.setQueryBuilder(queryBuilder);
        scrollSearchRequestVO.setLimit(10000);
        String scrollId = "";
        Set<String> list = new HashSet<>();
        Set<String> strings = new HashSet<>();
        Set<String> usernames = new HashSet<>();
        Set<String> nourlusernames = new HashSet<>();
        while (true) {
            try {
                scrollSearchRequestVO.setScrollId(scrollId);
                ScrollSearchResponseVO scrollSearchResponseVO = searchUtil.scrollSearch(scrollSearchRequestVO);
                List<Map<String, Object>> data = scrollSearchResponseVO.getData();
                if (CollectionUtils.isNotEmpty(data)) {
                    for (Map<String, Object> datum : data) {
                        Object id = datum.get("_id");
                        Object userUrl = datum.get("userUrl");
                        Object userFlag = datum.get("userflag");
                        Object userWebUrl = datum.get("userWebUrl");
                        Object fullname = datum.get("fullname");
                        Object username1 = datum.get("username");

                        if (id != null && StringUtils.isNotBlank(id.toString())) {
                            if (userUrl == null || StringUtils.isBlank(userUrl.toString())){
                                boolean username = nourlusernames.add(datum.get("username").toString());
                                if (!username) {
                                    list.add(id.toString());
                                    usernames.add(username1.toString());
                                }
                            }
                            if (userUrl != null && userFlag != null && userWebUrl != null && fullname != null) {
                                boolean username = strings.add(datum.get("username").toString());
                                if (!username) {
                                    list.add(id.toString());
                                    usernames.add(username1.toString());
                                }
                            }

//                            if (datum.containsKey("userUrl")) {
//                                Object userUrl = datum.get("userUrl");
//                                if (userUrl != null && StringUtils.isNotBlank(userUrl.toString())) {
//                                    continue;
//                                }
//                                list.add(id.toString());
//                            } else {
//                                log.info("=========" + id);
//                                list.add(id.toString());
//                            }
                        }

                    }
                    scrollId = scrollSearchResponseVO.getScrollId();
                } else {
                    break;
                }


            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
        log.info("++username====" + usernames.size());
        System.out.println(usernames);
        System.out.println(strings);
        log.info("+" + list.size());
        return list;
    }


//    public static void main(String[] args) {
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("pageNo", "1");
////        jsonObject.put("pageSize", "10");
////        jsonObject.put("username", "nor");
////        jsonObject.put("scType", "desc");
////        jsonObject.put("datasetType", "0");
////        PageResult pageResult = showFaceBook(jsonObject);
////        System.out.println(pageResult.getRows());
//
//        List list = selectEs(2, "4corners");
//        System.out.println(list);
//    }


    public static PageResult showFaceBook(JSONObject request) {

        PageResult pageResult = new PageResult<>();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();

        int datasetId = request.getIntValue("datasetId");
        AggRequestVO aggRequestVO = new AggRequestVO();
//        String s = spiderDatasetMapper.queryFbeetSpiderTypeByDatasetId(datasetId + "");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("delFlag", 0));
        // 0：人物 1:帖子
        int datasetType = request.getIntValue("datasetType");
        // 0：人物 1:帖子
        if (1 == datasetType) {
            searchRequestVO.setIndexName(new String[]{"js_facebook_post"});
            searchRequestVO.setType("post");

        } else if (0 == datasetType) {
            searchRequestVO.setIndexName(new String[]{"js_facebook_user"});
            searchRequestVO.setType("user");
            aggRequestVO.setIndexName(new String[]{"js_facebook_user"});
            aggRequestVO.setType("user");

        }
        List<String> userNames = new ArrayList<>();
        String username = "";

        if (request.containsKey("username")) {
            username = request.getString("username");
        }
        if (StringUtils.isNotBlank(username)) {
            if (1 == datasetType) {
                BoolQueryBuilder should = QueryBuilders.boolQuery().should(QueryBuilders.matchPhrasePrefixQuery("userName", username))
                        .should(QueryBuilders.matchPhrasePrefixQuery("quoteScreenName", username));
                boolQueryBuilder.must(should)
                        .must(QueryBuilders.termQuery("mediatname.keyword", "FbBook"))
                        .mustNot(QueryBuilders.termQuery("shareContent", ""));
//                        .must(QueryBuilders.termQuery("dataSetId", "99999"));
            } else if (0 == datasetType) {
                boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("userName", username))
                        .mustNot(QueryBuilders.termQuery("userName", ""));
            }
        }
        int pageNo = request.getIntValue("pageNo");
        int pageSize = request.getIntValue("pageSize");

        // 暂时为0

        // 排序字段
        String orderField = "";
        if (request.containsKey("orderField")) {
            if (StringUtils.isNotBlank(request.getString("orderField"))) {
                orderField = request.getString("orderField");
            } else {
                orderField = "";
            }
        }

        if (StringUtils.isBlank(orderField)) {
            if (0 == datasetType)
                orderField = "userName";
            else if (1 == datasetType)
                orderField = "publishTime";
        }
        // 默认降序排列
        String scType = "desc";
        if (request.containsKey("scType")) {
            if (StringUtils.isNotBlank(request.getString("scType"))) {
                scType = request.getString("scType");
            }
        }
        AggregationBuilder aggregation = null;
        // 关键词
        searchRequestVO.setPageSize(pageSize);
        searchRequestVO.setPageNo(pageNo);
        // 排序
        if (StringUtils.isNotBlank(orderField)) {
            if ("desc".equals(scType)) {
                if (0 == datasetType) {
                    //人物
                    aggregation = AggregationBuilders
                            .terms("agg").field("userName").size(1000)
                            .subAggregation(
                                    AggregationBuilders.topHits("top").sort(orderField, SortOrder.DESC).size(1)
                            );
                } else if (1 == datasetType) {
                    searchRequestVO.setSortOrder(SortOrder.DESC);
                }


            } else if ("asc".equals(scType)) {
                if (0 == datasetType) {
                    aggregation = AggregationBuilders
                            .terms("agg").field("userName").size(1000)
                            .subAggregation(AggregationBuilders.topHits("top").sort(orderField, SortOrder.ASC).size(1));
                } else {
                    searchRequestVO.setSortOrder(SortOrder.ASC);
                }

            }
        }





        // todo 按照数据集id进行查询


        if (0 == datasetType && StringUtils.isNotBlank(username)) {
//            aggRequestVO.setSize(userNames.size());

            aggRequestVO.setSize(userNames.size());
            aggRequestVO.setAggregationBuilder(aggregation);
            aggRequestVO.setQueryBuilder(boolQueryBuilder);


            try {
                List<cn.hutool.json.JSONObject> aggs = aggs(aggRequestVO);
                if (CollectionUtils.isNotEmpty(aggs)) {

                    int size = aggs.size();
                    int i = size / pageSize;
                    if (size % pageSize == 0) {

                        if (i >= pageNo) {
                            pageResult.setRows(aggs.subList((pageNo - 1) * pageSize, pageNo * pageSize));
                        } else {
                            pageResult.setRows(new ArrayList());
                        }
                    } else {
                        if (i >= pageNo) {
                            pageResult.setRows(aggs.subList((pageNo - 1) * pageSize, pageNo * pageSize));
                        } else if (i + 1 == pageNo) {
                            pageResult.setRows(aggs.subList((pageNo - 1) * pageSize, aggs.size()));
                        }
                    }
                }

                pageResult.setTotal(aggs.size());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pageResult;
    }

    public static List<cn.hutool.json.JSONObject> aggs(AggRequestVO aggRequestVO) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(aggRequestVO.getIndexName());
        searchRequest.types(aggRequestVO.getType());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(aggRequestVO.getQueryBuilder());
        searchSourceBuilder.aggregation(aggRequestVO.getAggregationBuilder());
        searchSourceBuilder.size(aggRequestVO.getSize());
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
                TopHits buckets = entry.getAggregations().get("buckets");
                if (topHits != null && topHits.getHits() != null) {
                    for (SearchHit hit : topHits.getHits()) {
                        String id = hit.getId();
                        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                        sourceAsMap.put("_id", id);
                        list.add(JSONUtil.parseFromMap(sourceAsMap));
                    }
                } else if (buckets != null && buckets.getHits() != null) {
                    for (SearchHit hit : buckets.getHits()) {
                        list.add(JSONUtil.parseFromMap(hit.getSourceAsMap()));
                    }

                } else {
                    String keyAsString = entry.getKeyAsString();
                    long docCount = entry.getDocCount();

                    cn.hutool.json.JSONObject js = new cn.hutool.json.JSONObject();
                    js.put(keyAsString, docCount);
                    list.add(js);
                }
            }
        });
    }


    public static List selectEs(int type, String username) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();

        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setSize(10000);
        AggregationBuilder aggregationBuilder = null;

        aggRequestVO.setIndexName(new String[]{"js_twitter_e"});
        aggRequestVO.setType("edge");

        switch (type) {
            case 1:
                //原创
                searchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
                searchRequestVO.setType("janusgraph_allv_twitter_text");
                boolQueryBuilder.must(QueryBuilders.termQuery("username__STRING", username.toLowerCase()))
                        .must(QueryBuilders.termQuery("delFlag", "0"))
                        .must(QueryBuilders.termQuery("datasetId__STRING", "9999"))
                        .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"));

                break;
            case 2:
                //评论
                searchRequestVO.setIndexName(new String[]{"js_twitter_e"});
                searchRequestVO.setType("edge");
                boolQueryBuilder.must(QueryBuilders.termQuery("startV", username.toLowerCase()))
                        .mustNot(QueryBuilders.termQuery("endV", ""))
                        .must(QueryBuilders.termQuery("datasetId", "9999"))
                        .must(QueryBuilders.termQuery("mediatname", "comment"))
                        .must(QueryBuilders.termQuery("delFlag", "0"));
                aggregationBuilder = AggregationBuilders.terms("agg").field("endV").size(10000);

                break;

            case 3:
                //转发
                searchRequestVO.setIndexName(new String[]{"js_twitter_e"});
                searchRequestVO.setType("edge");
                boolQueryBuilder.must(QueryBuilders.termQuery("startV", username.toLowerCase()))
                        .must(QueryBuilders.termQuery("delFlag", "0"))
                        .mustNot(QueryBuilders.termQuery("endV", ""))
                        .must(QueryBuilders.termQuery("datasetId", "9999"))
                        .must(QueryBuilders.termQuery("mediatname", "forward"));
                aggregationBuilder = AggregationBuilders.terms("agg").field("endV").size(10000);
                break;
            case 4:
                searchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
                searchRequestVO.setType("janusgraph_allv_twitter_text");
                boolQueryBuilder.must(QueryBuilders.termQuery("username__STRING", username.toLowerCase()))
                        .mustNot(QueryBuilders.termQuery("retweetScreenName__STRING", ""))
                        .must(QueryBuilders.termQuery("datasetId__STRING", "9999"))
                        .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"));
                break;
            default:
                break;
        }


        if (2 == type || 3 == type) {
            aggRequestVO.setAggregationBuilder(aggregationBuilder);
            aggRequestVO.setSize(10000);
            aggRequestVO.setQueryBuilder(boolQueryBuilder);

            try {
                List<cn.hutool.json.JSONObject> aggs = aggs(aggRequestVO);
                if (CollectionUtil.isNotEmpty(aggs)) {
                    System.out.println(aggs);
                    return aggs;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                searchRequestVO.setPageSize(100000);
                searchRequestVO.setQueryBuilder(boolQueryBuilder);
                SearchResponseVO search = searchUtil.search(searchRequestVO);
                List data = search.getData();
                if (CollectionUtils.isNotEmpty(data)) {

                    return data;
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.info("=====查询es数据异常====");
            }
        }


        return new ArrayList();
    }


    //解析excel
    public static void getExcel(){
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("/Users/qibin/zyyt相关/青岛详细/fbtw数据/facebook个人主页账号/fb个人主页用户信息_2_210121.xlsx"));
        List<List<Object>> read = reader.read();
        System.out.println(read.get(1));
    }



    public static void main(String[] args) {
        getExcel();
    }
}
