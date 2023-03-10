package com.tzj;


import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Slf4j
public class ExcelMergeUtil {

    public static void main(String[] args) throws Exception {
        String headerFile = "/Users/yk/Desktop/excel/table1_header.xlsx";

        String dataFile1 = factoryData("分厂1");
        System.out.println("dataFile1: " + dataFile1);

        String dataFile2 = factoryData("分厂2");
        System.out.println("dataFile2: " + dataFile2);

        String footerFile = "/Users/yk/Desktop/table1_footer.xlsx";
        footExport(footerFile);

        Workbook header = WorkbookFactory.create(new FileInputStream(headerFile));
        Workbook data1 = WorkbookFactory.create(new FileInputStream(dataFile1));
        Workbook data2 = WorkbookFactory.create(new FileInputStream(dataFile2));
        Workbook footer = WorkbookFactory.create(new FileInputStream(footerFile));
        List<Workbook> wbList = Lists.newArrayList(header, data1, data2, footer);

        PoiMergeUtil.merge(wbList, "/Users/yk/Desktop/merge.xlsx");
    }

    private static void footExport(String footerFile) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("total", RandomUtil.randomDouble());
        exportByTemplate("/Users/yk/Desktop/excel/table1_footer.xlsx", footerFile, map);
    }

    private static String factoryData(String factoryName) {
        // 注意map中put的值 不要为空binocularOperation()自己写的三目运算为空处理办法
        Map<String, Object> map = Maps.newHashMap();
        map.put("factoryName", factoryName);
        map.put("val1", "23432.34");
        map.put("val2", "343.23");
        map.put("val3", "99999");
        map.put("val4", "7657.23");
        map.put("val5", "7657.23");
        map.put("val6", "7657.23");
        map.put("val7", "7657.23");
        map.put("val8", "7657.23");
        map.put("val9", "7657.23");
        map.put("val10", "7657.23");
        map.put("val11", "7657.23");
        map.put("val12", "123.23");
        map.put("val13", "345.2344");

        // 燃料数
        int eCount = RandomUtil.randomInt(1, 4);
        System.out.println(factoryName + "燃料数: " + eCount);
        for (int i = 1; i <= eCount; i++) {
            String eName = "e" + i;
            map.put(eName, "燃料" + i);
            String vName1 = eName + "v1";
            map.put(vName1, RandomUtil.randomInt());
            String vName2 = eName + "v2";
            map.put(vName2, RandomUtil.randomInt());
        }

        String destFile = String.format("/Users/yk/Desktop/table1_data_%s.xlsx", factoryName);

        return exportData(map, eCount, destFile);
    }

    private static String exportData(Map<String, Object> map, int eCount, String destFilePath) {
        // 模板文件名
        String tmlName = String.format("/Users/yk/Desktop/excel/table1_data_%02d.xlsx", eCount);
        System.out.println("模板文件名: " + tmlName);

        exportByTemplate(tmlName, destFilePath, map);

        return destFilePath;
    }

    private static void exportByTemplate(String tplFilePath, String destFilePath, Map<String, Object> map) {
        FileOutputStream fileOut;
        try {
//            TemplateExportParams params = new TemplateExportParams(tplFilePath, true);
            //Workbook不可以转为HSSFWorkbook 具体逻辑调试看结果集
//            XSSFWorkbook workbook = (XSSFWorkbook) ExcelExportUtil.exportExcel(params, map);

            fileOut = new FileOutputStream(destFilePath);
//            workbook.write(fileOut);

            fileOut.close();
        } catch (Exception e) {
            log.error("导出模板excel失败", e);
        }
    }

}