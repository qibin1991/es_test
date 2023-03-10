package com.FileApi;

/**
 * @ClassName Excel2007CloneUtil
 * @Description TODO
 * @Author QiBin
 * @Date 2022/9/19 19:32
 * @Version 1.0
 **/

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

public class Excel2007CloneUtil {


    /**
     * 复制2007excel
     *
     * @param srcPath 原文件路径
     * @return targetPath 复制后的文件路径
     * @throws Exception
     */
    public static String cloneExcel(String srcPath) throws Exception {
        String dateStamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String targetPath = srcPath.trim().substring(0,
                srcPath.lastIndexOf('.'))
                + "_"
                + dateStamp
                + srcPath.trim().substring(srcPath.lastIndexOf('.'));
        try {
            ReadExcel(srcPath, targetPath);
        } catch (Exception e) {
            throw new Exception("excel文件复制失败：" + e.getMessage());
        }
        return targetPath;

    }

    static List<String> strings = Arrays.asList("0",
            "3",
            "5",
            "7",
            "9",
            "10",
            "11",
            "14",
            "15",
            "16");

    /**
     * @param srcPath    源文件目录
     * @param targetPath 目标文件
     */
    private static void ReadExcel(String srcPath, String targetPath)
            throws IOException {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcPath);
            out = new FileOutputStream(targetPath);
            XSSFWorkbook srcWb = new XSSFWorkbook(in);
            XSSFWorkbook wbCreat = new XSSFWorkbook();
            for (int i = 0; i < srcWb.getNumberOfSheets(); i++) {
                XSSFSheet sheet = srcWb.getSheetAt(i);
                CreatNewExcel(wbCreat, sheet, i);
            }
            wbCreat.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private static void CreatNewExcel(XSSFWorkbook wbCreat, XSSFSheet sheet,
                                      int chat) throws IOException {
        // 创建新的excel
        XSSFSheet sheetCreat = wbCreat.createSheet();
        wbCreat.setSheetName(chat, sheet.getSheetName());
        // 复制源表中的合并单元格
        MergerRegion(sheet, sheetCreat);
        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        for (int i = firstRow; i <= lastRow; i++) {
            // 创建新建excel Sheet的行
            XSSFRow rowCreat = sheetCreat.createRow(i);
            // 取得源有excel Sheet的行
            XSSFRow row = sheet.getRow(i);
            if (null != row) {
                // 单元格式样
                XSSFCellStyle cellStyle = null;
                int firstCell = row.getFirstCellNum();
                int lastCell = row.getLastCellNum();
                for (int j = firstCell; j < lastCell; j++) {
                    if (null != row.getCell(j)) {

                        // 自动适应列宽 貌似不起作用
                        sheetCreat.autoSizeColumn(j);
                        // new一个式样
                        cellStyle = wbCreat.createCellStyle();
                        // 设置边框线型
                        XSSFFont font = wbCreat.createFont();
                        font.setColor(Font.COLOR_RED);
                        if (strings.contains(String.valueOf(j))){
                            cellStyle.setFont(font);
                        }
                        cellStyle.setBorderTop(row.getCell(j).getCellStyle()
                                .getBorderTop());
                        cellStyle.setBorderBottom(row.getCell(j).getCellStyle()
                                .getBorderBottom());
                        cellStyle.setBorderLeft(row.getCell(j).getCellStyle()
                                .getBorderLeft());
                        cellStyle.setBorderRight(row.getCell(j).getCellStyle()
                                .getBorderRight());
                        // 设置内容位置:例水平居中,居右，居工
                        cellStyle.setAlignment(row.getCell(j).getCellStyle()
                                .getAlignment());
                        // 设置内容位置:例垂直居中,居上，居下
                        cellStyle.setVerticalAlignment(row.getCell(j)
                                .getCellStyle().getVerticalAlignment());
                        // 自动换行
                        cellStyle.setWrapText(row.getCell(j).getCellStyle()
                                .getWrapText());
                        rowCreat.createCell(j).setCellStyle(cellStyle);
                        // 设置单元格高度
                        rowCreat.getCell(j).getRow().setHeight(
                                row.getCell(j).getRow().getHeight());
                        // 单元格类型
//                        switch (row.getCell(j).getCellType()) {
//                            case XSSFCell.CELL_TYPE_STRING:
                        String strVal = removeInternalBlank(row.getCell(j)
                                .getStringCellValue());
                        rowCreat.getCell(j).setCellValue(strVal);
//                                break;
//                            case XSSFCell.CELL_TYPE_NUMERIC:
//                                rowCreat.getCell(j).setCellValue(
//                                        row.getCell(j).getNumericCellValue());
//                                break;
//                            case XSSFCell.CELL_TYPE_FORMULA:
//                                try {
////                                String temp = String.valueOf(row.getCell(j)
////                                        .getNumericCellValue());
////                                rowCreat.getCell(j).setCellValue(
////                                        new Double(temp));
//                                    rowCreat.getCell(j).setCellValue(
//                                            row.getCell(j).getNumericCellValue());
//                                    // rowCreat.getCell(j).setCellValue(
//                                    // String.valueOf(row.getCell(j)
//                                    // .getNumericCellValue()));
//                                } catch (Exception e) {
//                                    try {
//                                        rowCreat.getCell(j).setCellValue(
//                                                String.valueOf(row.getCell(j)
//                                                        .getRichStringCellValue()));
//                                    } catch (Exception ex) {
//                                        rowCreat.getCell(j).setCellValue("公式出错");
//                                    }
//                                }
//                                break;
//                        }
                    }
                }
            }
        }
    }

    /**
     * 复制原有sheet的合并单元格到新创建的sheet
     *
     * @param sheet      原有的sheet
     * @param sheetCreat 新创建sheet
     */
    private static void MergerRegion(XSSFSheet sheet, XSSFSheet sheetCreat) {
        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress mergedRegionAt = sheet.getMergedRegion(i);
            sheetCreat.addMergedRegion(mergedRegionAt);
        }

    }

    /**
     * 去除字符串内部空格
     */
    public static String removeInternalBlank(String s) {
        Pattern p = Pattern.compile("");
        Matcher m = p.matcher(s);
        char str[] = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                sb.append(' ');
            } else {
                break;
            }
        }
        String after = m.replaceAll("");
        return sb.toString() + after;
    }

    public static void main(String[] args) throws Exception, IOException {
        String xlsPath = "/Users/qibin/Downloads/mind/碳排放因子导入模板.xlsx";
        String newFilePath = cloneExcel(xlsPath);
        System.out.println(newFilePath);

    }
}