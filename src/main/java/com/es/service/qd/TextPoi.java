package com.es.service.qd;

/**
 * @ClassName TextPoi
 * @Description TODO
 * @Author QiBin
 * @Date 2021/1/22下午2:32
 * @Version 1.0
 **/

import cn.hutool.core.text.UnicodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.es.service.SearchRequestVO;
import com.es.service.TwoAggreationUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.cglib.beans.BeanMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextPoi {

    //id	user_scrapy	user_id	user_name	full_name	user_img
    // about_overview	about_work_and_education	about_places	about_contact_and_basic_info
    // about_family_and_relationships	about_details	about_life_events	insert_data	project_name

    //id	md5id
    // input_time	register_time	user_url
    // user_web_url	user_id	user_name	full_name
    // user_addr	user_img	user_flag	favorites
    // likes	follow	hometown	current_city	story	other
    // work	education	professional	lived_places	pagelet_bio	pagelet_nicknames
    // pagelet_pronounce	pagelet_blood_donations	pagelet_quotes	relationship
    // birthday	email	languages	gender	political	religious	websites	about
    // social	life_events	family	usertype
    // spider_task_id	update_clr_time	reserved1	reserved2	reserved3
    public static void main(String[] args) {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<JSONObject> list = null;
        String cellData = null;
        String filePath = "/Users/qibin/zyyt相关/青岛详细/fbtw数据/facebook公共主页主贴/facebook用户信息.xls";
        String columns[] = {"id", "user_scrapy", "user_id", "user_name", "full_name",
                "user_img", "about_overview", "about_work_and_education", "about_places",
                "about_contact_and_basic_info", "about_family_and_relationships", "about_details",
                "about_life_events", "insert_data", "project_name"};

//        String columns[] = {"id","md5id","register_time","user_url","user_web_url","user_id","user_name","full_name","user_addr","user_img","user_flag","favorites","likes","follow","hometown","current_city","story","other","work","education","professional","lived_places","pagelet_bio","pagelet_nicknames","pagelet_pronounce","pagelet_blood_donations","pagelet_quotes","relationship","birthday","email","languages","gender","political","religious","websites","about","social","life_events","family","usertype","spider_task_id","update_clr_time","reserved1","reserved2","reserved3" };


        wb = readExcel(filePath);
        if (wb != null) {
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
            for (int i = 1; i < rownum; i++) {
                JSONObject map = new JSONObject();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colnum; j++) {
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }
        List<FBUserVo> list1 = new ArrayList<>();
        //遍历解析出来的list
        for (JSONObject map : list) {
//            System.out.println(map);
            JSONObject user_img = map.getJSONObject("user_img");
            String user_friends_num = user_img.getString("user_friends_num");
            String fullName = map.getString("user_name");

            String userName = map.getString("full_name");


            String s = UnicodeUtil.toString(fullName);

            //查es  修改
            TwoAggreationUtil searchUtil = new TwoAggreationUtil();
            SearchRequestVO searchRequestVO = new SearchRequestVO();
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            searchRequestVO.setIndexName(new String[]{"js_qd_fb_post"});
            searchRequestVO.setType("post");

            searchRequestVO.setPageSize(2);
            searchRequestVO.setPageNo(1);

//            System.out.println(fbUserVo);
//            System.out.println(fbUserVo);

        }

//        int i = 0;
//        Map<String, String> map = new HashMap<>();
//        for (FBUserVo fbUserVo : list1) {
//
//            map.put(fbUserVo.getFullName(), fbUserVo.getUserName());
////            String s = JSONUtil.toJsonStr(fbUserVo);
////            System.out.println(fbUserVo);
//            IndexRequest request = new IndexRequest("js_facebook_user");
//            request.type("user");
//            Map<String, Object> stringObjectMap = beanToMap(fbUserVo);
//            String s = JSON.toJSONString(stringObjectMap);
//            request.source(s, XContentType.JSON);
//            try {
//                ESConnection.getClient().index(request, RequestOptions.DEFAULT);
//                System.out.println(i);
//                i++;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println(map.toString());
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
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }

        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
//                case Cell.CELL_TYPE_NUMERIC: {
//                    cellValue = String.valueOf(cell.getNumericCellValue());
//                    break;
//                }
//                case Cell.CELL_TYPE_FORMULA: {
//                    //判断cell是否为日期格式
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        //转换为日期格式YYYY-mm-dd
//                        cellValue = cell.getDateCellValue();
//                    } else {
//                        //数字
//                        cellValue = String.valueOf(cell.getNumericCellValue());
//                    }
//                    break;
//                }
//                case Cell.CELL_TYPE_STRING: {
//                    cellValue = cell.getRichStringCellValue().getString();
//                    break;
//                }
//                default:
//                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

}
