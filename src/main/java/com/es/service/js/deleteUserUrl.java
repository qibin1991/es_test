package com.es.service.js;

import com.es.service.ESConnection;
import com.es.service.SearchRequestVO;
import com.es.service.SearchResponseVO;
import com.es.service.TwoAggreationUtil;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName deleteUserUrl
 * @Description TODO
 * @Author QiBin
 * @Date 2021/3/26下午4:06
 * @Version 1.0
 **/
public class deleteUserUrl {

    public static void main(String[] args) {
        String s = "tomstites,rhpsia,deanstarkman,nytimes,austinramzy,ssboland,rudder,mbachelet,kiyyabaloch,shireenmazari1,begbol,radio_azattyk,aygeryma,l10nlab,belgiumuyghur,weiboscope,sumofus,statedeptspox,intypython,xiabamboohermit,jardemalie,abimalaysia,babussokutan,bencosmef,mhzaman,aslima03846189,donaldcclarke,mpwangtingyu,baldingsworld,molos123,robindbrant,gulchehrahoja,cuyghurs,ilhan,aasasianstudies,hoghuzkhan,yanghin_uyghur,uygurtimes,atajurt_kazak,mustafacan_aksu,uyghuryadvashem,yananw,trailbl16797622,arslanotkur,sophia_yan,atajurt,uygurhaber,muhammadrabiye,turkistantmes,uighurturk,gulnazuyghur,turkicuyghur,abdulahgulja,australianuygh1,tarimuyghur,sabriuyghur,bsintash,maryamhanim,mamatatawulla,uyghurn,drzuhdijasser,louisacgreve,mjt1214,gheribjan,magnus_fiskesjo,sophiehrw,salih_hudayar,beijingpalmer,nijatturkistan,bitterwintermag,weiwuernews,saveuighurusa,amnestyusa,uyghursvic,nuryturkel,uyghurcongress,grosetimothy,scmpnews,ajplus,dgtam86,williamyang120,vanessafrangvi1,wang_maya,j_smithfinley,gerryshih,joshchin,meghara,badiucao,hrw,uyghurproject,nurahmet9,sigalsamuel,norightsnogames,arslan_hidayat,uighurt,alfred_uyghur,0715rita,asiyeuyghur,erkin_azat,hkokbore,aydinanwar_,tombschrader,weneedtoknownow,adrianzenz,meclarke114,jimmillward,andersonelisem,jnbpage,junmai1103,chrisrickleton,mervesebnem,sophiemcneill,jenn_chowdhury,ak_mack,robbiegramer,abcchinese,4corners,epochtimes,faridadeif,tingdc,tumaris_almas,observatoryihr,cairnational,bequelin,jewherilham,turkistan_tv,abdughenisabit,ssbilir_,sedat_peker,patrickpoon,sarkikebir,turkistaner,aiduyghur,kuzzataltay,millioyghunush,eastturkistann,nedemocracy,ihhinsaniyardim,memettohti,gemkifujii,uyghur28933032,naokomizutani,nukinfo,saveeastturk,isobelyeung,petecirwin,mattjtucker55,hamish,asiamattersewc,abduwela,yy56936953,ssboland,rudder,hamish,rianthum,uyghurspeaker,,shahitbiz,fergusshiel,amytheblue,sashachavkin,shirafu,dtbyler,halmuratu,hamuttahir,uighurian,marinawalkerg,icijorg,he_shumei,antoniocuga,chubailiang,dakekang,bethanyallenebr";

        List<String> stringList = Arrays.asList(s.split(","));


        TwoAggreationUtil searchUtil = new TwoAggreationUtil();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchRequestVO searchRequestVO = new SearchRequestVO();

        searchRequestVO.setIndexName(new String[]{"janusgraph_janusgraph_allv_twitter_pesonal"});
        searchRequestVO.setType("janusgraph_allv_twitter_pesonal");

        searchRequestVO.setPageNo(1);
        searchRequestVO.setPageSize(10000);

        boolQueryBuilder.must(QueryBuilders.termsQuery("username", stringList))
                .must(QueryBuilders.termQuery("userWebUrl__STRING", "1111"));
        searchRequestVO.setQueryBuilder(boolQueryBuilder);

        try {
            SearchResponseVO search = searchUtil.search(searchRequestVO);
            List<Map<String, Object>> data = search.getData();
            for (Map<String, Object> datum : data) {
                Object id = datum.get("_id");
                Object userUrl = datum.get("userUrl");
                Object userWebUrl = datum.get("userWebUrl");
                delete("janusgraph_janusgraph_allv_twitter_pesonal", id.toString(), "janusgraph_allv_twitter_pesonal");

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("========");
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

}
