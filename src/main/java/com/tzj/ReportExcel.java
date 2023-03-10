package com.tzj;


import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName ReportExcel
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/1710:52
 * @Version 1.0
 **/
public class ReportExcel {
//    public static void main(String[] args) {
//        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("/Users/qibin/Downloads/2022.xlsx"), 0);
//        List<Map<String, Object>> maps = reader.readAll();
//        for (Map<String, Object> map : maps) {
//            System.out.println(map);
//        }
//        ExcelWriter writer = ExcelUtil.getWriter("/Users/qibin/Downloads/2023.xlsx");
//
//        writer.write(maps, true);
//        writer.close();


//    }
//    public static void main(String[] args)
//    {
//        try
//        {
//            String url="/Users/qibin/Downloads/2022.xlsx";
//            //第一步：选择模板文件 通过模板得到一个可写的Workbook：
//
//            jxl.Workbook wb = jxl.Workbook.getWorkbook(new File(url));
//            //这样定义这个输出流对象。第二个参数代表了要读取的模板。
//            ByteArrayOutputStream targetFile = new ByteArrayOutputStream();
//
//            WritableWorkbook wwb = jxl.Workbook.createWorkbook(targetFile, wb);
//            //第一个参数是一个输出流对象，比如可以
//            //第三步：选择模板的Sheet：
//            WritableSheet wws = wwb.getSheet(0);
//            //第四步：选择单元格，写入动态值，根据单元格的不同类型转换成相应类型的单元格：
///*          Label A1 = (Label)wws.getWritableCell(2,0);
//            A1.setString("单元格内容");*/
//            Label C1 = new Label(2,0,"单元格内容");
//            wws.addCell(C1);
//            wwb.write();
//            wwb.close();
//            wb.close();
//            FileOutputStream fos = new FileOutputStream("/Users/qibin/Downloads/2024.xlsx");
//            targetFile.writeTo(fos);
//            targetFile.close();
//
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }

    public static void main(String[] args) {
        readExcel();
    }

    public static void readExcel() {

        try {
// 创建对Excel工作簿文件的引用
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("/Users/qibin/Downloads/2022.xlsx"));
// workbook.getNumberOfSheets());//获取sheet数
            for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
                if (null != workbook.getSheetAt(numSheets)) {
                    XSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
                    System.err.println(aSheet.getFirstRowNum());
                    for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
                            .getLastRowNum(); rowNumOfSheet++) {
                        if (null != aSheet.getRow(rowNumOfSheet)) {
                            XSSFRow aRow = aSheet.getRow(rowNumOfSheet);
                            for (short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
                                if (null != aRow.getCell(cellNumOfRow)) {


                                    List list = new ArrayList();
                                    list.add("1");
                                    list.add("2");
// list.add("3");
// list.add("4");
// list.add("5");
// list.add("6");
// list.add("7");
// list.add("8");
// list.add("9");
// list.add("10");
// rowNumOfSheet 表示动态生成那一行的上一行的num，例如11行是模板中的出票保证信息
//                                    while (rowNumOfSheet == 10 && cellNumOfRow == 0) {
//                                        for (int i = list.size() - 1; i > 0; i--) {
//                                            int newRow = 11;
//                                            int rows = 1;//设定插入几行
//                                            aSheet.shiftRows(newRow, aSheet.getLastRowNum(), rows);
//                                            XSSFRow sourceRow = aSheet.getRow(newRow);
//
////                                            aSheet.addMergedRegion(new Region(newRow, (short) 2, newRow, (short) 2)); //
//                                            XSSFCell cell = sourceRow.createCell((short) 2);
//                                            addSyteml(workbook, aSheet, newRow, newRow, 2, 2);
////                                            cew2.setEncoding(XSSFCell.ENCODING_UTF_16);
//                                            cell.setCellValue("保证人名称");
//
////                                            aSheet.addMergedRegion(new Region(newRow, (short) 3, newRow, (short) 4)); //
//                                            XSSFCell cew3 = sourceRow.createCell((short) 3);
//                                            addSyteml(workbook, aSheet, newRow, newRow, 3, 4);
////                                            cew3.setEncoding(XSSFCell.ENCODING_UTF_16);
//                                            cew3.setCellValue(list.get(i).toString());
//
////                                            aSheet.addMergedRegion(new Region(newRow, (short) 5, newRow, (short) 6)); //
//                                            XSSFCell cew5 = sourceRow.createCell((short) 5);
//                                            addSyteml(workbook, aSheet, newRow, newRow, 5, 6);
////                                            cew5.setEncoding(XSSFCell.ENCODING_UTF_16);
//                                            cew5.setCellValue("保证人地址");
//
////                                            aSheet.addMergedRegion(new Region(newRow, (short) 7, newRow, (short) 12)); //
//                                            XSSFCell cew11 = sourceRow.createCell((short) 7);
//                                            addSyteml(workbook, aSheet, newRow, newRow, 7, 12);
////                                            cew11.setEncoding(XSSFCell.ENCODING_UTF_16);
//                                            cew11.setCellValue(list.get(i).toString());
//
////                                            aSheet.addMergedRegion(new Region(newRow, (short) 13, newRow, (short) 15)); //
//                                            XSSFCell cew12 = sourceRow.createCell((short) 13);
//                                            addSyteml(workbook, aSheet, newRow, newRow, 13, 15);
////                                            cew12.setEncoding(XSSFCell.ENCODING_UTF_16);
//                                            cew12.setCellValue("保证日期");
//
////                                            aSheet.addMergedRegion(new Region(newRow, (short) 16, newRow, (short) 18)); //
//                                            XSSFCell cew13 = sourceRow.createCell((short) 16);
//                                            addSyteml(workbook, aSheet, newRow, newRow, 16, 18);
////                                            cew13.setEncoding(XSSFCell.ENCODING_UTF_16);
//                                            cew13.setCellValue(list.get(i).toString());
//
//                                            if (i == 1) {
////                                                aSheet.addMergedRegion(new Region(10, (short) 0, 10 + list.size() - 1, (short) 1)); //
//                                                XSSFCell cew = sourceRow.createCell((short) 0);
//                                                addSyteml(workbook, aSheet, 10, 10 + list.size() - 1, 0, 1);
////                                                cew.setEncoding(XSSFCell.ENCODING_UTF_16);
//                                                cew.setCellValue("出票保证信息");
//                                            }
//                                        }
//                                        break;
//
//                                    }
                                    List lis1t = new ArrayList();
                                    lis1t.add("111");
                                    lis1t.add("222");
                                    lis1t.add("333");
                                    lis1t.add("444");
                                    lis1t.add("555");

//当出票保证信息为0条的时候下面的标题合并出问题,加此判断
                                    int size = list.size();
                                    if (size == 0) {
                                        size = 1;
                                    }

                                    while (rowNumOfSheet == 17 + size && cellNumOfRow == 0) {
                                        for (int i = lis1t.size() - 1; i > 0; i--) {
                                            int newRow = 17 + size;
                                            int rows = 1;//设定插入几行
                                            aSheet.shiftRows(newRow, aSheet.getLastRowNum(), rows);
                                            XSSFRow sourceRow = aSheet.getRow(newRow);

//                                            aSheet.addMergedRegion(new Region(newRow, (short) 2, newRow, (short) 2)); //
                                            XSSFCell cew2 = sourceRow.createCell((short) 2);
                                            addSyteml(workbook, aSheet, newRow, newRow, 2, 2);
//                                            cew2.setEncoding(XSSFCell.ENCODING_UTF_16);
                                            cew2.setCellValue("保证人名称");

//                                            aSheet.addMergedRegion(new Region(newRow, (short) 3, newRow, (short) 4)); //
                                            XSSFCell cew3 = sourceRow.createCell((short) 3);
                                            addSyteml(workbook, aSheet, newRow, newRow, 3, 4);
//                                            cew3.setEncoding(XSSFCell.ENCODING_UTF_16);
                                            cew3.setCellValue(lis1t.get(i).toString());

//                                            aSheet.addMergedRegion(new Region(newRow, (short) 5, newRow, (short) 6)); //
                                            XSSFCell cew5 = sourceRow.createCell((short) 5);
                                            addSyteml(workbook, aSheet, newRow, newRow, 5, 6);
//                                            cew5.setEncoding(XSSFCell.ENCODING_UTF_16);
                                            cew5.setCellValue("保证人地址");

//                                            aSheet.addMergedRegion(new Region(newRow, (short) 7, newRow, (short) 12)); //
                                            XSSFCell cew11 = sourceRow.createCell((short) 7);
                                            addSyteml(workbook, aSheet, newRow, newRow, 7, 12);
//                                            cew11.setEncoding(XSSFCell.ENCODING_UTF_16);
                                            cew11.setCellValue(lis1t.get(i).toString());

//                                            aSheet.addMergedRegion(new Region(newRow, (short) 13, newRow, (short) 15)); //
                                            XSSFCell cew12 = sourceRow.createCell((short) 13);
                                            addSyteml(workbook, aSheet, newRow, newRow, 13, 15);
//                                            cew12.setEncodin/g(XSSFCell.ENCODING_UTF_16);
                                            cew12.setCellValue("保证日期");

//                                            aSheet.addMergedRegion(new Region(newRow, (short) 16, newRow, (short) 18)); //
                                            XSSFCell cew13 = sourceRow.createCell((short) 16);
                                            addSyteml(workbook, aSheet, newRow, newRow, 16, 18);
//                                            cew13.setEncoding(XSSFCell.ENCODING_UTF_16);
                                            cew13.setCellValue(lis1t.get(i).toString());

                                            if (i == 1) {//当是循环中的最后一条数据的时候将总标题嵌入到最前面
//                                                aSheet.addMergedRegion(new Region(newRow - 1, (short) 0, newRow + lis1t.size() - 2, (short) 1)); //
                                                XSSFCell cew = sourceRow.createCell((short) 0);
                                                addSyteml(workbook, aSheet, newRow - 1, newRow + lis1t.size() - 2, 0, 1);
//                                                cew.setEncoding(XSSFCell.ENCODING_UTF_16);
                                                cew.setCellValue("出票保证信息");
                                            }
                                        }
                                        break;

                                    }

                                    XSSFCell aCell = aRow.getCell(cellNumOfRow);
                                    CellType cellType = aCell.getCellType();
                                    if (cellType.getCode() == 0) {//number型
//                                        aCell.setEncoding(XSSFCell.ENCODING_UTF_16);// 中文处理
                                        int NumericCellValue = (int) aCell.getNumericCellValue();//得到模板中值所在cell中的数字
                                        switch (NumericCellValue) {
                                            case 1:  //出票日期
                                                aCell.setCellValue("111");
                                                break;
                                            case 2:  //票据号码
                                                aCell.setCellValue("111");
                                                break;
                                            case 3://出 票 人全 称
                                                aCell.setCellValue("111");
                                                break;
                                            case 4://出票人账号
                                                aCell.setCellValue("111");
                                                break;
                                        }

                                    }
                                    FileOutputStream fOut = new FileOutputStream("/Users/qibin/Downloads/2025.xlsx");
// 把相应的Excel 工作簿存盘
                                    workbook.write(fOut);
                                    fOut.flush();
// 操作结束，关闭文件
                                    fOut.close();
                                }
                            }

                        }

                    }

                }
            }
        } catch (Exception e) {
            System.out.println("ReadExcelError " + e);
        }

    }

    /**
     * 设置动态数据的样式
     *
     */
    private static void addSyteml(XSSFWorkbook workbook,XSSFSheet aSheet,int rowFrom,int rowTo,int columnFrom,int columnTo){
        XSSFCellStyle cloneStyle = workbook.createCellStyle();
//        cloneStyle.setAlignment(XSSFCellStyle);
        cloneStyle.setBorderLeft(BorderStyle.valueOf((short)0));
//        cloneStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cloneStyle.setBorderBottom(BorderStyle.valueOf((short)0));
//        cloneStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        for(int i=rowFrom;i<=rowTo;i++){
            XSSFRow row_temp = aSheet.getRow(i);
            for(int j=columnFrom;j<=columnTo;j++){
                XSSFCell cell_temp = row_temp.getCell((short)j);
                if(cell_temp ==null ){
                    cell_temp = row_temp.createCell((short)j);
                }
                cell_temp.setCellStyle(cloneStyle);
            }
        }
    }
}
