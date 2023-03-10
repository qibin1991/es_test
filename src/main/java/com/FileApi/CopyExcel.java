package com.FileApi;

import com.spire.xls.*;
import com.spire.xls.collections.WorksheetsCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName CopyExcel
 * @Description TODO
 * @Author QiBin
 * @Date 2022/9/19 19:17
 * @Version 1.0
 **/
public class CopyExcel {


    public static void main(String[] args) throws IOException {

        try {
            Workbook wb = new Workbook();
            wb.loadFromFile(  "/Users/qibin/Downloads/mind/碳排放因子导入模板.xlsx");
//        wb.loadFromFile("/Users/qibin/Downloads/mind/" + "碳排放因子导入模板.xlsx");

            Worksheet worksheet = wb.getWorksheets().get("sheet1");
//            CellRange cellRange = worksheet.getCellRange(1, 1, 1, 45);

            Workbook wb2 = new Workbook();


            String errFilePath = "/Users/qibin/Downloads/mind/"
                    + "111--异常数据.xlsx";

            File file = new File(errFilePath);
            if (!file.exists())
                file.createNewFile();
            wb2.loadFromFile(errFilePath);

//        ExcelFont font = wb2.createFont();
//        font.setColor(Color.RED);

            Worksheet worksheet1 = wb2.getWorksheets().get(0);
            worksheet1.setName("sheet1");
//            CellRange cellRange1 = worksheet1.getCellRange(1, 1, 1, 45);
//            worksheet1.copy(cellRange, cellRange1, true);
            worksheet1.copyFrom(worksheet);
//        worksheet1.copyFrom(worksheet);
            wb2.save();
            Worksheet worksheet2 = wb2.getWorksheets().get(1);
            worksheet2.remove();
            wb2.save();
//        String fileName = wb2.getFileName();
//        System.out.println(fileName);
        } catch (IOException e) {
            System.out.println("=========="+e.getMessage());
            throw new RuntimeException(e);

        }

    }
}
