package com.es.service.js;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.es.service.ESConnection;
import com.es.service.SearchRequestVO;
import com.es.service.SearchResponseVO;
import com.es.service.TwoAggreationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName delEsFb
 * @Description TODO
 * @Author QiBin
 * @Date 2021/1/24下午4:36
 * @Version 1.0
 **/
@Component
public class delEsFb {

    static String s = "gene.bunin,FIPAlliance,Japan.Uyghur.Union,中央アジア研究所,turmuhammet.hashim,iuhrdf.jp,profile.php,e-EpSkWStg,uyghur.yaponiye,uyghurcongress,omer.kanat.39,gene.bunin,kuzzat.a,kuzzat.turk,bsintash,sawut.mamat,ErkinAsiyaRadiosi,uighurwhiteblue19,mattjtucker55,maya.mitalipova,台灣東突厥斯坦友好協會-621968524919502,mattjtucker55,urhun.uyghur,frankvanderlinde,sadullah.cetinkaya,rushan.abbas,turdi.ghoja,zulfiye.uyghur.31,turnisa.matsedikqira.5,isii93?hc_location=ufi,Turkanhotun,shengxue.ca,eldana.abbas.10,Ouighour,abdulaziz.belattar,UyghurSolidarity,NUYashliri,zehra.firdaus.9,abdaltwab.mohammed,martyna.kokotkiewicz,StateDRL,atajurtmed.atajurtmed,peteirwin,urhun.uyghur,uyghur.rohi,Uyghur.TJD,uighurkomiteen,abduwali.ayup.3";

    public static   void main(String[] args) {
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchRequestVO searchRequestVO = new SearchRequestVO();

        searchRequestVO.setIndexName(new String[]{"js_qd_fb_post"});
        searchRequestVO.setType("post");


        boolQueryBuilder.must(QueryBuilders.termQuery("mediatname.keyword", "FbBook"))
                .must(QueryBuilders.termQuery("dataSetId", "99999"));
        searchRequestVO.setQueryBuilder(boolQueryBuilder);

        searchRequestVO.setPageNo(1);
        searchRequestVO.setPageSize(3000);
        List<String> list = Arrays.asList(s.split(","));
        boolQueryBuilder.must(QueryBuilders.termsQuery("userName", list));
        searchRequestVO.setQueryBuilder(boolQueryBuilder);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            int i = 1;
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            List<JSONObject> list1 = new ArrayList<>();
            for (Map<String, Object> datum : data) {
                Object time = datum.get("time");
                Long publishTimeLong = (Long) datum.get("publishTimeLong");
//                if (time != null && StringUtils.isNotBlank(time.toString())) {
//                    datum.put("timeLong", Long.valueOf(time.toString()) * 1000);
//                    datum.put("publishTimeLong", Long.valueOf(time.toString()) * 1000);
//                    datum.put("publishTime", simpleDateFormat.format(new Date(Long.valueOf(time.toString()) * 1000)));
//                }


                datum.put("delFlag", 0);
//                Object languageCode1 = datum.get("languageCode");
//                if (languageCode1 != null && StringUtils.isNotBlank(languageCode1.toString())){
//                    continue;
//                }
//                Object shareContent = datum.get("shareContent");
//                if (shareContent != null && StringUtils.isNotBlank(shareContent.toString())) {
//                    ILangIdClassifier langIdClassifier = new LangIdV3();
//                    DetectedLanguage classify = langIdClassifier.classify(shareContent.toString(), false);
//                    String languageCode = classify.getLangCode();
//                    datum.put("languageCode", languageCode);
//
//                }

                Object id = datum.get("_id");
                datum.remove("_id");
                UpdateRequest updateRequest = new UpdateRequest("js_qd_fb_post"
                        , "post", id.toString())
                        .doc(JSONUtil.toJsonStr(datum), XContentType.JSON);
                try {
                    UpdateResponse update = ESConnection.getClient().update(updateRequest, RequestOptions.DEFAULT);
                    System.out.println("====成功1======" + i);
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//          bulkUpdate("js_qd_fb_post", "post", list1);
            Long totalCount = search.getTotalCount();
            System.out.println(totalCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (String s1 : list) {
//            boolQueryBuilder.must(QueryBuilders.termQuery("shareId", s1));
//            try {
//                SearchResponseVO search = searchUtil.search(searchRequestVO);
//                List<Map<String, Object>> data = search.getData();
//                if (data.size() > 1) {
//                    Object id = data.get(0).get("_id");
//                    delete("js_qd_fb_post", id.toString(), "post");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        }
    }

    public static boolean bulkUpdate(String indexName, String type, List<cn.hutool.json.JSONObject> idsAndDocs) {
        boolean isSuccess = false;
        BulkRequest bulkRequest = new BulkRequest();
        for (JSONObject obj : idsAndDocs) {
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(indexName);
            updateRequest.type(type);
            updateRequest.id(obj.getStr("_id"));
            updateRequest.doc(obj);
            bulkRequest.add(updateRequest);
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        try {
            BulkResponse bulkResponse = ESConnection.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            if (!bulkResponse.hasFailures()) {
                isSuccess = true;
            } else {
//                log.error("批量更新有失败，具体消息{}" ,bulkResponse.buildFailureMessage());
            }
        } catch (IOException e) {
//            log.error("issues: ", e);
        }
        return isSuccess;
    }


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





    public static void updateUserNameInEs(){
        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        SearchRequestVO searchRequestVO = new SearchRequestVO();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        searchRequestVO.setPageNo(1);
        searchRequestVO.setPageSize(10000);

        searchRequestVO.setIndexName(new String[]{"js_facebook_user"});
        searchRequestVO.setType("user");
        boolQueryBuilder.must(QueryBuilders.termQuery("delFlag", 0));
        searchRequestVO.setQueryBuilder(boolQueryBuilder);
        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            if (CollectionUtils.isNotEmpty(data)){
                for (Map<String, Object> datum : data) {
                    Object userName = datum.get("userName");
                    if (userName != null && StringUtils.isNotBlank(userName.toString())){
                        if (userName.toString().contains("/")){
                            String id = datum.get("_id").toString();
                            String substring = userName.toString().substring(0, userName.toString().length() - 1);
                            updateEsUserNameInEs(id, substring);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateEsUserNameInEs(String id,String username){
        XContentBuilder source = null;
        try {
            source = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("userName", username)//browseNum：要修改的字段名，num 修改的值
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            UpdateRequest updateRequest = new UpdateRequest("js_facebook_user"
                    , "user", id).doc(source);
            UpdateResponse update = ESConnection.getClient().update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
