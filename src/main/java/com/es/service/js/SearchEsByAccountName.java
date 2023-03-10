package com.es.service.js;

import com.alibaba.fastjson.JSONObject;
import com.es.service.AggRequestVO;
import com.es.service.SearchRequestVO;
import com.es.service.TwoAggreationUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName SearchEsByAccountName
 * @Description TODO
 * @Author QiBin
 * @Date 2021/4/2714:35
 * @Version 1.0
 **/
public class SearchEsByAccountName {
    public static void searchEsByAccountName(){

        String s = "tomstites,rhpsia,deanstarkman,nytimes,austinramzy,ssboland,rudder,mbachelet,kiyyabaloch,shireenmazari1,begbol,radio_azattyk,aygeryma,l10nlab,belgiumuyghur,weiboscope,sumofus,statedeptspox,intypython,xiabamboohermit,jardemalie,abimalaysia,babussokutan,bencosmef,mhzaman,aslima03846189,donaldcclarke,mpwangtingyu,baldingsworld,molos123,robindbrant,gulchehrahoja,cuyghurs,ilhan,aasasianstudies,hoghuzkhan,yanghin_uyghur,uygurtimes,atajurt_kazak,mustafacan_aksu,uyghuryadvashem,yananw,trailbl16797622,arslanotkur,sophia_yan,atajurt,uygurhaber,muhammadrabiye,turkistantmes,uighurturk,gulnazuyghur,turkicuyghur,abdulahgulja,australianuygh1,tarimuyghur,sabriuyghur,bsintash,maryamhanim,mamatatawulla,uyghurn,drzuhdijasser,louisacgreve,mjt1214,gheribjan,magnus_fiskesjo,sophiehrw,salih_hudayar,beijingpalmer,nijatturkistan,bitterwintermag,weiwuernews,saveuighurusa,amnestyusa,uyghursvic,nuryturkel,uyghurcongress,grosetimothy,scmpnews,ajplus,dgtam86,williamyang120,vanessafrangvi1,wang_maya,j_smithfinley,gerryshih,joshchin,meghara,badiucao,hrw,uyghurproject,nurahmet9,sigalsamuel,norightsnogames,arslan_hidayat,uighurt,alfred_uyghur,0715rita,asiyeuyghur,erkin_azat,hkokbore,aydinanwar_,tombschrader,weneedtoknownow,adrianzenz,meclarke114,jimmillward,andersonelisem,jnbpage,junmai1103,chrisrickleton,mervesebnem,sophiemcneill,jenn_chowdhury,ak_mack,robbiegramer,abcchinese,4corners,epochtimes,faridadeif,tingdc,tumaris_almas,observatoryihr,cairnational,bequelin,jewherilham,turkistan_tv,abdughenisabit,ssbilir_,sedat_peker,patrickpoon,sarkikebir,turkistaner,aiduyghur,kuzzataltay,millioyghunush,eastturkistann,nedemocracy,ihhinsaniyardim,memettohti,gemkifujii,uyghur28933032,naokomizutani,nukinfo,saveeastturk,isobelyeung,petecirwin,mattjtucker55,hamish,asiamattersewc,abduwela,yy56936953,ssboland,rudder,hamish,rianthum,uyghurspeaker,,shahitbiz,fergusshiel,amytheblue,sashachavkin,shirafu,dtbyler,halmuratu,hamuttahir,uighurian,marinawalkerg,icijorg,he_shumei,antoniocuga,chubailiang,dakekang,bethanyallenebr";
        List<String> accountNames = Arrays.asList(s.split(","));


        SearchRequestVO searchRequestVO = new SearchRequestVO();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder boolQueryBuilderToday = QueryBuilders.boolQuery();
        searchRequestVO.setPageSize(1);
        searchRequestVO.setPageNo(2);
        JSONObject jsonObject = new JSONObject();

        searchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
        searchRequestVO.setType("janusgraph_allv_twitter_text");

        boolQueryBuilder.must(QueryBuilders.termQuery("datasetId__STRING", "9999"))
                .must(QueryBuilders.termQuery("mediatname__STRING", "TwitterTweet"))
                .must(QueryBuilders.termsQuery("username", accountNames));

        AggRequestVO aggRequestVO = new AggRequestVO();
        aggRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_text"});
        aggRequestVO.setType("janusgraph_allv_twitter_text");
        AggregationBuilder aggregationBuilder = null;
        List<JSONObject> resultList = new ArrayList();
        aggregationBuilder = AggregationBuilders.terms("agg")
                .field("username__STRING").size(1000000);

        aggRequestVO.setAggregationBuilder(aggregationBuilder);
        aggRequestVO.setQueryBuilder(boolQueryBuilder);

        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        try {
            List<JSONObject> jsonObjects = searchUtil.aggsThree(aggRequestVO);
            for (JSONObject object : jsonObjects) {
                JSONObject node = object.getJSONObject("node");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        searchEsByAccountName();
    }
}
