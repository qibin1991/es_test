package com.es.service.js;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.beans.BeanMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FbFollowText
 * @Description TODO
 * @Author QiBin
 * @Date 2021/1/24下午4:17
 * @Version 1.0
 **/


public class FbFollowText {


    public static void main(String[] args) {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<JSONObject> list = null;
        String cellData = null;
        String columns[] = {"_id","md5id","input_time","tid","spider_url","fullname","screen_name","blogger_id","user_url","user_img","user_json","tag_json","tweets_id","conversation_id","tweets_content","tweets_url","time","timestr","quotetweet_id","quotetweet_url","outer_media_url","outer_media_shot_url","img_url","video_url","img_url_md5id","img_url_state","isori","is_reply","retweet_id","retweet_name","retweet_screen_name","retweet_user_id","retweet_content","retweet_url","retweeted_created","retweeted_lang","retweeted_status_reply_count","replied_id","replied_user","replied_screen_name","cmtcnt","rpscnt","atdcnt","likes","total_collect","cstate","spider_task_id","collect_comments_frequent","tweets_type","remarks","spidertype","lang","user_location","locate_name","locate_type","locate_1","locate_2","u_tweets","u_following","u_followers","verified","tag_json_str","comments_scroll","diff_time","source","source_tag","comfrom"
        };

//        String columns[] = {"id","md5id","register_time","user_url","user_web_url","user_id","user_name","full_name","user_addr","user_img","user_flag","favorites","likes","follow","hometown","current_city","story","other","work","education","professional","lived_places","pagelet_bio","pagelet_nicknames","pagelet_pronounce","pagelet_blood_donations","pagelet_quotes","relationship","birthday","email","languages","gender","political","religious","websites","about","social","life_events","family","usertype","spider_task_id","update_clr_time","reserved1","reserved2","reserved3" };


        String filePath ="/Users/qibin/twitter_23/twitter_2.xlsx";
        wb = readExcel(filePath);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<JSONObject>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                JSONObject map = new JSONObject();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
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

    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }

        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
//                case Cell.CELL_TYPE_NUMERIC:{
//                    cellValue = String.valueOf(cell.getNumericCellValue());
//                    break;
//                }
//                case Cell.CELL_TYPE_FORMULA:{
//                    //判断cell是否为日期格式
//                    if(DateUtil.isCellDateFormatted(cell)){
//                        //转换为日期格式YYYY-mm-dd
//                        cellValue = cell.getDateCellValue();
//                    }else{
//                        //数字
//                        cellValue = String.valueOf(cell.getNumericCellValue());
//                    }
//                    break;
//                }
//                case Cell.CELL_TYPE_STRING:{
//                    cellValue = cell.getRichStringCellValue().getString();
//                    break;
//                }
//                default:
//                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }





}
