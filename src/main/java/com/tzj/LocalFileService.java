package com.tzj;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.es.service.ESConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LocalFileService
 * @Description TODO
 * @Author QiBin
 * @Date 2021/8/2617:39
 * @Version 1.0
 **/
@Slf4j
public class LocalFileService {


    public static void main(String[] args) {
        readFile();
//        poiFile();
    }


    public static void readFile() {

        List<Integer> objects = new ArrayList<>();
        objects.add(99);
        try {

            ExcelReader reader = ExcelUtil.getReader("/Users/qibin/work/0826/jingnei(һ)20-08-27-21-08-26.xlsx");
            List<Map<String, Object>> read = reader.readAll();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<JSONObject> list = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : read) {
                Object time = stringObjectMap.get("time");
                Object title = stringObjectMap.get("title");
                Object text = stringObjectMap.get("text");
                Object media = stringObjectMap.get("media");
                Object country = stringObjectMap.get("country");
                Object url = stringObjectMap.get("url");
//                String s = url.toString();
//                s.substring(s.indexOf("//") + 1).substring(0, s.indexOf("/"));


                JSONObject jsonObject = new JSONObject();

                jsonObject.put("uuid", DigestUtils.md5Hex(url.toString().getBytes()));
                jsonObject.put("languageCode", "zh");
                jsonObject.put("languageTname", "中文");
                jsonObject.put("mediaLevel", "最高影响力");
                jsonObject.put("mediaName", media);
                jsonObject.put("mediaTname", "新闻");
                jsonObject.put("mediaType", "新闻");
                jsonObject.put("isSensitive", "是");
                jsonObject.put("domain", "是");
                jsonObject.put("url", url);
                jsonObject.put("countryNameZh", country);
                jsonObject.put("title", title);
                jsonObject.put("text", text);

                jsonObject.put("pubTime", simpleDateFormat.parse(time.toString()).getTime());
                jsonObject.put("pubdate", time);
                jsonObject.put("readSet", objects.toArray());
                list.add(jsonObject);
            }

            String type = "article";
            bulkInsert("news_1085", type, list);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void poiFile() {

        try {
            List<Integer> objects = new ArrayList<>();
            objects.add(99);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Workbook wb = null;
            Sheet sheet = null;
            Row row = null;
            List<JSONObject> list = null;
            List<Map<String, Object>> maps = new ArrayList<>();
            String cellData = null;
            String columns[] = {"time", "title", "text","media", "country", "url"};
            String filePath = "/Users/qibin/work/0826/jingnei(二)20-08-27-21-08-26.xlsx";
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
                    Map<String, Object> map = new HashMap<>();
                    row = sheet.getRow(i);
                    if (row != null) {
                        for (int j = 0; j < colnum; j++) {
                            cellData = (String) getCellFormatValue(row.getCell(j));
                            map.put(columns[j], cellData);
                        }
                    } else {
                        break;
                    }
                    maps.add(map);
                }

                for (Map<String, Object> stringObjectMap : maps) {
                    Object time = stringObjectMap.get("time");
                    Object title = stringObjectMap.get("title");
                    Object text = stringObjectMap.get("text");
                    Object media = stringObjectMap.get("media");
                    Object country = stringObjectMap.get("country");
                    Object url = stringObjectMap.get("url");
                    //                String s = url.toString();
                    //                s.substring(s.indexOf("//") + 1).substring(0, s.indexOf("/"));


                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("uuid", DigestUtils.md5Hex(url.toString().getBytes()));
                    jsonObject.put("languageCode", "zh");
                    jsonObject.put("languageTname", "中文");
                    jsonObject.put("mediaLevel", "3");
                    jsonObject.put("mediaName", media);
                    jsonObject.put("mediaTname", "新闻");
                    jsonObject.put("isSensitive", "1");
//                    jsonObject.put("domain", "是");
                    jsonObject.put("url", url);
                    jsonObject.put("countryNameZh", country);
                    jsonObject.put("title", title);
                    jsonObject.put("text", text);

                    jsonObject.put("pubTime", simpleDateFormat.parse(time.toString()).getTime());
                    jsonObject.put("pubdate", time);

                    jsonObject.put("dataset", objects.toArray());
                    list.add(jsonObject);
                }

                String type = "article";
                bulkInsert("news_1085", type, list);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


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
                 wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                 wb = new XSSFWorkbook(is);
            } else {
                 wb = null;
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
//            //判断cell类型
//            switch (cell.getCellType()) {
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
//            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }


    /**
     * 批量索引
     *
     * @param indexName
     * @param type
     * @param sources
     */
    public static boolean bulkInsert(String indexName, String type, List<JSONObject> sources) {
        boolean isSuccess = false;
        BulkRequest bulkRequest = new BulkRequest();
        for (JSONObject source : sources) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(indexName);
            indexRequest.id(source.getString("uuid"));
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
                log.error("批量索引有失败，具体消息：{}", bulkResponse.buildFailureMessage());
            }
        } catch (IOException e) {
            log.error("issues: ", e);
        }
        return isSuccess;
    }
}
