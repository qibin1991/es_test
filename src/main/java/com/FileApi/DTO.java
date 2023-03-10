package com.FileApi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName DTO
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/1814:02
 * @Version 1.0
 **/
public class DTO {


    public static void main(String[] args) {

        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("/Users/qibin/Downloads/碳排放因子导入111-1000001342463174--源文件.xlsx"));
        int rowCount = reader.getRowCount();


        List<Map<String, Object>> read = reader.read(0, 0, rowCount);


         ///Users/qibin/Downloads/奖品导入模板11111.xlsx
         ///Users/qibin/Downloads/奖品导入模板11111.xlsx

        ExcelWriter writer = ExcelUtil.getWriter("/Users/qibin/Downloads/奖品导入模板11111.xlsx","奖品导入模板_v1.0_20220801");


//        String stringCellValue = writer.getOrCreateRow(2).getCell( 6).getStringCellValue();
//        System.out.println(stringCellValue);
        StyleSet style = writer.getStyleSet();
        style.setBackgroundColor(IndexedColors.RED, false);

        Font font = writer.createFont();
        font.setColor(Font.COLOR_RED);
        font.setBold(true);
        writer.getStyleSet().setFont(font, true);

//        writer.getOrCreateRow(2).createCell(6).setCellStyle();
//        writer.getOrCreateRow(2).createCell(8).setCellValue("数据错误");
//        writer.getOrCreateRow(2).getCell(8).getCellStyle().setFont(font);
//        writer.getOrCreateRow(3).createCell(8).setCellValue("数据错误");
        writer.getOrCreateRow(4).createCell(8).setCellValue("数据错误");
        writer.getOrCreateRow(5).createCell(8).setCellValue("数据错误");
//        writer.getOrCreateRow(6).createCell(8).setCellValue("数据错误");
        writer.close();

//        ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file("/Users/qibin/Downloads/奖品导入模板error.xlsx"));
//
//        writer.merge(6, "注：\n" +
//                "1、带*为必填项；\n" +
//                "2、所属业务仅限系统规定业务名称；\n" +
//                "3、兑换数量限制需填写【限制】、【不显示】；如果选择【限制】，限制数量则必须填写，如果选择【不限制】，显示数量不需要填写；\n" +
//                "4、默认状态需填写【已上架】、【已下架】；\n" +
//                "5、消耗积分、限制数量仅限填写正整数；\n" +
//                "6、首行为示例，需删除后填写真实信息。");
//
//        writer.passCurrentRow();
//        writer.write(read);
//        writer.close();


//        read.forEach(a-> {

//            Set<String> strings = a.keySet();
//            for (String string : strings) {
//                if (string.contains("*")){
//                    string = string.replace("*", "");
//                    System.out.println(string);
//                }
//            }
//
//            System.out.println(a);
//        });


    }


    @Test
    public void redisZset(){



    }


}
