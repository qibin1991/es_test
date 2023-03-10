package com.es.service.js;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName Tweets
 * @Description TODO
 * @Author QiBin
 * @Date 2020/7/2113:48
 * @Version 1.0
 **/
@Data
public class Tweets {

    public static void main(String[] args) {
        String s = "\"{\"timestamp\": 1610464380, \"crawl_time\": 1610541761," +
                " \"share_num\": \"0\", \"post_data\": \"\", \"hashtag_list\": [], \"profilelink_list\": []," +
                " \"post_video\": \"\",   " +
                ", \"great_num\": 2, \"comment_num\": 0}\"";


        System.out.println(s.substring(1,s.length()-1));
        Object parse = JSONObject.parse(s.substring(1,s.length()-1));
        System.out.println(parse);
//        JSONObject jsonObject = JSONObject.parseObject(message);
//        System.out.println(jsonObject);



    }


    String mediatname = "";
    String spider_type = "";
    Integer total_collect = 0;
    String insert_data_type = "0";
    Integer comments_scroll = 0;
    String is_comments = "";
    String md5id ="";
    String input_time="";
    String spider_url="";
    String fullname="";
    String screen_name="";
     String blogger_id =  "";

    String user_url="";
    String datasetId="";
    String user_img="";
    String user_json="";
    String tag_json="";
    String tweets_id="";
    String conversation_id="00";
    String tweets_content="";

    String source_content="";

    String tweets_url="";
    String time="";
    Date timestr = new Date();
    String quotetweet_id="";
    String quotetweet_url="";
    String outer_media_url="";
    String outer_media_shot_url="";

    String video_url="";

    String img_url="";
    Integer isori=0;
    Integer is_reply=0;
    String retweet_id="";
    String retweet_name="";
    String retweet_screen_name="";
    String retweet_user_id="";
    String retweet_content="";
    String retweet_url="";
    String retweeted_created="";
    String retweeted_lang="";
    String retweeted_status_reply_count="";
    String replied_id="";
    String replied_user="";
    String replied_screen_name="";

    Integer cmtcnt=0;

    Integer rpscnt=0;

    Integer atdcnt=0;

    Integer likes=0;

    String tweets_type="";
    String cstate="";
    String lang="";
    String locate_name="";
    String locate_type="";
    String locate_1="";
    String locate_2="";
    String spider_task_id="";
    String remarks="";
    String level="";
    String datasetName="";
    String tableName = "";
    String server_node;
    String location = "";
    String location_id;
    Date update_clr_time =new Date();
    Date next_spider_comments_time = new Date();
    String  collect_comments_frequent;
    String locate_id;
    String reserved1;
    String   reserved2;
    String reserved3;



}
